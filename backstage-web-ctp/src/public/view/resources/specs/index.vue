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
                <el-table-column prop="name" label="规格名称" sortable>
                    <template slot-scope="scope">
                         <span :title="scope.row.name">{{scope.row.name}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="specsType" label="资源类型" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.specsType'>{{scope.row.specsType}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="model" label="设备型号" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.model'>{{scope.row.model}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="cpu" label="CPU型号规格" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.cpu'>{{scope.row.cpu}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="graphics" label="显卡型号" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.graphics'>{{scope.row.graphics}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="detail" label="详情" >
                    <template slot-scope="scope">
                        <router-link v-bind:to="'/specs?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.name)">
                            <span>查看</span>
                        </router-link>
                    </template>
                </el-table-column>
            </el-table>
            <div v-if="pageInstan.showPage">
                <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
            </div>
        </div>
        <detail-page :props="hasRouteQuery">
            <specs-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></specs-detail>
        </detail-page>
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
                    <span class="red">*</span>规格名称：
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
                    <span class="red">*</span>资源类型：
                </label>
                <div class="controls">
                    <el-radio-group v-model="specsForm.type">
                        <el-radio :label="0">arm</el-radio>
                        <el-radio :label="1">x86</el-radio>
                    </el-radio-group>
                    <div class="error">
                        <span v-show="errors.has('type')" class="error">{{ errors.first('type') }}</span>
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
                    <span class="red">*</span>CPU型号规格：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.cpu" data-vv-as="CPU型号规格" name="cpu" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('cpu')" class="error">{{ errors.first('cpu') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>CPU个数：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.cpuNumber" data-vv-as="CPU个数" name="cpuNumber" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('cpuNumber')" class="error">{{ errors.first('cpuNumber') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>CPU插槽数：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.cpuSlot" data-vv-as="CPU插槽数" name="cpuSlot" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('cpuSlot')" class="error">{{ errors.first('cpuSlot') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>内存规格：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.memoryModel" data-vv-as="内存规格" name="memoryModel" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('memoryModel')" class="error">{{ errors.first('memoryModel') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>内存：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.memory" data-vv-as="内存" name="memory" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('memory')" class="error">{{ errors.first('memory') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>插槽数：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.memorySlot" data-vv-as="插槽数" name="memorySlot" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('memorySlot')" class="error">{{ errors.first('memorySlot') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>显卡型号：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.graphics" data-vv-as="显卡型号" name="graphics" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('graphics')" class="error">{{ errors.first('graphics') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>磁盘型号：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.diskModel" data-vv-as="磁盘型号" name="diskModel" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('diskModel')" class="error">{{ errors.first('diskModel') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>磁盘大小（Byte）：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.diskSize" data-vv-as="磁盘大小（Byte）" name="diskSize" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('diskSize')" class="error">{{ errors.first('diskSize') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>网卡规格：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="specsForm.networkCard" data-vv-as="网卡规格" name="networkCard" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('networkCard')" class="error">{{ errors.first('networkCard') }}</span>
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
import {getBaseList,addDeviceBase,updateDeviceBase,deleteDeviceBase} from '@/public/api/specs.js'
import detailPage from '@/public/common-tmpl/detail.vue' 
import specsDetail from './specsDetail.vue'
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
        detailPage,specsDetail
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
            getBaseList(oData).then((res) =>{
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
