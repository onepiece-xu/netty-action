import $http from "./http";
import qs from "qs";


// 集群列表
export const getClusters = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/clusters/getClusters`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 集群id列表
export const getClusterById = (id) =>{
    return $http({
        url: `/cgp-paas-admin/v1/clusters/getClusterById/${id}`,
        method: "GET",
    });
}
// 新建集群
export const createCluster = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/clusters/createCluster`,
        method: "POST",
        data:data
    });
};
// 集群下的用户LIst
export const serviceUserList = () => {
    return $http({
        url: `/cgp-paas-admin/v1/userInfo/serviceUserList`,
        method: "GET",
    });
};
// 编辑集群
export const updateCluster = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/clusters/updateCluster`,
        method: "POST",
        data:data
    });
};
// 删除集群
export const deleteClusters = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/clusters/deleteClusters`,
        method: "POST",
        data:data
    });
};
// 集群绑定WebRTC 配置
export const bindConfig = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/clusters/bindConfig`,
        method: "POST",
        data:data
    });
};
// 集群signal列表
export const getSignal = (id) =>{
    return $http({
        url: `/cgp-paas-admin/v1/clusters/getSignal/${id}`,
        method: "GET",
    });
}
// 集群Turn列表
export const getTurn = (id) =>{
    return $http({
        url: `/cgp-paas-admin/v1/clusters/getTurn/${id}`,
        method: "GET",
    });
}