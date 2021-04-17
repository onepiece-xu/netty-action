<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新增</button>
            <button type="button" class="btn btn-primary" @click="importItem">批量导入</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除</button>
            <div class="pull-right">
                <div class="search-box">
                    <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                        <button class="btn" type="submit" :disabled="false">
                            <i class="icon-aw-search"></i>
                        </button>
                        <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入文件名" name="searchTerm" v-model="globalSearchTerm"/>
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
                <el-table-column prop="name" label="文件名" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.name'>{{scope.row.name}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="size" label="文件大小(MB)" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.size'>{{scope.row.size}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="fileServerPath" label="文件服务器存放路径" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.fileServerPath'>{{scope.row.fileServerPath}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="fileClientPath" label="文件客户机存放路径" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.fileClientPath'>{{scope.row.fileClientPath}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="enterpriseName" label="所属企业用户" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.createTime'>{{scope.row.createTime}}</span>
                    </template> 
                </el-table-column>
            </el-table>
            <div v-if="pageInstan.showPage">
                <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
            </div>
        </div>
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
                    <span class="red">*</span>文件名：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="fileForm.name" data-vv-as="文件名" name="name" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>所属企业用户：
                </label>
                <div class="controls">
                    <el-select
                        v-model="fileForm.identification"
                        filterable
                        clearable
                        name="identification"
                        data-vv-as="所属企业用户"
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
                    <span class="red">*</span>文件大小(MB)：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="fileForm.size" data-vv-as="文件大小（M）" name="size" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('size')" class="error">{{ errors.first('size') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>文件服务器存放路径：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="fileForm.fileServerPath" data-vv-as="文件服务器存放路径" name="size" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('fileServerPath')" class="error">{{ errors.first('fileServerPath') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>文件客户机存放路径：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="fileForm.fileClientPath" data-vv-as="文件客户机存放路径" name="fileClientPath" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('fileClientPath')" class="error">{{ errors.first('fileClientPath') }}</span>
                    </div>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="commitModel()">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 删除 -->
        <el-dialog
            :visible.sync="deleteDialogVisible"
            custom-class="small-dialog delete-dialog"
            center
            :close-on-click-modal="false">
            <span>您确定要删除选择的文件吗？</span>
            <div slot="footer">
                <button class="btn btn-warning" type="button" :disabled="deleteItemDisabled" @click="confirm('del')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="deleteDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 批量导入 -->
        <el-dialog
            :visible.sync="importDialogVisible"
            title="批量导入文件信息"
            custom-class="normal-dialog"
            center
            v-if="importDialogVisible"
            :close-on-click-modal="false">
            <div class="authSetting">
                <div class="control-group">
                    <label class="control-label">模板下载：</label>
                    <div class="controls">
                        <a href="#">批量导入文件信息模板.xlsx</a>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>上传文件：</label>
                    <div class="controls">
                        <div class="file-box">
                            <input class="form-controls" data-vv-as="上传文件" v-validate="'required'" name="upload" disabled
                            v-model="formData.name"/>
                            <input type="file" @change="selectFile($event)" ref="clearFile"/>
                            <button type="button" class="choose">
                                选择文件
                            </button>
                            <div class="error">
                                <span v-show="error" class="help is-danger">文件格式不正确</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="confirm('import')" :disabled='!importItemDisabled'>{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="importDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {getFileList,createFile,updateFile,deleteFile,fileUpload} from '@/public/api/file.js';
import {getuserList} from '@/public/api/userManager.js';
export default {
    data(){
        return {
            canDel:false,
            canEdit:false,
            hasRouteQuery:0,
            multipleSelection: [],
            editData:{
                opentype:'',
                optionWrap:false,
                content:this.multipleSelection
            },
            dataList:[],
            deleteDialogVisible:false,
            deleteItemDisabled:false,
            // page
            globalSearchTerm:'',
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            importItemDisabled:false,
            importDialogVisible:false,
            fileList:[],
            addDialogVisible:false,
            userList:[],
            formData:{},
            error:false,
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
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            getFileList(oData).then((res) =>{
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
            getuserList().then((res) =>{
                console.log(res);
                this.userList = res.data.records
            })
        },
        optionData(data){
            data.forEach(function(item){
                item.createTime = item.createTime?moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"):'';
            })
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            if(val.length == 0){
                this.canEdit = false
                this.canDel = false
            }else if(val.length == 1){
                this.canEdit = true
                this.canDel = true
            }else if(val.length > 1){
                this.canEdit = false
                this.canDel = true
            }
        },
        createItem(){
            this.type = 'add',
            this.addTitle = "新建文件",
            this.addDialogVisible = true;
            this.fileForm = {
                "name":"",
                "size":"",
                "fileServerPath":"",
                "fileClientPath":""
            };
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑应用",
            this.fileForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
        },
        importItem(){
            this.importDialogVisible = true
            this.formData = {}
            this.error = false
            this.importItemDisabled = false
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        commitModel(){
            let data = {
                "name":this.fileForm.name,
                "size":this.fileForm.size,
                "fileServerPath":this.fileForm.fileServerPath,
                "fileClientPath":this.fileForm.fileClientPath,
                "identification":this.fileForm.identification
            }
            console.log(data)
            // return
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        createFile(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.fileForm.id;
                        updateFile(data).then(() =>{
                            this.initData()
                        })
                    }
                    
                }
            })
        },
        selectFile(e){
            console.log(e)
            let file = e.target.files[0];
            if (file && ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"].indexOf(file.type) > -1) {
                this.file = e.target.files[0];
                this.formData= {
                    name:this.file.name
                };
                this.error = false
                this.importItemDisabled = true
            }else{
                this.$refs.clearFile.value = '';
                this.file = null;
                this.formData= {
                    name:""
                };
                this.error = true
                this.importItemDisabled = false
            }
        },
        confirm(type){
            switch (type) {
            case'del':
                let ids = [];
                this.multipleSelection.map(item =>{
                    ids.push(item.id)
                })
                let data = ids
                deleteFile(data).then((res) =>{
                    console.log(res)
                    this.deleteDialogVisible = false;
                    this.initData()
                })
                break;
            case 'import':
                let data3 = new FormData();
                data3.append('file', this.file);
                this.$validator.validateAll().then((result)=>{
                    if(result){
                        fileUpload(data3).then(res =>{
                            console.log(res);
                            if(res&&res.code == 200){
                                this.$refs.clearFile.value = '';
                                this.file = null;
                                this.formData= {
                                    name:""
                                };
                                this.importDialogVisible = false;
                                this.initData()
                            }
                        })
                    }
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
