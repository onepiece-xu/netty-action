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
                <table class='table-dex'>
                    <tr>
                        <td>TURN服务名称</td>
                        <td>{{detailInfo.serverName}}</td>
                        <td>域名</td>
                        <td>{{detailInfo.domain}}</td>
                    </tr>
                    <tr>
                        <td>STUN URL</td>
                        <td colspan="3">{{detailInfo.stunUrl}}</td>
                    </tr>
                    <tr>
                        <td>TURN URL(TCP)</td>
                        <td colspan="3">{{detailInfo.turnTcpUrl}}</td>
                    </tr>
                    <tr>
                        <td>TURN URL(UDP)</td>
                        <td colspan="3">{{detailInfo.turnUdpUrl}}</td>
                    </tr>
                    <tr>
                        <td>账号</td>
                        <td>{{detailInfo.userName}}</td>
                        <td>密码</td>
                        <td>{{detailInfo.credential}}</td>
                    </tr>
                    <tr>
                        <td>公网IP</td>
                        <td>{{detailInfo.publicIp}}</td>
                        <td>公网端口</td>
                        <td>{{detailInfo.publicPort}}</td>
                    </tr>
                    <tr>
                        <td>内网IP</td>
                        <td>{{detailInfo.internalIp}}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </table>
                <p class="app-manager">已绑定集群</p>
                <div class="table-content">
                    <el-table ref="multipleTable" 
                        :data="dataList">
                        <el-table-column prop="id" label="集群ID">
                            <template slot-scope="scope">
                                <span :title="scope.row.id">{{scope.row.id}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="集群名称">
                            <template slot-scope="scope">
                                <span :title='scope.row.name'>{{scope.row.name}}</span>
                            </template> 
                        </el-table-column>
                    </el-table>
                    <div v-if="pageInstan.showPage">
                        <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
                    </div>
                </div>
            </div>
            
        </div>
    </div>
</template>

<script>
import {getInfo,getbindList} from '@/public/api/turn.js'

export default {
    data(){
        return {
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            detailInfo:{},
            dataList:[]
        }
    },
    methods:{
        //切换页码组，每页展示多少条
        handleSizeChange(val) {
            this.pageInstan.pageSize = val;
            this.initData();
        },
        //切换页码
        handleCurrentChange(val) {
            this.pageInstan.currentPage = val;
            this.initData('','pageOne');
        },
        initData(){
            // let data = {
            //     poolId:this.$route.query.id
            // }
            let id = this.$route.query.id
            getInfo(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                }
            })
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize,
                'id': this.$route.query.id
            }
            getbindList(oData,id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.dataList = res.data.records;
                    if(this.dataList.length > 0){
                        this.pageInstan.Total = res.data.total;
                        this.pageInstan.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan.Total = res.data.total;
                        this.pageInstan.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                }
            })
        },
    },
    mounted(){
        this.initData();
    }
}
</script>
