import $http from "./http";
import qs from "qs";


// 规格列表
export const getBaseList = (params) => {
    return $http({
        url: `/cgp-paas-admin/deviceBase/getBaseList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 规格详情列表
export const getBaseById = (id) =>{
    return $http({
        url: `/cgp-paas-admin/deviceBase/getBaseById/${id}`,
        method: "GET",
    });
}
// 新建规格
export const addDeviceBase = (data) => {
    return $http({
        url: `/cgp-paas-admin/deviceBase/addDeviceBase`,
        method: "POST",
        data:data
    });
};
// 编辑规格
export const updateDeviceBase = (data) => {
    return $http({
        url: `/cgp-paas-admin/deviceBase/updateDeviceBase`,
        method: "POST",
        data:data
    });
};
// 删除规格
export const deleteDeviceBase = (data) => {
    return $http({
        url: `/cgp-paas-admin/deviceBase/deleteDeviceBase`,
        method: "POST",
        data:data
    });
};