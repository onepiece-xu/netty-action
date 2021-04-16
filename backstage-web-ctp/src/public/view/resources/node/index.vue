<template>
    <div>
        <!-- <div class="path-title">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item>资源管理</el-breadcrumb-item>
                <el-breadcrumb-item>集群管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div> -->
        <div class="page-inner">
            <div class="table-action">
                <button type="button" class="btn btn-primary" @click="createItem">新建</button>
                <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑</button>
                <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除</button>
                <!-- <button type="button" class="btn btn-info" @click="installItem" :disabled="!canInstall">绑定</button> -->
                <div class="pull-right">
                    <div class="search-box">
                        <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                            <button class="btn" type="submit" :disabled="false">
                                <i class="icon-aw-search"></i>
                            </button>
                            <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入集群名称/IP" name="searchTerm" v-model="globalSearchTerm"/>
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
                    @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="name" label="集群名称" sortable>
                        <template slot-scope="scope">
                            <router-link v-bind:to="'/equipmentlist1?clusterId='+scope.row.id+'&name='+encodeURIComponent(scope.row.name)+'&status=1'">
                                <span :title="scope.row.name">{{scope.row.name}}</span>
                            </router-link>
                        </template>
                    </el-table-column>
                    <el-table-column prop="accessIp" label="集群IP地址" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.accessIp'>{{scope.row.accessIp}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="apiUrl" label="API_URL" width="300" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.apiUrl'>{{scope.row.apiUrl}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="" label="集群状态" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.colonyschStatus'>{{scope.row.colonyschStatus}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="deviceNum" label="集群管理的设备数" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.deviceNum'>{{scope.row.deviceNum}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="detail" label="详情" >
                        <template slot-scope="scope">
                            <router-link v-bind:to="'/node?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.name)">
                                <span>查看</span>
                            </router-link>
                        </template>
                    </el-table-column>
                </el-table>
                <div v-if="pageInstan.showPage">
                    <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
                </div>
            </div>
            <!-- 删除 -->
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
                        <span class="red">*</span>集群名称：
                    </label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="colonyForm.name" data-vv-as="所属设备组" name="name" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>集群IP地址：
                    </label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="colonyForm.accessIp" data-vv-as="集群IP地址" name="accessIp" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('accessIp')" class="error">{{ errors.first('accessIp') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">所属用户：</label>
                    <div class="controls">
                        <el-select
                            v-model="colonyForm.userIds"
                            clearable
                            multiple
                            name="userIds"
                        >
                            <el-option
                                v-for="item in userList"
                                :key="item.id"
                                :label="item.enterpriseName"
                                :value="item.identification"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>API_URL：
                    </label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="colonyForm.apiUrl" data-vv-as="API_URL" name="apiUrl" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('apiUrl')" class="error">{{ errors.first('apiUrl') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>节点类型：
                    </label>
                    <div class="controls">
                        <el-radio-group v-model="colonyForm.type">
                            <el-radio :label="0">中心节点</el-radio>
                            <el-radio :label="1">边缘节点</el-radio>
                        </el-radio-group>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>是否本地节点：
                    </label>
                    <div class="controls">
                        <el-radio-group v-model="colonyForm.isLocal">
                            <el-radio :label="0">是</el-radio>
                            <el-radio :label="1">否</el-radio>
                        </el-radio-group>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">描述：</label>
                    <div class="controls">
                        <textarea class="form-controls" name="description" v-model="colonyForm.description"> </textarea>
                    </div>
                </div>
                <div slot="footer">
                    <button class="btn btn-primary" type="button" @click="commitModel()">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
                </div>
            </el-dialog>
            <!-- 批量安装 -->
            <el-dialog
                :visible.sync="installDialogVisible"
                title="绑定WebRTC配置"
                custom-class="big-dialog"
                center
                v-if="installDialogVisible"
                :close-on-click-modal="false">
                <div class="table-content batch">
                    <el-table ref="multipleTable" 
                        :data="dataList1" 
                        @selection-change="handleSelectionChange1" >
                        <el-table-column type="selection" width="40"> </el-table-column>
                        <el-table-column prop="signalServer" label="Http信令服务器地址" sortable>
                            <template slot-scope="scope">
                                <span :title="scope.row.signalServer">{{scope.row.signalServer}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="signalServerHttps" label="Https信令服务器地址" sortable>
                            <template slot-scope="scope">
                                <span :title='scope.row.signalServerHttps'>{{scope.row.signalServerHttps}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="turnServer" label="turn服务器地址" sortable>
                            <template slot-scope="scope">
                                <span :title='scope.row.turnServer'>{{scope.row.turnServer}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="turnUser" label="turn服务器登录名称" sortable>
                            <template slot-scope="scope">
                                <span :title='scope.row.turnUser'>{{scope.row.turnUser}}</span>
                            </template> 
                        </el-table-column>
                    </el-table>
                    <div v-if="pageInstan1.showPage">
                        <page-query v-if="pageInstan1.showPage" :currentPage="pageInstan1.currentPage" :total="pageInstan1.Total" :pageSize="pageInstan1.pageSize" @handleSizeChangeSub="handleSizeChange1" @handleCurrentChangeSub="handleCurrentChange1"></page-query>
                    </div>
                </div>
                <div slot="footer">
                    <button class="btn btn-primary" type="button" :disabled='!install' @click="confirm('install')">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="installDialogVisible = false">{{$t('cn.action.cancel')}}</button>
                </div>
            </el-dialog>
            <detail-page :props="hasRouteQuery">
                <node-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></node-detail>
            </detail-page>
        </div>
    </div>
    
</template>

<script>
import {getClusters,createCluster,updateCluster,deleteClusters,bindConfig,serviceUserList} from '@/public/api/node.js';
import {getConfigList} from '@/public/api/WebRTCConfig.js'
import detailPage from '@/public/common-tmpl/detail.vue' 
import nodeDetail from './nodeDetail.vue'

export default {
    data(){
        return {
            canEdit:false,
            canDel:false,
            canInstall:false,
            hasRouteQuery:0,
            multipleSelection: [],
            multipleSelection1:[],
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
            deleteItemDisabled:false,
            addTitle:"新建集群",
            addDialogVisible:false,
            type:'add',
            colonyForm:{},
            installDialogVisible:false,
            install:false,
            userList:[]
        }
    },
    components: {
        detailPage,nodeDetail
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
            this.installItem();
        },
        //切换页码
        handleCurrentChange1(val) {
            this.pageInstan1.currentPage = val;
            this.installItem('','pageOne');
        },
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            getClusters(oData).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.dataList = res.data.records;
                    localStorage.resource = 'node';
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
            serviceUserList().then((res) =>{
                if(res&&res.data){
                    this.userList = res.data
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
                // item.nodeType = item.type == 0?'中心集群':'边缘集群';
                item.colonyschStatus = item.schStatus?'启用':'禁用';
            })
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            if(val.length == 0){
                this.canEdit = false,
                this.canDel = false,
                this.canInstall = false
            }else if(val.length == 1){
                this.canEdit = true,
                this.canDel = true,
                this.canInstall = true
            }else if(val.length > 1){
                this.canEdit = false,
                this.canDel = true,
                this.canInstall = false
            }
        },
        handleSelectionChange1(val) {
            this.multipleSelection1 = val;
            if(val.length == 0){
                this.install = false
            }else if(val.length == 1){
                this.install = true
            }else if(val.length > 1){
                this.install = false
            }
        },
        createItem(){
            this.type = "add";
            this.addDialogVisible = true,
            this.addTitle = "新建集群",
            this.colonyForm = {
                "name":"",
                "accessIp":"",
                "userIds":"",
                "apiUrl":"",
                "description":"",
                "type":0,
                "isLocal":0
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑集群",
            this.colonyForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            console.log(this.colonyForm)
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        installItem(){
            this.installDialogVisible = true
            this.install = false
            let oData = {
                'pageNum': this.pageInstan1.currentPage,
                'pageSize': this.pageInstan1.pageSize
            }
            getConfigList(oData).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.dataList1 = res.data.records;
                    if(this.dataList.length > 0){
                        this.pageInstan1.Total = res.data.total;
                        this.pageInstan1.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan1.Total = res.data.total;
                        this.pageInstan1.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                }
            })
        },
        commitModel(){
            let data = {
                "name":this.colonyForm.name,
                "accessIp":this.colonyForm.accessIp,
                "userIds":this.colonyForm.userIds,
                "apiUrl":this.colonyForm.apiUrl,
                "description":this.colonyForm.description,
                "type":this.colonyForm.type,
                "isLocal":this.colonyForm.isLocal
            }
            console.log(data)
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        createCluster(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.colonyForm.id;
                        updateCluster(data).then((res) =>{
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
                deleteClusters(data).then((res) =>{
                    console.log(res)
                    this.deleteDialogVisible = false;
                    this.initData()
                })
                break;
            case'install':
                let data1 = {
                    "clusterId":JSON.parse(this.multipleSelection.map(item=>{return item.id})),
                    "configId":JSON.parse(this.multipleSelection1.map(item=>{return item.id}))
                }
                bindConfig(data1).then((res) =>{
                    console.log(res)
                    this.installDialogVisible = false;
                    this.initData()
                })
                break;
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
