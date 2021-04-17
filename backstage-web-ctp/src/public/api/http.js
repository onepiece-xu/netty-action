import {
    Notification,
    MessageBox
} from 'element-ui';
import Vue from 'vue';
import axios from 'axios';

Vue.prototype.$_ajax = axios;
axios.interceptors.request.use(function (config) {
    // Do something before request is sent
    // var awstackUser;
    config.url = window.GLOBALCONFIG.APIHOST.API + config.url;
    var auth_token = localStorage.$AUTH_TOKEN;
    config.headers["Authorization"] = auth_token;
    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});

axios.interceptors.response.use(function (res) {
    // Do something with response data
    
    if (res.data.code != 200) {
        Notification({
            //title: $_I18n.t("cn.action.error"),
            //message: res.data.msg,
            message: res.data.msg,
            duration: 5000,
            type: "error",
            dangerouslyUseHTMLString: true,
            customClass: "error"
        });
        return res.data;
    };
    if (res.config.method.toUpperCase() != "GET") {
        Notification({
            title: '',
            message: $_I18n.t("cn.code.0"),
            duration: 3000,
            type: "success",
            customClass: "success"
        });
        return res.data;
    }
    return res.data;
}, function (error) {
    // Do something with response error
    if (error.response) {
        switch (error.response.status) {
        case 401:
            // 返回 401 清除token信息并跳转到登录页面
            MessageBox.alert('', '登录已失效,请重新登录！', {
                confirmButtonText: '确定',
                type: 'warning',
                center: true,
                showClose:false,
                callback: () => {
                    localStorage.clear();
                    window.location.href = "#/login";
                }
            })
            break;
        default:
            Message({
                message: error.message,
                type: 'error'
            });
        }
    }
    return error;
});
export default axios