package com.ydw.control.model.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
public class SignalServer implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String serverName;

    private String domain;

    private String publicIp;

    private Integer publicHttpPort;

    private Integer publicHttpsPort;

    private String httpUrl;

    private String httpsUrl;

    private String internalIp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public Integer getPublicHttpPort() {
        return publicHttpPort;
    }

    public void setPublicHttpPort(Integer publicHttpPort) {
        this.publicHttpPort = publicHttpPort;
    }

    public Integer getPublicHttpsPort() {
        return publicHttpsPort;
    }

    public void setPublicHttpsPort(Integer publicHttpsPort) {
        this.publicHttpsPort = publicHttpsPort;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpsUrl() {
        return httpsUrl;
    }

    public void setHttpsUrl(String httpsUrl) {
        this.httpsUrl = httpsUrl;
    }

    public String getInternalIp() {
        return internalIp;
    }

    public void setInternalIp(String internalIp) {
        this.internalIp = internalIp;
    }

    @Override
    public String toString() {
        return "SignalServer{" +
        "id=" + id +
        ", serverName=" + serverName +
        ", domain=" + domain +
        ", publicIp=" + publicIp +
        ", publicHttpPort=" + publicHttpPort +
        ", publicHttpsPort=" + publicHttpsPort +
        ", httpUrl=" + httpUrl +
        ", httpsUrl=" + httpsUrl +
        ", internalIp=" + internalIp +
        "}";
    }
}
