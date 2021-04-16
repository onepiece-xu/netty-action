<template>
    <div>
        <div class="page-inner query">
            <div class="table-action">
                <button type="button" class="btn btn-info" @click="restartItem" >关机</button>               
                <div class="pull-right">
                    <div class="search-box">
                        <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                            <button class="btn" type="submit" :disabled="false">
                                <i class="icon-aw-search"></i>
                            </button>
                            <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入设备名称/IP" name="searchTerm" v-model="globalSearchTerm"/>
                        </form>
                    </div>
                    <el-tooltip class="item" effect="dark" :content="$t('cn.tabelTips.refreshList')" placement="top">
                        <button class="btn btn-renovat btn-search" @click="refresh()">
                            <i class="icon-aw-refresh"></i>
                        </button>
                    </el-tooltip>
                </div>                
                <div class="btn-search" v-show="open">
                    <button type="button" class="btn btn-primary" @click="searchItem">搜索</button>
                    <button type="button" class="btn btn-info" @click="emptyItem">清空</button>
                </div>
            </div>
            <div class="table-content">
                <el-table ref="multipleTable" 
                    :data="dataList" 
                    @filter-change="handleFilterChange"
                    @selection-change="handleSelectionChange" >
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="id" label="设备ID" width="180px">
                        <template slot-scope="scope">
                            <span :title='scope.row.id'>{{scope.row.id}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="设备名称" sortable >
                        <template slot-scope="scope">
                            <span :title='scope.row.name'>{{scope.row.name}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="macAddr" label="设备mac" sortable >
                        <template slot-scope="scope">
                            <span :title='scope.row.macAddr'>{{scope.row.macAddr}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="intranetIp" label="设备内网管理IP" width="170" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.intranetIp'>{{scope.row.intranetIp}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="status" label="设备运行状态" column-key="status" :filter-multiple="false" :filters="[{text: '离线', value: 0},{text: '在线', value: 1}]">
                        <template slot-scope="scope">
                            <span :title='scope.row.status' :class="{'bg-green':scope.row.status == '在线','bg-red':scope.row.status == '离线'}">{{scope.row.status}}</span>
                        </template> 
                    </el-table-column>               
                    <el-table-column prop="detail" label="详情">
                        <template slot-scope="scope" v-if="scope.row.status == '在线'">
                            <span class="btn btn-primary" @click="openStream(scope.row.id)">查看</span>
                        </template>
                        <template v-else>
                            <span>--</span>
                        </template>
                    </el-table-column>
                </el-table>
                <div v-if="pageInstan.showPage">
                    <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
                </div>
                <detail-page :props="hasRouteQuery">
                    <equipment-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></equipment-detail>
                </detail-page>
            </div>
            <el-dialog
                :visible.sync="operationDialogVisible"
                custom-class="small-dialog delete-dialog"
                center
                :close-on-click-modal="false">
                <span>{{operationTitle}}</span>
                <div slot="footer">
                    <button class="btn btn-warning" type="button" :disabled="operationItemDisabled" @click="operation()">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="operationDialogVisible = false">{{$t('cn.action.cancel')}}</button>
                </div>
            </el-dialog>
        </div>
    </div>
    
    
</template>

<script>
import {shutdown,getDeviceList,connectStream} from '@/public/api/equipment.js';
import detailPage from '@/public/common-tmpl/detail.vue' 
export default {
    data(){
        return {
            open:false,
            hasRouteQuery:0,
            multipleSelection: [],
            dataList:[],
            // page
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            queryForm:{
                "id":"",
                "name":'',
                "macAddr":'',
                "intranetIp":'',
                "status":'',
            },
            globalSearchTerm:'',
            operationItemDisabled:false,
            operationDialogVisible:false,
            operationTitle:'',
        }
    },
    components: {
        detailPage
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
        handleFilterChange(filters){
            console.log(filters)
            this.pageInstan.pageSize = 50
            this.pageInstan.currentPage = 1
            if(filters.status){
                this.status = filters.status[0]
            }
            this.initData()
        },
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize,
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            if(this.status){
                oData.status = this.status
            }
            if(this.appStatus){
                oData.appStatus = this.appStatus
            }
            getDeviceList(oData).then((res) =>{
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
                    this.optionData(this.dataList)
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
                item.status = item.status == 0 ?'离线':'在线'
            })
        },
        restartItem(){
            this.type = 'shutdown'
            this.operationDialogVisible = true
            this.operationTitle = '是否确认关闭选择的设备？'
        },       
        operation(){
            let ids = [];
            this.multipleSelection.map(item =>{
                ids.push(item.id)
            })
            let data = ids
            console.log(data)
            // return
            if(this.type == 'shutdown'){
                shutdown(data).then((res) =>{
                    console.log(res)
                    this.operationDialogVisible = false;
                    this.initData()
                })
            }
        },
        applyGlobalSearch(){
            this.initData()
        },
        // 搜索
        searchItem(){
            this.initData()
        },
        //清空
        emptyItem(){
            this.queryForm = {}
        },
        refresh(){
            this.globalSearchTerm = ''
            this.initData()
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        openItem(){
            this.open = true
        },
        retractItem(){
            this.open = false
            this.queryForm = {}
        },
        openStream(id){
            let oData = {
                'deviceId': id,
            }
            connectStream(oData).then((res) =>{
                if(res&&res.code == 200){
                    res.data.deviceType = 1;
                    res.data.screen = 1;
                    window.setDeviceConnect(JSON.stringify(res.data));
                    window.open('#/run','_blank')
                } else {
                    this.$store.commit('connectError',{'data':res,'time':Date.now()})
                    this.$message.error('非常抱歉，暂时无法查看！');
                }
            }) 
        },
    },
    computed:{
        bth_check(){
         
        }
    },
    watch:{
        $route(){
            this.hasRouteQuery = this.$route.query.id?1:0;
        }
    },
    mounted(){
        this.initData()
        this.hasRouteQuery = this.$route.query.id?1:0;
    }
}
</script>
