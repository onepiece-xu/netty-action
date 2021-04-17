import $http from "./http";
import qs from "qs";

// 文件列表
export const getFileList = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/files/getFileList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 新建文件
export const createFile = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/files/createFile`,
        method: "POST",
        data:data
    });
};
// 编辑文件
export const updateFile = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/files/updateFile`,
        method: "POST",
        data:data
    });
};
// 删除文件
export const deleteFile = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/files/deleteFile`,
        method: "POST",
        data:data
    });
};
// 文件上传
export const fileUpload = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/files/fileUpload`,
        method: "POST",
        data:data
    });
};