import $http from "./http";
import qs from "qs";

// 计量列表
export const getMeterageList = (params) => {
    return $http({
        url: `/ctp-paas-control/meterage/getMeterageList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};