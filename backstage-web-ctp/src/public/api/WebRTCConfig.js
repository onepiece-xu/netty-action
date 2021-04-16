import $http from "./http";
import qs from "qs";


// WebRTC配置列表
export const getConfigList = (params) => {
    return $http({
        url: `/cgp-paas-admin/webrtcConfig/getConfigList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// WebRTC配置详情列表
export const getBaseById = (id) =>{
    return $http({
        url: `/cgp-paas-admin/deviceBase/getBaseById/${id}`,
        method: "GET",
    });
}
// 新建WebRTC配置
export const addConfig = (data) => {
    return $http({
        url: `/cgp-paas-admin/webrtcConfig/addConfig`,
        method: "POST",
        data:data
    });
};
// 编辑WebRTC配置
export const updateConfig = (data) => {
    return $http({
        url: `/cgp-paas-admin/webrtcConfig/updateConfig`,
        method: "POST",
        data:data
    });
};
// 删除WebRTC配置
export const deleteConfig = (data) => {
    return $http({
        url: `/cgp-paas-admin/webrtcConfig/deleteConfig`,
        method: "POST",
        data:data
    });
};