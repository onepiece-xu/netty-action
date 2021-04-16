<template>
    <div class="page-inner">
        <div class="echarts-module">
            <el-row :gutter="20">
                <el-col :span="12">
                    <div class="title">
                        <h4 class="text">实时设备利用率</h4>
                    </div>
                    <div class="module-wrap">
                        <div class="chart-wrap" v-show="dataList2.length>0">
                            <div id="status-data" style="width:100%;height:250px"></div>
                            <h4>总利用率（实时）：{{sum}}</h4>
                        </div>
                        <p class="no-data" v-show="dataList2.length==0">暂无数据</p>
                    </div>
                    <div class="table-content">
                        <el-table ref="multipleTable" 
                            :data="dataList2">
                            <el-table-column prop="clusterName" label="所属集群" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.clusterName'>{{scope.row.clusterName}}</span>
                                </template> 
                            </el-table-column>
                            <el-table-column prop="connectCount" label="连接数" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.connectCount'>{{scope.row.connectCount}}</span>
                                </template> 
                            </el-table-column>
                            <el-table-column prop="deviceCount" label="设备数" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.deviceCount'>{{scope.row.deviceCount}}</span>
                                </template> 
                            </el-table-column>
                            <el-table-column prop="" label="设备利用率" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.percentage1'>{{scope.row.percentage1}}</span>
                                </template> 
                            </el-table-column>
                        </el-table>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="title">
                        <h4 class="text">历史设备利用率</h4>
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
                    <div class="module-wrap">
                        <div class="chart-wrap" v-show="dataList5.length>0">
                            <div id="status-data1" style="width:100%;height:250px"></div>
                            <h4>总利用率（历史）：{{sum1}}</h4>
                        </div>
                        <p class="no-data" v-show="dataList5.length==0">暂无数据</p>
                    </div>
                    <div class="table-content">
                        <el-table ref="multipleTable" 
                            :data="dataList5">
                            <el-table-column prop="clusterName" label="所属集群" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.clusterName'>{{scope.row.clusterName}}</span>
                                </template> 
                            </el-table-column>
                            <el-table-column prop="" label="连接时长（小时）" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.totalTime1'>{{scope.row.totalTime1}}</span>
                                </template> 
                            </el-table-column>
                            <el-table-column prop="deviceCount" label="设备数" sortable> 
                                <template slot-scope="scope">
                                    <span :title='scope.row.deviceCount'>{{scope.row.deviceCount}}</span>
                                </template> 
                            </el-table-column>
                            <el-table-column prop="" label="设备利用率" sortable>
                                <template slot-scope="scope">
                                    <span :title='scope.row.deviceUsage1'>{{scope.row.deviceUsage1}}</span>
                                </template> 
                            </el-table-column>
                        </el-table>
                    </div>
                </el-col>
            </el-row>
        </div>
        <h4 style="font-size: 16px; font-weight: 600;margin-bottom:10px">设备利用率趋势图</h4>
        <div class="echarts-module" >
            <el-row :gutter="20">
                <el-col :span="24">
                    <div class="title">
                        <!-- <div class="control-group">
                            <label class="control-label">
                                <span class="red">*</span>应用：
                            </label>
                            <div class="controls">
                                <el-select
                                    v-model="appId"
                                    clearable
                                    multiple
                                    name="appId"
                                    placeholder="不选默认全部"
                                >
                                    <el-option
                                        v-for="item in appList"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </div>
                        </div> -->
                        <div class="time-tab">
                            <el-radio-group v-model="history1" size="mini" @change="selectTime1">
                                <!-- <el-radio-button :label="1">实时</el-radio-button> -->
                                <el-radio-button :label="3">近三天</el-radio-button>
                                <el-radio-button :label="7">近七天</el-radio-button>
                                <el-radio-button :label="30">近一个月</el-radio-button>
                                <el-radio-button :label="0">
                                    <el-date-picker
                                    v-model="time1"
                                    clearable
                                    type="daterange"
                                    @change="changeTime1"
                                    :picker-options="pickerOptions"
                                    value-format="yyyy-MM-dd"
                                    range-separator='至'
                                    start-placeholder='开始时间'
                                    end-placeholder='结束时间'>
                                    </el-date-picker>
                                </el-radio-button>
                            </el-radio-group>
                            
                        </div>
                        <!-- <div class="control-group" style="margin-left: 20px;">
                            <label class="control-label"></label>
                            <div class="controls">
                                <button type="button" class="btn btn-primary" @click="queryItem()">查询</button>
                            </div>
                        </div> -->
                    </div>
                    <div class="module-wrap" style="height:300px">
                        <div class="chart-wrap">
                            <div id="status-data2" style="width:100%;height:300px"></div>
                        </div>
                        <!-- <p class="no-data" v-show="dataList.length==0">暂无数据</p> -->
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import {getDeviceUsageRate,getDeviceUsageHistory,getTotalUsage,getUsageTrend} from '@/public/api/statistics.js'
export default {
    data(){
        return {
            dataList:[],
            dataList1:[],
            dataList2:[],
            dataList3:[],
            dataList4:[],
            dataList5:[],
            sum:'',
            sum1:'',
            history:'3',
            history1:'3',
            time:'',
            time1:'',
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now(); //设置选择今天以及今天以前的日期
                }
            },
        }
    },
    components: {

    },
    methods:{
        initEcharts(){
            getDeviceUsageRate().then((res)=>{
                if(res&&res.code == '200'){
                    this.dataList2 = res.data.records;
                    let sum = 0
                    let sum1 = 0
                    this.dataList2.forEach(function(item){
                        item.percentage1 = item.percentage?(Number(item.percentage)*100).toFixed(2)+"%":'';
                        sum += Number(item.connectCount)
                        sum1 += Number(item.deviceCount)
                    })
                    this.sum = (sum/sum1*100).toFixed(2) + "%"
                    let arr = res.data.records;
                    arr.forEach((item) => {
                        let obj = item.clusterName
                        this.dataList.push(obj)
                        let obj1 = (Number(item.percentage)*100).toFixed(2)
                        this.dataList1.push(obj1)
                    });
                    setTimeout(() => {
                        statisticsOrderBar = echarts.init(document.getElementById('status-data'))
                        statisticsOrderBar.setOption(option)
                        statisticsOrderBar.setOption({series:[
                            {
                                data:this.dataList1
                            }
                        ],xAxis:[
                            {
                                data:this.dataList
                            }
                        ]})
                    }, 0);
                }
            })
            // 饼状图
            let statisticsOrderBar ="";
            let option = {
                tooltip: {
                    trigger: 'axis',
                    formatter: "{b} <br/>设备利用率: {c}%"
                },
                xAxis: {
                    position:{
                        bottom:30,
                    },
                    type: 'category',
                    data: []
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: '{value}%'
                    }
                },
                grid:{
                    x:60,
                    y:60,
                    x2:60,
                    y2:40,
                },
                series: [{
                    data: [],
                    type: 'bar',
                    barWidth : 30,
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
                }]
            }
            /*窗口自适应，关键代码*/
            window.onresize = function () {
                statisticsOrderBar.resize();
            }
        },
        initEcharts1(start,end){
            let odta ={
                startTime:start,
                endTime:end
            }
            getTotalUsage(odta).then((res) =>{
                if(res&&res.code == '200'){
                    this.sum1 = (Number(res.msg)*100).toFixed(2) + '%'
                }
            })
            getDeviceUsageHistory(odta).then((res)=>{
                if(res&&res.code == '200'){
                    this.dataList5 = res.data.records;
                    this.dataList5.forEach(function(item){
                        item.deviceUsage1 = item.deviceUsage?(Number(item.deviceUsage)*100).toFixed(2)+"%":'';
                        item.totalTime1 = item.totalTime?(Number(item.totalTime)/60/60).toFixed(2):'';
                    })
                    let arr = res.data.records;
                    arr.forEach((item) => {
                        let obj = item.clusterName
                        this.dataList3.push(obj)
                        let obj1 = (Number(item.deviceUsage)*100).toFixed(2)
                        this.dataList4.push(obj1)
                    });
                    setTimeout(() => {
                        statisticsOrderBar1 = echarts.init(document.getElementById('status-data1'))
                        statisticsOrderBar1.setOption(option1)
                        statisticsOrderBar1.setOption({series:[
                            {
                                data:this.dataList4
                            }
                        ],xAxis:[
                            {
                                data:this.dataList3
                            }
                        ]})
                    }, 0);
                }
            })
            // 饼状图
            let statisticsOrderBar1 ="";
            let option1 = {
                // color:['#42bc9b','#e74c3c','#f39c12','#b936ff','#4c5bf8'],
                // title : {
                //     text: '设备利用率(历史)',
                //     left:20,
                //     top:20,
                //     textStyle:{
                //         fontSize:16,
                //         color:"#666666"
                //     }
                // },
                tooltip: {
                    trigger: 'axis',
                    
                    formatter: "{b} <br/>设备利用率: {c}%"
                },
                xAxis: {
                    type: 'category',
                    data: []
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: '{value}%'
                    }
                },
                grid:{
                    x:60,
                    y:60,
                    x2:60,
                    y2:40,
                },
                series: [{
                    data: [],
                    type: 'bar',
                    barWidth : 30,
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
                }]
            }
            /*窗口自适应，关键代码*/
            window.onresize = function () {
                statisticsOrderBar1.resize();
            }
        },
        selectTime(){
            this.dataList3 = []
            this.dataList4 = []
            this.dataList5 = []
            let startTime = "";
            let endTime = "";
            if(this.history == 1){
                startTime = moment().format("YYYY-MM-DD");
                this.initEcharts1(startTime,startTime,'day');
            }else{
                startTime = moment().subtract((this.history-1), "days").format("YYYY-MM-DD");
                endTime = moment().format("YYYY-MM-DD");
                this.initEcharts1(startTime,endTime,'week');
            }
            
        },
        changeTime(val){
            console.log(val);
            this.history = 0;
            this.dataList3 = []
            this.dataList4 = []
            this.dataList5 = []
            this.initEcharts1(val[0],val[1]);
        },
        historyAmount(start,end){
            let timeData = {
                startTime:start,
                endTime:end
            }
            let statisticsOrderBar2 = echarts.init(document.getElementById('status-data2'))
            let option_line_data = [];
            let data =[];
            let seriesdata = [];
            let option_line = {
                // color:['#2aabfd','#42bc9b','#ff8a00'],
                tooltip : {
                    trigger: 'axis',
                    axisPointer: {
                        label: {
                            backgroundColor: '#6a7985',
                        }
                    },
                    formatter : function(params) {
                        var relVal = params[0].axisValue;  
                        for (var i = 0, l = params.length; i < l; i++) {  
                            relVal += '<br/>' + params[i].seriesName + ' : ' + params[i].value+"%";
                        }
                        return relVal;
                    }

                },
                xAxis: {
                    // name: '时间',
                    type: 'category',
                    boundaryGap: false,
                    data: option_line_data,
                    axisLabel: {
                        show: true,
                        fontSize:12,
                        color: '#7991AB',
                        margin:10,
                    }
                },
                grid: {
                    left: 45,
                    right: 50,
                    bottom: 30,
                    top:40,
                    containLabel: true
                },
                yAxis: {
                    name: '利用率',
                    type: 'value',
                    axisLabel: {
                        show: true,
                        fontSize:12,
                        color: '#7991AB',
                        formatter:'{value}%',
                    }
                },
                series:[],
            }
            getUsageTrend(timeData).then(res =>{
                if(res&&res.data){
                    option_line_data = res.data.time
                    data = res.data.list
                    data.forEach((item) => {
                        // let arr = item.deviceUsage.split(',')
                        let arr = item.deviceUsage
                        let arr1 = []
                        arr.forEach((item) =>{
                            arr1.push((item*100).toFixed(2))
                        })
                        // console.log(arr1)
                        let seriesDataMap = {
                            data: arr1,
                            type: 'line',
                            name:item.clusterName,
                            //填充色
                            // areaStyle: {
                            //     normal:{
                            //         color:'#022132'
                            //     }
                            // },
                            lineStyle:{
                                // color: "#2aabfd",
                                width:'3',
                            },
                            //平滑曲线
                            // smooth: 0.5,
                        }
                        seriesdata.push(seriesDataMap)
                    });
                    console.log(seriesdata)
                    setTimeout(() => {
                        statisticsOrderBar2.setOption(option_line,true)
                        statisticsOrderBar2.setOption({series:seriesdata,xAxis:[
                            {
                                data:option_line_data
                            }
                        ]})
                    }, 0);
                }
            })
            
        },
        queryItem(){
            let startTime = "";
            let endTime = "";
            if(this.time1&&this.time1.length>0){
                startTime = this.time1[0]
                endTime = this.time1[1]
            }else{
                startTime = moment().subtract((this.history1-1), "days").format("YYYY-MM-DD");
                endTime = moment().format("YYYY-MM-DD");
            }
            console.log(startTime)
            // return
            this.historyAmount(startTime,endTime)
        },
        selectTime1(){
            // this.time1 = ''
            let startTime = "";
            let endTime = "";
            startTime = moment().subtract((this.history1-1), "days").format("YYYY-MM-DD");
            endTime = moment().format("YYYY-MM-DD");
            this.historyAmount(startTime,endTime);
            
        },
        changeTime1(val){
            console.log(val);
            this.history = 0;
            this.historyAmount(val[0],val[1]);
        },
    },
    computed:{
        
    },
    watch:{
        
    },
    mounted(){
        let startTime = moment().subtract('days',2).format("YYYY-MM-DD");
        let endTime = moment().format("YYYY-MM-DD");
        this.initEcharts()
        this.initEcharts1(startTime,endTime,'week');
        this.historyAmount(startTime,endTime)
    }
}
</script>
