import $http from "./http";
import qs from "qs";


// 流服务策略列表
export const getStrategyList = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/appStrategy/getStrategyList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 新建流服务策略
export const createStrategy = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/appStrategy/createStrategy`,
        method: "POST",
        data:data
    });
};
// 编辑流服务策略
export const updateStrategy = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/appStrategy/updateStrategy`,
        method: "POST",
        data:data
    });
};
// 删除流服务策略
export const delStrategy = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/appStrategy/delStrategy`,
        method: "POST",
        data:data
    });
};
// 流策略已绑定应用列表 
export const getBindApps = (params,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/appStrategy/getBindApps/${id}`,
        method: "get",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 流策略未绑定应用列表 
export const getUnBindApps = (params,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/appStrategy/getUnBindApps/${id}`,
        method: "get",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 绑定应用
export const bindStrategy = (data,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/appStrategy/bindStrategy/${id}`,
        method: "POST",
        data:data
    });
};
// 解绑应用
export const UnbindStrategy = (data,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/appStrategy/UnbindStrategy/${id}`,
        method: "POST",
        data:data
    });
};