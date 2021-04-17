<template>
    <div class="tab-content">
        <div class="detail-row">
            <div class="detail-title">
                <a class="an-close" @click="$router.go(-1)">
                    <i class="icon-aw-mail-reply"></i>
                </a>
                <span>用户详情</span>
            </div>
            <div class="detail-info">
                <table class='table-dex' >
                    <h4>企业信息</h4>
                    <tr>
                        <td>企业名称</td>
                        <td>{{detailInfo.enterpriseName}}</td>
                        <td>企业ID</td>
                        <td>{{detailInfo.identification}}</td>
                    </tr>
                    <tr>
                        <td>企业key</td>
                        <td colspan="3">{{detailInfo.secretKey}}</td>
                        <!-- <td></td>
                        <td></td> -->
                    </tr>
                    <h4>账号信息</h4>
                    <tr>
                        <td>用户登录名</td>
                        <td>{{detailInfo.loginName}}</td>
                        <td>用户邮箱</td>
                        <td>{{detailInfo.email}}</td>
                    </tr>
                    <tr>
                        
                        <td>用户类型</td>
                        <td>{{detailInfo.userType}}</td>
                        <td>状态</td>
                        <td>{{detailInfo.schStatus?"正常":"禁用"}}</td>
                    </tr>
                    <tr>
                        <td>用户手机号</td>
                        <td>{{detailInfo.mobileNumber}}</td>
                        <td>电话号码（座机）</td>
                        <td>{{detailInfo.telNumber}}</td>
                    </tr>
                    <tr>
                        <td>创建时间</td>
                        <td>{{detailInfo.createTime}}</td>
                        <td>修改时间</td>
                        <td>{{detailInfo.modifiedTime}}</td>
                    </tr>
                     <tr>
                        <td>备注</td>
                        <td colspan="3">{{detailInfo.description}}</td>
                        <!-- <td></td>
                        <td></td> -->
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
import {getUserInfo} from '@/public/api/userManager.js';

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
            getUserInfo(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                    this.optionData([this.detailInfo])
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
                item.createTime = item.createTime?moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.modifiedTime = item.modifiedTime?moment(item.modifiedTime).format("YYYY-MM-DD HH:mm:ss"):'';
                if(item.type == 1){
                    item.userType = '平台管理用户'
                }else if(item.type == 2){
                    item.userType = '企业用户'
                }else if(item.accountStatus == 3){
                    item.userType = '个人用户'
                }
            })
        },
    },
    mounted(){
        this.initData();
    }
}
</script>
