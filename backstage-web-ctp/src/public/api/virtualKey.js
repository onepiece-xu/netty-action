import $http from "./http";
// import qs from "qs";


// 虚拟按键列表
export const getConfigList = (data) => {
    return $http({
        url: `/cgp-paas-admin/keyConfig/getConfigList`,
        method: "POST",
        data:data
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