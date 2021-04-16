import $http from "./http";
import qs from "qs";


// turn服务器列表
export const getTurnServerList = (params) => {
    return $http({
        url: `/cgp-paas-admin/turnServer/getTurnServerList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// turn服务器详情
export const getInfo = (id) =>{
    return $http({
        url: `/cgp-paas-admin/turnServer/getInfo/${id}`,
        method: "GET",
    });
}
// 新建turn服务器
export const addTurnServer = (data) => {
    return $http({
        url: `/cgp-paas-admin/turnServer/addTurnServer`,
        method: "POST",
        data:data
    });
};
// 编辑turn服务器
export const updateTurnServer = (data) => {
    return $http({
        url: `/cgp-paas-admin/turnServer/updateTurnServer`,
        method: "POST",
        data:data
    });
};
// 删除turn服务器
export const deleteTurnServer = (data) => {
    return $http({
        url: `/cgp-paas-admin/turnServer/delete`,
        method: "POST",
        data:data
    });
};
// 集群綁定/解綁 信令服务器 
export const bindSignal = (data) => {
    return $http({
        url: `/cgp-paas-admin/turnServer/bind`,
        method: "POST",
        data:data
    });
};
// 绑定集群列表
export const getbindList = (params) => {
    return $http({
        url: `/cgp-paas-admin/turnServer/bindList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 未绑定集群列表
export const getUnBindList = (params) => {
    return $http({
        url: `/cgp-paas-admin/turnServer/unbindList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};