import $http from "./http";
// import qs from "qs";

// 登录
export const userLoginIn = (data) => {
    return $http({
        url: `/ctp-paas-control/login/login`,
        method: "POST",
        data:data
    });
};
// 登出
export const userLoginOut = (data) => {
    return $http({
        url: `/ctp-paas-control/login/logout`,
        method: "POST",
        data:data
    });
};