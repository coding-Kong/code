<template>
  <el-form ref="activityRefForm" :model="activityQuery" :title="activityQuery.id>0?'编辑活动':'新增活动'" label-width="120px" :rules="activityRules">
    <el-form-item label="负责人" prop="ownerId">
      <el-select
          v-model="activityQuery.ownerId"
          placeholder="请选择负责人"
          clearable
          style="width:100%;">
        <el-option
            v-for="item in ownerOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
      </el-select>
    </el-form-item>

    <el-form-item label="活动名称" prop="name">
      <el-input v-model="activityQuery.name"/>
    </el-form-item>

    <el-form-item label="开始时间" prop="startTime">
      <el-date-picker
          v-model="activityQuery.startTime"
          type="datetime"
          placeholder="请选择开始时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          clearable
          style="width: 100%;"/>
    </el-form-item>

    <el-form-item label="结束时间" prop="endTime">
      <el-date-picker
          v-model="activityQuery.endTime"
          type="datetime"
          placeholder="请选择结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          clearable
          style="width: 100%;"/>
    </el-form-item>

    <el-form-item label="活动预算" prop="cost">
      <el-input v-model="activityQuery.cost"/>
    </el-form-item>

    <el-form-item label="活动描述" prop="description">
      <el-input
          v-model="activityQuery.description"
          :rows="8"
          type="textarea"
         />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="activitySubmit">提 交</el-button>
      <el-button type="success" plain @click="goBack">返 回</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {doGet, doPost, doPost2, doPut} from "../http/httpRequest";
import {goBack, messageTip} from "../util/utils";

export default {
  name: "ActivityItemView",

  //注入
  inject : ['reload'],

  data() {
    return {
      //市场活动表单对象，初始值是空
      activityQuery : {},
      //定义市场活动负责人的下拉选项数据（是一个数组），初始值是空
      ownerOptions : [{}],
      //定义录入市场活动表单验证规则
      activityRules : {
        ownerId : [
          { required: true, message: '请选择负责人', trigger: 'blur' },
        ],
        name : [
          { required: true, message: '活动名称不能为空', trigger: 'blur' },
          { min: 5, max: 128, message: '活动名称长度范围为5-128个字符', trigger: 'blur' }
        ],
        startTime : [
          { required: true, message: '请选择开始时间', trigger: 'blur' },
        ],
        endTime : [
          { required: true, message: '请选择结束时间', trigger: 'blur' },
        ],
        cost : [
          { required: true, message: '请输入活动预算', trigger: 'blur' },
          { pattern : /^[0-9]+(\.[0-9]{2})?$/, message: '活动预算必须是整数或者两位小数', trigger: 'blur'}
        ],
        description : [
          { required: true, message: '活动描述不能为空', trigger: 'blur' },
          { min: 5, max: 255, message: '活动名称长度范围为5-255个字符', trigger: 'blur' }
        ]
      }
    }
  },

  mounted() {
    this.loadOwner();
    this.loadActivityDetail();
  },

  methods : {
    //声明一下返回函数
    goBack,

    //加载负责人
    loadOwner() {
      doGet("/api/user/owner", {}).then( resp => {
        if (resp.data.code === 200) {
          this.ownerOptions = resp.data.data;
        }
      })
    },

    //录入、编辑市场活动提交保存
    activitySubmit() {
      this.$refs.activityRefForm.validate( (isValid) => {
        if (isValid) {
          //验证通过了，可以提交
          let formData = new FormData();
          for (let field in this.activityQuery) {
            /*if (this.activityQuery[field]) {
              formData.append(field, this.activityQuery[field]);
            }*/
            formData.append(field, this.activityQuery[field]);
          }
          //判断是新增还是编辑
          if (this.activityQuery.id > 0) {//编辑
            doPut("/api/activity", formData).then( (resp) => { //获取ajax异步请求后的结果
              console.log(resp);
              if (resp.data.code === 200) {
                //编辑成功，提示一下
                messageTip("编辑成功", "success");
                //跳转到活动列表页
                this.$router.push("/dashboard/activity");
              } else {
                //编辑失败，提示一下
                messageTip("编辑失败", "error");
              }
            });
          } else {
            doPost2("/api/activity", formData).then( (resp) => { //获取ajax异步请求后的结果
              console.log(resp);
              if (resp.data.code === 200) {
                //录入成功，提示一下
                messageTip("录入成功", "success");
                //跳转到活动列表页
                this.$router.push("/dashboard/activity");
              } else {
                //录入失败，提示一下
                messageTip("录入失败", "error");
              }
            });
          }
        }
      })
    },

    //编辑时，查询出对应id的市场活动数据详情
    loadActivityDetail() {
      let id = this.$route.params.id;
      if (id) { //id存在，id不是空，id不是undefined
        //说明是编辑，需要查询一下数据
        doGet("/api/activity/" + id, {}).then(resp => {
          if (resp.data.code === 200) {
            this.activityQuery = resp.data.data;
          }
        })
      }
    }
  }
}
</script>

<style scoped>

</style>