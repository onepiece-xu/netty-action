<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新建类型</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑类型</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除类型</button>
            <div class="pull-right">
                <div class="search-box">
                    <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                        <button class="btn" type="submit" :disabled="false">
                            <i class="icon-aw-search"></i>
                        </button>
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入类型名称" name="searchTerm" v-model="globalSearchTerm"/>
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
                <el-table-column prop="id" label="类型ID" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.id'>{{scope.row.id}}</span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="tagTypeName" label="类型名称" sortable>
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
            <span>您确定要删除选择的标签类型吗？</span>
            <div slot="footer">
                <button class="btn btn-warning" type="button" :disabled="deleteItemDisabled" @click="confirm()">{{$t('cn.action.ok')}}</button>
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
                    <span class="red">*</span>类型名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="tagTypeForm.tagTypeName" data-vv-as="类型名称" name="tagTypeName" v-validate="'required'">
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
    </div>
</template>

<script>
import {getTagTypeList,addTagType,updateTagType,deleteTagType} from '@/public/api/label.js';
export default {
    data(){
        return {
            canEdit:false,
            canDel:false,
            hasRouteQuery:0,
            multipleSelection: [],
            dataList:[],
            // page
            globalSearchTerm:'',
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            addDialogVisible:false,
            addTitle:"新建标签类型",
            type:'add',
            deleteItemDisabled:false,
            deleteDialogVisible:false
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
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize,
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            getTagTypeList(oData).then((res) =>{
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
            this.addTitle = "新建标签类型",
            this.tagTypeForm = {
                "tagTypeName":"",
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑标签类型",
            this.tagTypeForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
        },
        commitModel(){
            let data = {
                "tagTypeName":this.tagTypeForm.tagTypeName
            }
            console.log(data)
            // return
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        addTagType(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.tagTypeForm.id;
                        updateTagType(data).then((res) =>{
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
        confirm(){
            let ids = [];
            this.multipleSelection.map(item =>{
                ids.push(item.id)
            })
            let data = ids
            deleteTagType(data).then(() =>{
                this.deleteDialogVisible = false;
                this.initData()
            })
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            if(val.length == 0){
                this.canEdit = false,
                this.canDel = false
            }else if(val.length == 1){
                this.canEdit = true,
                this.canDel = true
            }else if(val.length > 1){
                this.canEdit = false,
                this.canDel = true
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
