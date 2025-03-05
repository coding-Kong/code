import { createApp } from 'vue'
// import './style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 引入element-plus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue' //根组件
//导入路由组件
import router from './router/router.js'
// 引入element-plus中文包
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

let app = createApp(App);

//注册图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus,{locale: zhCn}).use(router).mount('#app')
