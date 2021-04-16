<template>
    <div class="page-inner">
        <div class="echarts-module">
            <el-row :gutter="20">
                <el-col :span="12">
                    <div class="title">
                        <h4 class="text">集群连接数统计</h4>
                        <div class="time-tab">
                            <el-radio-group v-model="history" size="mini" @change="selectTime">
                                <el-radio-button :label="1">实时</el-radio-button>
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
                        <div class="chart-wrap" v-show="dataList.length>0">
                            <div id="status-data" style="width:100%;height:250px"></div>
                            <h4>总连接次数：{{sum}}</h4>
                        </div>
                        <p class="no-data" v-show="dataList.length==0">暂无数据</p>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="title">
                        <h4 class="text">历史集群连接时长统计</h4>
                        <div class="time-tab">
                            <el-radio-group v-model="history1" size="mini" @change="selectTime1">
                                <!-- <el-radio-button :label="1">历史</el-radio-button> -->
                                <el-radio-button :label="3">近三天</el-radio-button>
                                <el-radio-button :label="7">近七天</el-radio-button>
                                <el-radio-button :label="30">近一个月</el-radio-button>
                                <el-radio-button :label="0">
                                    <el-date-picker
                                    v-model="time1"
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
                    </div>
                    <div class="module-wrap">
                        <div class="chart-wrap" v-show="dataList1.length>0">
                            <div id="status-data1" style="width:100%;height:250px"></div>
                            <h4>总连接时长：{{sum1}}小时</h4>
                        </div>
                        <p class="no-data" v-show="dataList1.length==0">暂无数据</p>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import {getClusterConnectionsStatistics,getClusterConnectTimeStatistics,getClusterConnectionsRealTime} from '@/public/api/statistics.js'
export default {
    data(){
        return {
            dataList:[],
            dataList1:[],
            sum:'',
            sum1:'',
            time:'',
            time1:'',
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now(); //设置选择今天以及今天以前的日期
                }
            },
            history:'1',
            history1:'3',
        }
    },
    components: {

    },
    methods:{
        initEcharts(start,end,type){
            let timeData = {
                startTime:start,
                endTime:end
            }
            this.dataList = []
            if(type == 'day'){
                getClusterConnectionsRealTime().then((res)=>{
                    if(res&&res.code == '200'){
                        // this.dataList = res.data
                        let arr = res.data;
                        let sum = 0;
                        arr.forEach((item) => {
                            sum += Number(item.count)
                            let obj = {
                                name:item.clusterName,
                                value:item.count
                            }
                            this.dataList.push(obj)
                        });
                        this.sum = sum
                        console.log(this.sum)
                        console.log(this.dataList)
                        setTimeout(() => {
                            statisticsOrderBar = echarts.init(document.getElementById('status-data'))
                            statisticsOrderBar.setOption(option)
                            statisticsOrderBar.setOption({series:[
                                {
                                    data:this.dataList
                                }
                            ],legend:[
                                {
                                    data:this.dataList
                                }
                            ]})
                        }, 0);
                    }
                })
            }else{
                getClusterConnectionsStatistics(timeData).then((res)=>{
                    if(res&&res.code == '200'){
                        
                        // this.dataList = res.data
                        let arr = res.data;
                        let sum = 0;
                        arr.forEach((item) => {
                            sum += Number(item.count)
                            let obj = {
                                name:item.clusterName,
                                value:item.count
                            }
                            this.dataList.push(obj)
                        });
                        this.sum = sum
                        console.log(this.sum)
                        console.log(this.dataList)
                        setTimeout(() => {
                            statisticsOrderBar = echarts.init(document.getElementById('status-data'))
                            statisticsOrderBar.setOption(option)
                            let resize = {
                                width: this.initWidth,
                                height: 250
                            };
                            statisticsOrderBar.resize(resize);
                            statisticsOrderBar.setOption({series:[
                                {
                                    data:this.dataList
                                }
                            ],legend:[
                                {
                                    data:this.dataList
                                }
                            ]})
                        }, 0);
                    }
                })
            }
            // 饼状图
            let statisticsOrderBar ="";

            let option = {
                // color:['#42bc9b','#e74c3c','#f39c12','#b936ff','#4c5bf8'],
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    right:20,
                    top:20,
                    itemWidth:24,
                    itemHeight:10,
                    data: ['集群1', '集群2', '集群3', '集群4', '集群5', '集群6', '集群7']
                },
                series: [
                    {
                        name:'集群连接数',
                        type:'pie',
                        radius: '70%',
                        center: ['45%', '55%'],
                        data: [
                            {value: 335, name: '集群1'},
                            {value: 310, name: '集群2'},
                            {value: 234, name: '集群3'},
                            {value: 135, name: '集群4'},
                            {value: 1548, name: '集群5'},
                            {value: 500, name: '集群6'},
                            {value: 500, name: '集群7'}
                        ],
                    }
                ]
            }

            /*窗口自适应，关键代码*/
            window.onresize = function () {
                statisticsOrderBar.resize();
            }
        },
        initEcharts1(start,end,type){
            let timeData = {
                startTime:start,
                endTime:end
            }
            this.dataList1 = []
            if(type == 'day'){
                getClusterConnectTimeStatistics().then((res)=>{
                    if(res&&res.code == '200'){
                        // this.dataList = res.data
                        let arr = res.data;
                        let sum1 = 0;
                        arr.forEach((item) => {
                            sum1 += Number(item.count)
                            let obj = {
                                name:item.clusterName,
                                value:Number(item.count).toFixed(2)
                            }
                            this.dataList1.push(obj)
                        });
                        this.sum1 = sum1.toFixed(2)
                        console.log(this.sum1)
                        console.log(this.dataList1)
                        setTimeout(() => {
                            statisticsOrderBar1 = echarts.init(document.getElementById('status-data1'))
                            statisticsOrderBar1.setOption(option)
                            statisticsOrderBar1.setOption({series:[
                                {
                                    data:this.dataList1
                                }
                            ],legend:[
                                {
                                    data:this.dataList1
                                }
                            ]})
                        }, 0);
                    }
                })
            }else{
                getClusterConnectTimeStatistics(timeData).then((res)=>{
                    if(res&&res.code == '200'){
                        
                        // this.dataList = res.data
                        let arr = res.data;
                        let sum1 = 0;
                        arr.forEach((item) => {
                            sum1 += Number(item.count)
                            let obj = {
                                name:item.clusterName,
                                value:Number(item.count).toFixed(2)
                            }
                            this.dataList1.push(obj)
                        });
                        this.sum1 = sum1.toFixed(2)
                        console.log(this.sum1)
                        console.log(this.dataList1)
                        setTimeout(() => {
                            statisticsOrderBar1 = echarts.init(document.getElementById('status-data1'))
                            statisticsOrderBar1.setOption(option)
                            let resize = {
                                width: this.initWidth,
                                height: 250
                            };
                            statisticsOrderBar1.resize(resize);
                            statisticsOrderBar1.setOption({series:[
                                {
                                    data:this.dataList1
                                }
                            ],legend:[
                                {
                                    data:this.dataList1
                                }
                            ]})
                        }, 0);
                    }
                })
            }
            // 饼状图
            let statisticsOrderBar1 ="";

            let option = {
                // color:['#42bc9b','#e74c3c','#f39c12','#b936ff','#4c5bf8'],
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} 小时({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    right:20,
                    top:20,
                    itemWidth:24,
                    itemHeight:10,
                    data: ['集群1', '集群2', '集群3', '集群4', '集群5', '集群6', '集群7']
                },
                series: [
                    {
                        name:'集群连接时长',
                        type:'pie',
                        radius: '70%',
                        center: ['45%', '55%'],
                        data: [
                            {value: 335, name: '集群1'},
                            {value: 310, name: '集群2'},
                            {value: 234, name: '集群3'},
                            {value: 135, name: '集群4'},
                            {value: 1548, name: '集群5'},
                            {value: 500, name: '集群6'},
                            {value: 500, name: '集群7'}
                        ],
                    }
                ]
            }

            /*窗口自适应，关键代码*/
            window.onresize = function () {
                statisticsOrderBar1.resize();
            }
        },
        selectTime(){
            let startTime = "";
            let endTime = "";
            if(this.history == 1){
                startTime = moment().format("YYYY-MM-DD");
                this.initEcharts(startTime,startTime,'day');
            }else{
                startTime = moment().subtract((this.history-1), "days").format("YYYY-MM-DD");
                endTime = moment().format("YYYY-MM-DD");
                this.initEcharts(startTime,endTime,'week');
            }
            
        },
        changeTime(val){
            console.log(val);
            this.history = 0;
            this.initEcharts(val[0],val[1]);
        },
        selectTime1(){
            let startTime = "";
            let endTime = "";
            if(this.history1 == 1){
                startTime = moment().format("YYYY-MM-DD");
                this.initEcharts1(startTime,startTime,'day');
            }else{
                startTime = moment().subtract((this.history1-1), "days").format("YYYY-MM-DD");
                endTime = moment().format("YYYY-MM-DD");
                this.initEcharts1(startTime,endTime,'week');
            }
            
        },
        changeTime1(val){
            console.log(val);
            this.history1 = 0;
            this.initEcharts1(val[0],val[1]);
        },
    },
    computed:{
        
    },
    watch:{
        
    },
    mounted(){
        let startTime = moment().subtract('days',2).format("YYYY-MM-DD");
        let endTime = moment().format("YYYY-MM-DD");
        this.initEcharts(startTime,endTime,'day')
        this.initEcharts1(startTime,endTime,'week')
    }
}
</script>
