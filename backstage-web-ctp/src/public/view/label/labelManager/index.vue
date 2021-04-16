<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新建标签</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑标签</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除标签</button>
            <button type="button" class="btn btn-info" @click="bindItem" :disabled="!canBind">绑定</button>
            <button type="button" class="btn btn-info" @click="untyingItem" :disabled="!canUntying">解绑</button>
            <div class="pull-right">
                <div class="search-box">
                    <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                        <button class="btn" type="submit" :disabled="false">
                            <i class="icon-aw-search"></i>
                        </button>
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入标签名称" name="searchTerm" v-model="globalSearchTerm"/>
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
                <el-table-column prop="id" label="标签ID" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.id'>{{scope.row.id}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="tagName" label="标签名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.tagName'>{{scope.row.tagName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="tagTypeName" label="标签类型" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.tagTypeName'>{{scope.row.tagTypeName}}</span>
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
            <span>您确定要删除选择的标签吗？</span>
            <div slot="footer">
                <button class="btn btn-warning" type="button" :disabled="deleteItemDisabled" @click="confirm('del')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="deleteDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 新建 编辑 -->
        <el-dialog
            :visible.sync="addDialogVisible"
            :title="addTitle"
            custom-class="normal-dialog"
            center
            v-if="addDialogVisible"
            :close-on-click-modal="false">
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>标签名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="tagForm.tagName" data-vv-as="标签名称" name="tagName" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('tagName')" class="error">{{ errors.first('tagName') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>标签类型：
                </label>
                <div class="controls">
                    <el-select
                        v-model="tagForm.tagType"
                        filterable
                        clearable
                        name="tagType"
                        data-vv-as="用户类型"
                        v-validate="'required'"
                    >
                        <el-option
                            v-for="item in tagTypeList"
                            :key="item.id"
                            :label="item.tagTypeName"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                    <div class="error">
                        <span v-show="errors.has('tagTypeName')" class="error">{{ errors.first('tagTypeName') }}</span>
                    </div>
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
            title="绑定应用"
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
                    <el-table-column prop="appId" label="应用ID">
                        <template slot-scope="scope">
                            <span :title="scope.row.appId">{{scope.row.appId}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="appName" label="应用名称">
                        <template slot-scope="scope">
                            <span :title='scope.row.appName'>{{scope.row.appName}}</span>
                        </template> 
                    </el-table-column>
                    <!-- <el-table-column prop="enterpriseName" label="所属用户">
                        <template slot-scope="scope">
                            <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="type" label="应用类型">
                        <template slot-scope="scope">
                            <span :title='scope.row.type'>{{scope.row.type}}</span>
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
            <div class="table-content">
                <el-table ref="multipleTable" 
                    :data="dataList2"
                    height="295" 
                    @selection-change="handleSelectionChange2">
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="appId" label="应用ID">
                        <template slot-scope="scope">
                            <span :title="scope.row.appId">{{scope.row.appId}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="appName" label="应用名称">
                        <template slot-scope="scope">
                            <span :title='scope.row.appName'>{{scope.row.appName}}</span>
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
    </div>
</template>

<script>
import {getTags,getTagTypeList,addTag,updateTag,deleteTags,getUnBindApps,getBindApps,bindTags} from '@/public/api/label.js';
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
            tagTypeList:[],
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
            addDialogVisible:false,
            addTitle:"新建标签",
            type:'add',
            deleteItemDisabled:false,
            deleteDialogVisible:false,
            bindDialogVisible:false,
            dataList1:[],
            untyingDialogVisible:false,
            dataList2:[],
            multipleSelection1: [],
            multipleSelection2: []
        }
    },
    components: {
        
    },
    methods:{
        handleSizeChange(val) {
            this.pageInstan.pageSize = val;
            this.pageInstan.currentPage = 1;
            this.initData();
        },
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
                'pageSize': this.pageInstan.pageSize,
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            getTags(oData).then((res) =>{
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
            getTagTypeList().then((res) =>{
                if(res&&res.data){
                    this.tagTypeList = res.data;
                }
            })
        },
        createItem(){
            this.type = "add";
            this.addDialogVisible = true,
            this.addTitle = "新建标签",
            this.tagForm = {
                "tagName":"",
                "tagType":"",
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑标签",
            console.log(JSON.parse(JSON.stringify(this.multipleSelection[0])))
            this.tagForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
        },
        commitModel(){
            let data = {
                "tagName":this.tagForm.tagName,
                "tagType":this.tagForm.tagType
            }
            console.log(data)
            // return
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        addTag(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.tagForm.id;
                        updateTag(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }
                    
                }
            })
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        // confirm(){
        //     let ids = [];
        //     this.multipleSelection.map(item =>{
        //         ids.push(item.id)
        //     })
        //     let data = ids
        //     deleteTags(data).then(() =>{
        //         this.deleteDialogVisible = false;
        //         this.initData()
        //     })
        // },
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
                'tagId': this.multipleSelection.map(item=>{return item.id})
            }
            console.log(data)
            // return
            getUnBindApps(data).then((res) =>{
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
                'tagId': this.multipleSelection.map(item=>{return item.id})
            }
            console.log(data)
            // return
            getBindApps(data).then((res) =>{
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
        confirm(type){
            switch(type){
            case'del':
                let ids = [];
                this.multipleSelection.map(item =>{
                    ids.push(item.id)
                })
                let data = ids
                deleteTags(data).then((res) =>{
                    console.log(res)
                    this.deleteDialogVisible = false;
                    this.initData()
                })
                break;
            case'bind':
                let ids1 = [];
                this.multipleSelection1.map(item =>{
                    ids1.push(item.appId)
                })
                let data1 = {
                    "appIds":ids1,
                    "tagId":JSON.parse(JSON.stringify(this.multipleSelection[0].id)),
                    "type":"bind"
                }
                console.log(data1)
                // return
                bindTags(data1).then((res) =>{
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
                    ids2.push(item.appId)
                })
                let data2 = {
                    "appIds":ids2,
                    "tagId":JSON.parse(JSON.stringify(this.multipleSelection[0].id)),
                    "type":"unBind"
                }
                console.log(data2)
                // return
                bindTags(data2).then((res) =>{
                    console.log(res);
                    if(res&&res.code == 200){
                        this.untyingDialogVisible = false;
                        this.initData()
                    }
                })
                break;
            }
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
        this.hasRouteQuery = this.$route.query.id?1:0;
    }
}
</script>