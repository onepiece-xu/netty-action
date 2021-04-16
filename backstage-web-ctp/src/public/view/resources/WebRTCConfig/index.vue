<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新建</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除</button>
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
                    <span class="red">*</span>Http信令服务器地址：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="configForm.signalServer" data-vv-as="Http信令服务器地址" name="signalServer" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('signalServer')" class="error">{{ errors.first('signalServer') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>Https信令服务器地址：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="configForm.signalServerHttps" data-vv-as="Https信令服务器地址" name="signalServerHttps" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('signalServerHttps')" class="error">{{ errors.first('signalServerHttps') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>turn服务器地址：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="configForm.turnServer" data-vv-as="turn服务器地址" name="turnServer" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('turnServer')" class="error">{{ errors.first('turnServer') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>turn服务器登录名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="configForm.turnUser" data-vv-as="turn服务器登录名称" name="turnUser" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('turnUser')" class="error">{{ errors.first('turnUser') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>turn服务器登录密码：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="configForm.turnPassword" data-vv-as="turn服务器登录密码" name="turnPassword" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('turnPassword')" class="error">{{ errors.first('turnPassword') }}</span>
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
import {getConfigList,addConfig,updateConfig,deleteConfig} from '@/public/api/WebRTCConfig.js'
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
            addTitle:"新建WebRTC配置",
            configForm:{},
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
                }
            })
            
        },
        createItem(){
            this.type = "add";
            this.addDialogVisible = true,
            this.addTitle = "新建WebRTC配置",
            this.configForm = {
                "signalServer":"",
                "signalServerHttps":"",
                "turnServer":"",
                "turnUser":"",
                "turnPassword":""
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑WebRTC配置",
            this.configForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            console.log(this.configForm)
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        commitModel(){
            let data = {
                "signalServer":this.configForm.signalServer,
                "signalServerHttps":this.configForm.signalServerHttps,
                "turnServer":this.configForm.turnServer,
                "turnUser":this.configForm.turnUser,
                "turnPassword":this.configForm.turnPassword,
            }
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        addConfig(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.configForm.id;
                        updateConfig(data).then((res) =>{
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
                deleteConfig(data).then((res) =>{
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
