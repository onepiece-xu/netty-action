import $http from "./http";
import qs from "qs";


// 获取集群连接数（历史）
export const getClusterConnectionsStatistics = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getClusterConnectionsStatistics`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 获取集群连接数（实时）
export const getClusterConnectionsRealTime = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getClusterConnectionsRealTime`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 节点连接时长统计（历史）
export const getClusterConnectTimeStatistics = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getClusterConnectTimeStatistics`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 历史应用连接数统计
export const getAppsConnectionsStatistics  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getAppsConnectionsStatistics`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 应用连接数统计（实时）
export const getAppsConnectionsRealTime  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getAppsConnectionsRealTime`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 历史应用连接时长统计
export const getAppsConnectTimeStatistics  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getAppsConnectTimeStatistics`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 设备利用率（实时）
export const getDeviceUsageRate  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getDeviceUsageRate`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 设备利用率（历史）
export const getDeviceUsageHistory  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getDeviceUsageHistory`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
// 设备总利用率（历史）
export const getTotalUsage  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getTotalUsage`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
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

export const getAppsConnectionsRealTimeTopTen  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getAppsConnectionsRealTimeTopTen`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
//应用连接数10
export const getAppsConnectionsStatisticsTopTen  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getAppsConnectionsStatisticsTopTen`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
//应用连接时长10
export const getAppsConnectTimeStatisticsTopTen  = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getAppsConnectTimeStatisticsTopTen`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};
//设备利用率趋势图 
export const getUsageTrend = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getUsageTrend`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};

//应用排队情况统计
export const getAppQueueNum = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getAppQueueNum`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};

// 清理应用排队僵尸数据
export const clearQueueCorpse = (data) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/clearQueueCorpse`,
        method: "POST",
        data:data
    });
};
// 在线用户查询
export const getOnlineUsers = (params) => {
    return $http({
        url: `/cgp-paas-admin/v1/monitor/getOnlineUsers`,
        method: "GET",
        params:params,
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    });
};