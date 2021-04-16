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
                    <tr>
                        <td>设备ID</td>
                        <td>{{detailInfo.name}}</td>
                        <td>设备名称</td>
                        <td>{{detailInfo.baseId}}</td>
                    </tr>
                    <tr>
                        <td>设备内网管理IP</td>
                        <td>{{detailInfo.deviceCode}}</td>
                        <td>设备MAC</td>
                        <td>{{detailInfo.clusterId}}</td>
                    </tr>
                    <tr>
                        <td>设备外网业务IP</td>
                        <td>{{detailInfo.idc}}</td>
                        <td>设备外网业务端口</td>
                        <td>{{detailInfo.cabinet}}</td>
                    </tr>
                    <tr>
                        <td>设备所属机房</td>
                        <td>{{detailInfo.location}}</td>
                        <td>设备所属机架号</td>
                        <td>{{detailInfo.slot}}</td>
                    </tr>
                    <tr>
                        <td>服务器机架槽位</td>
                        <td>{{detailInfo.groupId}}</td>
                        <td>设备所在服务器内部槽位</td>
                        <td>{{detailInfo.managerIp}}</td>
                    </tr>
                    <tr>
                        <td>服务器管理IP</td>
                        <td>{{detailInfo.managerPort}}</td>
                        <td>服务器管理端口</td>
                        <td>{{detailInfo.innerIp}}</td>
                    </tr>
                    <tr>
                        <td>设备类型</td>
                        <td>{{detailInfo.innerPort}}</td>
                        <td>设备规格名称</td>
                        <td>{{detailInfo.ip}}</td>
                    </tr>
                    <tr>
                        <td>设备状态</td>
                        <td>{{detailInfo.port}}</td>
                        <td>已用空间</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>可用空间</td>
                        <td>{{detailInfo.port}}</td>
                        <td>所属集群</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>所属设备组</td>
                        <td>{{detailInfo.port}}</td>
                        <td>描述</td>
                        <td></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
import {getDeviceById} from '@/public/api/equipment.js';

export default {
    data(){
        return {
            detailInfo:{}
        }
    },
    methods:{
        initData(){
            // let data = {
            //     poolId:this.$route.query.id
            // }
            let id = this.$route.query.id
            getDeviceById(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                }
            })
        },
    },
    mounted(){
        this.initData();
    }
}
</script>
