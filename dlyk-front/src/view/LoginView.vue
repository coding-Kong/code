<template>
  <el-container>
    <!--左侧-->
    <el-aside width="200px">
      <img src="../assets/loginBox.svg" class="asideImg">
      <p class="asideTitle">
        欢迎使用动力云客系统
      </p>
    </el-aside>
    <!--右侧-->
    <el-main>
      <div class="mainTitle">欢迎登录</div>
      <div class="mainForm">
        <el-form ref="loginRefForm" :model="user" label-width="120px" :rules="loginRules"><!--验证第一步-->
          <el-form-item label="账号" prop="loginAct">
            <el-input v-model="user.loginAct" />
          </el-form-item>

          <el-form-item label="密码" prop="loginPwd">
            <el-input type="password" v-model="user.loginPwd" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="login">登 录</el-button>
          </el-form-item>

          <el-form-item>
            <el-checkbox v-model="user.rememberMe" label="记住我" size="large" />
          </el-form-item>
        </el-form>
      </div>
    </el-main>
    </el-container>
</template>

<script>
import {doGet, doGet2, doPost2} from "../http/httpRequest";
import {clearToken, jwtName, messageTip} from "../util/utils.js";

export default {
  name: "LoginView",
  data() {
    //一定要return，代码结构是固定的
    return {
      //表单数据对象,定义对象用{},一般初始值都为空，具体值需要从后端查询后再赋值，此处只是定义一下
      user: {},
      age: 0,
      name: '',
      arr: [],
      userList: [{}],
      //定义form表单的验证规则对象
      loginRules: {
        //验证登录账号字段
        loginAct: [
          {required: true, message: '请输入登录账号', trigger: 'blur'}
        ],
        //验证登录密码字段
        loginPwd: [
          {required: true, message: '请输入登录密码', trigger: 'blur'},
          {min: 6,max: 16, message: '登录密码长度为6-16位', trigger: 'blur'}

        ]

      }
    }
  },
  mounted() {
    this.freeLogin();
  },
  methods: {
    //登录方法
    login: function () {
      //验证表单是否合法
      this.$refs.loginRefForm.validate((isValid) => {
        if (isValid) {
          //验证通过，调用后台接口
          let formData = new FormData();
          formData.append("loginAct", this.user.loginAct);
          formData.append("loginPwd", this.user.loginPwd);
          formData.append("rememberMe", this.user.rememberMe);
          doPost2("/api/login",formData).then((response)=>{
            //获取ajax异步请求后的结果
            console.log(response);
            if(response.data.code===200){

              //封装的消息提示组件
              messageTip("登录成功", "success");

              //清除token
              clearToken();
              if(this.user.rememberMe===true){
                window.localStorage.setItem(jwtName(),response.data.data);
              }else {
                window.sessionStorage.setItem(jwtName(),response.data.data);
              }
              window.location.href = "/dashboard";
            }else {
              messageTip("登录失败","error");
            }
          });
        }
      })
    },
    freeLogin(){
      let token = window.localStorage.getItem(jwtName());
      if(token){
        doGet2("api/login/free",{}).then(resp=>{
          if(resp.data.code===200){
            window.location.href = "/dashboard";
          }
        })
      }
    }
  }
}
</script>

<style scoped>

.el-aside {
  background: black;
  width: 40%;
  text-align: center;
}
.el-main {
  height: calc(100vh);
}
.asideImg {
  width: 400px;
}
.asideTitle {
  color: white;
  font-size: 28px;
}
.mainTitle {
  text-align: center;
  margin-top: 100px;
  margin-bottom: 25px;
  font-weight: bold;
}
.mainForm {
  text-align: center;
  width: 60%;
  margin: auto;
}
.el-button {
  width: 100%;
}
</style>