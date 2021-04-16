<template>
    <div class="page-inner">
        <div class="table-action metering">          
            <div class="pull-right">
                <div class="control-group">
                    <div class="controls">
                        <input type="text" class="form-control" placeholder="请输入设备名称/IP" v-model="globalSearchTerm" name="globalSearchTerm">
                    </div>
                </div>
                <div class="control-group" style="width:30%">
                    <label class="control-label" style="width:20%">日期：</label>
                    <div class="controls">
                        <el-date-picker
                            v-model="timeRange"
                            :picker-options="pickerOptions"
                            type="daterange"
                            value-format="yyyy-MM-dd"
                            format="yyyy-MM-dd"
                            range-separator="-"
                            start-placeholder='选择日期'
                            end-placeholder='选择日期'>
                        </el-date-picker>
                    </div>
                </div>
                <div class="control-group" style="width:5%">
                    <label class="control-label"></label>
                    <div class="controls">
                        <button class="btn btn-primary" type="button" @click="searchItem">搜索</button>
                    </div>
                </div>                    
            </div>          
        </div>

        <div class="table-content">
            <el-table ref="multipleTable" 
                :data="dataList" 
                @selection-change="handleSelectionChange" >
                <el-table-column prop="id" label="连接ID" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.id'>{{scope.row.id}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="deviceId" label="设备ID" width="180px">
                    <template slot-scope="scope">
                        <span :title='scope.row.deviceId'>{{scope.row.deviceId}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="deviceName" label="设备名称" sortable >
                    <template slot-scope="scope">
                        <span :title='scope.row.deviceName'>{{scope.row.deviceName}}</span>
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
                <el-table-column prop="beginTime" label="计量起始时间" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.beginTime'>{{scope.row.beginTime}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="endTime" label="计量结束时间" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.endTime'>{{scope.row.endTime}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="totalTime" label="计量时长（min）" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.totalTime'>{{scope.row.totalTime}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="status" label="计量状态" :filter-multiple="false">
                    <template slot-scope="scope">
                        <span :title='scope.row.status' :class="{'bg-green':scope.row.status == '已结束','bg-blue':scope.row.status == '未完成'}">{{scope.row.status}}</span>
                    </template> 
                </el-table-column>
            </el-table>
            <div v-if="pageInstan.showPage">
                <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
            </div>
        </div>
        <detail-page :props="hasRouteQuery">
            <metering-detail :detailProps="{id:$route.query.id}" v-if='hasRouteQuery'></metering-detail>
        </detail-page>
    </div>
</template>

<script>
//时间转换
import {getMeterageList} from '@/public/api/metering.js';
import detailPage from '@/public/common-tmpl/detail.vue' 
export default {
    data(){
        return {
            multipleSelection: [],
            dataList:[],
            hasRouteQuery:0,
            // page
            globalSearchTerm:'',
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            meteringForm:{},
            timeRange:'',
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now(); //设置选择今天以及今天以前的日期
                }
            },
            timeDisabled1:false,
            timeDisabled:false
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
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize,
            }
            if(this.timeRange&&this.timeRange.length>0){
                oData.beginDate = this.timeRange[0]
                oData.endDate = this.timeRange[1]
            }           
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            getMeterageList(oData).then((res) =>{
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
        select(){
            if(this.meteringForm.type){
                this.timeDisabled = true
                this.timeDisabled1 = true
                this.timeRange1 = ''
            }else{
                this.timeDisabled = false
                this.timeDisabled1 = false
                this.timeRange = ''
            }
        },
        optionData(data){
            data.forEach(function(item){
                item.beginTime = item.beginTime?moment(item.beginTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.endTime = item.endTime?moment(item.endTime).format("YYYY-MM-DD HH:mm:ss"):'';
                if(item.status == 0){
                    item.status = '未完成'
                    item.totalTime = '-'
                }else{
                    item.status = '已结束'
                    item.totalTime = item.totalTime?(item.totalTime/60).toFixed(0):'';
                }
            })
        },
        // 搜索
        searchItem(){
            this.initData()
        },
        //清空
        emptyItem(){
            this.queryForm = {}
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },       
        //刷新
        refresh(){
            this.globalSearchTerm = ''
            this.initData()
        },
        // 搜索
        applyGlobalSearch(){
            this.pageInstan.pageSize = 50
            this.pageInstan.currentPage = 1
            this.initData()
        },
        
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
