package com.ydw.control.service;

import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.model.vo.StreamInfo;

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

    /**
     * 连接流服务
     * @param deviceId
     * @return
     */
    ResultInfo connectStream(String deviceId);
}
