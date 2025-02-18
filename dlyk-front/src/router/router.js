//从vue-router框架导入createRouter(), createWebHistory()函数
import { createRouter, createWebHistory } from "vue-router";

//创建一个路由对象
let router = createRouter({
    //配置路由的历史
    history: createWebHistory(),

    //配置路由的路径和组件，是数组，可以配置多个路由
    routes :[
        {
            //路由的路径
            path : '/',
            //路由路径所对应的vue页面
            component : () => import('../view/LoginView.vue'),
        },
        {
            path: '/dashboard',
            component: () => import('../view/DashboardView.vue'),
            //配置子路由，子路由可以配置多个
            children : [
                {
                    //子路由不能以斜杆开头
                    path : 'user',
                    //当访问 /dashboard/user 路由的时候，就渲染显示UserView.vue页面
                    component : () => import('../view/UserView.vue'),
                },

            ]
        }
]
            //配置子路由，子路由可以配置多个
    //         children : [
    //             {
    //                 //子路由不能以斜杆开头
    //                 path : '',
    //                 //当访问 /dashboard/ 路由的时候，就渲染显示StatisticView.vue页面
    //                 component : () => import('../view/StatisticView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头
    //                 path : 'user',
    //                 //当访问 /dashboard/user 路由的时候，就渲染显示UserView.vue页面
    //                 component : () => import('../view/UserView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头，这个叫动态路由，id是一个变量，值是动态的
    //                 path : 'user/:id',
    //                 //当访问 /dashboard/user/6 路由的时候，就渲染显示UserDetailView.vue页面
    //                 component : () => import('../view/UserDetailView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头
    //                 path : 'activity',
    //                 //当访问 /dashboard/activity 路由的时候，就渲染显示ActivityView.vue页面
    //                 component : () => import('../view/ActivityView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头，这个叫动态路由，id是一个变量，值是动态的
    //                 path : 'activity/:id',
    //                 //当访问 /dashboard/activity/6 路由的时候，就渲染显示ActivityDetailView.vue页面
    //                 component : () => import('../view/ActivityDetailView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头
    //                 path : 'activity/add',
    //                 //当访问 /dashboard/activity/add 路由的时候，就渲染显示ActivityItemView.vue页面
    //                 component : () => import('../view/ActivityItemView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头，这个叫动态路由，id是一个变量，值是动态的
    //                 path : 'activity/edit/:id',
    //                 //当访问 /dashboard/activity/edit/5 路由的时候，就渲染显示ActivityItemView.vue页面
    //                 component : () => import('../view/ActivityItemView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头
    //                 path : 'clue',
    //                 //当访问 /dashboard/clue 路由的时候，就渲染显示ClueView.vue页面
    //                 component : () => import('../view/ClueView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头
    //                 path : 'clue/add',
    //                 //当访问 /dashboard/clue/add 路由的时候，就渲染显示ClueItemView.vue页面
    //                 component : () => import('../view/ClueItemView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头，这个叫动态路由，id是一个变量，值是动态的 （id：线索id）
    //                 path : 'clue/edit/:id',
    //                 //当访问 /dashboard/clue/edit/3 路由的时候，就渲染显示ClueItemView.vue页面
    //                 component : () => import('../view/ClueItemView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头，这个叫动态路由，id是一个变量，值是动态的
    //                 path : 'clue/:id',
    //                 //当访问 /dashboard/clue/5 路由的时候，就渲染显示ClueDetailView.vue页面
    //                 component : () => import('../view/ClueDetailView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头
    //                 path : 'customer',
    //                 //当访问 /dashboard/customer 路由的时候，就渲染显示CustomerView.vue页面
    //                 component : () => import('../view/CustomerView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头，这个叫动态路由，id是一个变量，值是动态的
    //                 path : 'customer/detail/:id',
    //                 //当访问 /dashboard/customer/detail/5 路由的时候，就渲染显示CustomerDetailView.vue页面
    //                 component : () => import('../view/CustomerDetailView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头
    //                 path : 'tran',
    //                 //当访问 /dashboard/tran 路由的时候，就渲染显示TranView.vue页面
    //                 component : () => import('../view/TranView.vue'),
    //             },
    //             {
    //                 //子路由不能以斜杆开头，这个叫动态路由，id是一个变量，值是动态的
    //                 path : 'tran/detail/:id',
    //                 //当访问 /dashboard/tran/detail/5 路由的时候，就渲染显示TranDetailView.vue页面
    //                 component : () => import('../view/TranDetailView.vue'),
    //             }
    //         ]
    //     }
    // ]
})
//把路由对象导出，default只能导出一个
export default router;