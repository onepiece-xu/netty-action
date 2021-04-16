<template>
    <div class="tab-content">
        <div class="detail-row">
            <div class="detail-title">
                <a class="an-close" @click="$router.go(-1)">
                    <i class="icon-aw-mail-reply"></i>
                </a>
                <span>规格详情</span>
            </div>
            <div class="detail-info">
                <table class='table-dex' >
                    <!-- <h4>企业信息</h4> -->
                    <tr>
                        <td>规格名称</td>
                        <td>{{detailInfo.name}}</td>
                        <td>资源类型</td>
                        <td>{{detailInfo.type == 0?"arm":"x86"}}</td>
                    </tr>
                    <tr>
                        <td>设备型号</td>
                        <td>{{detailInfo.model}}</td>
                        <td>CPU型号规格</td>
                        <td>{{detailInfo.cpu}}</td>
                    </tr>
                    <!-- <h4>账号信息</h4> -->
                    <tr>
                        <td>CPU个数</td>
                        <td>{{detailInfo.cpuNumber}}</td>
                        <td>CPU插槽数</td>
                        <td>{{detailInfo.cpuSlot}}</td>
                    </tr>
                    <tr>
                        
                        <td>内存规格</td>
                        <td>{{detailInfo.memoryModel}}</td>
                        <td>内存</td>
                        <td>{{detailInfo.memory}}</td>
                    </tr>
                    <tr>
                        <td>插槽数</td>
                        <td>{{detailInfo.memorySlot}}</td>
                        <td>显卡型号</td>
                        <td>{{detailInfo.graphics}}</td>
                    </tr>
                    <tr>
                        <td>磁盘型号</td>
                        <td>{{detailInfo.diskModel}}</td>
                        <td> 磁盘大小（Byte）</td>
                        <td>{{detailInfo.diskSize}}</td>
                    </tr>
                    <tr>
                        <td>网卡规格</td>
                        <td>{{detailInfo.networkCard}}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
import {getBaseById} from '@/public/api/specs.js';

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
            getBaseById(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                }
            })
        }
    },
    mounted(){
        this.initData();
    }
}
</script>
