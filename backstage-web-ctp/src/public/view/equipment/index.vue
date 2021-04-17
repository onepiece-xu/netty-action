<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新增</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!canDel">删除</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!canEdit">编辑</button>
            <button type="button" class="btn btn-info" @click="importItem">批量导入</button>
            <button type="button" class="btn btn-info" @click="bootUpItem" :disabled="!canBootUp">开机</button>
            <button type="button" class="btn btn-info" @click="shutdownItem" :disabled="!canShutdown">关机</button>
            <button type="button" class="btn btn-info" @click="shutdownItem" :disabled="!canShutdown">重启</button>
            <button type="button" class="btn btn-info" @click="shutdownItem" :disabled="!canShutdown">重置</button>
            <button type="button" class="btn btn-info" @click="appItem" :disabled="!canApp">应用管理</button>
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
                <el-table-column prop="name" label="设备ID" sortable>
                    
                </el-table-column>
                <el-table-column prop="name" label="设备名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.name'>{{scope.row.name}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="baseId" label="设备内网管理IP" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.baseId'>{{scope.row.baseId}}</span>
                    </template>
                 </el-table-column>
                <el-table-column prop="deviceCode" label="设备类型" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.deviceCode'>{{scope.row.deviceCode}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="clusterId" label="设备规格名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.clusterId'>{{scope.row.clusterId}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="idc" label="设备状态" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.idc'>{{scope.row.idc}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="cabinet" label="已用空间" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.cabinet'>{{scope.row.cabinet}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="location" label="可用空间" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.location'>{{scope.row.location}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="location" label="所属集群" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.location'>{{scope.row.location}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="location" label="所属设备组" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.location'>{{scope.row.location}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="detail" label="详情" >
                    <template slot-scope="scope">
                        <router-link v-bind:to="'/equipmentlist?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.name)">
                            <span>查看</span>
                        </router-link>
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
            <span>您确定要删除选择的设备吗？</span>
            <div slot="footer">
                <button class="btn btn-warning" type="button" :disabled="deleteItemDisabled" @click="confirm('del')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="deleteDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <el-dialog :title="courseTitle" :visible.sync="courseDialogFormVisible" center custom-class="normal-dialog"  v-if="courseDialogFormVisible" :close-on-click-modal="false">
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>设备ID：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.name" data-vv-as="设备ID" name="name" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>设备名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.name" data-vv-as="设备名称" name="name" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('name')" class="error">{{ errors.first('name') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>设备内网管理IP：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.baseId" data-vv-as="设备内网管理IP" name="baseId" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('baseId')" class="error">{{ errors.first('baseId') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>设备MAC：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.deviceCode" data-vv-as="设备MAC" name="deviceCode" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('deviceCode')" class="error">{{ errors.first('deviceCode') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>设备外网业务IP：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.clusterId" data-vv-as="设备外网业务IP" name="clusterId" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('clusterId')" class="error">{{ errors.first('clusterId') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>设备外网业务端口：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.idc" data-vv-as="设备外网业务端口" name="idc" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('idc')" class="error">{{ errors.first('idc') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>设备所属机房：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.cabinet" data-vv-as="设备所属机房" name="cabinet" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('cabinet')" class="error">{{ errors.first('cabinet') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>设备所属机架号：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.location" data-vv-as="设备所属机架号" name="location" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('location')" class="error">{{ errors.first('location') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>服务器机架槽位：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.slot" data-vv-as="服务器机架槽位" name="slot" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('slot')" class="error">{{ errors.first('slot') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>设备所在服务器内部槽位：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.groupId" data-vv-as="设备所在服务器内部槽位" name="groupId" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('groupId')" class="error">{{ errors.first('groupId') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>服务器管理IP：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.managerIp" data-vv-as="服务器管理IP" name="managerIp" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('managerIp')" class="error">{{ errors.first('managerIp') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>服务器管理端口：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.managerPort" data-vv-as="服务器管理端口" name="managerPort" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('managerPort')" class="error">{{ errors.first('managerPort') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>设备规格名称：</label>
                <div class="controls">
                    <el-select
                        v-model="equipmentForm.specs"
                        filterable
                        clearable
                        name="specs"
                        data-vv-as="设备规格"
                        v-validate="'required'"
                    >
                        <el-option
                            v-for="item in specsList"
                            :key="item.id"
                            :label="item.specs"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                    <div class="error">
                        <span v-show="errors.has('specs')" class="error">{{ errors.first('specs') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>所属集群：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.innerPort" data-vv-as="所属集群" name="innerPort" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('innerPort')" class="error">{{ errors.first('innerPort') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="red">*</span>所属设备组：</label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="equipmentForm.ip" data-vv-as="所属设备组" name="ip" v-validate="'required'">
                    <div class="error">
                        <span v-show="errors.has('ip')" class="error">{{ errors.first('ip') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">描述：</label>
                <div class="controls">
                    <textarea class="form-controls" name="description" v-model="equipmentForm.description"> </textarea>
                </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <button  class="btn btn-primary" @click="commitModel()">{{$t('cn.action.ok')}}</button >
                <button  @click="courseDialogFormVisible = false" class="btn btn-info">{{$t('cn.action.cancel')}}</button >
			</div>
		</el-dialog>
        <detail-page :props="hasRouteQuery">
            <equipment-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></equipment-detail>
        </detail-page>
    </div>
</template>

<script>
import {getDevicesList,createDevice,updateDevice,deleteDevices} from '@/public/api/equipment.js';
import detailPage from '@/public/common-tmpl/detail.vue' 
import equipmentDetail from './equipmentDetail.vue'
export default {
    data(){
        return {
            canDel:false,
            canEdit:false,
            canBootUp:false,
            canShutdown:false,
            canDel1:false,
            canEdit1:false,
            canApp:false,
            hasRouteQuery:0,
            multipleSelection: [],
            multipleSelection1: [],
            dataList:[],
            obj:[],
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
            courseTitle:"新增设备",
            courseDialogFormVisible:false,
            type:"add",
            specsList:[]
        }
    },
    components: {
        detailPage,equipmentDetail
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
                // 'name': 'wd5041701'
            }
            getDevicesList(oData).then((res) =>{
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
            this.type = "add"
            this.courseTitle = "新增设备",
            this.courseDialogFormVisible = true;
            this.equipmentForm = {
                "name":'',
                "baseId":'',
                "deviceCode":'',
                "clusterId":'',
                "idc":'',
                "cabinet":'',
                "location":'',
                "slot":'',
                "groupId":'',
                "managerIp":'',
                "managerPort":'',
                "innerIp":'',
                "innerPort":'',
                "ip":'',
                "port":'',
            };
        },
        editItem(){
            this.type = "edit"
            this.courseTitle = "编辑设备",
            this.equipmentForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            this.courseDialogFormVisible = true;
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        importItem(){

        },
        bootUpItem(){

        },
        shutdownItem(){

        },
        appItem(){
            
        },
        optionData(data){
            data.forEach(function(item){
                item.setupTime = item.setupTime?moment(item.setupTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.uninstallTime = item.uninstallTime?moment(item.uninstallTime).format("YYYY-MM-DD HH:mm:ss"):'';
                item.status = item.status == 1?'使用中':'终止';
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
                deleteDevices(data).then((res) =>{
                    console.log(res)
                    this.deleteDialogVisible = false;
                    this.initData()
                })
                break;
            }
        },
        commitModel(){
            let Odata = {
                "name":this.equipmentForm.name,
                "baseId":this.equipmentForm.baseId,
                "deviceCode":this.equipmentForm.deviceCode,
                "clusterId":this.equipmentForm.clusterId,
                "idc":this.equipmentForm.idc,
                "cabinet":this.equipmentForm.cabinet,
                "location":this.equipmentForm.location,
                "slot":this.equipmentForm.slot,
                "groupId":this.equipmentForm.groupId,
                "managerIp":this.equipmentForm.managerIp,
                "managerPort":this.equipmentForm.managerPort,
                "innerIp":this.equipmentForm.innerIp,
                "innerPort":this.equipmentForm.innerPort,
                "ip":this.equipmentForm.ip,
                "port":this.equipmentForm.port,
            }
            // 调取api
            this.$validator.validateAll().then((result)=>{
                if(result){
                    this.courseDialogFormVisible = false;
                    if(this.type == 'add'){
                        createDevice(Odata).then(() =>{
                            this.initData()
                        })
                    }else{
                        Odata.id = this.equipmentForm.id;
                        updateDevice(Odata).then(() =>{
                            this.initData()
                        })
                    }
                }
            })
        },
        commitModel1(){
            let oData = {
                'appId':this.appForm.appId
            }
            oData.deviceId = this.obj.id
            // 调取api
            this.$validator.validateAll().then((result)=>{
                if(result){
                    this.appcourseDialogFormVisible = false;
                    if(this.type == 'add'){
                        createApp(oData).then(() =>{
                            this.appItem()
                        })
                    }else{
                        oData.id = this.appForm.id;
                        updateApp(oData).then(() =>{
                            this.appItem()
                        })
                    }
                }
            })
        },
        //刷新
        refresh(){
            this.globalSearchTerm = ''
            this.pageInstan.pageSize = 50
            this.pageInstan.currentPage = 1
            this.initData()
        },
        // 搜索
        applyGlobalSearch(){
            this.initData()
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            if(val.length == 0){
                this.canDel = false,
                this.canEdit = false,
                this.canBootUp = false,
                this.canShutdown = false,
                this.canApp = false
            }else if(val.length == 1){
                this.canDel = true,
                this.canEdit = true,
                this.canBootUp = true,
                this.canShutdown = true,
                this.canApp = true
            }else if(val.length > 1){
                this.canDel = true,
                this.canEdit = false,
                this.canBootUp = true,
                this.canShutdown = true,
                this.canApp = false
            }
        },
        handleSelectionChange1(val) {
            this.multipleSelection1 = val;
            if(val.length == 0){
                this.canDel1 = false,
                this.canEdit1 = false
            }else if(val.length == 1){
                this.canDel1 = true,
                this.canEdit1 = true
            }else if(val.length > 1){
                this.canDel1 = true,
                this.canEdit1 = false
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
        this.hasRouteQuery = this.$route.query.id?1:0;
    }
}
</script>
