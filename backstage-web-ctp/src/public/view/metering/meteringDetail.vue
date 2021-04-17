<template>
    <div class="tab-content">
        <div class="detail-row">
            <div class="detail-title">
                <a class="an-close" @click="$router.go(-1)">
                    <i class="icon-aw-mail-reply"></i>
                </a>
                <span>连接详情</span>
            </div>
            <div class="detail-info">
                <table class='table-dex' >
                    <h4>连接信息</h4>
                    <tr>
                        <td>连接ID</td>
                        <td colspan="3">{{detailInfo.id}}</td>
                    </tr>
                    <tr>
                        <td>应用ID</td>
                        <td>{{detailInfo.appId}}</td>
                        <td>应用名称</td>
                        <td>{{detailInfo.appName}}</td>
                    </tr>
                    <tr>
                        <td>连接设备ID</td>
                        <td>{{detailInfo.deviceId}}</td>
                        <td>连接设备名称</td>
                        <td>{{detailInfo.deviceName}}</td>
                    </tr>
                    <tr>
                        <td>连接起始时间</td>
                        <td>{{detailInfo.beginTime}}</td>
                        <td>连接断开时间</td>
                        <td>{{detailInfo.endTime}}</td>
                    </tr>
                    <tr>
                        <td>连接时长（min）</td>
                        <td colspan="3">{{detailInfo.totalTime}}</td>
                    </tr>
                    <h4>用户信息</h4>
                    <tr>
                        <td>企业用户ID</td>
                        <td>{{detailInfo.enterpriseId}}</td>
                        <td>企业用户名称</td>
                        <td>{{detailInfo.enterpriseName}}</td>
                    </tr>
                    <tr>
                        <td>终端用户ID</td>
                        <td>{{detailInfo.customId}}</td>
                        <td>访问来源IP</td>
                        <td>{{detailInfo.fromIp}}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
import {getUsedListById} from '@/public/api/metering.js';

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
            getUsedListById(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                    this.optionData([this.detailInfo])
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
                item.totalTime = item.totalTime?(item.totalTime/60).toFixed(2):'';
            })
        },
    },
    mounted(){
        this.initData();
    }
}
</script>
