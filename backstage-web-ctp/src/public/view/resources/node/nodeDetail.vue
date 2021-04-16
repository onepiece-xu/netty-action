<template>
    <div class="tab-content">
        <div class="detail-row">
            <div class="detail-title">
                <a class="an-close" @click="$router.go(-1)">
                    <i class="icon-aw-mail-reply"></i>
                </a>
                <span>{{decodeURIComponent($route.query.name)}}详情</span>
            </div>
            <div class="detail-info">
                <table class='table-dex' >
                    <h4>集群信息</h4>
                    <tr>
                        <td>集群ID</td>
                        <td>{{detailInfo.id}}</td>
                        <td>集群名称</td>
                        <td>{{detailInfo.name}}</td>
                        
                    </tr>
                    <tr>
                        <td>集群状态</td>
                        <td>{{detailInfo.schStatus?'启用':'禁用'}}</td>
                        <td>集群管理的设备数</td>
                        <td>{{detailInfo.deviceNum}}</td>
                        
                        
                    </tr>
                    <h4>节点信息</h4>
                    <tr>
                        <td>集群IP地址</td>
                        <td>{{detailInfo.accessIp}}</td>
                        <td>API_URL</td>
                        <td>{{detailInfo.apiUrl}}</td>
                    </tr>
                    <tr>
                        <td>节点类型</td>
                        <td>{{detailInfo.type==0?'中心节点':'边缘节点'}}</td>
                        <td>是否本地节点</td>
                        <td>{{detailInfo.isLocal==0?'是':'否'}}</td>
                        
                    </tr>
                    <tr>
                        <td>创建时间</td>
                        <td>{{detailInfo.createTime}}</td>
                        <td>修改时间</td>
                        <td>{{detailInfo.updateTime}}</td>
                    </tr>
                    <tr>
                        <td>描述</td>
                        <td colspan="3">{{detailInfo.description}}</td>
                        <!-- <td></td>
                        <td></td> -->
                    </tr>
                    <h4>信令服务信息</h4>
                    <tr>
                        <td>信令服务名称</td>
                        <td>{{detailInfo1.serverName}}</td>
                        <td>域名</td>
                        <td>{{detailInfo1.domain}}</td>
                    </tr>
                    <tr>
                        <td>Http地址</td>
                        <td>{{detailInfo1.httpUrl}}</td>
                        <td>Http端口</td>
                        <td>{{detailInfo1.publicHttpPort}}</td>
                    </tr>
                    <tr>
                        <td>Https地址</td>
                        <td>{{detailInfo1.httpsUrl}}</td>
                        <td>Https端口</td>
                        <td>{{detailInfo1.publicHttpsPort}}</td>
                    </tr>
                    <tr>
                        <td>公网IP</td>
                        <td>{{detailInfo1.publicIp}}</td>
                        <td>内网IP</td>
                        <td>{{detailInfo1.internalIp}}</td>
                    </tr>
                    <h4>TURN服务信息</h4>
                    <tr>
                        <td>TURN服务名称</td>
                        <td>{{detailInfo2.serverName}}</td>
                        <td>域名</td>
                        <td>{{detailInfo2.domain}}</td>
                    </tr>
                    <tr>
                        <td>STUN URL</td>
                        <td colspan="3">{{detailInfo2.stunUrl}}</td>
                    </tr>
                    <tr>
                        <td>TURN URL(TCP)</td>
                        <td colspan="3">{{detailInfo2.turnTcpUrl}}</td>
                    </tr>
                    <tr>
                        <td>TURN URL(UDP)</td>
                        <td colspan="3">{{detailInfo2.turnUdpUrl}}</td>
                    </tr>
                    <tr>
                        <td>账号</td>
                        <td>{{detailInfo2.userName}}</td>
                        <td>密码</td>
                        <td>{{detailInfo2.credential}}</td>
                    </tr>
                    <tr>
                        <td>公网IP</td>
                        <td>{{detailInfo2.publicIp}}</td>
                        <td>公网端口</td>
                        <td>{{detailInfo2.publicPort}}</td>
                    </tr>
                    <tr>
                        <td>内网IP</td>
                        <td>{{detailInfo2.internalIp}}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
import {getClusterById,getSignal,getTurn} from '@/public/api/node.js';

export default {
    data(){
        return {
            detailInfo:{},
            detailInfo1:{},
            detailInfo2:{}
        }
    },
    methods:{
        initData(){
            // let data = {
            //     poolId:this.$route.query.id
            // }
            let id = this.$route.query.id
            getClusterById(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                    this.optionData([this.detailInfo])
                }
            })
            getSignal(id).then((res) =>{
                if(res&&res.data){
                    this.detailInfo1 = res.data;
                }
            })
            getTurn(id).then((res) =>{
                if(res&&res.data){
                    this.detailInfo2 = res.data;
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
                item.createTime = item.createTime?moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.updateTime = item.updateTime?moment(item.updateTime).format("YYYY-MM-DD HH:mm:ss"):'';
            })
        },
    },
    mounted(){
        this.initData();
    }
}
</script>
