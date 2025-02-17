import { createApp } from 'vue'
// import './style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//import LoginView from './view/LoginView.vue'
import App from './App.vue'
import router from './router/router.js'

let app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus).use(router).mount('#app')
