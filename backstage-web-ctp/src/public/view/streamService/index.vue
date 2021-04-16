<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新建</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!bth_check.canEdit">编辑</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!bth_check.canDel">删除</button>
            <button type="button" class="btn btn-info" @click="bindItem" :disabled="!bth_check.canBind">绑定</button>
            <button type="button" class="btn btn-info" @click="untyingItem" :disabled="!bth_check.canUntying">解绑</button>
            <div class="pull-right">
                <div class="search-box">
                    <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                        <button class="btn" type="submit" :disabled="false">
                            <i class="icon-aw-search"></i>
                        </button>
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入流策略名称" name="searchTerm" v-model="globalSearchTerm"/>
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
                @filter-change="handleFilterChange"
                @selection-change="handleSelectionChange" >
                <el-table-column type="selection" width="40"> </el-table-column>
                <el-table-column prop="id" label="流服务策略ID" sortable>
                </el-table-column>
                <el-table-column prop="name" label="流服务策略名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.name'>{{scope.row.name}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="encode" label="编码方式" column-key="encode" :filter-multiple="false" :filters="[{text: 'H.264', value: 264},{text: 'H.265', value: 265}]">
                    <template slot-scope="scope">
                        <span :title='scope.row.encode'>H.{{scope.row.encode}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="output" label="分辨率" column-key="output" :filter-multiple="false" :filters="[{text: '720p', value: 720},{text: '1080p', value: 1080},{text: '1440p', value: 1440},{text: '2160p', value: 2160}]">
                    <template slot-scope="scope">
                        <span :title='scope.row.output'>{{scope.row.output}}p</span>
                    </template> 
                </el-table-column>
                <!-- <el-table-column prop="" label="画质优先">
                    <template slot-scope="scope">
                        <span :title='scope.row.video1'>{{scope.row.video1}}</span>
                    </template> 
                </el-table-column> -->
                <el-table-column prop="speed" label="码率(KBps)" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.speed'>{{scope.row.speed}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="fps" label="帧率（fps）" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.fps'>{{scope.row.fps}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="clientPath" label="绑定应用列表" >
                    <template slot-scope="scope">
                        <router-link v-bind:to="'/streamService?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.name)">
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
            <span>您确定要删除选择的流服务策略吗？</span>
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
                    <span class="red">*</span>流服务策略名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="colonyForm.name" data-vv-as="流服务策略名称" name="name" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>编码方式：
                </label>
                <div class="controls">
                    <el-radio-group v-model="radio">
                        <el-radio label="0">H.264</el-radio>
                        <el-radio label="1">H.265</el-radio>
                    </el-radio-group>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>分辨率：
                </label>
                <div class="controls">
                    <el-radio-group v-model="radio1">
                        <el-radio label="1">720p</el-radio>
                        <el-radio label="2">1080p</el-radio>
                        <el-radio label="3">1440p</el-radio>
                        <el-radio label="4">2160p</el-radio>
                    </el-radio-group>
                </div>
            </div>
            <!-- <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>画质优先：
                </label>
                <div class="controls">
                    <el-radio-group v-model="colonyForm.radio2">
                        <el-radio :label="0">关</el-radio>
                        <el-radio :label="1">开</el-radio>
                    </el-radio-group>
                </div>
            </div> -->
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>码率(KBps)：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="colonyForm.speed" data-vv-as="码率" name="speed" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('speed')" class="error">{{ errors.first('speed') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>帧率（fps）：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="colonyForm.fps" data-vv-as="帧率" name="fps" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('fps')" class="error">{{ errors.first('fps') }}</span>
                    </div>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="confirm('add')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 绑定 -->
        <el-dialog
            :visible.sync="bindDialogVisible"
            title="绑定应用"
            custom-class="normal-dialog"
            center
            class="bind"
            v-if="bindDialogVisible"
            :close-on-click-modal="false">
            <div class="table-action">
                <div class="control-group">
                    <label class="control-label">应用名称：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="bingForm.name" name="speed">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">所属用户：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="bingForm.enterpriseName" name="speed">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">应用类型：</label>
                    <div class="controls">
                        <el-select
                            v-model="bingForm.type"
                            filterable
                            clearable
                            name="type"
                        >
                            <el-option
                                v-for="item in appTypeList"
                                :key="item.id"
                                :label="item.lable"
                                :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><button type="button" class="btn btn-info" @click="searchItem" style="height:30px">搜索</button></label>
                    <div class="controls">
                    </div>
                </div>
            </div>
            <div class="table-content">
                <el-table ref="multipleTable" 
                    :data="dataList1"
                    height="295" 
                    @selection-change="handleSelectionChange1">
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="id" label="应用ID" width="200">
                        <template slot-scope="scope">
                            <span :title="scope.row.id">{{scope.row.id}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="应用名称" width="200">
                        <template slot-scope="scope">
                            <span :title='scope.row.name'>{{scope.row.name}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="enterpriseName" label="所属用户">
                        <template slot-scope="scope">
                            <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="type" label="应用类型">
                        <template slot-scope="scope">
                            <span :title='scope.row.type'>{{scope.row.type}}</span>
                        </template> 
                    </el-table-column>
                    <!-- <el-table-column prop="" label="" >
                        <template slot-scope="scope">
                            <i class="el-icon-check bind-icon" @click="bindModel(scope.row.id)"></i>
                        </template>
                    </el-table-column> -->
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
            title="解绑应用"
            custom-class="normal-dialog"
            center
            class="untying"
            v-if="untyingDialogVisible"
            :close-on-click-modal="false">
            <div class="table-action">
                <div class="control-group">
                    <label class="control-label">应用名称：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="bingForm.name" name="speed">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">所属用户：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="bingForm.enterpriseName" name="speed">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">应用类型：</label>
                    <div class="controls">
                        <el-select
                            v-model="bingForm.type"
                            filterable
                            clearable
                            name="type"
                        >
                            <el-option
                                v-for="item in appTypeList"
                                :key="item.id"
                                :label="item.lable"
                                :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><button type="button" class="btn btn-info" @click="searchItem1" style="height:30px">搜索</button></label>
                    <div class="controls">
                    </div>
                </div>
                
            </div>
            <div class="table-content">
                <el-table ref="multipleTable" 
                    :data="dataList2"
                    height="295" 
                    @selection-change="handleSelectionChange2">
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="id" label="应用ID" width="200">
                        <template slot-scope="scope">
                            <span :title="scope.row.id">{{scope.row.id}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="应用名称" width="200">
                        <template slot-scope="scope">
                            <span :title='scope.row.name'>{{scope.row.name}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="enterpriseName" label="所属用户">
                        <template slot-scope="scope">
                            <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="type" label="应用类型">
                        <template slot-scope="scope">
                            <span :title='scope.row.type'>{{scope.row.type}}</span>
                        </template> 
                    </el-table-column>
                    <!-- <el-table-column prop="" label="" >
                        <template slot-scope="scope">
                            <i class="el-icon-close untying-icon" @click="untyingModel(scope.row.id)"></i>
                        </template>
                    </el-table-column> -->
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
            <bind-list :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></bind-list>
        </detail-page>
    </div>
</template>

<script>
import {getStrategyList,getBindApps,getUnBindApps,bindStrategy,UnbindStrategy,createStrategy,updateStrategy,delStrategy} from '@/public/api/streamService.js';
import detailPage from '@/public/common-tmpl/detail.vue' 
import bindList from './bind-list.vue'
export default {
    data(){
        return {
            canEdit:false,
            canDel:false,
            canBind:false,
            canUntying:false,
            radio:"0",
            radio1:"1",
            hasRouteQuery:0,
            multipleSelection: [],
            multipleSelection1: [],
            multipleSelection2: [],
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
            // bth_check:{
            //     canEdit:false,
            //     canDel:false,
            // },
            deleteItemDisabled:false,
            addTitle:"新建流服务策略",
            addDialogVisible:false,
            colonyForm:{},
            bindDialogVisible:false,
            dataList1:[],
            untyingDialogVisible:false,
            dataList2:[],
            bingForm:{
                'name':'',
                'enterpriseName':'',
                'type':''
            },  
            appTypeList:[
                {
                    'id':1,
                    'lable':'arm',
                    'value':'0'
                },
                {
                    'id':2,
                    'lable':'x86',
                    'value':'1'
                },
            ],
            encode:'',
            output:''
        }
    },
    components: {
        detailPage,bindList
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
        handleFilterChange(filters){
            console.log(filters)
            this.pageInstan.pageSize = 50
            this.pageInstan.currentPage = 1
            if(filters.encode){
                this.encode = filters.encode[0]
            }
            if(filters.output){
                this.output = filters.output[0]
            }
            this.initData()
        },
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            if(this.encode){
                oData.encode = this.encode
            }
            if(this.output){
                oData.output = this.output
            }
            getStrategyList(oData).then((res) =>{
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
                    // this.optionData(this.dataList)
                }
            })
        },
        // optionData(data){
        //     data.forEach(function(item){
        //         item.video1 = item.video == 0?'关':'开';
        //     })
        // },
        createItem(){
            this.type = "add"
            this.addDialogVisible = true
            this.addTitle = "新建流服务策略"
            this.radio = "0"
            this.radio1 = "1"
            this.colonyForm = {
                'name':"",
                'speed':'1000',
                'fps':'30'
            }
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑流服务策略",
            this.colonyForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            console.log(this.colonyForm)
            if(this.colonyForm.encode == 264){
                this.radio = "0"
            }else{
                this.radio = "1"
            }
            if(this.colonyForm.output == 720){
                this.radio1 = "1"
            }else if(this.colonyForm.output == 1080){
                this.radio1 = "2"
            }else if(this.colonyForm.output == 1440){
                this.radio1 = "3"
            }else if(this.colonyForm.output == 2160){
                this.radio1 = "4"
            }
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        bind(){
            let oData = {
                'pageNum': this.pageInstan1.currentPage,
                'pageSize': this.pageInstan1.pageSize
            }
            if(this.bingForm.name){
                oData.appName = this.bingForm.name
            }
            if(this.bingForm.enterpriseName){
                oData.enterpriseName = this.bingForm.enterpriseName
            }
            if(this.bingForm.type){
                oData.type = this.bingForm.type
            }
            console.log(oData)
            // return
            var id = this.multipleSelection.map(item=>{return item.id})
            getUnBindApps(oData,id).then((res) =>{
                if(res&&res.data){
                    this.dataList1 = res.data.records;
                    if(this.dataList1.length > 0){
                        this.pageInstan1.Total = res.data.total;
                        this.pageInstan1.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan1.Total = res.data.total;
                        this.pageInstan1.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                    this.dataList1.forEach(function(item){
                        item.type = item.type == 0?'arm':'x86';
                    })
                }
            })
        },
        bindItem(){
            this.bingForm = {
                'name':'',
                'enterpriseName':'',
                'type':''
            },
            this.pageInstan1 ={
                currentPage:1,
                pageSize:10
            }
            this.bindDialogVisible = true
            this.bind()
        },
        untying(){
            let oData = {
                'pageNum': this.pageInstan2.currentPage,
                'pageSize': this.pageInstan2.pageSize
            }
            if(this.bingForm.name){
                oData.appName = this.bingForm.name
            }
            if(this.bingForm.enterpriseName){
                oData.enterpriseName = this.bingForm.enterpriseName
            }
            if(this.bingForm.type){
                oData.type = this.bingForm.type
            }
            console.log(oData)
            // return
            var id = this.multipleSelection.map(item=>{return item.id})
            getBindApps(oData,id).then((res) =>{
                if(res&&res.data){
                    this.dataList2 = res.data.records;
                    if(this.dataList2.length > 0){
                        this.pageInstan2.Total = res.data.total;
                        this.pageInstan2.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan2.Total = res.data.total;
                        this.pageInstan2.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                    this.dataList2.forEach(function(item){
                        item.type = item.type == 0?'arm':'x86';
                    })
                }
            })
        },
        untyingItem(){
            this.bingForm = {
                'name':'',
                'enterpriseName':'',
                'type':''
            },
            this.pageInstan2 ={
                currentPage:1,
                pageSize:50
            }
            this.untyingDialogVisible = true
            this.untying()
        },
        searchItem(){
            this.bind()
        },
        searchItem1(){
            this.untying()
        },
        confirm(type){
            switch(type){
            case'add':
                let data = {
                    "name":this.colonyForm.name,
                    "speed":this.colonyForm.speed,
                    "fps":this.colonyForm.fps
                }
                if(this.radio == "0"){
                    data.encode = 264
                }else{
                    data.encode = 265
                }
                if(this.radio1 == '1'){
                    data.output = 720
                }else if(this.radio1 == '2'){
                    data.output = 1080
                }else if(this.radio1 == '3'){
                    data.output = 1440
                }else if(this.radio1 == '4'){
                    data.output = 2160
                }
                this.$validator.validateAll().then((result)=>{
                    if (result) {
                        if(this.type == 'add'){
                            createStrategy(data).then((res) =>{
                                console.log(res)
                                this.addDialogVisible = false;
                                this.initData()
                            })
                        }else if(this.type == 'edit'){
                            data.id = this.colonyForm.id;
                            updateStrategy(data).then((res) =>{
                                console.log(res)
                                this.addDialogVisible = false;
                                this.initData()
                            })
                        }
                        
                    }
                })
                break;
            case'del':
                let ids = [];
                this.multipleSelection.map(item =>{
                    ids.push(item.id)
                })
                let data1 = ids
                delStrategy(data1).then((res) =>{
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
                let data2 = ids1
                console.log(data2)
                // return
                var id = this.multipleSelection.map(item=>{return item.id})
                bindStrategy(data2,id).then((res) =>{
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
                let data3 = ids2
                console.log(data3)
                // return
                var id1 = this.multipleSelection.map(item=>{return item.id})
                UnbindStrategy(data3,id1).then((res) =>{
                    console.log(res);
                    if(res&&res.code == 200){
                        this.untyingDialogVisible = false;
                        this.initData()
                    }
                })
                break;
            }
        },
        // bindModel(data){
        //     var id = this.multipleSelection.map(item=>{return item.id})
        //     bindStrategy([data],id).then((res) =>{
        //         console.log(res);
        //         if(res&&res.code == 200){
        //             this.bindItem()
        //         }
        //     })
        // },
        // untyingModel(data){
        //     var id = this.multipleSelection.map(item=>{return item.id})
        //     UnbindStrategy([data],id).then((res) =>{
        //         console.log(res);
        //         if(res&&res.code == 200){
        //             this.untyingItem()
        //         }
        //     })
        // },
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
        handleSelectionChange1(val){
            this.multipleSelection1 = val;
        },
        handleSelectionChange2(val){
            this.multipleSelection2 = val;
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
    },
    computed:{
        bth_check(){
            if(this.multipleSelection.length ==1){
                if(this.multipleSelection[0].type == 0){
                    return {
                        canDel:false,
                        canEdit:true,
                        canBind:true,
                        canUntying:false,
                    }
                }else{
                    return {
                        canDel:true,
                        canEdit:true,
                        canBind:true,
                        canUntying:true,
                    }
                }
                
            }else if(this.multipleSelection.length > 1){
                let status = this.multipleSelection.some((item) =>{
                    return item.type == 0;
                })
                return {
                    canDel:!status,
                    canEdit:false,
                    canBind:false,
                    canUntying:false,
                }
            }else{
                return {
                    canDel:false,
                    canEdit:false,
                    canBind:false,
                    canUntying:false,
                }
            }
        }
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
