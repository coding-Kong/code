<template>
  <el-button type="primary" class="btn" @click="addUser">新增用户</el-button>
  <el-button type="danger" class="btn" @click="batchDelUser">批量删除</el-button>

  <el-table
      :data="userList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
<!--    复选框-->
    <el-table-column type="selection" width="50"/>
    <el-table-column type="index" label="序号" width="65"/>
    <el-table-column property="loginAct" label="账号"/>
    <el-table-column property="name" label="姓名"/>
    <el-table-column property="phone" label="手机"/>
    <el-table-column property="email" label="邮箱"/>
    <el-table-column property="createTime" label="创建时间"/>
    <el-table-column label="操作" width="230">
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.id)" v-hasPermission="'user:view'">详情</el-button>
        <el-button type="success" @click="edit(scope.row.id)" v-hasPermission="'user:edit'">编辑</el-button>
        <el-button type="danger" @click="del(scope.row.id)" v-hasPermission="'user:delte'">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <p>
    <el-pagination
        background
        layout="prev, pager, next"
        :page-size="pageSize"
        :total="total"
        @prev-click="page"
        @next-click="page"
        @current-change="page"/>
  </p>

  <!--新增用户的弹窗（对话框）-->
  <el-dialog v-model="userDialogVisible" title="新增用户" width="55%" center>

    <el-form ref="userRefForm" :model="userQuery" label-width="110px" :rules="userRules">
      <el-form-item label="账号" prop="loginAct">
        <el-input v-model="userQuery.loginAct" />
      </el-form-item>

      <el-form-item label="密码" v-if="userQuery.id > 0">
        <el-input type="password" v-model="userQuery.loginPwd" />
      </el-form-item>

      <el-form-item label="密码" prop="loginPwd" v-else>
        <el-input type="password" v-model="userQuery.loginPwd" />
      </el-form-item>

      <el-form-item label="姓名" prop="name">
        <el-input v-model="userQuery.name" />
      </el-form-item>

      <el-form-item label="手机" prop="phone">
        <el-input v-model="userQuery.phone" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="userQuery.email" />
      </el-form-item>

      <el-form-item label="账号未过期" prop="accountNoExpired">
        <el-select v-model="userQuery.accountNoExpired" placeholder="请选择" style="width: 100%;">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>

      <el-form-item label="密码未过期" prop="credentialsNoExpired">
        <el-select v-model="userQuery.credentialsNoExpired" placeholder="请选择" style="width: 100%;">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>

      <el-form-item label="账号未锁定" prop="accountNoLocked">
        <el-select v-model="userQuery.accountNoLocked" placeholder="请选择" style="width: 100%;">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>

      <el-form-item label="账号是否可用" prop="accountEnabled">
        <el-select v-model="userQuery.accountEnabled" placeholder="请选择" style="width: 100%;">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="userDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="userSubmit">提 交</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import {doDelete, doGet, doGet2, doPost, doPut} from "../http/httpRequest";
import {messageConfirm, messageTip} from "../util/utils";

export default {
  name: "UserView",

  //注入
  inject : ['reload', 'phone', 'idArray', 'user'],

  data() {
    return {
      //定义表格数据的用户List，初始值是空
      userList : [{}],
      //分页时每页显示多少条数据
      pageSize : 0,
      //总共有多少条
      total : 0,
      //定义控制弹窗是否弹出来的变量，false是不弹出来，true就弹出来
      userDialogVisible : false,
      //定义新增用户的表单user对象，初始值是空
      userQuery : {},
      //定义新增用户表单提交时的验证规则
      userRules : {
        loginAct : [
          { required: true, message: '登录账号不能为空', trigger: 'blur' },
          { min: 3, max: 32, message: '登录账号长度范围为3-32个字符', trigger: 'blur' }
        ],
        loginPwd : [
          { required: true, message: '登录密码不能为空', trigger: 'blur' },
          { min: 6, max: 16, message: '登录密码长度为6-16位', trigger: 'blur' }
        ],
        name : [
          { required: true, message: '姓名不能为空', trigger: 'blur' },
          { pattern : /^[\u4e00-\u9fa5]{0,}$/, message: '姓名必须是中文汉字', trigger: 'blur'}
        ],
        phone : [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          { pattern : /^1[3-9]\d{9}$/, message: '手机号码格式有误', trigger: 'blur'}
        ],
        email : [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { pattern : /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式有误', trigger: 'blur'}
        ],
        accountNoExpired : [
          { required: true, message: '请选择账号是否未过期', trigger: 'blur' },
        ],
        credentialsNoExpired : [
          { required: true, message: '请选择密码是否未过期', trigger: 'blur' },
        ],
        accountNoLocked : [
          { required: true, message: '请选择账号是否未未锁定', trigger: 'blur' },
        ],
        accountEnabled : [
          { required: true, message: '请选择账号是否可用', trigger: 'blur' },
        ]
      },
      //定义账号未过期的数组
      options : [
        {label : '是', value : 1},
        {label : '否', value : 0}
      ],
      //定义一个userId的数组，初始值是空
      userIdArray : []
    }
  },

  //页面渲染后执行该函数钩子
  mounted() {
    this.getData(1);
  },

  methods : {
    //当选择项发生变化时会触发该函数，也就是勾选或者取消勾选checkbox的时候触发该函数
    handleSelectionChange(dataObjectArray) {
      //触发该函数的时候，把原来数组里面的id清除掉
      this.userIdArray = [];
      //循环这个数据对象的数据，把数组里面的每个对象的id取出来
      dataObjectArray.forEach( dataObject => {
        let dataId = dataObject.id; //这个数据id就是我们每一行user对象的id
        this.userIdArray.push(dataId);
      })
    },

    //获取用户分页列表数据
    getData(current) {
      doGet2("/api/users", {
        current : current //当前页，前面是参数名，后面是参数值
      }).then(resp => {
        if (resp.data.code === 200) {
          this.userList = resp.data.data.list; // resp.data  =  R 对象
          this.pageSize = resp.data.data.pageSize;
          this.total = resp.data.data.total;
        }
      })
    },

    //分页函数
    page(number) { //number值是element-plus回调时传给我们的，number这个值就是当前页
      this.getData(number);
    },

    //查看详情
    view(id) {
      //查看用户详情，跳转到/dashboard/user/6
      this.$router.push("/dashboard/user/" + id);
    },

    //点击添加用户按钮触发该函数
    addUser() {
      //弹出一个窗，然后在窗里面录入信息提交
      this.userDialogVisible = true;
      //把userQuery对象置为空
      this.userQuery = {};
    },

    //新增、编辑用户提交保存
    userSubmit() {
      this.$refs.userRefForm.validate( (valid) => {
        if (valid) {
          //验证通过了，可以提交
          let formData = new FormData();
          for (let field in this.userQuery) {
            formData.append(field, this.userQuery[field]);
          }

          if (this.userQuery.id > 0) { //编辑用户
            doPut("/api/user", formData).then( (resp) => { //获取ajax异步请求后的结果
              console.log(resp);
              if (resp.data.code === 200) {
                //编辑用户成功，提示一下
                messageTip("编辑成功", "success");
                //刷新一下页面
                //window.location.reload();//原始的js的方式刷新，是整个页面都刷新；
                this.reload();
              } else {
                //新增用户失败，提示一下
                messageTip("编辑失败", "error");
              }
            });
          } else {
            doPost("/api/user", formData).then( (resp) => { //获取ajax异步请求后的结果
              console.log(resp);
              if (resp.data.code === 200) {
                //新增用户成功，提示一下
                messageTip("新增成功", "success");
                //刷新一下页面
                //window.location.reload();//原始的js的方式刷新，是整个页面都刷新；
                this.reload();
              } else {
                //新增用户失败，提示一下
                messageTip("新增失败", "error");
              }
            });
          }
        }
      })
    },

    //编辑用户
    edit(id) {
      console.log(id)
      //编辑用户的弹窗和新增用户共用同一个弹窗
      this.userDialogVisible = true;
      //查询出该id用户的详情
      this.loadUserDetail(id);
    },

    //加载用户详情的函数
    loadUserDetail(id) {
      doGet("/api/user/" + id, {}).then(resp => {
        if (resp.data.code === 200) {
          this.userQuery = resp.data.data;
          this.userQuery.loginPwd = "";
        }
      })
    },

    //删除用户
    del(id) {
      messageConfirm("您确定要删除此数据吗？").then(() => { //当点击“确定”按钮就执行该then函数
        doDelete("/api/user/" + id, {}).then( (resp) => { //获取ajax异步请求后的结果
          if (resp.data.code === 200) {
            //删除用户成功，提示一下
            messageTip("删除成功", "success");
            //刷新一下页面
            this.reload();
          } else {
            //删除用户失败，提示一下
            messageTip("删除失败，原因：" + resp.data.msg, "error");
          }
        });
      }).catch(() => { //当点击“取消”按钮就执行该catch函数
        messageTip("取消删除", "warning");
      })
    },

    //批量删除用户
    batchDelUser() {
      if (this.userIdArray.length <= 0) {
        messageTip("请选择要删除的数据", "warning");
        return;
      }
      messageConfirm("您确定要删除这些数据吗？").then(() => {
        // userIdArray [1,3,5,6,12,15] ==>  ids = "1,3,5,6,12,15";
        let ids = this.userIdArray.join(",");
        doDelete("/api/user/batch", {
          ids : ids
        }).then( resp => {
          if (resp.data.code === 200) {
            //批量删除用户成功，提示一下
            messageTip("批量删除成功", "success");
            //刷新一下页面
            this.reload();
          } else {
            //批量删除用户失败，提示一下
            messageTip("批量删除失败，原因：" + resp.data.msg, "error");
          }
        })
      }).catch(() => { //当点击“取消”按钮就执行该catch函数
        messageTip("取消批量删除", "warning");
      })
    }
  }
}
</script>

<style scoped>
.btn {
  margin-bottom: 10px;
}
</style>