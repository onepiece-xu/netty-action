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
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" :placeholder="$t('cn.action.search')" name="searchTerm" v-model="globalSearchTerm"/>
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
                <el-table-column prop="id" label="集群ID" sortable>
                    
                </el-table-column>
                <el-table-column prop="name" label="所属设备组" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.name'>{{scope.row.name}}</span>
                    </template>
                 </el-table-column>
                <el-table-column prop="clusterId" label="边缘节点编号" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.clusterId'>{{scope.row.clusterId}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="description" label="描述" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.description'>{{scope.row.description}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="ipRegion" label="Ip范围" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.ipRegion'>{{scope.row.ipRegion}}</span>
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
                    <span class="red">*</span>所属设备组：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="colonyForm.name" data-vv-as="用户名称" name="name" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>边缘节点编号：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="colonyForm.clusterId" data-vv-as="边缘节点编号" name="clusterId" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('clusterId')" class="error">{{ errors.first('clusterId') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>描述：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="colonyForm.description" data-vv-as="描述" name="description" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('description')" class="error">{{ errors.first('description') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>Ip范围：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="colonyForm.ipRegion" data-vv-as="Ip范围" name="ipRegion" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('ipRegion')" class="error">{{ errors.first('ipRegion') }}</span>
                    </div>
                </div>
            </div>
            
            <!-- <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>集群类型：
                </label>
                <div class="controls">
                    <el-select
                        v-model="colonyForm.colonyType"
                        filterable
                        clearable
                        name="colonyType"
                        data-vv-as="集群类型"
                        v-validate="'required'"
                    >
                        <el-option
                            v-for="item in colonyTypeList"
                            :key="item.id"
                            :label="item.colonyType"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                    <div class="error">
                        <span v-show="errors.has('colonyType')" class="error">{{ errors.first('colonyType') }}</span>
                    </div>
                </div>
            </div> -->
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="commitModel()">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {getClusterList,createCluster,updateCluster,deleteCluster} from '@/public/api/colony.js';

export default {
    data(){
        return {
            canEdit:false,
            canDel:false,
            hasRouteQuery:0,
            multipleSelection: [],
            editData:{
                opentype:'',
                optionWrap:false,
                content:this.multipleSelection
            },
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
            // bth_check:{
            //     canEdit:false,
            //     canDel:false,
            // },
            deleteItemDisabled:false,
            addTitle:"新建用户",
            addDialogVisible:false,
            type:'add'
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
        initData(){
            let oData = {
                'search': this.globalSearchTerm
            }
            getClusterList(oData).then((res) =>{
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
                item.createTime = item.createTime?moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.updateTime = item.updateTime?moment(item.updateTime).format("YYYY-MM-DD HH:mm:ss"):'';
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
        createItem(){
            this.type = "add";
            this.addDialogVisible = true,
            this.addTitle = "新建集群",
            this.colonyForm = {
                "name":"",
                "clusterId":"",
                "description":"",
                "ipRegion":"",
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑集群",
            this.colonyForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        commitModel(){
            let data = {
                "name":this.colonyForm.name,
                "clusterId":this.colonyForm.clusterId,
                "description":this.colonyForm.description,
                "ipRegion":this.colonyForm.ipRegion
            }
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
                deleteCluster(data).then((res) =>{
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
