import $http from "./http";
import qs from "qs";


// 应用列表
export const getUserApps = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/getUserApps`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 应用详情
export const getUserApp = (id) =>{
    return $http({
        url: `/cgp-paas-admin/v1/userApps/getUserApp/${id}`,
        method: "GET",
    });
}
// 新建应用
export const createUserApp = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/createUserApp`,
        method: "POST",
        data:data
    });
};
// 编辑应用
export const updateUserApp = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/updateUserApp`,
        method: "POST",
        data:data
    });
};
// 删除应用
export const deleteUserApp = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/deleteUserApp`,
        method: "POST",
        data:data
    });
};
// 安装应用
export const createDeviceApp = (data,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/tbDeviceApps/createDeviceApp/${id}`,
        method: "POST",
        data:data
    });
};
// 卸载应用
export const UninstallApp = (data,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/tbDeviceApps/UninstallApp/${id}`,
        method: "POST",
        data:data
    });
};
// 启用应用
export const enableApps = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/enableApps`,
        method: "POST",
        data:data
    });
};
// 禁用应用
export const disableApps = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/disableApps`,
        method: "POST",
        data:data
    });
};
// 根据应用查询可批量安装设备
export const getCanInstallDevicesByAppId = (params,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/devices/getCanInstallDevicesByAppId/${id}`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 根据应用查询可批量卸载设备
export const getCanUninstallDevicesByAppId = (params,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/devices/getCanUninstallDevicesByAppId/${id}`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
//设备详情根据应用查询可批量安装设备
export const getDevicesByInstallAppId = (params,id) =>{
    return $http({
        url: `/cgp-paas-admin/v1/devices/getDevicesByInstallAppId/${id}`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
}
// 应用批量安装到设备
// export const InstallToDevices = (data,id) => {
//     return $http({
//         url: `/cgp-paas-admin/v1/tbDeviceApps/InstallToDevices/${id}`,
//         method: "POST",
//         data:data
//     });
// };
// 应用批量卸载
// export const UninstallToDevices = (data,id) => {
//     return $http({
//         url: `/cgp-paas-admin/v1/tbDeviceApps/UninstallToDevices/${id}`,
//         method: "POST",
//         data:data
//     });
// };
// 文件上传
export const fileUpload = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/fileUpload`,
        method: "POST",
        data:data
    });
};

//应用安装
export const InstallToDevices = (data,id) => {
    return $http({
        url: `/cgp-paas-resource/deviceApp/install/${id}`,
        method: "POST",
        data:data
    });
};
//应用卸载
export const UninstallToDevices = (data,id) => {
    return $http({
        url: `/cgp-paas-resource/deviceApp/uninstall/${id}`,
        method: "POST",
        data:data
    });
};
// 上传安装包
export const apkUpload = (data,id) => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/upload?appId=${id}`,
        method: "POST",
        data:data
    });
};
// 上传图片
export const upload = (data) => {
    return $http({
        url: `/cgp-paas-admin/tbAppPictures/upload`,
        method: "POST",
        data:data
    });
};
// 获取应用平台类型列表
export const getPlatformList = () => {
    return $http({
        url: `/cgp-paas-admin/v1/userApps/getPlatformList`,
        method: "get"
    });
};