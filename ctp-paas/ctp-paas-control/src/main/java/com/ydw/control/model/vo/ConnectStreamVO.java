package com.ydw.control.model.vo;


/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1613:04
 */
public class ConnectStreamVO extends WebrtcConfig{

    /**
     * c-c连接密钥
     */
    private String token;

    /**
     * c-c连接id
     */
    private String connectId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     *
     */
    private String signalServer;

    /**
     *
     */
    private String[] turnServer;

    private int screen = 1;

    public ConnectStreamVO(String token, String connectId, String deviceName, WebrtcConfig webrtcConfig) {
        super(webrtcConfig);
        this.token = token;
        this.connectId = connectId;
        this.deviceName = deviceName;
        this.signalServer = webrtcConfig.getSignalServerHttps();
        this.turnServer = new String[]{webrtcConfig.getStunUrl(),webrtcConfig.getTurnTcpUrl(), webrtcConfig.getTurnUdpUrl()};
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String getSignalServer() {
        return signalServer;
    }

    @Override
    public void setSignalServer(String signalServer) {
        this.signalServer = signalServer;
    }

    public String[] getTurnServer() {
        return turnServer;
    }

    public void setTurnServer(String[] turnServer) {
        this.turnServer = turnServer;
    }

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }
}
