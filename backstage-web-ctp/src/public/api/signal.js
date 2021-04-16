import $http from "./http";
import qs from "qs";


// 信令服务器列表
export const getSignalServerList = (params) => {
    return $http({
        url: `/cgp-paas-admin/signalServer/getSignalServerList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 信令服务器详情
export const getInfo = (id) =>{
    return $http({
        url: `/cgp-paas-admin/signalServer/getInfo/${id}`,
        method: "GET",
    });
}
// 新建信令服务器
export const addSignalServer = (data) => {
    return $http({
        url: `/cgp-paas-admin/signalServer/addSignalServer`,
        method: "POST",
        data:data
    });
};
// 编辑信令服务器
export const updateSignalServer = (data) => {
    return $http({
        url: `/cgp-paas-admin/signalServer/updateSignalServer`,
        method: "POST",
        data:data
    });
};
// 删除信令服务器
export const deleteSignalServer = (data) => {
    return $http({
        url: `/cgp-paas-admin/signalServer/delete`,
        method: "POST",
        data:data
    });
};
// 集群綁定/解綁 信令服务器 
export const bindSignal = (data) => {
    return $http({
        url: `/cgp-paas-admin/signalServer/bind`,
        method: "POST",
        data:data
    });
};
// 绑定集群列表
export const getbindList = (params) => {
    return $http({
        url: `/cgp-paas-admin/signalServer/bindList`,
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
        url: `/cgp-paas-admin/signalServer/unbindList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};