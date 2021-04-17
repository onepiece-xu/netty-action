package com.ydw.monitor.service;

import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.model.vo.StreamInfo;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:02
 */
public interface IStreamService {

    /**
     * 开流服务
     * @param param
     * @return
     */
    ResultInfo startStream(StreamInfo param);

    /**
     * 关闭流服务
     * @param param
     * @return
     */
    ResultInfo stopStream(StreamInfo param);
}
