package com.ydw.control.service.impl;

import com.ydw.control.model.db.Cluster;
import com.ydw.control.dao.ClusterMapper;
import com.ydw.control.service.IClusterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 集群（网吧） 服务实现类
 * </p>
 *
 * @author xulh
 * @since 2021-04-27
 */
@Service
public class ClusterServiceImpl extends ServiceImpl<ClusterMapper, Cluster> implements IClusterService {

}
