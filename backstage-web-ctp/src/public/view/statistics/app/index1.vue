<template>
    <div class="page-inner">
        <div class="echarts-module">
            <el-row :gutter="20">
                <el-col :span="12">
                    <div class="title">
                        <div class="time-tab">
                            <el-radio-group v-model="history" size="mini" @change="selectTime">
                                <!-- <el-radio-button :label="1">实时</el-radio-button> -->
                                <el-radio-button :label="3">近三天</el-radio-button>
                                <el-radio-button :label="7">近七天</el-radio-button>
                                <el-radio-button :label="30">近一个月</el-radio-button>
                                <el-radio-button :label="0">
                                    <el-date-picker
                                    v-model="time"
                                    type="daterange"
                                    @change="changeTime"
                                    :picker-options="pickerOptions"
                                    value-format="yyyy-MM-dd"
                                    range-separator='至'
                                    start-placeholder='开始时间'
                                    end-placeholder='结束时间'>
                                    </el-date-picker>
                                </el-radio-button>
                            </el-radio-group>
                        </div>
                    </div>
                    <div class="module-wrap" style="max-height:600px;min-height: 600px;">
                        <div class="chart-wrap" v-show="dataList1.length>0">
                            <div id="status-data" style="width:100%;height:600px"></div>
                        </div>
                        <p class="no-data" v-show="dataList1.length==0">暂无数据</p>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="table-content" style="margin-top:50px">
                        <el-table ref="multipleTable" 
                            :data="dataList"
                            >
                            <el-table-column prop="appName" label="应用名称" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.appName'>{{scope.row.appName}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="count" label="应用连接时长（小时）" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.count'>{{scope.row.count}}</span>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div v-if="pageInstan.showPage">
                            <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <!-- <div class="table-content">
                <el-table ref="multipleTable" 
                    :data="dataList"
                    show-summary>
                    <el-table-column prop="appName" label="应用名称" >
                        <template slot-scope="scope">
                            <span :title='scope.row.appName'>{{scope.row.appName}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="count" label="连接数" >
                        <template slot-scope="scope">
                            <span :title='scope.row.count'>{{scope.row.count}}</span>
                        </template>
                    </el-table-column>
                </el-table>
                <div v-if="pageInstan.showPage">
                    <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
                </div>
            </div> -->
        </div>
    </div>
</template>

<script>
import {getAppsConnectTimeStatistics,getAppsConnectTimeStatisticsTopTen} from '@/public/api/statistics.js'
export default {
    data(){
        return {
            multipleSelection: [],
            dataList:[],
            dataList1:[],
            // page
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            appForm:{},
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now(); //设置选择今天以及今天以前的日期
                }
            },
            history:3,
            time:''

        }
    },
    components: {

    },
    methods:{
        handleSizeChange(val) {
            this.pageInstan.pageSize = val;
            this.pageInstan.currentPage = 1;
            // this.initData();
            let startTime = "";
            let endTime = "";
            startTime = moment().subtract((this.history-1), "days").format("YYYY-MM-DD");
            endTime = moment().format("YYYY-MM-DD");
            this.initData(startTime,endTime,);
        },
        handleCurrentChange(val) {
            this.pageInstan.currentPage = val;
            // this.initData('','pageOne');
            let startTime = "";
            let endTime = "";
            startTime = moment().subtract((this.history-1), "days").format("YYYY-MM-DD");
            endTime = moment().format("YYYY-MM-DD");
            this.initData(startTime,endTime);
        },
        initData(start,end){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize,
                'startTime': start,
                'endTime': end
            }
            let data ={
                'startTime': start,
                'endTime': end
            }
            console.log(oData)
            getAppsConnectTimeStatistics(oData).then((res) =>{
                if(res&&res.data){
                    this.dataList = res.data.records;
                    if(this.dataList.length > 0){
                        this.pageInstan.Total = res.data.total;
                        this.pageInstan.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan.Total = res.data.total;
                        this.pageInstan.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                    this.dataList.forEach(function(item){
                        item.count = Number(item.count).toFixed(2)
                    })
                }
            })
            getAppsConnectTimeStatisticsTopTen(data).then((res) =>{
                if(res&&res.data){
                    this.dataList1 = res.data;
                    let obj = []
                    let obj1 = []
                    this.dataList1.forEach((item) => {
                        // let obj = {
                        //     name:item.appName,
                        //     value:item.count
                        // }
                        obj.push(item.appName)
                        obj1.push(Number(item.count).toFixed(2))
                    });
                    console.log(obj)
                    setTimeout(() => {
                        statisticsOrderBar = echarts.init(document.getElementById('status-data'))
                        statisticsOrderBar.setOption(option)
                        let resize = {
                            width: this.initWidth,
                            height: 600
                        };
                        statisticsOrderBar.resize(resize);
                        statisticsOrderBar.setOption({series:[
                            {
                                data:obj1
                            }
                        ],yAxis:[
                            {
                                data:obj
                            }
                        ]})
                    }, 0);
                }
                
            })
            // 饼状图
            let statisticsOrderBar ="";

            let option = {
                // color:['#42bc9b','#e74c3c','#f39c12','#b936ff','#4c5bf8'],
                title : {
                    text: '应用连接时长（Top10）',
                    left:20,
                    top:20,
                    textStyle:{
                        fontSize:16,
                        color:"#666666"
                    }
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c}小时"
                },
                grid: {
                    left: 40,
                    right: 50,
                    bottom: 30,
                    top:50,
                    containLabel: true
                },
                xAxis: {
                    type: 'value'
                },
                yAxis: {
                    type: 'category',
                    data: []
                },
                series: [
                    {
                        name:'应用连接时长',
                        type:'bar',
                        barWidth : 30,
                        data: [],
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#83bff6'},
                                    {offset: 0.5, color: '#188df0'},
                                    {offset: 1, color: '#188df0'}
                                ]
                            )
                        },
                    }
                ]
            }
            // setTimeout(() => {
            //     statisticsOrderBar = echarts.init(document.getElementById('status-data'))
            //     statisticsOrderBar.setOption(option)
            // }, 0);
            /*窗口自适应，关键代码*/
            window.onresize = function () {
                statisticsOrderBar.resize();
            }
        },
        // queryItem(){
        //     this.initData();
        // },
        selectTime(){
            let startTime = "";
            let endTime = "";
            startTime = moment().subtract((this.history-1), "days").format("YYYY-MM-DD");
            endTime = moment().format("YYYY-MM-DD");
            this.initData(startTime,endTime);
        },
        changeTime(val){
            console.log(val);
            this.history = 0;
            this.initData(val[0],val[1]);
        },
        handleSelectionChange(val){
            this.multipleSelection = val;
        }
    },
    computed:{
        
    },
    watch:{
        
    },
    mounted(){
        let startTime = moment().subtract('days',2).format("YYYY-MM-DD");
        let endTime = moment().format("YYYY-MM-DD");
        this.initData(startTime,endTime);
    }
}
</script>
