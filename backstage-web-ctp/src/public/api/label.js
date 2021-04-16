import $http from "./http";
import qs from "qs";

// 标签列表
export const getTags = (params) => {
    return $http({
        url: `/cgp-paas-admin/tag/getTags`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 新建标签
export const addTag = (data) => {
    return $http({
        url: `/cgp-paas-admin/tag/add`,
        method: "POST",
        data:data
    });
};
// 编辑标签
export const updateTag = (data) => {
    return $http({
        url: `/cgp-paas-admin/tag/updateTag`,
        method: "POST",
        data:data
    });
};
// 删除标签
export const deleteTags = (data) => {
    return $http({
        url: `/cgp-paas-admin/tag/deleteTags`,
        method: "POST",
        data:data
    });
};
//未绑定应用
export const getUnBindApps = (params) => {
    return $http({
        url: `/cgp-paas-admin/tag/getUnBindApps`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
//绑定 解绑
export const bindTags = (data) => {
    return $http({
        url: `/cgp-paas-admin/tag/bindTags`,
        method: "POST",
        data:data
    });
};
//获取标签已绑定应用
export const getBindApps = (params) => {
    return $http({
        url: `/cgp-paas-admin/tag/getBindApps`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 标签类型列表
export const getTagTypeList = (params) => {
    return $http({
        url: `/cgp-paas-admin/tagType/getTagTypeList`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 新建标签类型
export const addTagType = (data) => {
    return $http({
        url: `/cgp-paas-admin/tagType/add`,
        method: "POST",
        data:data
    });
};
// 编辑标签类型
export const updateTagType = (data) => {
    return $http({
        url: `/cgp-paas-admin/tagType/updateTagType`,
        method: "POST",
        data:data
    });
};
// 删除标签类型
export const deleteTagType = (data) => {
    return $http({
        url: `/cgp-paas-admin/tagType/deleteTagType`,
        method: "POST",
        data:data
    });
};