import $http from "./http";
import qs from "qs";

// 设备状态统计
export const getDeviceStatusCount  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getDeviceStatusCount`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
//获取应用安装统计
export const getAppInstallCount  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getAppInstallCount`,
        method: "GET",
        params:params
    });
};
//获取应用安装统计
export const getClusterDevicesStatus  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getClusterDevicesStatus`,
        method: "GET",
        params:params
    });
};