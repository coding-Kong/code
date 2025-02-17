import {ElMessage, ElMessageBox} from "element-plus";

/**
 * 返回jwt的名字
 *
 * @returns {string}
 */
export function jwtName() {
    return "userToken";
}

/**
 * 封装的消息提示函数
 *
 * @param msg
 * @param type
 */
export function messageTip(msg, type) {
    ElMessage({
        showClose: true, //是否展示关闭按钮
        message: msg, //提示消息
        type: type, //提示类型,'success' | 'warning' | 'info' | 'error'
        duration : 4000 //消息提示几秒后消失，单位是毫秒
    })
}

/**
 * 清理浏览器存储的token
 *
 */
export function clearToken() {
    window.sessionStorage.removeItem(jwtName());
    window.localStorage.removeItem(jwtName());
}

/**
 * 封装确认消息提示
 *
 * @param msg
 * @returns {Promise<MessageBoxData>}
 */
export function messageConfirm(msg) {
    return ElMessageBox.confirm(
        msg,
        '系统提示',
        {
            confirmButtonText: '确 定',
            cancelButtonText: '取 消',
            type: 'warning',
        }
    )
}

/**
 * 封装返回功能
 */
export function goBack() {
    this.$router.go(-1);
}

/**
 * 封装的获取登录Token的函数
 *
 * @returns {string}
 */
export function getToken() {
    let token = window.sessionStorage.getItem(jwtName());
    if (!token) {
        token = window.localStorage.getItem(jwtName());
    }
    if (!token) {//两个地方都没拿到，说明在浏览器客户端没有登录
        messageConfirm("Token不存在, 是否重新去登录？").then(() => { //当点击“确定”按钮就执行该then函数
            //去重新登录，把浏览器的token清理一下
            clearToken();
            //跳到登录页
            window.location.href = "/";
        }).catch(() => { //当点击“取消”按钮就执行该catch函数
            messageTip("取消去登录", "warning");
        })
    } else {
        return token;
    }
}