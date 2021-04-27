package com.ydw.control.model.db;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xulh
 * @since 2021-04-15
 */
public class User implements Serializable {

    /**
     * 用户id
     */
    private String id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户凭证
     */
    private String token;

    /**
     * 所拥有的集群
     */
    private String clusterIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClusterIds() {
        return clusterIds;
    }

    public void setClusterIds(String clusterIds) {
        this.clusterIds = clusterIds;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", clusterIds='" + clusterIds + '\'' +
                '}';
    }
}
