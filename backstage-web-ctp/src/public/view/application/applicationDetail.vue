<template>
    <div class="tab-content">
        <div class="detail-row">
            <div class="detail-title">
                <a class="an-close" @click="$router.go(-1)">
                    <i class="icon-aw-mail-reply"></i>
                </a>
                <span>应用详情</span>
            </div>
            <div class="detail-info">
                <table class='table-dex' >
                    <h4>应用信息</h4>
                    <tr>
                        <td>应用ID</td>
                        <td>{{detailInfo.id}}</td>
                        <td>应用名称</td>
                        <td>{{detailInfo.name}}</td>
                    </tr>
                    <tr>
                        <td>应用英文名称</td>
                        <td>{{detailInfo.englishName}}</td>
                        <td>应用类型</td>
                        <td>{{detailInfo.appType}}</td>
                        
                    </tr>
                    <tr>
                        <td>应用显示类型</td>
                        <td>{{detailInfo.screen ==1?"横屏":"竖屏"}}</td>
                        <td>应用许可证号(版号)</td>
                        <td>{{detailInfo.accessId}}</td>
                        
                    </tr>
                    <tr>
                        <td>所属用户</td>
                        <td>{{detailInfo.enterpriseName}}</td>
                        <td>应用描述</td>
                        <td>{{detailInfo.description}}</td>
                    </tr>
                    <h4>部署信息</h4>
                    <tr>
                        <td>安装文件名</td>
                        <td>{{detailInfo.packageFileName}}</td>
                        <td>安装文件路径</td>
                        <td>{{detailInfo.path}}</td>
                    </tr>
                    <tr>
                        <td>包名</td>
                        <td>{{detailInfo.packageName}}</td>
                        <td>流服务策略</td>
                        <td>{{detailInfo.strategyName}}</td>
                    </tr>
                    <tr>
                        <td>是否自动安装</td>
                        <td>{{detailInfo.schInstall?'是':'否'}}</td>
                        <td>最大安装数</td>
                        <td>{{detailInfo.installMaxNumber}}</td>
                    </tr>
                    <tr>
                        <td>安装包大小(MB)</td>
                        <td>{{detailInfo.size}}</td>
                        <td>安装后大小(MB)</td>
                        <td>{{detailInfo.realSize}}</td>
                    </tr>
                    
                    <tr>
                        <td>账号数据路径</td>
                        <td>{{detailInfo.savePath}}</td>
                        <td>存档数据路径</td>
                        <td>{{detailInfo.accountPath}}</td>
                    </tr>
                    <tr>
                        <td>预启动脚本路径</td>
                        <td>{{detailInfo.startShell}}</td>
                        <td>结束脚本路径</td>
                        <td>{{detailInfo.closeShell}}</td>
                    </tr>
                    <tr>
                        <td>上传时间</td>
                        <td>{{detailInfo.uploadTime}}</td>
                        <td>上架时间</td>
                        <td>{{detailInfo.approvalTime}}</td>
                    </tr>
                </table>
                <p class="app-manager">应用已安装设备</p>
                <div class="table-content">
                    <el-table ref="multipleTable" 
                        :data="dataList"
                        height="345">
                        <el-table-column prop="id" label="设备ID">
                            
                        </el-table-column>
                        <el-table-column prop="name" label="设备名称">
                            <template slot-scope="scope">
                                <span :title='scope.row.name'>{{scope.row.name}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="clusterName" label="所属集群">
                            <template slot-scope="scope">
                                <span :title='scope.row.clusterName'>{{scope.row.clusterName}}</span>
                            </template>
                        </el-table-column>
                        <!-- <el-table-column prop="groupName" label="所属设备组">
                            <template slot-scope="scope">
                                <span :title='scope.row.groupName'>{{scope.row.groupName}}</span>
                            </template>
                        </el-table-column> -->
                        <el-table-column prop="ip" label="设备内部IP">
                            <template slot-scope="scope">
                                <span :title='scope.row.ip'>{{scope.row.ip}}</span>
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
import {getUserApp,getDevicesByInstallAppId} from '@/public/api/application.js';

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
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize
            }
            let id = this.$route.query.id
            getUserApp(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                    this.optionData([this.detailInfo])
                }
            })
            getDevicesByInstallAppId(oData,id).then((res) =>{
                console.log(res);
                this.dataList = res.data.records
                if(this.dataList.length > 0){
                    this.pageInstan.Total = res.data.total;
                    this.pageInstan.showPage = true;//判断数据是否为空 为空则不显示分页
                }else{
                    this.pageInstan.Total = res.data.total;
                    this.pageInstan.showPage = false;//判断数据是否为空 为空则不显示分页
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
                item.uploadTime = item.uploadTime?moment(item.uploadTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.approvalTime = item.approvalTime?moment(item.approvalTime).format("YYYY-MM-DD HH:mm:ss"):'';
                if(item.type == 0){
                    item.appType = 'arm'
                }else if(item.type == 1){
                    item.appType = 'x86'
                }else if(item.type == 2){
                    item.appType = '云手机'
                }else if(item.type == 3){
                    item.appType = '云电脑'
                }
            })
        },
    },
    mounted(){
        this.initData();
    }
}
</script>
