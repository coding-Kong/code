<template>
  <el-form ref="activityRefForm" :inline="true" :model="activityQuery" :rules="activityRules">
    <el-form-item label="负责人" class="options">
      <el-select
          v-model="activityQuery.ownerId"
          placeholder="请选择负责人"
          clearable
          @click="loadOwner">
        <el-option
            v-for="item in ownerOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
      </el-select>
    </el-form-item>

    <el-form-item label="活动名称">
      <el-input v-model="activityQuery.name" placeholder="请输入活动名称" clearable />
    </el-form-item>

    <el-form-item label="开始时间">
      <el-date-picker
          v-model="activityQuery.startTime"
          type="datetime"
          placeholder="请选择开始时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          clearable/>
    </el-form-item>

    <el-form-item label="结束时间">
      <el-date-picker
          v-model="activityQuery.endTime"
          type="datetime"
          placeholder="请选择结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          clearable/>
    </el-form-item>

    <el-form-item label="活动预算" prop="cost">
      <el-input v-model="activityQuery.cost" placeholder="请输入活动预算" clearable />
    </el-form-item>

    <el-form-item label="创建时间">
      <el-date-picker
          v-model="activityQuery.createTime"
          type="datetime"
          placeholder="请选择创建时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          clearable/>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="onSearch">搜 索</el-button>
      <el-button type="primary" plain @click="onReset">重 置</el-button>
    </el-form-item>
  </el-form>

  <el-button type="primary" class="btn" @click="addActivity">录入市场活动</el-button>
  <el-button type="danger" class="btn" @click="batchDelActivity">批量删除</el-button>

  <el-table
      :data="activityList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="50"/>
    <el-table-column type="index" label="序号" width="65"/>
    <el-table-column property="ownerDO.name" label="负责人"/>
    <el-table-column property="name" label="活动名称"/>
    <el-table-column property="startTime" label="开始时间"/>
    <el-table-column property="endTime" label="结束时间"/>
    <el-table-column property="cost" label="活动预算"/>
    <el-table-column property="createTime" label="创建时间"/>
    <el-table-column label="操作" width="230">
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
        <el-button type="success" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
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

</template>

<script>
import {doDelete, doGet, doGet2} from "../http/httpRequest";
import {messageConfirm, messageTip} from "../util/utils";

export default {
  name: "ActivityView",

  //注入
  inject : ['reload'],

  data() {
    return {
      //定义页面form市场活动搜索的表单对象，初始值是空
      activityQuery : {},
      //定义市场活动负责人的下拉选项数据（是一个数组），初始值是空
      ownerOptions : [{}],
      //市场活动List数据，初始值是空
      activityList : [{
        ownerDO : {}
      }],
      //分页时每页显示多少条数据
      pageSize : 0,
      //总共有多少条
      total : 0,
      //定义搜索表单的验证规则
      activityRules : {
        //验证活动预算字段
        cost : [
            //正则表达式，从网上找，或者AI工具找，找到后需要测试一下，因为有可能找到的正则有问题
          { pattern : /^[0-9]+(\.[0-9]{2})?$/, message: '活动预算必须是整数或者两位小数', trigger: 'blur'}
        ]
      },
      //活动id的数组，初始值是空
      activityIdArray : []
    }
  },

  //页面渲染后执行该函数钩子
  mounted() {
    this.getData(1);
  },

  methods : {
    //加载负责人
    loadOwner() {
      doGet("/api/user/owner", {}).then( resp => {
        if (resp.data.code === 200) {
          this.ownerOptions = resp.data.data;
        }
      })
    },

    //获取市场活动分页列表数据
    getData(current) {
      doGet2("/api/activitys", {
        current : current, //当前页，前面是参数名，后面是参数值

        //带上6个搜索条件
        ownerId : this.activityQuery.ownerId,
        name : this.activityQuery.name,
        startTime : this.activityQuery.startTime,
        endTime : this.activityQuery.endTime,
        cost : this.activityQuery.cost,
        createTime : this.activityQuery.createTime
      }).then(resp => {
        if (resp.data.code === 200) {
          this.activityList = resp.data.data.list; // resp.data  =  R 对象
          this.pageSize = resp.data.data.pageSize;
          this.total = resp.data.data.total;
        }
      })
    },

    //分页函数
    page(number) { //number值是element-plus回调时传给我们的，number这个值就是当前页
      this.getData(number);
    },

    //搜索函数
    onSearch() {
      this.$refs.activityRefForm.validate( (valid) => {
        if (valid) {
          //带上搜索条件去分页查询
          this.getData(1);
        }
      })
    },

    //重置
    onReset() {
      this.activityQuery = {}
    },

    //查看市场活动详情
    view(id) {
      //查看市场活动详情，跳转到/dashboard/activity/6
      this.$router.push({path:'/dashboard/activity/' + id});
    },

    //录入市场活动
    addActivity() {
      //跳转到此路由上
      this.$router.push("/dashboard/activity/add");
    },

    //编辑市场活动
    edit(id) {
      //跳转到此路由上
      this.$router.push("/dashboard/activity/edit/" + id);
    },

    //删除市场活动
    del(id) {
      messageConfirm("您确定要删除此数据吗？").then(() => { //当点击“确定”按钮就执行该then函数
        doDelete("/api/activity/" + id, {}).then( (resp) => { //获取ajax异步请求后的结果
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
    batchDelActivity() {
      if (this.activityIdArray.length <= 0) {
        messageTip("请选择要删除的数据", "warning");
        return;
      }
      messageConfirm("您确定要删除这些数据吗？").then(() => {
        // userIdArray [1,3,5,6,12,15] ==>  ids = "1,3,5,6,12,15";
        let ids = this.activityIdArray.join(",");
        doDelete("/api/activity/batch", {
          ids : ids
        }).then( resp => {
          if (resp.data.code === 200) {
            //批量删除成功，提示一下
            messageTip("批量删除成功", "success");
            //刷新一下页面
            this.reload();
          } else {
            //批量删除失败，提示一下
            messageTip("批量删除失败，原因：" + resp.data.msg, "error");
          }
        })
      }).catch(() => { //当点击“取消”按钮就执行该catch函数
        messageTip("取消批量删除", "warning");
      })
    },

    //当选择项发生变化时会触发该函数，也就是勾选或者取消勾选checkbox的时候触发该函数
    handleSelectionChange(dataObjectArray) {
      //触发该函数的时候，把原来数组里面的id清除掉
      this.activityIdArray = [];
      //循环这个数据对象的数据，把数组里面的每个对象的id取出来
      dataObjectArray.forEach( dataObject => {
        let dataId = dataObject.id; //这个数据id就是我们每一行user对象的id
        this.activityIdArray.push(dataId);
      })
    },
  }
}
</script>

<style scoped>
.btn {
  margin-top: 20px;
  margin-bottom: 10px;
}
.options{

}
</style>