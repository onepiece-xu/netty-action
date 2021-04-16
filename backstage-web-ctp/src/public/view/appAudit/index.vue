<template>
    <div class="page-inner">
        <div class="table-action">
            <!-- <button type="button" class="btn btn-info" @click="approvalItem" :disabled="!bth_check.canApproval">审批</button> -->
            <button type="button" class="btn btn-info" @click="shelfItem" :disabled="!bth_check.canShelf">上架</button>
            <div class="pull-right">
                <div class="search-box">
                    <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                        <button class="btn" type="submit" :disabled="false">
                            <i class="icon-aw-search"></i>
                        </button>
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入应用名称" name="searchTerm" v-model="globalSearchTerm"/>
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
                <el-table-column prop="name" label="应用名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.name'>{{scope.row.name}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="gameMaker" label="游戏发行公司" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.gameMaker'>{{scope.row.gameMaker}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="" label="合作类型" column-key="cooperationType" :filter-multiple="false" :filters="[{text: '游戏运营', value: 1},{text: '游戏广告', value: 2}]">
                    <template slot-scope="scope">
                        <span :title='scope.row.cooperationType1'>{{scope.row.cooperationType1}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" column-key="status" :filter-multiple="false" :filters="[{text: '审批中', value: 1},{text: '审批不通过', value: 2},{text: '审批通过', value: 3},{text: '已上架', value: 4}]">
                    <template slot-scope="scope">
                        <span :title='scope.row.status1' :class="scope.row.status == 2?'bg-red':'bg-green'">{{scope.row.status1}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="accessId" label="游戏版号" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.accessId'>{{scope.row.accessId}}</span>
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
            :visible.sync="approvalDialogVisible"
            title="审批"
            custom-class="big-dialog"
            center
            v-if="approvalDialogVisible"
            :close-on-click-modal="false">
            <div class="detail-info">
                <table class='table-dex' >
                    <tr>
                        <td>应用名称</td>
                        <td>{{detailInfo.name}}</td>
                        <td>应用英文名称</td>
                        <td>{{detailInfo.englishName}}</td>
                    </tr>
                    <tr>
                        <td>游戏制作公司</td>
                        <td>{{detailInfo.gameMaker}}</td>
                        <td>游戏发行公司</td>
                        <td>{{detailInfo.gamePublisher}}</td>
                    </tr>
                    <tr>
                        <td>游戏类型</td>
                        <td>{{gameType1}}</td>
                        <td>合作类型</td>
                        <td>{{detailInfo.cooperationType}}</td>
                    </tr>
                    <tr>
                        <td>是否有版号</td>
                        <td>{{detailInfo.hasAccessId}}</td>
                        <td>游戏版号</td>
                        <td>{{detailInfo.accessId}}</td>
                        <!-- <td>游戏版号图片</td>
                        <td>{{detailInfo.accessId}}</td> -->
                    </tr>
                    <tr>
                        <td>游戏版号图片</td>
                        <td><a :href="imgUrl" target="_blank" v-show="!imgShow">查看图片</a></td>
                        <td>应用安装文件名</td>
                        <td>{{detailInfo.packageFileName}}</td>
                    </tr>
                    <tr>
                        <td>包名</td>
                        <td>{{detailInfo.packageName}}</td>
                        <td>安装包大小（MB）</td>
                        <td>{{detailInfo.size}}</td>
                    </tr>
                    <tr>
                        <td>安装包大小（MB）</td>
                        <td>{{detailInfo.realSize}}</td>
                        <td>应用类型</td>
                        <td>{{detailInfo.type}}</td>
                    </tr>
                    <tr>
                        <td>账号数据存放路径</td>
                        <td>{{detailInfo.accountPath}}</td>
                        <td>存档数据存放路径</td>
                        <td>{{detailInfo.dataPath}}</td>
                    </tr>
                    <tr>
                        <td>安装包获取路径信息</td>
                        <td>{{detailInfo.packagePath}}</td>
                        <td>应用描述</td>
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
                <button class="btn btn-primary" type="button" @click="commitModel('approval')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="approvalDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 上架 -->
        <el-dialog
            :visible.sync="addDialogVisible"
            title="上架"
            custom-class="normal-dialog"
            center
            v-if="addDialogVisible"
            :close-on-click-modal="false">
            <!-- <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>应用ID：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.id" data-vv-as="应用名称" name="id" v-validate="'required'" :disabled="prohibit">
                    <div class="error">
                        <span v-show="errors.has('id')" class="error">{{ errors.first('id') }}</span>
                    </div>
                </div>
            </div> -->
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>所属企业：
                </label>
                <div class="controls">
                    <el-select
                        v-model="userAppForm.identification"
                        filterable
                        clearable
                        name="identification"
                        data-vv-as="所属企业"
                        v-validate="'required'"
                    >
                        <el-option
                            v-for="item in userList"
                            :key="item.identification"
                            :label="item.enterpriseName"
                            :value="item.identification"
                        >
                        </el-option>
                    </el-select>
                    <div class="error">
                        <span v-show="errors.has('identification')" class="error">{{ errors.first('identification') }}</span>
                    </div>
                </div>
            </div>
            
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>应用名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.name" data-vv-as="应用名称" name="name" v-validate="'required'" placeholder="请填写应用名称">
                    <div class="error">
                        <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>应用许可证号(版号)：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.accessId" data-vv-as="应用许可证号" name="accessId" v-validate="'required'" placeholder="请填写应用许可证号">
                    <div class="error">
                        <span v-show="errors.has('accessId')" class="error">{{ errors.first('accessId') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>包名：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.packageName" data-vv-as="包名" name="packageName" v-validate="'required'" placeholder="请填写包名,例如：com.tencent.tmgp.sgame">
                    <div class="error">
                        <span v-show="errors.has('packageName')" class="error">{{ errors.first('packageName') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>安装文件名：
                    <!-- <el-tooltip placement="top">
                        <div slot="content">arm应用：填写安装文件名称<br/>x86应用：填写执行文件名称</div>
                        <i class="el-icon-info"></i>
                    </el-tooltip> -->
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.packageFileName" data-vv-as="安装文件名" name="packageFileName" v-validate="'required'" placeholder="x86应用填写安装后的可执行文件名称">
                    <div class="error">
                        <span v-show="errors.has('packageFileName')" class="error">{{ errors.first('packageFileName') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>安装文件路径：
                    <!-- <el-tooltip placement="top">
                        <div slot="content">arm应用：填写安装文件路径<br/>x86应用：填写执行文件路径</div>
                        <i class="el-icon-info"></i>
                    </el-tooltip> -->
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.path" data-vv-as="安装文件路径"  name="path" v-validate="'required'" placeholder="x86应用填写安装后的可执行文件路径">
                    <div class="error">
                        <span v-show="errors.has('path')" class="error">{{ errors.first('path') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">安装包大小(MB)：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.size" name="size" placeholder="x86应用不涉及">
                    <!-- <div class="error">
                        <span v-show="errors.has('size')" class="error">{{ errors.first('size') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">安装后大小(MB)：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.realSize" name="realSize" placeholder="x86应用不涉及">
                    <!-- <div class="error">
                        <span v-show="errors.has('realSize')" class="error">{{ errors.first('realSize') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>应用类型：
                </label>
                <div class="controls">
                    <el-radio-group v-model="userAppForm.type">
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
                    <span class="red">*</span>流策略名称：
                </label>
                <div class="controls">
                    <el-select
                        v-model="userAppForm.strategyId"
                        filterable
                        clearable
                        name="strategyId"
                        data-vv-as="流策略名称"
                        v-validate="'required'"
                    >
                        <el-option
                            v-for="item in strategyList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                    <div class="error">
                        <span v-show="errors.has('strategyId')" class="error">{{ errors.first('strategyId') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">是否自动安装：</label>
                <div class="controls">
                    <el-radio-group v-model="userAppForm.schInstall">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="2">否</el-radio>
                    </el-radio-group>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">最大安装数：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.installMaxNumber" name="installMaxNumber" placeholder="输入0为没有上限">
                    <!-- <div class="error">
                        <span v-show="errors.has('savePath')" class="error">{{ errors.first('savePath') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">账号数据路径：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.savePath" name="savePath">
                    <!-- <div class="error">
                        <span v-show="errors.has('savePath')" class="error">{{ errors.first('savePath') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">存档数据路径：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.accountPath" name="accountPath">
                    <!-- <div class="error">
                        <span v-show="errors.has('accountPath')" class="error">{{ errors.first('saveaccountPathPath') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">预启动脚本路径：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.startShell" name="startShell">
                    <!-- <div class="error">
                        <span v-show="errors.has('startShell')" class="error">{{ errors.first('startShell') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">结束脚本路径：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.closeShell" name="closeShell">
                    <!-- <div class="error">
                        <span v-show="errors.has('closeShell')" class="error">{{ errors.first('closeShell') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">应用描述：</label>
                <div class="controls">
                    <textarea class="form-controls" name="description" v-model="userAppForm.description"> </textarea>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="commitModel('shelf')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {getAppApproveList,getAppApprove,approveApp,getGameType,launchApps} from '@/public/api/approval.js';
import {getuserList} from '@/public/api/userManager.js';
import {getStrategyList} from '@/public/api/streamService.js';
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
            approvalDialogVisible:false,
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
            imgShow:false,
            gameType1:'',
            addDialogVisible:false,
            userAppForm:{},
            userList:[],
            strategyList:[],
            cooperationType:'',
            status:''
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
            if(filters.status){
                this.status = filters.status[0]
            }
            if(filters.cooperationType){
                this.cooperationType = filters.cooperationType[0]
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
            if(this.status){
                oData.status = this.status
            }
            if(this.cooperationType){
                oData.cooperationType = this.cooperationType
            }
            getAppApproveList(oData).then((res) =>{
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
                if(item.cooperationType == 1){
                    item.cooperationType1 = '游戏运营'
                }else if(item.cooperationType == 2){
                    item.cooperationType1 = '游戏广告'
                }
                if(item.status == 0){
                    item.status1 = '待提交'
                }else if(item.status == 1){
                    item.status1 = '审批中'
                }else if(item.status == 2){
                    item.status1 = '审批不通过'
                }else if(item.status == 3){
                    item.status1 = '审批通过'
                }else if(item.status == 4){
                    item.status1 = '已上架'
                }
            })
        },
        approvalItem(){
            this.userForm = {}
            this.approvalDialogVisible = true
            var id = this.multipleSelection.map(item=>{return item.id})
            getAppApprove(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                    if(res.data.accessIdPic){
                        this.imgUrl = window.GLOBALCONFIG.APIHOST.IMG + res.data.accessIdPic
                        this.imgShow = false
                    }else{
                        this.imgShow = true
                    }
                    this.detailInfo.gameType = (this.detailInfo.gameType.split(','))
                    getGameType().then((res) =>{
                        if(res&&res.data){
                            let arr = res.data;
                            let grade = arr.filter((item1) => {
                                return this.detailInfo.gameType.find((item2) => {
                                    return item2 == item1.id
                                })
                            })
                            console.log(grade)
                            let obj = []
                            grade.map((item) => {
                                obj.push(item.name)
                            });
                            this.gameType1 = obj.toString()
                        }
                    })
                    this.optionData1([this.detailInfo])
                }
            })
        },
        optionData1(data){
            data.forEach(function(item){
                if(item.cooperationType == 1){
                    item.cooperationType = '游戏运营'
                }else if(item.cooperationType == 2){
                    item.cooperationType = '游戏广告'
                }
                if(item.type == 1){
                    item.type = 'arm'
                }else if(item.type == 2){
                    item.type = 'x86'
                }
                if(item.hasAccessId == 1){
                    item.hasAccessId = '有'
                }else if(item.hasAccessId == 2){
                    item.hasAccessId = '无'
                }else if(item.hasAccessId == 3){
                    item.hasAccessId = '审批中'
                }
                if(item.status == 0){
                    item.status1 = '待提交'
                }else if(item.status == 1){
                    item.status1 = '审核中'
                }else if(item.status == 2){
                    item.status1 = '审批不通过'
                }else if(item.status == 3){
                    item.status1 = '审核通过'
                }else if(item.status == 4){
                    item.status1 = '已上架'
                }
            })
        },
        shelfItem(){
            this.addDialogVisible = true
            this.userAppForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            if(this.userAppForm.schInstall){
                this.userAppForm.schInstall = 1
            }else{
                this.userAppForm.schInstall = 2
            }
            getuserList().then((res) =>{
                console.log(res);
                this.userList = res.data.records
            })
            getStrategyList().then((res) =>{
                console.log(res);
                this.strategyList = res.data.records
            })
        },
        commitModel(type){
            switch (type) {
            case 'approval':
                let oData ={
                    'id':JSON.parse(JSON.stringify(this.multipleSelection[0].id)),
                    'result':this.userForm.result,
                    'type':this.userForm.type
                }
                this.$validator.validateAll().then((result)=>{
                    if (result) {
                        approveApp(oData).then(() =>{
                            this.approvalDialogVisible = false;
                            this.initData()
                        })
                    }
                })
                break;
            case 'shelf':
                let data = {
                    "id":this.userAppForm.id,
                    "name":this.userAppForm.name,
                    "accessId":this.userAppForm.accessId,
                    "packageName":this.userAppForm.packageName,
                    "packageFileName":this.userAppForm.packageFileName,
                    "size":this.userAppForm.size,
                    "identification":this.userAppForm.identification,
                    "path":this.userAppForm.path,
                    "realSize":this.userAppForm.realSize,
                    "type":this.userAppForm.type,
                    "strategyId":this.userAppForm.strategyId,
                    "savePath":this.userAppForm.savePath,
                    "accountPath":this.userAppForm.accountPath,
                    "startShell":this.userAppForm.startShell,
                    "closeShell":this.userAppForm.closeShell,
                    "description":this.userAppForm.description
                }
                data.schInstall = this.userAppForm.schInstall
                data.installMaxNumber = this.userAppForm.installMaxNumber
                console.log(data)
                // return
                this.$validator.validateAll().then((result)=>{
                    if (result) {
                        this.addDialogVisible = false;
                        launchApps(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }
                })
                break;
            }
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
            if(this.multipleSelection.length == 1){
                if(this.multipleSelection[0].status == 1){
                    return {
                        canApproval:true,
                        canShelf:false
                    }
                }else if(this.multipleSelection[0].status == 3){
                    return {
                        canApproval:false,
                        canShelf:true
                    }
                }else{
                    return {
                        canApproval:false,
                        canShelf:false
                    }
                }
            }else if(this.multipleSelection.length > 1){
                return {
                    canApproval:false,
                    canShelf:false
                }
            }else{
                return {
                    canApproval:false,
                    canShelf:false
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
