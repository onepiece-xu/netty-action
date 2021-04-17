<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-info" @click="approvalItem" :disabled="!bth_check.canApproval">审批</button>
            <div class="pull-right">
                <div class="search-box">
                    <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                        <button class="btn" type="submit" :disabled="false">
                            <i class="icon-aw-search"></i>
                        </button>
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入企业名称" name="searchTerm" v-model="globalSearchTerm"/>
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
                <el-table-column prop="enterpriseName" label="企业名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="identification" label="企业税务识别码" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.identification'>{{scope.row.identification}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="loginName" label="用户登录名" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.loginName'>{{scope.row.loginName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="" label="状态" column-key="schStatus" :filter-multiple="false" :filters="[{text: '审批中', value: 1},{text: '审批通过', value: 2},{text: '审批不通过', value: 3}]">
                    <template slot-scope="scope">
                        <span :title='scope.row.userStatus' :class="scope.row.schStatus == 2?'bg-red':'bg-green'">{{scope.row.userStatus}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.createTime'>{{scope.row.createTime}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="modifiedTime" label="修改时间" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.modifiedTime'>{{scope.row.modifiedTime}}</span>
                    </template> 
                </el-table-column>
                <!-- <el-table-column prop="detail" label="详情">
                    <template slot-scope="scope">
                        <router-link v-bind:to="'/userAudit?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.enterpriseName)">
                            <span>审批</span>
                        </router-link>
                    </template>
                </el-table-column> -->
            </el-table>
            <div v-if="pageInstan.showPage">
                <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
            </div>
        </div>
        <!-- <detail-page :props="hasRouteQuery">
            <user-audit-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></user-audit-detail>
        </detail-page> -->
        <el-dialog
            :visible.sync="addDialogVisible"
            title="审批"
            custom-class="big-dialog"
            center
            v-if="addDialogVisible"
            :close-on-click-modal="false">
            <div class="detail-info">
                <table class='table-dex' >
                    <tr>
                        <td>企业名称</td>
                        <td>{{detailInfo.enterpriseName}}</td>
                        <td>企业税务识别码</td>
                        <td>{{detailInfo.identification}}</td>
                    </tr>
                    <tr>
                        <td>用户登录名</td>
                        <td>{{detailInfo.loginName}}</td>
                        <td>用户邮箱</td>
                        <td>{{detailInfo.email}}</td>
                        <!-- <td>状态</td>
                        <td>{{detailInfo.userStatus}}</td> -->
                    </tr>
                    <tr>
                        <td>用户手机号</td>
                        <td>{{detailInfo.mobileNumber}}</td>
                        <td>电话号码（座机）</td>
                        <td>{{detailInfo.telNumber}}</td>
                    </tr>
                    <tr>
                        <td>创建时间</td>
                        <td>{{detailInfo.createTime}}</td>
                        <td>修改时间</td>
                        <td>{{detailInfo.modifiedTime}}</td>
                    </tr>
                    <tr>
                        <td>营业执照</td>
                        <td><a :href="imgUrl" target="_blank">查看图片</a></td>
                        <td>备注</td>
                        <td>{{detailInfo.description}}</td>
                    </tr>
                </table>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>审批结果：
                </label>
                <div class="controls">
                    <el-select
                        v-model="userForm.type"
                        filterable
                        clearable
                        name="type"
                        data-vv-as="审批结果"
                        v-validate="'required'"
                    >
                        <el-option
                            v-for="item in typeList"
                            :key="item.id"
                            :label="item.type"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                    <div class="error">
                        <span v-show="errors.has('type')" class="error">{{ errors.first('type') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>审批意见：</label>
                <div class="controls">
                    <textarea class="form-controls" name="result" placeholder="请填写审批意见" v-model="userForm.result" data-vv-as="审批意见" v-validate="'required'"> </textarea>
                    <div class="error">
                        <span v-show="errors.has('result')" class="error">{{ errors.first('result') }}</span>
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
import {getUserApproveList,getUserInfo,approve} from '@/public/api/approval.js';
// import detailPage from '@/public/common-tmpl/detail.vue' 
// import userAuditDetail from './userAuditDetail.vue'
export default {
    data(){
        return {
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
            detailInfo:{},
            userForm:{},
            typeList:[
                {
                    id:0,
                    type:"审批不通过",
                },
                {
                    id:1,
                    type:"审批通过",
                },
            ],
            imgUrl:'',
            // imgShow:false
            schStatus:''
        }
    },
    components: {
        // detailPage,userAuditDetail
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
        handleFilterChange(filters){
            console.log(filters)
            this.pageInstan.pageSize = 50
            this.pageInstan.currentPage = 1
            if(filters.schStatus){
                this.schStatus = filters.schStatus[0]
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
            if(this.schStatus){
                if(this.schStatus == 1){
                    oData.schStatus = 0
                }else if(this.schStatus == 2){
                    oData.schStatus = 1
                }else if(this.schStatus == 3){
                    oData.schStatus = 2
                }
            }
            getUserApproveList(oData).then((res) =>{
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
                
                item.userStatus = item.schStatus?"正常":"禁用";
                if(item.schStatus == 0){
                    item.userStatus = '审批中'
                }else if(item.schStatus == 2){
                    item.userStatus = '审批不通过'
                }else if(item.schStatus == 1){
                    item.userStatus = '审批通过'
                }
                item.createTime = item.createTime?moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.modifiedTime = item.modifiedTime?moment(item.modifiedTime).format("YYYY-MM-DD HH:mm:ss"):'';
            })
        },
        approvalItem(){
            this.userForm = {}
            this.addDialogVisible = true
            var id = this.multipleSelection.map(item=>{return item.id})
            getUserInfo(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                    this.optionData1([this.detailInfo])
                    this.imgUrl = window.GLOBALCONFIG.APIHOST.IMG + res.data.userPic
                }
            })
        },
        optionData1(data){
            data.forEach(function(item){
                item.createTime = item.createTime?moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.modifiedTime = item.modifiedTime?moment(item.modifiedTime).format("YYYY-MM-DD HH:mm:ss"):'';
            })
        },
        commitModel(){
            let oData ={
                'id':JSON.parse(JSON.stringify(this.multipleSelection[0].id)),
                'result':this.userForm.result,
                'type':this.userForm.type
            }
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    approve(oData).then(() =>{
                        this.addDialogVisible = false;
                        this.initData()
                    })
                }
            })
            
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
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
        bth_check(){
            // 0启用
            // 1禁用
            if(this.multipleSelection.length ==1){
                if(this.multipleSelection[0].schStatus == 0){
                    return {
                        canApproval:true,
                    }
                }else{
                    return {
                        canApproval:false,
                    }
                }
            }else if(this.multipleSelection.length > 1){
                return {
                    canApproval:false,
                }
            }else{
                return {
                    canApproval:false,
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
