<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-info" @click="clearItem" :disabled="!canClear">清理</button>
            <div class="pull-right">
                <div class="search-box">
                    <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                        <button class="btn" type="submit" :disabled="false">
                            <i class="icon-aw-search"></i>
                        </button>
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入应用名称" name="searchTerm" v-model="globalSearchTerm"/>
                    </form>
                </div>
                <el-tooltip class="item" effect="dark" :content="$t('cn.tabelTips.refreshList')" placement="top">
                    <button class="btn btn-renovat btn-search" @click="refresh()">
                        <i class="icon-aw-refresh"></i>
                    </button>
                </el-tooltip>
            </div>
        </div>
        <div class="table-content">
            <el-table ref="multipleTable" 
                :data="dataList" 
                @selection-change="handleSelectionChange" >
                <el-table-column type="selection" width="40"> </el-table-column>
                <el-table-column prop="appId" label="应用ID" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.appId'>{{scope.row.appId}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="appName" label="应用名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.appName'>{{scope.row.appName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="clusterName" label="所属区服" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.clusterName'>{{scope.row.clusterName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="queue" label="排队数量" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.queue'>{{scope.row.queue}}</span>
                    </template>
                </el-table-column>
            </el-table>
            <div v-if="pageInstan.showPage">
                <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
            </div>
        </div>
        <el-dialog
            :visible.sync="clearDialogVisible"
            custom-class="small-dialog delete-dialog"
            center
            :close-on-click-modal="false">
            <span>是否确认清理应用排队僵尸数据选择的应用？</span>
            <div slot="footer">
                <button class="btn btn-warning" type="button" :disabled="clearItemDisabled" @click="clear()">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="clearDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {getAppQueueNum,clearQueueCorpse} from '@/public/api/statistics.js';
export default {
    data(){
        return {
            canClear:false,
            hasRouteQuery:0,
            multipleSelection: [],
            dataList:[],
            // page
            globalSearchTerm:'',
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            clearDialogVisible:false,
            clearItemDisabled:false
        }
    },
    components: {
        
    },
    methods:{
        handleSizeChange(val) {
            this.pageInstan.pageSize = val;
            this.pageInstan.currentPage = 1;
            this.initData();
        },
        handleCurrentChange(val) {
            this.pageInstan.currentPage = val;
            this.initData('','pageOne');
        },
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize,
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            getAppQueueNum(oData).then((res) =>{
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
        clearItem(){
            this.clearDialogVisible = true
        },
        clear(){
            let ids = [];
            this.multipleSelection.map(item =>{
                let a = { 
                    'appId':item.appId,
                    'clusterId':item.clusterId
                }
                ids.push(a)
            })
            let data = ids
            console.log(data)
            clearQueueCorpse(data).then((res) =>{
                console.log(res)
                this.clearDialogVisible = false;
                this.initData()
            })
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            if(this.multipleSelection.length == 0){
                this.canClear = false
            }else if(this.multipleSelection.length == 1){
                this.canClear = true
            }else if(this.multipleSelection.length > 1){
                this.canClear = true
            }
        },
        //刷新
        refresh(){
            this.globalSearchTerm = ''
            // this.pageInstan.pageSize = 50
            // this.pageInstan.currentPage = 1
            this.initData()
        },
        // 搜索
        applyGlobalSearch(){
            this.initData()
        }
    },
    computed:{
        
    },
    watch:{
        $route(){
            this.hasRouteQuery = this.$route.query.id?1:0;
        }
    },
    mounted(){
        this.initData();
        this.hasRouteQuery = this.$route.query.id?1:0;
    }
}
</script>
