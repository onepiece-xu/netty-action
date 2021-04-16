<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新建</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除</button>
            <button type="button" class="btn btn-info" @click="bindItem" :disabled="!canBind">绑定</button>
            <button type="button" class="btn btn-info" @click="untyingItem" :disabled="!canUntying">解绑</button>
            <!-- <div class="pull-right">
                <div class="search-box">
                    <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                        <button class="btn" type="submit" :disabled="false">
                            <i class="icon-aw-search"></i>
                        </button>
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入规格名称" name="searchTerm" v-model="globalSearchTerm"/>
                    </form>
                </div>
                <el-tooltip class="item" effect="dark" :content="$t('cn.tabelTips.refreshList')" placement="top">
                    <button class="btn btn-renovat btn-search" @click="refresh()">
                        <i class="icon-aw-refresh"></i>
                    </button>
                </el-tooltip>
            </div> -->
        </div>

        <div class="table-content">
            <el-table ref="multipleTable" 
                :data="dataList" 
                @selection-change="handleSelectionChange" >
                <el-table-column type="selection" width="40"> </el-table-column>
                <el-table-column prop="serverName" label="信令服务名称" sortable>
                    <template slot-scope="scope">
                         <span :title="scope.row.serverName">{{scope.row.serverName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="domain" label="域名" sortable>
                    <template slot-scope="scope">
                         <span :title="scope.row.domain">{{scope.row.domain}}</span>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="httpUrl" label="Http地址" sortable>
                    <template slot-scope="scope">
                         <span :title="scope.row.httpUrl">{{scope.row.httpUrl}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="httpsUrl" label="Https地址" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.httpsUrl'>{{scope.row.httpsUrl}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="publicHttpPort" label="Http端口" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.publicHttpPort'>{{scope.row.publicHttpPort}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="publicHttpsPort" label="Https端口" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.publicHttpsPort'>{{scope.row.publicHttpsPort}}</span>
                    </template> 
                </el-table-column> -->
                <el-table-column prop="publicIp" label="公网IP" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.publicIp'>{{scope.row.publicIp}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="internalIp" label="内网IP" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.internalIp'>{{scope.row.internalIp}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="detail" label="详情" >
                    <template slot-scope="scope">
                        <router-link v-bind:to="'/signal?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.serverName)">
                            <span>查看</span>
                        </router-link>
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
                    <span class="red">*</span>服务器名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="signalForm.serverName" data-vv-as="服务器名称" name="serverName" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('serverName')" class="error">{{ errors.first('serverName') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>域名：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="signalForm.domain" data-vv-as="域名" name="domain" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('domain')" class="error">{{ errors.first('domain') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>Http地址：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="signalForm.httpUrl" data-vv-as="Http地址" name="httpUrl" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('httpUrl')" class="error">{{ errors.first('httpUrl') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>Https地址：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="signalForm.httpsUrl" data-vv-as="Https地址" name="httpsUrl" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('httpsUrl')" class="error">{{ errors.first('httpsUrl') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Http端口：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="signalForm.publicHttpPort" name="publicHttpPort">
                    <!-- <div class="error">
                        <span v-show="errors.has('publicHttpPort')" class="error">{{ errors.first('publicHttpPort') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Https端口：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="signalForm.publicHttpsPort" name="publicHttpsPort">
                    <!-- <div class="error">
                        <span v-show="errors.has('publicHttpsPort')" class="error">{{ errors.first('publicHttpsPort') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">公网IP：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="signalForm.publicIp" name="publicIp">
                    <!-- <div class="error">
                        <span v-show="errors.has('publicIp')" class="error">{{ errors.first('publicIp') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">内网IP：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="signalForm.internalIp" name="internalIp">
                    <!-- <div class="error">
                        <span v-show="errors.has('internalIp')" class="error">{{ errors.first('internalIp') }}</span>
                    </div> -->
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="commitModel()">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 绑定 -->
        <el-dialog
            :visible.sync="bindDialogVisible"
            title="绑定signal服务器"
            custom-class="normal-dialog"
            center
            class="bind"
            v-if="bindDialogVisible"
            :close-on-click-modal="false">
            <div class="table-content">
                <el-table ref="multipleTable" 
                    :data="dataList1"
                    height="295" 
                    @selection-change="handleSelectionChange1">
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="id" label="集群ID">
                        <template slot-scope="scope">
                            <span :title="scope.row.id">{{scope.row.id}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="集群名称">
                        <template slot-scope="scope">
                            <span :title='scope.row.name'>{{scope.row.name}}</span>
                        </template> 
                    </el-table-column>
                </el-table>
                <div v-if="pageInstan1.showPage">
                    <page-query v-if="pageInstan1.showPage" :currentPage="pageInstan1.currentPage" :total="pageInstan1.Total" :pageSize="pageInstan1.pageSize" @handleSizeChangeSub="handleSizeChange1" @handleCurrentChangeSub="handleCurrentChange1"></page-query>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="confirm('bind')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="bindDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 解绑 -->
        <el-dialog
            :visible.sync="untyingDialogVisible"
            title="解绑turn服务器"
            custom-class="normal-dialog"
            center
            class="untying"
            v-if="untyingDialogVisible"
            :close-on-click-modal="false">
            <div class="table-content">
                <el-table ref="multipleTable" 
                    :data="dataList2"
                    height="295" 
                    @selection-change="handleSelectionChange2">
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="id" label="集群ID">
                        <template slot-scope="scope">
                            <span :title="scope.row.id">{{scope.row.id}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="集群名称">
                        <template slot-scope="scope">
                            <span :title='scope.row.name'>{{scope.row.name}}</span>
                        </template> 
                    </el-table-column>
                </el-table>
                <div v-if="pageInstan2.showPage">
                    <page-query v-if="pageInstan2.showPage" :currentPage="pageInstan2.currentPage" :total="pageInstan2.Total" :pageSize="pageInstan2.pageSize" @handleSizeChangeSub="handleSizeChange2" @handleCurrentChangeSub="handleCurrentChange2"></page-query>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="confirm('untying')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="untyingDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <detail-page :props="hasRouteQuery">
            <signal-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></signal-detail>
        </detail-page>
    </div>

</template>

<script>
import {getSignalServerList,addSignalServer,updateSignalServer,deleteSignalServer,getbindList,getUnBindList,bindSignal} from '@/public/api/signal.js'
import detailPage from '@/public/common-tmpl/detail.vue' 
import signalDetail from './signalDetail.vue'
export default {
    data(){
        return {
            canEdit:false,
            canDel:false,
            canBind:false,
            canUntying:false,
            hasRouteQuery:0,
            multipleSelection: [],
            dataList:[],
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
            pageInstan2:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            deleteItemDisabled:false,
            addTitle:"新建信令服务器",
            signalForm:{},
            addDialogVisible:false,
            type:'add',
            bindDialogVisible:false,
            dataList1:[],
            untyingDialogVisible:false,
            dataList2:[],
            multipleSelection1: [],
            multipleSelection2: []
        }
    },
    components: {
        detailPage,signalDetail
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
            this.bind();
        },
        //切换页码
        handleCurrentChange1(val) {
            this.pageInstan1.currentPage = val;
            this.bind('','pageOne');
        },
        //切换页码组，每页展示多少条
        handleSizeChange2(val) {
            this.pageInstan2.pageSize = val;
            this.untying();
        },
        //切换页码
        handleCurrentChange2(val) {
            this.pageInstan2.currentPage = val;
            this.untying('','pageOne');
        },
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            getSignalServerList(oData).then((res) =>{
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
        createItem(){
            this.type = "add";
            this.addDialogVisible = true,
            this.addTitle = "新建信令服务器",
            this.signalForm = {
                "serverName":"",
                "domain":"",
                "httpUrl":"",
                "httpsUrl":"",
                "publicHttpPort":"",
                "publicHttpsPort":"",
                "publicIp":"",
                "internalIp":""
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑信令服务器",
            this.signalForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            console.log(this.signalForm)
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        //绑定
        bindItem(){
            this.pageInstan1 ={
                currentPage:1,
                pageSize:10
            }
            this.bindDialogVisible = true
            this.bind()
        },
        bind(){
            let data = {
                'pageNum': this.pageInstan1.currentPage,
                'pageSize': this.pageInstan1.pageSize,
                // 'id': this.multipleSelection.map(item=>{return item.id})
            }
            console.log(data)
            // return
            getUnBindList(data).then((res) =>{
                if(res&&res.data){
                    this.dataList1 = res.data.records;
                    if(this.dataList1.length > 0){
                        this.pageInstan1.Total = res.data.total;
                        this.pageInstan1.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan1.Total = res.data.total;
                        this.pageInstan1.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                }
            })
        },
        //解绑
        untyingItem(){
            this.pageInstan2 ={
                currentPage:1,
                pageSize:10
            }
            this.untyingDialogVisible = true
            this.untying()
        },
        untying(){
            let data = {
                'pageNum': this.pageInstan2.currentPage,
                'pageSize': this.pageInstan2.pageSize,
                'id': this.multipleSelection.map(item=>{return item.id})
            }
            console.log(data)
            // return
            getbindList(data).then((res) =>{
                if(res&&res.data){
                    this.dataList2 = res.data.records;
                    if(this.dataList2.length > 0){
                        this.pageInstan2.Total = res.data.total;
                        this.pageInstan2.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan2.Total = res.data.total;
                        this.pageInstan2.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                }
            })
        },
        commitModel(){
            let data = {
                "serverName":this.signalForm.serverName,
                "domain":this.signalForm.domain,
                "httpUrl":this.signalForm.httpUrl,
                "httpsUrl":this.signalForm.httpsUrl,
                "publicHttpPort":this.signalForm.publicHttpPort,
                "publicHttpsPort":this.signalForm.publicHttpsPort,
                "publicIp":this.signalForm.publicIp,
                "internalIp":this.signalForm.internalIp
            }
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        addSignalServer(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.signalForm.id;
                        updateSignalServer(data).then((res) =>{
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
                deleteSignalServer(data).then((res) =>{
                    console.log(res)
                    this.deleteDialogVisible = false;
                    this.initData()
                })
                break;
            case'bind':
                let ids1 = [];
                this.multipleSelection1.map(item =>{
                    ids1.push(item.id)
                })
                let data1 = {
                    "clusterIds":ids1,
                    "signalServer":JSON.parse(JSON.stringify(this.multipleSelection[0].id)),
                    "type":"bind"
                }
                bindSignal(data1).then((res) =>{
                    console.log(res);
                    if(res&&res.code == 200){
                        this.bindDialogVisible = false;
                        this.initData()
                    }
                })
                break;
            case'untying':
                let ids2 = [];
                this.multipleSelection2.map(item =>{
                    ids2.push(item.id)
                })
                let data2 = {
                    "clusterIds":ids2,
                    "signalServer":JSON.parse(JSON.stringify(this.multipleSelection[0].id)),
                    "type":"unBind"
                }
                bindSignal(data2).then((res) =>{
                    if(res&&res.code == 200){
                        this.untyingDialogVisible = false;
                        this.initData()
                    }
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
        handleSelectionChange(val) {
            this.multipleSelection = val;
            if(val.length == 0){
                this.canEdit = false,
                this.canDel = false,
                this.canBind = false,
                this.canUntying = false
            }else if(val.length == 1){
                this.canEdit = true,
                this.canDel = true,
                this.canBind = true,
                this.canUntying = true
            }else if(val.length > 1){
                this.canEdit = false,
                this.canDel = true,
                this.canBind = false,
                this.canUntying = false
            }
        },
        handleSelectionChange1(val){
            this.multipleSelection1 = val;
        },
        handleSelectionChange2(val){
            this.multipleSelection2 = val;
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
        // this.initEcharts();
        this.hasRouteQuery = this.$route.query.id?1:0;
    }
}
</script>
