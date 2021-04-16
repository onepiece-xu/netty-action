package com.ydw.control.model.vo;

import com.ydw.control.model.db.SignalServer;
import com.ydw.control.model.db.TurnServer;

import java.io.Serializable;

/**
 * webrtc连接信息类
 * @author xulh
 * @since 2020-07-30
 */
public class WebrtcConfig implements Serializable {

    /**
     * 信令服务器url
     */
    protected String signalServer;

    protected String signalServerHttps;

    /**
     * turn服务url
     */
    protected String stunUrl;

    protected String turnTcpUrl;

    protected String turnUdpUrl;

    /**
     * turn 服务登录名称
     */
    protected String turnUser;

    /**
     * turn 登录密码
     */
    protected String turnPassword;

    public WebrtcConfig(WebrtcConfig webrtcConfig) {
        this.signalServer = webrtcConfig.getSignalServer();
        this.signalServerHttps = webrtcConfig.getSignalServerHttps();
        this.stunUrl = webrtcConfig.getStunUrl();
        this.turnTcpUrl = webrtcConfig.getTurnTcpUrl();
        this.turnUdpUrl = webrtcConfig.getTurnUdpUrl();
        this.turnUser = webrtcConfig.getTurnUser();
        this.turnPassword = webrtcConfig.getTurnPassword();
    }

    public WebrtcConfig(SignalServer signalServer, TurnServer turnServer){
        this.signalServer = signalServer.getHttpUrl();
        this.signalServerHttps = signalServer.getHttpsUrl();
        this.stunUrl = turnServer.getStunUrl();
        this.turnTcpUrl = turnServer.getTurnTcpUrl();
        this.turnUdpUrl = turnServer.getTurnUdpUrl();
        this.turnUser = turnServer.getUserName();
        this.turnPassword = turnServer.getCredential();
    }
    
	public String getSignalServer() {
        return signalServer;
    }

    public void setSignalServer(String signalServer) {
        this.signalServer = signalServer;
    }

    public String getSignalServerHttps() {
        return signalServerHttps;
    }

    public void setSignalServerHttps(String signalServerHttps) {
        this.signalServerHttps = signalServerHttps;
    }

    public String getTurnUser() {
        return turnUser;
    }

    public void setTurnUser(String turnUser) {
        this.turnUser = turnUser;
    }

    public String getTurnPassword() {
        return turnPassword;
    }

    public void setTurnPassword(String turnPassword) {
        this.turnPassword = turnPassword;
    }

	public String getStunUrl() {
		return stunUrl;
	}

	public void setStunUrl(String stunUrl) {
		this.stunUrl = stunUrl;
	}

	public String getTurnTcpUrl() {
		return turnTcpUrl;
	}

	public void setTurnTcpUrl(String turnTcpUrl) {
		this.turnTcpUrl = turnTcpUrl;
	}

	public String getTurnUdpUrl() {
		return turnUdpUrl;
	}

	public void setTurnUdpUrl(String turnUdpUrl) {
		this.turnUdpUrl = turnUdpUrl;
	}

}
