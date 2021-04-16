import $http from "./http";
import qs from "qs";


// 用户列表
export const getuserList = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/userInfo/userList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 用户详情
export const getUserInfo = (id) => {
    return $http({
        url: `/cgp-paas-admin/v1/userInfo/getUserInfo/${id}`,
        method: "GET",
    });
};
// 新建用户
export const createUser = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userInfo/createUser`,
        method: "POST",
        data:data
    });
};
// 用户下的集群
export const getServiceClusters = () => {
    return $http({
        url: `/cgp-paas-admin/v1/clusters/getServiceClusters`,
        method: "GET",
    });
};
// 编辑用户
export const updateUser = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userInfo/updateUser`,
        method: "POST",
        data:data
    });
};
// 删除用户
export const deleteUser = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userInfo/deleteUser`,
        method: "POST",
        data:data
    });
};
// 重置密码
export const resetPassword = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userInfo/resetPassword`,
        method: "POST",
        data:data
    });
};
// 禁用启用
export const disableUser = (data,type) => {
    return $http({
        url: `/cgp-paas-admin/v1/userInfo/disableUser/${type}`,
        method: "POST",
        data:data
    });
};