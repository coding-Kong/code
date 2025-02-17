<template>
  <el-container>
    <!--左侧菜单-->
    <el-aside :width="isCollapse ? '64px' : '200px'">
      <div class="menuTitle" @click="dashboard">@动力云客管理系统</div>
      <el-menu
          active-text-color="#ffd04b"
          background-color="#334157"
          :default-active="activeRouterIndex"
          text-color="#ffffff"
          :unique-opened="true"
          :collapse="isCollapse"
          :collapse-transition="false"
          :router="true"
          style="border-right: solid 0px">

        <!--市场活动菜单(测试)-->
        <el-sub-menu :index="index+1" v-for="(menu, index) in permissionMenuList" :key="menu.id">
          <template #title>
            <el-icon><component :is="menu.icon"></component></el-icon>
            <span>{{menu.name}}</span>
          </template>
          <el-menu-item :index="subMenu.url" v-for="subMenu in menu.permissionPOList" :key="subMenu.id">
            <el-icon><component :is="subMenu.icon"></component></el-icon>
            {{subMenu.name}}
          </el-menu-item>
        </el-sub-menu>

      </el-menu>
    </el-aside>

    <!--右侧内容体-->
    <el-container class="rightContainer">
      <!--右侧顶部-->
      <el-header>
        <el-icon @click="menuShow"><Fold /></el-icon>

        <el-dropdown @command="handleCommand" style="float: right; line-height: 38px;">
          <span class="el-dropdown-link">
            {{ loginUserName }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="a">我的资料</el-dropdown-item>
              <el-dropdown-item command="b">修改密码</el-dropdown-item>
              <el-dropdown-item command="e" divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <!--右侧内容体-->
      <el-main>
        <!--/dashboard子路由的页面在此处显示-->
        <router-view v-if="isRouterAlive"/>
      </el-main>

      <!--右侧底部-->
      <el-footer>
        @版权所有 2009-2099 动力节点 北京市通州区马驹桥镇景盛中街17号顺景总部公元B4栋
      </el-footer>
    </el-container>

  </el-container>
</template>

<script>
import {doGet, doGet2} from "../http/httpRequest";
import {clearToken} from "../util/utils";

export default {
  name: "DashboardView",

  //所有页面上使用的变量都需要再data()中定义
  data() {
    return {
      //定义菜单是展开还是折叠，false是展开，true是折叠
      isCollapse : false,
      //登录人的姓名
      loginUserName : '',
      //定义变量，控制右侧内容体是否显示，true显示，false隐藏
      isRouterAlive : true,
      //激活哪个路由菜单，也就是展开哪个菜单，初始值是空
      activeRouterIndex : '',
      //菜单权限List，初始值是空
      permissionMenuList : [{
        //permissionPOList : [{}]
      }]
    }
  },

  //在父页面提供一些属性、函数...等等，那么在其他子页面就可以注入使用
  provide() {
    return {
      //提供一个函数 (页面局部刷新函数)
      reload : () => {
        this.isRouterAlive = false;
        this.$nextTick(function () {
          this.isRouterAlive = true;
        })
      },

      //提供一个属性
      phone : '13720202020',

      //提供一个数组
      idArray : [ 1,2,3,4,5 ],

      //提供一个对象
      user : {id : 10389, name : '张三', age : 18}
    }
  },

  //vue的生命周期钩子函数，在页面渲染后会执行
  mounted() {
    this.loadLoginInfo();
    this.activeMenuIndex();
  },

  methods : {
    //菜单的展开和折叠函数
    menuShow() {
      this.isCollapse = !this.isCollapse;
    },

    //退出登录
    logout() {
      doGet2("/api/logout", {}).then( (resp) => { //获取ajax异步请求后的结果
        console.log(resp);
        if (resp.data.code === 200) {
          //退出成功，清理前端的token
          clearToken();
          //跳转到登录页
          window.location.href = "/";
        }
      }).catch( (error) => { //当发生异常执行该catch函数
        console.log(error);
      }).finally( () => { //总是会执行
        // 总是会执行
      });
    },

    //加载登录人的信息
    loadLoginInfo() {
      doGet("/api/login/info", {}).then(resp => {
        if (resp.data.code === 200) {
          this.loginUserName = resp.data.data.name;
          this.loginUserId = resp.data.data.id;
          this.loadPermissionMenuList(this.loginUserId);
        }
      })
    },

    //激活展开当前路由所对应的菜单
    activeMenuIndex() {
      //拿到当前访问的路由路径
      let path = this.$route.path;
      //  /dashboard/user/1 包含3段，转换成2段： /dashboard/user
      let pathArr = path.split("/");  // -->  [, 'dashboard', 'user', '1' ]
      if (pathArr.length >= 3) {
        this.activeRouterIndex = "/" +  pathArr[1] + "/" + pathArr[2];
      }
    },

    //进入驾驶舱、仪表盘(数据统计页面)
    dashboard() {
      this.$router.push("/dashboard");
    },

    //加载权限菜单列表
    loadPermissionMenuList(loginUserId) {
      doGet("/api/user/" + loginUserId + "/menu", {}).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp)
          this.permissionMenuList = resp.data.data;
        }
      })
    }
  }
}
</script>

<style scoped>
.el-aside {
  background: black;
}
.el-header {
  background: azure;
  height: 38px;
  line-height: 38px;
}
.el-footer {
  background: aliceblue;
  height: 38px;
  line-height: 38px;
  text-align: center;
}
.rightContainer {
  height: calc(100vh);
}
.menuTitle {
  color: white;
  height: 38px;
  line-height: 38px;
  text-align: center;
  cursor: pointer;
}
</style>