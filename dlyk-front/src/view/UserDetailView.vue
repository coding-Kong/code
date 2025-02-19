<template>
  <el-form :model="userDetail" label-width="120px">
    <el-form-item label="ID">
      <el-input v-model="userDetail.id" disabled/>
    </el-form-item>

    <el-form-item label="账号">
      <el-input v-model="userDetail.loginAct" disabled/>
    </el-form-item>

    <el-form-item label="密码">
      <el-input type="password" v-model="userDetail.loginPwd" disabled/>
    </el-form-item>

    <el-form-item label="姓名">
      <el-input v-model="userDetail.name" disabled/>
    </el-form-item>

    <el-form-item label="手机">
      <el-input v-model="userDetail.phone" disabled/>
    </el-form-item>

    <el-form-item label="邮箱">
      <el-input v-model="userDetail.email" disabled/>
    </el-form-item>

    <el-form-item label="账号未过期">
      <el-input :model-value="userDetail.accountNoExpired === 1 ? '是' : '否'" disabled/>
    </el-form-item>

    <el-form-item label="密码未过期">
      <el-input :model-value="userDetail.credentialsNoExpired === 1 ? '是' : '否'" disabled/>
    </el-form-item>

    <el-form-item label="账号未锁定">
      <el-input :model-value="userDetail.accountNoLocked === 1 ? '是' : '否'" disabled/>
    </el-form-item>

    <el-form-item label="账号是否可用">
      <el-input :model-value="userDetail.accountEnabled === 1 ? '是' : '否'" disabled/>
    </el-form-item>

    <el-form-item label="创建时间">
      <el-input v-model="userDetail.createTime" disabled/>
    </el-form-item>

    <el-form-item label="创建人">
      <el-input v-model="userDetail.createByDO.name" />
    </el-form-item>

    <el-form-item label="编辑时间">
      <el-input v-model="userDetail.editTime" disabled/>
    </el-form-item>

    <el-form-item label="编辑人">
      <el-input v-model="userDetail.editByDO.name" />
    </el-form-item>

    <el-form-item label="最近登录时间">
      <el-input v-model="userDetail.lastLoginTime" disabled/>
    </el-form-item>

    <el-form-item>
      <el-button type="success" @click="goBack">返 回</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {doGet, doGet2} from "../http/httpRequest";
import {goBack} from "../util/utils";

export default {
  name: "UserDetailView",

  data() {
    return {
      //用户详情对象,初始值是空
      userDetail : {
        createByDO : {},
        editByDO : {}
      }
    }
  },

  //页面渲染后执行该钩子函数
  mounted() {
    this.loadUserDetail();
  },

  methods : {
    //声明一下该方法
    goBack,

    //加载用户详情的函数
    loadUserDetail() {
      //从动态路由地址中获取用户id参数
      let id = this.$route.params.id; //id参数名要和路由中配置的path : 'user/:id', 的参数名相同
      doGet2("/api/user/" + id, {}).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp.data.data);
          this.userDetail = resp.data.data;
          if(!this.userDetail.createByDO){
            this.userDetail.createByDO = {};
          }
          if(!this.userDetail.editByDO){
            this.userDetail.editByDO = {};
          }
        }
      })
    }
  }
}
</script>

<style scoped>

</style>