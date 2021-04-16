import $http from "./http";
import qs from "qs";

// 获取审批列表
export const getUserApproveList = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApprove/getUserApproveList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 获取审批详情
export const getUserInfo = (id) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApprove/getUserInfo/${id}`,
        method: "GET",
    });
};

// 审批
export const approve = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApprove/approve`,
        method: "POST",
        data:data
    });
};
// 获取审批列表
export const getAppApproveList = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/appApprove/getAppApproveList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 获取审批详情
export const getAppApprove = (id) => {
    return $http({
        url: `/cgp-paas-admin/v1/appApprove/getAppApprove/${id}`,
        method: "GET",
    });
};

// 审批
export const approveApp = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/appApprove/approveApp`,
        method: "POST",
        data:data
    });
};
// 应用类型列表
export const getGameType = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/appApprove/getGameType`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 上架应用
export const launchApps = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/appApprove/launchApps`,
        method: "POST",
        data:data,
    });
};