import $http from "./http";
import qs from "qs";

// 设备查询
export const getDeviceList = (params) => {
    return $http({
        url: `/ctp-paas-control/device/getDeviceList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
//设备关机
export const shutdown = (data) => {
    return $http({
        url: `/ctp-paas-control/device/shutdown`,
        method: "POST",
        data:data
    });
};

//设备关机
export const connectStream = (params) => {
    return $http({
        url: `/ctp-paas-control/stream/connectStream`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
