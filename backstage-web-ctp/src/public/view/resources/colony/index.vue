<template>
    <div>
        <!-- <div class="path-title">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item>资源管理</el-breadcrumb-item>
                <el-breadcrumb-item>设备组管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div> -->
        
        <div class="page-inner">
            <!-- <div class="echarts-module">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <div class="module-wrap">
                            <div class="chart-wrap">
                                <div id="status-data" style="width:100%;height:280px"></div>
                                <h4 style="top: 55px;left: 26px;">设备总数：{{sum}}</h4>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div> -->
            <div class="table-action">
                <button type="button" class="btn btn-primary" @click="createItem">新建</button>
                <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑</button>
                <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除</button>
                <button type="button" class="btn btn-info" @click="importItem" :disabled="!canImport">添加设备</button>
                <button type="button" class="btn btn-info" @click="exportItem" :disabled="!canExport">移出设备</button>
                <!-- <button type="button" class="btn btn-info" @click="uninstallItem" :disabled="!bth_check.canUninstall">批量卸载</button> -->
                <div class="pull-right">
                    <div class="search-box">
                        <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                            <button class="btn" type="submit" :disabled="false">
                                <i class="icon-aw-search"></i>
                            </button>
                            <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入组名称" name="searchTerm" v-model="globalSearchTerm"/>
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
                    <el-table-column prop="name" label="组名称" sortable>
                        <template slot-scope="scope">
                            <router-link v-bind:to="'/equipmentlist?groupId='+scope.row.id+'&name='+encodeURIComponent(scope.row.name)+'&status=2'">
                                <span :title="scope.row.name">{{scope.row.name}}</span>
                            </router-link>
                        </template>
                    </el-table-column>
                    <!-- <el-table-column prop="" label="所属集群" sortable>
                        <template slot-scope="">
                            <span :title='decodeURIComponent($route.query.name)'>{{decodeURIComponent($route.query.name)}}</span>
                        </template> 
                    </el-table-column> -->
                    <!-- <el-table-column prop="deviceNum" label="集群管理的设备数">
                        <template slot-scope="scope">
                            <span :title='scope.row.deviceNum'>{{scope.row.deviceNum}}</span>
                        </template> 
                    </el-table-column> -->
                    <!-- <el-table-column prop="status" label="集群状态">
                        <template slot-scope="scope">
                            <span :title='scope.row.status'>{{scope.row.status}}</span>
                        </template> 
                    </el-table-column> -->
                    <el-table-column prop="description" label="描述" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.description'>{{scope.row.description}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="createTime" label="创建时间" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.createTime'>{{scope.row.createTime}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="updateTime" label="修改时间" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.updateTime'>{{scope.row.updateTime}}</span>
                        </template> 
                    </el-table-column>
                </el-table>
                <div v-if="pageInstan.showPage">
                    <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
                </div>
            </div>

            <el-dialog
                :visible.sync="deleteDialogVisible"
                custom-class="small-dialog delete-dialog"
                center
                :close-on-click-modal="false">
                <span>您确定要删除选择的集群吗？</span>
                <div slot="footer">
                    <button class="btn btn-warning" type="button" :disabled="deleteItemDisabled" @click="confirm('del')">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="deleteDialogVisible = false">{{$t('cn.action.cancel')}}</button>
                </div>
            </el-dialog>
            <!-- 新建 -->
            <el-dialog
                :visible.sync="addDialogVisible"
                :title="addTitle"
                custom-class="normal-dialog"
                center
                v-if="addDialogVisible"
                :close-on-click-modal="false">
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>组名称：
                    </label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="groupForm.name" data-vv-as="组名称" name="name" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">描述：</label>
                    <div class="controls">
                        <textarea class="form-controls" name="description" v-model="groupForm.description"> </textarea>
                    </div>
                </div>
                <div slot="footer">
                    <button class="btn btn-primary" type="button" @click="commitModel()">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
                </div>
            </el-dialog>
            <!-- 导入/导出设备 -->
            <el-dialog
                :visible.sync="importDialogVisible"
                :title="importTitle"
                custom-class="large-dialog"
                center
                v-if="importDialogVisible"
                :close-on-click-modal="false">
                <div class="table-action">
                    <div class="control-group" style="float:left;margin-right:10px">
                        <label class="control-label">选择集群：</label>
                        <div class="controls">
                            <el-select
                                v-model="node"
                                filterable
                                clearable
                                name="node"
                            >
                                <el-option
                                    v-for="item in nodeList"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id"
                                >
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                    <button type="button" class="btn btn-primary" @click="searchItem" style="height:30px">搜索</button>
                </div>
                <div class="table-content batch">
                    <el-table ref="multipleTable" 
                        :data="dataList1" 
                        @selection-change="handleSelectionChange1" 
                        height="545">
                        <el-table-column type="selection" width="40"> </el-table-column>
                        <el-table-column prop="id" label="设备ID" width="180px">
                            
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
                        <el-table-column prop="innerIp" label="设备内部IP">
                            <template slot-scope="scope">
                                <span :title='scope.row.innerIp'>{{scope.row.innerIp}}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div v-if="pageInstan1.showPage">
                        <page-query v-if="pageInstan1.showPage" :currentPage="pageInstan1.currentPage" :total="pageInstan1.Total" :pageSize="pageInstan1.pageSize" @handleSizeChangeSub="handleSizeChange1" @handleCurrentChangeSub="handleCurrentChange1"></page-query>
                    </div>
                </div>
                <div slot="footer">
                    <button class="btn btn-primary" type="button" :disabled='!import1' @click="commitModel1()">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="importDialogVisible = false">{{$t('cn.action.cancel')}}</button>
                </div>
            </el-dialog>
        </div>
    </div>

</template>

<script>
import {getDeviceGroupList,createDeviceGroup,updateDeviceGroup,deleteDeviceGroup,getAddDevices,addDevices,removeDevices} from '@/public/api/colony.js';
import {getGroupDevices} from '@/public/api/equipment.js';
// import {getDeviceStatusCount} from '@/public/api/statistics.js'
import {getClusters} from '@/public/api/node.js';
export default {
    data(){
        return {
            canEdit:false,
            canDel:false,
            canImport:false,
            canExport:false,
            hasRouteQuery:0,
            multipleSelection: [],
            editData:{
                opentype:'',
                optionWrap:false,
                content:this.multipleSelection
            },
            dataList:[],
            dataList1:[],
            deleteDialogVisible:false,
            // page
            globalSearchTerm:'',
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            pageInstan1:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            // bth_check:{
            //     canEdit:false,
            //     canDel:false,
            // },
            deleteItemDisabled:false,
            addTitle:"新建设备组",
            addDialogVisible:false,
            type:'add',
            statusList:[],
            sum:'',
            importDialogVisible:false,
            import1:false,
            nodeList:[],
            node:'',
            importTitle:''
        }
    },
    components: {

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
        //切换页码组，每页展示多少条
        handleSizeChange1(val) {
            this.pageInstan1.pageSize = val;
            this.searchItem();
        },
        //切换页码
        handleCurrentChange1(val) {
            this.pageInstan1.currentPage = val;
            this.searchItem('','pageOne');
        },
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            // let id = this.$route.query.clusterId
            getDeviceGroupList(oData).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.dataList = res.data.records;
                    localStorage.resource = 'colony';
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
                item.status = item.status == 0?'启用':'禁用';
                item.createTime = item.createTime?moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.updateTime = item.updateTime?moment(item.updateTime).format("YYYY-MM-DD HH:mm:ss"):'';
            })
        },
        // initEcharts(){
        //     let oData1 = {
        //         'clusterId':this.$route.query.clusterId
        //     }
        //     getDeviceStatusCount(oData1).then((res)=>{
        //         if(res&&res.code == '200'){
        //             let arr = Object.entries(res.data); 
        //             arr.forEach(item =>{
        //                 switch (item[1].status) {
        //                 case 'IDLE':
        //                     let obj = {
        //                         name:'空闲',
        //                         value:item[1].count,
        //                     }
        //                     this.statusList.push(obj)
        //                     break;
        //                 case 'USED':
        //                     let obj1 = {
        //                         name:'使用',
        //                         value:item[1].count,
        //                     }
        //                     this.statusList.push(obj1)
        //                     break;
        //                 case 'ERROR':
        //                     let obj2 = {
        //                         name:'错误',
        //                         value:item[1].count,
        //                     }
        //                     this.statusList.push(obj2)
        //                     break;
        //                 case 'INSTALLING':
        //                     let obj3 = {
        //                         name:'安装中',
        //                         value:item[1].count,
        //                     }
        //                     this.statusList.push(obj3)
        //                     break;
        //                 case 'REBOOTING':
        //                     let obj4 = {
        //                         name:'重启中',
        //                         value:item[1].count,
        //                     }
        //                     this.statusList.push(obj4)
        //                     break;
        //                 case 'TOTAL':
        //                     this.sum = item[1].count
        //                     break;
        //                 default:
        //                     break;
        //                 }
        //             })
        //             console.log(this.sum)
        //             setTimeout(() => {
        //                 statisticsOrderBar = echarts.init(document.getElementById('status-data'))
        //                 statisticsOrderBar.setOption(option)
        //                 statisticsOrderBar.setOption({series:[
        //                     {
        //                         data:this.statusList
        //                     }
        //                 ],legend:[
        //                     {
        //                         data:this.statusList
        //                     }
        //                 ]})
        //             }, 0);
        //         }
        //     })
        //     // 饼状图
        //     let statisticsOrderBar ="";

        //     let option = {
        //         color:['#42bc9b','#f39c12','#e74c3c','#b936ff','#4c5bf8'],
        //         title : {
        //             text: '设备状态统计',
        //             left:20,
        //             top:20,
        //             textStyle:{
        //                 fontSize:16,
        //                 color:"#666666"
        //             }
        //         },
        //         tooltip: {
        //             trigger: 'item',
        //             formatter: "{a} <br/>{b}: {c} ({d}%)"
        //         },
        //         legend: {
        //             orient: 'vertical',
        //             right:20,
        //             top:20,
        //             itemWidth:24,
        //             itemHeight:10,
        //             data: ['集群1', '集群2', '集群3', '集群4', '集群5', '集群6', '集群7']
        //         },
        //         series: [
        //             {
        //                 name:'设备状态',
        //                 type:'pie',
        //                 radius: '70%',
        //                 center: ['45%', '55%'],
        //                 data: [
        //                     {value: 335, name: '集群1'},
        //                     {value: 310, name: '集群2'},
        //                     {value: 234, name: '集群3'},
        //                     {value: 135, name: '集群4'},
        //                     {value: 1548, name: '集群5'},
        //                     {value: 500, name: '集群6'},
        //                     {value: 500, name: '集群7'}
        //                 ],
        //             }
        //         ]
        //     }
        //     /*窗口自适应，关键代码*/
        //     window.onresize = function () {
        //         statisticsOrderBar.resize();
        //     }
        // },
        
        createItem(){
            this.type = "add";
            this.addDialogVisible = true,
            this.addTitle = "新建设备组",
            this.groupForm = {
                "name":"",
                "description":"",
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑设备组",
            this.groupForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        commitModel(){
            let data = {
                "name":this.groupForm.name,
                // "clusterId":this.$route.query.clusterId,
                "description":this.groupForm.description,
            }
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        createDeviceGroup(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.groupForm.id;
                        updateDeviceGroup(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }
                    
                    
                }
            })
        },
        confirm(type){
            switch (type){
            case'del':
                let ids = [];
                this.multipleSelection.map(item =>{
                    ids.push(item.id)
                })
                let data = ids
                deleteDeviceGroup(data).then((res) =>{
                    console.log(res)
                    this.deleteDialogVisible = false;
                    this.initData()
                })
                break;
            }
        },
        importItem(){
            this.type = 'import'
            this.importTitle = '添加设备'
            this.importDialogVisible = true;
            this.pageInstan1.showPage = false;
            this.dataList1 = [];
            this.node = '';
            this.import1 = false
            getClusters().then((res) =>{
                this.nodeList = res.data.records
            })
        },
        exportItem(){
            this.type = 'export'
            this.importTitle = '移出设备'
            this.importDialogVisible = true;
            this.pageInstan1.showPage = false;
            this.dataList1 = [];
            this.node = '';
            this.import1 = false
            getClusters().then((res) =>{
                this.nodeList = res.data.records
            })
        },
        searchItem(){
            let oData = {
                'pageNum': this.pageInstan1.currentPage,
                'pageSize': this.pageInstan1.pageSize
            }
            if(this.node){
                oData.clusterId = this.node
            }
            oData.groupId = this.multipleSelection.map(item=>{return item.id})
            if(this.type == 'import'){
                getAddDevices(oData).then((res) =>{
                    console.log(res);
                    if(res&&res.data){
                        this.dataList1 = res.data.records
                        if(this.dataList1.length > 0){
                            this.pageInstan1.Total = res.data.total;
                            this.pageInstan1.showPage = true;//判断数据是否为空 为空则不显示分页
                        }else{
                            this.pageInstan1.Total = res.data.total;
                            this.pageInstan1.showPage = false;//判断数据是否为空 为空则不显示分页
                        }
                    }
                })
            }else if(this.type == 'export'){
                getGroupDevices(oData).then((res) =>{
                    console.log(res);
                    if(res&&res.data){
                        this.dataList1 = res.data.records
                        if(this.dataList1.length > 0){
                            this.pageInstan1.Total = res.data.total;
                            this.pageInstan1.showPage = true;//判断数据是否为空 为空则不显示分页
                        }else{
                            this.pageInstan1.Total = res.data.total;
                            this.pageInstan1.showPage = false;//判断数据是否为空 为空则不显示分页
                        }
                    }
                })
            }
            
        },
        commitModel1(){
            let ids = [];
            this.multipleSelection1.map(item =>{
                ids.push(item.id)
            })
            let data = {
                ids:ids,
                groupId:JSON.parse(this.multipleSelection.map(item=>{return item.id}))
            }
            console.log(data)
            // return
            // var id = this.multipleSelection.map(item=>{return item.id})
            if(this.type == 'import'){
                addDevices(data).then((res) =>{
                    console.log(res);
                    if(res&&res.code == 200){
                        this.import1 = false
                        this.importDialogVisible = false;
                        this.initData()
                    }
                })
            }else if(this.type == 'export'){
                removeDevices(data).then((res) =>{
                    console.log(res)
                    if(res&&res.code == 200){
                        this.import1 = false
                        this.importDialogVisible = false
                        this.initData()
                    }
                })
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
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            if(val.length == 0){
                this.canEdit = false,
                this.canDel = false,
                this.canImport = false,
                this.canExport = false
            }else if(val.length == 1){
                this.canEdit = true,
                this.canDel = true,
                this.canImport = true,
                this.canExport = true
            }else if(val.length > 1){
                this.canEdit = false,
                this.canDel = true,
                this.canImport = false,
                this.canExport = false
            }
        },
        handleSelectionChange1(val){
            this.multipleSelection1 = val;
            if(val.length == 0){
                this.import1 = false
            }else if(val.length == 1){
                this.import1 = true
            }else if(val.length > 1){
                this.import1 = true
            }
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
        // this.initEcharts();
        this.hasRouteQuery = this.$route.query.id?1:0;
    }
}
</script>
