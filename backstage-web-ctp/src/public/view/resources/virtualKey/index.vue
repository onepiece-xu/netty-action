<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新建</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除</button>
            <div class="pull-right">
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
            </div>
        </div>

        <div class="table-content">
            <el-table ref="multipleTable" 
                :data="dataList" 
                @selection-change="handleSelectionChange" >
                <el-table-column type="selection" width="40"> </el-table-column>
                <el-table-column prop="name" label="虚拟按键名称" sortable>
                    <template slot-scope="scope">
                         <span :title="scope.row.name">{{scope.row.name}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="appName" label="游戏名称" sortable>
                    <template slot-scope="scope">
                         <span :title="scope.row.appName">{{scope.row.appName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="author" label="创建人" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.author'>{{scope.row.author}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="config" label="配置" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.config'>{{scope.row.config}}</span>
                    </template> 
                </el-table-column>
            </el-table>
            <div v-if="pageInstan.showPage">
                <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
            </div>
        </div>
        <!-- <detail-page :props="hasRouteQuery">
            <specs-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></specs-detail>
        </detail-page> -->
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
                    <span class="red">*</span>按键名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.name" data-vv-as="规格名称" name="name" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>游戏名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.appName" data-vv-as="游戏名称" name="appName" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('appName')" class="error">{{ errors.first('appName') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>设备型号：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.model" data-vv-as="设备型号" name="model" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('model')" class="error">{{ errors.first('model') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>配置：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.config" data-vv-as="配置" name="config" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('config')" class="error">{{ errors.first('config') }}</span>
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
import {addDeviceBase,updateDeviceBase,deleteDeviceBase} from '@/public/api/specs.js'
import {getConfigList} from '@/public/api/virtualKey.js'
// import detailPage from '@/public/common-tmpl/detail.vue' 
// import specsDetail from './specsDetail.vue'
export default {
    data(){
        return {
            canEdit:false,
            canDel:false,
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
            deleteItemDisabled:false,
            addTitle:"新建规格",
            specsForm:{},
            addDialogVisible:false,
            type:'add',
        }
    },
    components: {
        // detailPage,specsDetail
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
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            getConfigList(oData).then((res) =>{
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
                    this.optionData(this.dataList)
                }
            })
            
        },
        optionData(data){
            data.forEach(function(item){
                item.specsType = item.type == 0?'arm':'x86';
            })
        },
        createItem(){
            this.type = "add";
            this.addDialogVisible = true,
            this.addTitle = "新建规格",
            this.specsForm = {
                "name":"",
                "type":0,
                "model":"",
                "cpu":"",
                "cpuNumber":"",
                "cpuSlot":"",
                "memoryModel":"",
                "memory":"",
                "memorySlot":"",
                "graphics":"",
                "diskModel":"",
                "diskSize":"",
                "networkCard":""
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑规格",
            this.specsForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            console.log(this.specsForm)
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        commitModel(){
            let data = {
                "name":this.specsForm.name,
                "type":this.specsForm.type,
                "model":this.specsForm.model,
                "cpu":this.specsForm.cpu,
                "cpuNumber":this.specsForm.cpuNumber,
                "cpuSlot":this.specsForm.cpuSlot,
                "memoryModel":this.specsForm.memoryModel,
                "memory":this.specsForm.memory,
                "memorySlot":this.specsForm.memorySlot,
                "graphics":this.specsForm.graphics,
                "diskModel":this.specsForm.diskModel,
                "diskSize":this.specsForm.diskSize,
                "networkCard":this.specsForm.networkCard
            }
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        addDeviceBase(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.specsForm.id;
                        updateDeviceBase(data).then((res) =>{
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
                deleteDeviceBase(data).then((res) =>{
                    console.log(res)
                    this.deleteDialogVisible = false;
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
