<template>
    <div class="page-inner">
        <div class="echarts-module">
            <el-row :gutter="20">
                <el-col :span="12">
                    <!-- <div class="title">
                        <h4 class="text">集群连接数统计</h4>
                    </div> -->
                    <div class="module-wrap">
                        <div class="chart-wrap" v-show="statusList.length>0">
                            <div id="status-data" style="width:100%;height:250px"></div>
                            <div class="status-list">
                                <ul class="status-title">
                                    <li>状态</li>
                                    <li>数量</li>
                                    <li>占比</li>
                                </ul>
                                <ul class="status-data">
                                    <li v-for="item in statusList" :key="item.id">
                                        <span :style="{'backgroundColor':item.bgColor}" class="status-color"></span>
                                        <span :title="item.name">{{item.name}}</span>
                                        <span :title="item.value">{{item.value}}</span>
                                        <span :title="item.percent">{{item.percent}}</span>
                                    </li>
                                </ul>
                            </div>
                            <!-- <h4>总连接次数：{{sum}}</h4> -->
                        </div>
                        <p class="no-data" v-show="statusList.length==0">暂无数据</p>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="module-wrap">
                        <div class="chart-wrap">
                            <div id="status-data4" style="width:100%;height:250px"></div>
                            <!-- <h4>总连接次数：{{sum}}</h4> -->
                        </div>
                        <!-- <p class="no-data" v-show="statusList.length==0">暂无数据</p> -->
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <div class="title">
                        <h4 class="text">集群实时统计：{{sum1}}次</h4>
                        <!-- <h4 class="text" style="margin-left:25px">实时连接次数：{{sum1}}</h4> -->
                    </div>
                    <div class="module-wrap">
                        <div class="chart-wrap" v-show="dataList.length>0">
                            <div id="status-data1" style="width:100%;height:250px"></div>
                            <!-- <h4>总连接次数：{{sum1}}</h4> -->
                        </div>
                        <p class="no-data" v-show="dataList.length==0">暂无数据</p>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="title">
                        <h4 class="text">应用实时统计：{{sum2}}次</h4>
                    </div>
                    <div class="module-wrap">
                        <div class="chart-wrap" v-show="dataList1.length>0">
                            <div id="status-data2" style="width:100%;height:250px"></div>
                        </div>
                        <p class="no-data" v-show="dataList1.length==0">暂无数据</p>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <div class="title">
                        <h4 class="text">应用安装统计</h4>
                    </div>
                    <div class="module-wrap">
                        <div class="chart-wrap">
                            <div id="status-data3" style="width:100%;height:250px"></div>
                        </div>
                        <!-- <p class="no-data" v-show="dataList3.length==0">暂无数据</p> -->
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import {getDeviceStatusCount,getAppInstallCount,getClusterDevicesStatus} from '@/public/api/overview.js';
import {getClusterConnectionsRealTime} from '@/public/api/statistics.js'
import {getAppsConnectionsRealTime} from '@/public/api/statistics.js'
export default {
    data(){
        return {
            statusList:[],
            dataList:[],
            dataList1:[],
            sum:'',
            sum1:'',
            sum2:''
        }
    },
    components: {

    },
    methods:{
        initEcharts(){
            let error = [];
            let idle = [];
            let installing = [];
            let rebooting = [];
            let used = [];
            getDeviceStatusCount().then((res) =>{
                if(res&&res.code == '200'){
                    let arr = Object.entries(res.data);
                    arr.forEach(item =>{
                        switch (item[1].status) {
                        case 'IDLE':
                            let obj = {
                                name:'空闲',
                                value:item[1].count,
                                percent:Number(item[1].percentage*100).toFixed(2) + "%",
                                bgColor:"#f39c12",
                            }
                            this.statusList.push(obj)
                            break;
                        case 'USED':
                            let obj1 = {
                                name:'使用',
                                value:item[1].count,
                                percent:Number(item[1].percentage*100).toFixed(2) + "%",
                                bgColor:"#42bc9b",
                            }
                            this.statusList.push(obj1)
                            break;
                        case 'ERROR':
                            let obj2 = {
                                name:'错误',
                                value:item[1].count,
                                percent:Number(item[1].percentage*100).toFixed(2) + "%",
                                bgColor:"#e74c3c",
                            }
                            this.statusList.push(obj2)
                            break;
                        case 'INSTALLING':
                            let obj3 = {
                                name:'安装中',
                                value:item[1].count,
                                percent:Number(item[1].percentage*100).toFixed(2) + "%",
                                bgColor:"#b936ff",
                            }
                            this.statusList.push(obj3)
                            break;
                        case 'REBOOTING':
                            let obj4 = {
                                name:'重启中',
                                value:item[1].count,
                                percent:Number(item[1].percentage*100).toFixed(2) + "%",
                                bgColor:"#4c5bf8",
                            }
                            this.statusList.push(obj4)
                            break;
                        case 'TOTAL':
                            this.sum = item[1].count
                            break;
                        default:
                            break;
                        }
                    })
                    console.log(this.statusList)
                    setTimeout(() => {
                        statisticsOrderBar = echarts.init(document.getElementById('status-data'))
                        statisticsOrderBar.setOption(option)
                        statisticsOrderBar.setOption({series:[
                            {
                                data:this.statusList
                            }
                        ],graphic:[
                            {
                                style:{
                                    text:this.sum<10?' '+this.sum:this.sum
                                }
                            }
                        ]})
                    }, 0);
                }
            })
            getClusterConnectionsRealTime().then((res)=>{
                if(res&&res.code == '200'){
                    // this.dataList = res.data
                    let arr = res.data;
                    let sum1 = 0;
                    arr.forEach((item) => {
                        sum1 += Number(item.count)
                        let obj = {
                            name:item.clusterName,
                            value:item.count
                        }
                        this.dataList.push(obj)
                    });
                    this.sum1 = sum1
                    console.log(this.sum1)
                    console.log(this.dataList)
                    setTimeout(() => {
                        statisticsOrderBar1 = echarts.init(document.getElementById('status-data1'))
                        statisticsOrderBar1.setOption(option1)
                        statisticsOrderBar1.setOption({series:[
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
            getAppsConnectionsRealTime().then((res) =>{
                if(res&&res.data){
                    let arr = res.data;
                    let sum2 =0;
                    arr.forEach((item) => {
                        sum2 += Number(item.count)
                        let obj = {
                            name:item.appName,
                            value:item.count
                        }
                        this.dataList1.push(obj)
                    });
                    this.sum2 = sum2
                    setTimeout(() => {
                        statisticsOrderBar2 = echarts.init(document.getElementById('status-data2'))
                        statisticsOrderBar2.setOption(option2)
                        statisticsOrderBar2.setOption({series:[
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
            getAppInstallCount().then((res)=>{
                if(res&&res.code == '200'){
                    let arr = res.data;
                    let dataList = []
                    let dataList1 = []
                    arr.forEach((item) => {
                        let obj = item.appName
                        dataList.push(obj)
                        let obj1 = item.count
                        dataList1.push(obj1)
                    });
                    setTimeout(() => {
                        statisticsOrderBar3 = echarts.init(document.getElementById('status-data3'))
                        statisticsOrderBar3.setOption(option3)
                        statisticsOrderBar3.setOption({series:[
                            {
                                data:dataList1
                            }
                        ],xAxis:[
                            {
                                data:dataList
                            }
                        ]})
                    }, 0);
                }
            })
            getClusterDevicesStatus().then((res)=>{
                if(res&&res.code == '200'){
                    let arr = res.data;
                    let nameList = []
                    arr.forEach((item) => {
                        nameList.push(item.clusterName)
                        error.push(Number(item.error))
                        idle.push(Number(item.idle))
                        installing.push(Number(item.installing))
                        rebooting.push(Number(item.rebooting))
                        used.push(Number(item.used))
                    });
                    setTimeout(() => {
                        statisticsOrderBar4 = echarts.init(document.getElementById('status-data4'))
                        statisticsOrderBar4.setOption(option4)
                        statisticsOrderBar4.setOption({xAxis:[
                            {
                                data:nameList
                            }
                        ]})
                    }, 0);
                }
            })
            // 饼状图
            let statisticsOrderBar ="";
            let statisticsOrderBar1 ="";
            let statisticsOrderBar2 ="";
            let statisticsOrderBar3 ="";
            let statisticsOrderBar4 ="";

            let option = {
                color:['#f39c12','#42bc9b','#e74c3c','#b936ff','#4c5bf8'],
                title : {
                    text: '设备状态统计',
                    left:20,
                    top:20,
                    textStyle:{
                        fontSize:16,
                        color:"#666666"
                    }
                },
                graphic: [
                    {　　　　　　　　　　　　　　　
                        type: 'text',　　　　　
                        left: '28%',　　　　　　　　　　
                        top: '42%',
                        style: {　　　　　　　　　　　　　　　　
                            text: " ",
                            textAlign: 'center',
                            left:"center",
                            top:"center",
                            fill: '#666666',　　
                            font:'bolder 28px "Microsoft YaHei", sans-serif',
                        }
                    },
                    {　　　　　　　　　　　　　　
                        type: 'text',　　　　　　　　
                        left: '28%',　　　　　　　　　　
                        top: '54%',
                        style: {　　　　　　　　　　　　　　　　
                            text: '总数量',
                            textAlign: 'center',
                            left:"center",
                            top:"center",
                            fill: '#666666',　　
                            font:'12px "Microsoft YaHei", sans-serif',
                        }
                    },
                ],
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                // legend: {
                //     orient: 'vertical',
                //     right:20,
                //     top:20,
                //     itemWidth:14,
                //     itemHeight:8,
                //     data: ['集群1', '集群2', '集群3', '集群4', '集群5'],
                // },
                series: [
                    {
                        name:'云桌面状态统计',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        center: ['30%', '50%'],
                        data: [
                            {value: 335, name: '集群1'},
                            {value: 310, name: '集群2'},
                            {value: 234, name: '集群3'},
                            {value: 135, name: '集群4'},
                            {value: 1548, name: '集群5'},
                        ],
                        label: {
                            normal: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                show: false,
                                textStyle: {
                                    fontSize: '30',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        // labelLine: {
                        //     normal: {
                        //         show: true,
                        //         length: 20,
                        //         length2: 30,
                        //         lineStyle: {
                        //             color: '#d3d3d3'
                        //         },
                        //         align: 'right'
                        //     },
                        //     color: "#000",
                        //     emphasis: {
                        //         show: true
                        //     }
                        // },
                        // label: {
                        //     formatter: '{b|{b}：}{d|{d}%}  ',
                        //     rich: {
                        //         b: {
                        //             fontSize: 16,
                        //             lineHeight: 33
                        //         },
                        //         c: {
                        //             fontSize: 16,
                        //             lineHeight: 33
                        //         },
                        //         d: {
                        //             fontSize: 16,
                        //             lineHeight: 33
                        //         },
                        //     }
                        // },
                    }
                ]
            }
            let option1 = {
                // color:['#42bc9b','#e74c3c','#f39c12','#b936ff','#4c5bf8'],
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    right:40,
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
                        avoidLabelOverlap: false,
                        center: ['30%', '50%'],
                        data: [
                            {value: 335, name: '集群1'},
                            {value: 310, name: '集群2'},
                            {value: 234, name: '集群3'},
                            {value: 135, name: '集群4'},
                            {value: 1548, name: '集群5'},
                            {value: 500, name: '集群6'},
                            {value: 500, name: '集群7'}
                        ],
                        // label: {
                        //     normal: {
                        //         show: false,
                        //         position: 'center'
                        //     },
                        //     emphasis: {
                        //         show: false,
                        //         textStyle: {
                        //             fontSize: '30',
                        //             fontWeight: 'bold'
                        //         }
                        //     }
                        // },
                        // labelLine: {
                        //     normal: {
                        //         show: false
                        //     }
                        // },
                    }
                ]
            }
            let option2 = {
                // color:['#42bc9b','#e74c3c','#f39c12','#b936ff','#4c5bf8'],
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    orient: 'vertical',
                    right:40,
                    top:20,
                    itemWidth:24,
                    itemHeight:10,
                    data: ['应用1', '应用2', '应用3', '应用4', '应用5', '应用6', '应用7']
                },
                series: [
                    {
                        name:'应用连接数',
                        type:'pie',
                        radius: '70%',
                        avoidLabelOverlap: false,
                        center: ['30%', '50%'],
                        data: [
                            {value: 335, name: '应用1'},
                            {value: 310, name: '应用2'},
                            {value: 234, name: '应用3'},
                            {value: 135, name: '应用4'},
                            {value: 1548, name: '应用5'},
                            {value: 500, name: '应用6'},
                            {value: 500, name: '应用7'}
                        ],
                    }
                ]
            }
            let option3 = {
                tooltip: {
                    trigger: 'axis',
                    formatter: "{b} <br/>应用安装数: {c}"
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
                },
                grid:{
                    x:60,
                    y:40,
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
            let option4 = {
                color:['#f39c12','#42bc9b','#e74c3c','#b936ff','#4c5bf8'],
                title : {
                    text: '集群状态统计',
                    left:20,
                    top:20,
                    textStyle:{
                        fontSize:16,
                        color:"#666666"
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
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
                },
                grid:{
                    x:60,
                    y:60,
                    x2:60,
                    y2:40,
                },
                series: [
                    {
                        name: '空闲',
                        type: 'bar',
                        stack: '总量',
                        barWidth : 30,
                        data: idle
                    },
                    {
                        name: '使用',
                        type: 'bar',
                        stack: '总量',
                        barWidth : 30,
                        data: used
                    },
                    {
                        name: '错误',
                        type: 'bar',
                        stack: '总量',
                        barWidth : 30,
                        data: error
                    },
                    {
                        name: '安装中',
                        type: 'bar',
                        stack: '总量',
                        barWidth : 30,
                        data: installing
                    },
                    {
                        name: '重启中',
                        type: 'bar',
                        stack: '总量',
                        barWidth : 30,
                        data: rebooting
                    }
                ]
            }
            /*窗口自适应，关键代码*/
            window.onresize = function () {
                statisticsOrderBar.resize();
                statisticsOrderBar1.resize();
                statisticsOrderBar2.resize();
                statisticsOrderBar3.resize();
                statisticsOrderBar4.resize();
            }
        }
    },
    computed:{
        
    },
    watch:{
        
    },
    mounted(){
        this.initEcharts()
    }
}
</script>
