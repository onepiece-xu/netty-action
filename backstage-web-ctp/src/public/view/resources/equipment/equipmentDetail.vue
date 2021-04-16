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
                    <h4>设备信息</h4>
                    <tr>
                        <td>设备ID</td>
                        <td>{{detailInfo.id}}</td>
                        <td>设备名称</td>
                        <td>{{detailInfo.name}}</td>
                    </tr>
                    <tr>
                        <td>设备类型</td>
                        <td>{{detailInfo.baseType == 0?"arm":"x86"}}</td>
                        <td>设备规格名称</td>
                        <td>{{detailInfo.baseName}}</td>
                    </tr>
                    <tr>
                        <td>所属集群</td>
                        <td>{{detailInfo.clusterName}}</td>
                        <td>所属设备组</td>
                        <td>{{detailInfo.groupName}}</td>
                    </tr>
                    <tr>
                        <td>所属机房</td>
                        <td>{{detailInfo.idc}}</td>
                        <td>所属机架号</td>
                        <td>{{detailInfo.cabinet}}</td>
                    </tr>
                    <tr>
                        <td>服务器机架槽位</td>
                        <td>{{detailInfo.location}}</td>
                        <td>服务器内部槽位</td>
                        <td>{{detailInfo.slot}}</td>
                    </tr>
                    <h4>设备网络信息</h4>
                    <tr>
                        <td>管理IP</td>
                        <td>{{detailInfo.innerIp}}</td>
                        <td>管理端口</td>
                        <td>{{detailInfo.innerPort}}</td>
                    </tr>
                    <tr>
                        <td>设备MAC</td>
                        <td colspan="3">{{detailInfo.innerMac}}</td>
                    </tr>
                    <tr>
                        <td>外网IP</td>
                        <td>{{detailInfo.ip}}</td>
                        <td>外网端口</td>
                        <td>{{detailInfo.port}}</td>
                    </tr>
                    <tr>
                        <td>服务器管理IP</td>
                        <td>{{detailInfo.managerIp}}</td>
                        <td>服务器管理端口</td>
                        <td>{{detailInfo.managerPort}}</td>
                    </tr>
                    <tr>
                        <td>adb连接IP</td>
                        <td>{{detailInfo.adbIp}}</td>
                        <td>adb连接端口</td>
                        <td>{{detailInfo.adbPort}}</td>
                    </tr>
                    <h4>设备状态信息</h4>
                    <tr>
                        <td>设备运行状态</td>
                        <td>{{detailInfo.status}}</td>
                        <td>设备启用状态</td>
                        <td>{{detailInfo.schStatus?'正常':'禁用'}}</td>
                    </tr>
                    <tr>
                        <td>已用空间(MB)</td>
                        <td>{{detailInfo.usedSpace}}</td>
                        <td>可用空间(MB)</td>
                        <td>{{detailInfo.freeSpace}}</td>
                    </tr>
                    <tr>
                        <td>注册状态</td>
                        <td>{{detailInfo.init?"已注册":"未注册"}}</td>
                        <td>描述</td>
                        <td>{{detailInfo.description}}</td>
                    </tr>
                </table>
                <p class="app-manager">已安装应用</p>
                <div class="table-content">
                    <el-table ref="multipleTable" 
                        :data="dataList">
                        <el-table-column prop="id" label="应用ID">
                            <template slot-scope="scope">
                                <span :title="scope.row.id">{{scope.row.id}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="应用名称">
                            <template slot-scope="scope">
                                <span :title='scope.row.name'>{{scope.row.name}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="size" label="安装包大小(MB)">
                            <template slot-scope="scope">
                                <span :title='scope.row.size'>{{scope.row.size}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="realSize" label="安装后大小(MB)">
                            <template slot-scope="scope">
                                <span :title='scope.row.realSize'>{{scope.row.realSize}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="enterpriseName" label="所属用户">
                            <template slot-scope="scope">
                                <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
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
import {getDeviceById,getInstallApps} from '@/public/api/equipment.js';

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
            getDeviceById(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                    this.optionData([this.detailInfo])
                }
            })
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize,
            }
            getInstallApps(oData,id).then((res) =>{
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
        optionData(data){
            data.forEach(function(item){
                if(item.status == 1){
                    item.status = '空闲'
                }else if(item.status == 2){
                    item.status = '使用'
                }else if(item.status == 3){
                    item.status = '错误'
                }else if(item.status == 4){
                    item.status = '安装中'
                }else if(item.status == 5){
                    item.status = '重启中'
                }
            })
        },
    },
    mounted(){
        this.initData();
    }
}
</script>
