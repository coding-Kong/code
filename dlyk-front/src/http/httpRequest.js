//从axios框架导入axios组件
import axios from "axios";
import {clearToken, jwtName, messageConfirm, messageTip} from "../util/utils.js";

//axios请求后端接口时的接口路径前缀
axios.defaults.baseURL = "http://localhost:8082";
//axios.defaults.baseURL = "http://192.168.194.131:8089";

//get和delete代码一样
//post和put代码一样
//它们都有两种写法

/**
 * 封装get请求
 *
 * @param url ： "/api/freeLogin"
 * @param params :  {
 *                    ID: 12345,
 *                    NAME : 'zhangsan'
 *                }
 */
export function doGet(url, params) {
    return axios.get(url, { //发送ajax异步请求
        params: params
    })
}

export function doGet2(url,params) {
    return axios({
        method: 'get',
        url: url,
        params: params,
        dataType: 'json',
    })
}

/**
 *
 * @param url ： /api/user
 * @param params :  {
 *             ID: 12345,
 *             NAME : 'zhangsan'
 *         }
 */
export function doDelete(url, params) {
    return axios.delete(url, { //发送ajax异步请求
        params: params
    })
}

export function doDelete2(url,params) {
    axios({
        method: 'delete',
        url: url,
        params: params,
        dataType: 'json',
    })
}

/**
 *
 * @param url : '/api/login'
 * @param data : {
 *                  firstName: 'Fred',
 *                  lastName: 'Flintstone'
 *               }
 *               这个data参数往后端传的时候，是一个json，json参数那么后端接收的时候需要使用@RequestBody注解
 */
export function doPost(url, data) {
    return axios.post(url, data); //发送ajax异步请求
}

export function doPost2(url,data) {
    // 发起一个post请求
    return axios({
        method: 'post',
        url: url,
        data: data,
        dataType: 'json'
    })
}

    /**
     * 封装put请求，put请求和post请求代码写法一样的
     *
     * @param url
     * @param data
     * @returns {Promise<axios.AxiosResponse<any>>}
     */
    export function doPut(url, data) {
        return axios.put(url, data); //发送ajax异步请求
    }

    export function doPut2(url, data) {
        return axios({
            method: 'put',
            url: url,
            data: data,
            dataType: 'json'
        });
    }

// 添加请求拦截器
axios.interceptors.request.use( (config) => {
    // 在发送请求之前做些什么
    let token = window.sessionStorage.getItem(jwtName());
    if (!token) {
        token = window.localStorage.getItem(jwtName());
        if (token) { //说明用户选择了记住我
            config.headers['rememberMe'] = true;
        }
    }
    if (token) {
        //在请求头中放了一个token，后端就可以从请求头中接收到该token
        //let tokenObject = JSON.parse(token);
        config.headers['Authorization'] = token;
    }
    return config;
},  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use( (response) => {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    // 提示一下token不合法的原因
    if (response.data.code > 900) { //code码大于900都是token问题
        messageConfirm(response.data.msg + ", 是否重新去登录？").then(() => { //当点击“确定”按钮就执行该then函数
            //去重新登录，把浏览器的token清理一下
            clearToken();
            //跳到登录页
            window.location.href = "/";
        }).catch(() => { //当点击“取消”按钮就执行该catch函数
            messageTip("取消去登录", "warning");
        })
    }
    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
});