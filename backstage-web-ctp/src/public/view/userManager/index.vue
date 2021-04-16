<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新建用户</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!bth_check.canEdit">编辑用户</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!bth_check.canDel">删除用户</button>
            <button type="button" class="btn btn-info" @click="resetItem" :disabled="!bth_check.canReset">用户密码重置</button>
            <button type="button" class="btn btn-info" @click="enableItem" :disabled="!bth_check.canEnable">启用</button>
            <button type="button" class="btn btn-info" @click="prohibitItem" :disabled="!bth_check.canProhibit">禁用</button>
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
                @selection-change="handleSelectionChange" >
                <el-table-column type="selection" width="40"> </el-table-column>
                <el-table-column prop="enterpriseName" label="企业名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="identification" label="企业ID" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.identification'>{{scope.row.identification}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="loginName" label="用户登录名" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.loginName'>{{scope.row.loginName}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="" label="用户类型" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.userType'>{{scope.row.userType}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="" label="状态" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.userStatus' :class="scope.row.schStatus?'bg-green':'bg-red'">{{scope.row.userStatus}}</span>
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
                <el-table-column prop="detail" label="详情" >
                    <template slot-scope="scope">
                        <router-link v-bind:to="'/userManager?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.enterpriseName)">
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
            <user-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></user-detail>
        </detail-page>
        <!-- 删除 -->
        <el-dialog
            :visible.sync="courseDialogFormVisible"
            custom-class="small-dialog delete-dialog"
            center
            :close-on-click-modal="false">
            <span>{{deleteTips}}</span>
            <div slot="footer">
                <button class="btn btn-warning" type="button" :disabled="deleteItemDisabled" @click="confirm()">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="courseDialogFormVisible = false">{{$t('cn.action.cancel')}}</button>
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
                    <span class="red">*</span>企业名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userForm.enterpriseName" data-vv-as="用户名称" name="enterpriseName" v-validate="'required'" placeholder="请输入用户名称">
                    <div class="error">
                        <span v-show="errors.has('enterpriseName')" class="error">{{ errors.first('enterpriseName') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>企业ID：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userForm.identification" data-vv-as="认证号码" name="identification" v-validate="'required'" placeholder="请输入企业ID">
                    <div class="error">
                        <span v-show="errors.has('identification')" class="error">{{ errors.first('identification') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">所属集群：</label>
                <div class="controls">
                    <el-select
                        v-model="userForm.clusterIds"
                        clearable
                        multiple
                        name="clusterIds"
                    >
                        <el-option
                            v-for="item in clustersList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>用户登录名：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userForm.loginName" data-vv-as="用户登录名" name="loginName" v-validate="'required'" placeholder="请输入用户登录名">
                    <div class="error">
                        <span v-show="errors.has('loginName')" class="error">{{ errors.first('loginName') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group" v-show="!prohibit">
                <label class="control-label">密码：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userForm.password" name="password" placeholder="可缺省密码，默认为系统设置的初始密码">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>用户类型：
                </label>
                <div class="controls">
                    <el-select
                        v-model="userForm.type"
                        filterable
                        clearable
                        name="type"
                        data-vv-as="用户类型"
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
                <label class="control-label">
                    <span class="red">*</span>邮箱：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userForm.email" data-vv-as="邮箱" name="email" v-validate="'required|email'" placeholder="真实有效的邮箱，用以接收系统消息">
                    <div class="error">
                        <span v-show="errors.has('email')" class="error">{{ errors.first('email') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">手机号：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userForm.mobileNumber" name="mobileNumber" placeholder="请输入手机号">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">电话号码（座机）：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userForm.telNumber" name="telNumber" placeholder="请输入电话号码（座机）">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">备注：</label>
                <div class="controls">
                    <textarea class="form-controls" name="description" placeholder="请输入备注" v-model="userForm.description"> </textarea>
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
import {createUser,updateUser,deleteUser,resetPassword,getuserList,disableUser,getServiceClusters} from '@/public/api/userManager.js';
import detailPage from '@/public/common-tmpl/detail.vue' 
import userDetail from './userDetail.vue'
export default {
    data(){
        return {
            // canEdit:false,
            // canReset:false,
            // canDel:false,
            // canEnable:false,
            // canProhibit:false,
            hasRouteQuery:0,
            multipleSelection: [],
            dataList:[],
            courseDialogFormVisible:false,
            deleteTips:"",
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
            userForm:{},
            addTitle:"新建用户",
            addDialogVisible:false,
            type:'add',
            prohibit:false,
            typeList:[
                // {
                //     "id":"1",
                //     "value":"平台管理用户",
                // },
                {
                    id:2,
                    type:"企业用户",
                },
                // {
                //     "id":"3",
                //     "value":"个人用户",
                // },
            ],
            clustersList:[]
        }
    },
    components: {
        detailPage,userDetail
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
            getuserList(oData).then((res) =>{
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
            getServiceClusters().then((res) =>{
                if(res&&res.data){
                    this.clustersList = res.data
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
                item.userStatus = item.schStatus?"正常":"禁用";
                item.createTime = item.createTime?moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.modifiedTime = item.modifiedTime?moment(item.modifiedTime).format("YYYY-MM-DD HH:mm:ss"):'';
                if(item.type == 1){
                    item.userType = '平台管理用户'
                }else if(item.type == 2){
                    item.userType = '企业用户'
                }else if(item.accountStatus == 3){
                    item.userType = '个人用户'
                }
            })
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            // if(val.length == 0){
            //     this.canEdit = false,
            //     this.canReset = false,
            //     this.canDel = false,
            //     this.canEnable = false,
            //     this.canProhibit = false
            // }else if(val.length == 1){
            //     this.canReset = true,
            //     this.canDel = true,
            //     this.canEdit = true
            //     if(val[0].status == 0){
            //         this.canEnable = false,
            //         this.canProhibit = true
            //     }else{
            //         this.canEnable = true,
            //         this.canProhibit = false
            //     }
            // }else if(val.length > 1){
            //     this.canReset = false,
            //     this.canEdit = false,
            //     this.canDel = true,
            //     this.canEnable = false,
            //     this.canProhibit = false
            // }
        },
        createItem(){
            this.type = 'add',
            this.addTitle = "新建用户",
            this.addDialogVisible = true;
            this.prohibit = false
            this.userForm = {
                "enterpriseName":"",
                "loginName":"",
                "password":"",
                "identification":"",
                "clusterIds":"",
                "userType":"",
                "email":"",
                "mobileNumber":"",
                "telNumber":"",
                "description":""
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑用户",
            this.userForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            console.log(this.userForm)
            // if(this.userForm.type == 2){
            //     this.userForm.type = '企业用户'
            // }
            this.prohibit = true
        },
        resetItem(){
            this.courseDialogFormVisible = true
            this.deleteTips = "您确定要重置用户密码吗？"
            this.type = 'reset'
        },
        deleteItem(){
            this.courseDialogFormVisible = true
            this.deleteTips = "您确定要删除选择的用户吗？"
            this.type = 'del'
        },
        enableItem(){
            this.courseDialogFormVisible = true
            this.deleteTips = "您确定要启用选择的用户吗？"
            this.type = 'enable'
        },
        prohibitItem(){
            this.courseDialogFormVisible = true
            this.deleteTips = "您确定要禁用选择的用户吗？"
            this.type = 'prohibit'
        },
        commitModel(){
            let data = {
                "enterpriseName":this.userForm.enterpriseName,
                "loginName":this.userForm.loginName,
                "email":this.userForm.email,
                "identification":this.userForm.identification,
                "clusterIds":this.userForm.clusterIds,
                "type":this.userForm.type,
                "mobileNumber":this.userForm.mobileNumber,
                "telNumber":this.userForm.telNumber,
                "description":this.userForm.description
            }
            console.log(data)
            // return
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        if(this.userForm.userPassword){
                            data.password = this.userForm.password
                        }else{
                            data.password = 'yidianwan'
                        }
                        createUser(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.userForm.id;
                        updateUser(data).then(() =>{
                            this.initData()
                        })
                    }
                    
                }
            })
            
        },
        confirm(){
            if(this.type == 'reset'){
                let obj =JSON.parse(JSON.stringify(this.multipleSelection[0]))
                let data = {
                    "id":obj.id,
                    'password':'yidianwan'
                }
                resetPassword(data).then(() =>{
                    this.courseDialogFormVisible = false;
                    this.initData()
                })
            }else if(this.type == 'del'){
                let ids = [];
                this.multipleSelection.map(item =>{
                    ids.push(item.id)
                })
                let data = ids
                deleteUser(data).then(() =>{
                    this.courseDialogFormVisible = false;
                    this.initData()
                })
            }else if(this.type == 'enable'){
                var id = this.multipleSelection.map(item=>{return item.id})
                var type = 0
                disableUser(id,type).then(() =>{
                    this.courseDialogFormVisible = false;
                    this.initData()
                })
            }else if(this.type == 'prohibit'){
                var id1 = this.multipleSelection.map(item=>{return item.id})
                var type1 = 1
                disableUser(id1,type1).then(() =>{
                    this.courseDialogFormVisible = false;
                    this.initData()
                })
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
            // let oData = {
            //     'pageNum': this.pageInstan.currentPage,
            //     'pageSize': this.pageInstan.pageSize,
            //     'enterpriseName':this.globalSearchTerm
            // }
            // getuserList(oData).then((res) =>{
            //     console.log(res);
            //     if(res&&res.data){
            //         this.dataList = res.data.records;
            //         if(this.dataList.length > 0){
            //             this.pageInstan.Total = res.data.total;
            //             this.pageInstan.showPage = true;//判断数据是否为空 为空则不显示分页
            //         }else{
            //             this.pageInstan.Total = res.data.total;
            //             this.pageInstan.showPage = false;//判断数据是否为空 为空则不显示分页
            //         }
            //     }
            // })
            this.initData()
        }
    },
    computed:{
        bth_check(){
            // 0启用
            // 1禁用
            if(this.multipleSelection.length ==1){
                if(this.multipleSelection[0].type == 1){
                    return {
                        canEdit:false,
                        canReset:false,
                        canDel:false,
                        canEnable:false,
                        canProhibit:false
                    }
                }else{
                    if(this.multipleSelection[0].schStatus){
                        return {
                            canEdit:true,
                            canReset:true,
                            canDel:true,
                            canEnable:false,
                            canProhibit:true
                        }
                    }else{
                        return {
                            canEdit:true,
                            canReset:true,
                            canDel:true,
                            canEnable:true,
                            canProhibit:false
                        } 
                    }
                }
            }else if(this.multipleSelection.length > 1){
                let status = this.multipleSelection.some((item) =>{
                    return item.type == 1;
                })
                let startNum = 0,
                    forbidNum = 0;
                this.multipleSelection.map((item) =>{
                    if(item.schStatus){
                        startNum++
                    }else{
                        forbidNum++
                    }
                })
                if(status){
                    return {
                        canEdit:false,
                        canReset:false,
                        canDel:false,
                        canEnable:false,
                        canProhibit:false
                    }
                }else{
                    if(startNum == this.multipleSelection.length){
                        return {
                            canDel:true,
                            canEdit:false,
                            canReset:true,
                            canEnable:false,
                            canProhibit:true,
                        }
                    }else if(forbidNum == this.multipleSelection.length){
                        return {
                            canDel:true,
                            canEdit:false,
                            canReset:true,
                            canEnable:true,
                            canProhibit:false,
                        }
                    }else{
                        return {
                            canDel:true,
                            canEdit:false,
                            canReset:true,
                            canEnable:false,
                            canProhibit:false,
                        }
                    }
                }
                
            }else{
                return {
                    canEdit:false,
                    canReset:false,
                    canDel:false,
                    canEnable:false,
                    canProhibit:false,
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
