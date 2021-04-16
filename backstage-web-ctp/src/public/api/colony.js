import $http from "./http";
import qs from "qs";

// 设备组列表
export const getDeviceGroupList = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/deviceGroup/getDeviceGroupList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 新建设备组
export const createDeviceGroup = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/deviceGroup/createDeviceGroup`,
        method: "POST",
        data:data
    });
};
// 修改设备组
export const updateDeviceGroup = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/deviceGroup/updateDeviceGroup`,
        method: "POST",
        data:data
    });
};
// 删除设备组
export const deleteDeviceGroup = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/deviceGroup/deleteDeviceGroup`,
        method: "POST",
        data:data
    });
};
//查询可添加设备列表
export const getAddDevices = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/devices/getAddDevices`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
//添加设备到设备组
export const addDevices = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/deviceGroup/addDevices`,
        method: "POST",
        data:data
    });
};
//移除设备从设备组
export const removeDevices = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/deviceGroup/removeDevices`,
        method: "POST",
        data:data
    });
};