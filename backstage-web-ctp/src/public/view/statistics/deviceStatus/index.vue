<template>
    <div class="page-inner">
        <div class="echarts-module">
            <el-row :gutter="20">
                <el-col :span="8">
                    <div class="module-wrap">
                        <div class="chart-wrap">
                            <div id="status-data" style="width:100%;height:250px"></div>
                        </div>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="module-wrap">
                        <!-- <div class="chart-wrap">
                            <div id="status-data1" style="width:100%;height:250px"></div>
                        </div> -->
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="module-wrap">
                        
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>

export default {
    data(){
        return {
            
        }
    },
    components: {

    },
    methods:{
        initEcharts(){
            // 饼状图
            let statisticsOrderBar ="";

            let option = {
                // color:['#42bc9b','#e74c3c','#f39c12','#b936ff','#4c5bf8'],
                title : {
                    text: '云桌面状态统计',
                    left:20,
                    top:20,
                    textStyle:{
                        fontSize:16,
                        color:"#666666"
                    }
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    right:20,
                    top:20,
                    itemWidth:14,
                    itemHeight:8,
                    data: ['集群1', '集群2', '集群3', '集群4', '集群5', '集群6', '集群7']
                },
                series: [
                    {
                        name:'云桌面状态统计',
                        type:'pie',
                        radius: '55%',
                        center: ['45%', '60%'],
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
                        //     formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
                        //     backgroundColor: '#eee',
                        //     borderColor: '#aaa',
                        //     borderWidth: 1,
                        //     borderRadius: 4,
                        //     rich: {
                        //         a: {
                        //             color: '#999',
                        //             lineHeight: 22,
                        //             align: 'center'
                        //         },
                        //         hr: {
                        //             borderColor: '#aaa',
                        //             width: '100%',
                        //             borderWidth: 0.5,
                        //             height: 0
                        //         },
                        //         b: {
                        //             // fontSize: 10,
                        //             lineHeight: 33
                        //         },
                        //         per: {
                        //             color: '#eee',
                        //             backgroundColor: '#334455',
                        //             padding: [2, 4],
                        //             borderRadius: 2
                        //         }
                        //     }
                        // },
                    }
                ]
            }

            setTimeout(() => {
                statisticsOrderBar = echarts.init(document.getElementById('status-data'))
                statisticsOrderBar.setOption(option)
            }, 0);

            /*窗口自适应，关键代码*/
            window.onresize = function () {
                statisticsOrderBar.resize();
                // statisticsOrderBar3.resize();
                // statisticsOrderBar4.resize();
                // statisticsOrderBar5.resize();
                // statisticsOrderBar6.resize();
            }

            //折线图
            let statisticsOrderBar1 = echarts.init(document.getElementById('status-data1'))
            let option1 = {
                title: {
                    text: '堆叠区域图'
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        label: {
                            backgroundColor: '#6a7985'
                        }
                    }
                },
                legend: {
                    data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '邮件营销',
                        type: 'line',
                        stack: '总量',
                        areaStyle: {},
                        data: [120, 132, 101, 134, 90, 230, 210]
                    },
                    {
                        name: '联盟广告',
                        type: 'line',
                        stack: '总量',
                        areaStyle: {},
                        data: [220, 182, 191, 234, 290, 330, 310]
                    },
                    {
                        name: '视频广告',
                        type: 'line',
                        stack: '总量',
                        areaStyle: {},
                        data: [150, 232, 201, 154, 190, 330, 410]
                    },
                    {
                        name: '直接访问',
                        type: 'line',
                        stack: '总量',
                        areaStyle: {},
                        data: [320, 332, 301, 334, 390, 330, 320]
                    },
                    {
                        name: '搜索引擎',
                        type: 'line',
                        stack: '总量',
                        label: {
                            normal: {
                                show: true,
                                position: 'top'
                            }
                        },
                        areaStyle: {},
                        data: [820, 932, 901, 934, 1290, 1330, 1320]
                    }
                ]
            };
            statisticsOrderBar1.setOption(option1)
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
