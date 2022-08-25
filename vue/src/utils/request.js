import axios from 'axios'
import router from "@/router";

const request = axios.create({
    baseURL: '/api',
    timeout: 10000
})

function getCookie(cookie_name) {
    const allCookies = document.cookie;
    let cookie_pos = allCookies.indexOf(cookie_name);
    if (cookie_pos !== -1) {
        cookie_pos = cookie_pos + cookie_name.length + 1;
        let cookie_end = allCookies.indexOf(";", cookie_pos);
        if (cookie_end === -1) {
            cookie_end = allCookies.length;
        }
        var value = unescape(allCookies.substring(cookie_pos, cookie_end));
    }
    return value;
}

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    if(getCookie("user")) {
        config.headers['token'] = JSON.parse(getCookie("user")).token;  // 设置请求头
    }
    // 取出sessionStorage里面缓存的用户信息
    let userJSON = sessionStorage.getItem("user")
    if(!userJSON) {
        router.push("/login")
    }
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

