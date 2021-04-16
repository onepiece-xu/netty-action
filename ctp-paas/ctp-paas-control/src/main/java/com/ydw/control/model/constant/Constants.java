package com.ydw.control.model.constant;

/**
 * 常量
 * @author huzh
 * @date 2020/5/9
 */
public final class Constants {

    public static final String AUTH_HEADER = "Authorization";

    public static final int DEVICE_ARCH_ARM        =  0;     ///arm设备
    public static final int DEVICE_ARCH_X86        =  1;     ///x86 设备

    public static final String DEVICE_ENCODE_H264 = "264";   ///h264 编码
    public static final String DEVICE_ENCODE_H265 = "265";   ///h265 编码

    public static final Integer CONNECT_TYPE_APK = 0;         ///客户端为apk类型
    public static final Integer CONNECT_TYPE_WEBRTC = 1;      ///客户端为webrtc类型

    public static final Integer CLIENT_TYPE_MOBILE = 0;             ///移动客户端
    public static final Integer CLIENT_TYPE_PC = 1;             ///pc 客户端
}
