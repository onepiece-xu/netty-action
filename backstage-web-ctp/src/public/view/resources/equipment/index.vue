<template>
    <div>
        <div class="path-title">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <!-- <el-breadcrumb-item>资源管理</el-breadcrumb-item> -->
                <!-- <el-breadcrumb-item :to="{ path: '/node' }">集群管理</el-breadcrumb-item> -->
                <el-breadcrumb-item><a  @click="$router.go(-1)">{{$route.query.status == 2?'组管理':'集群管理'}}</a></el-breadcrumb-item>
                <el-breadcrumb-item>设备管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="page-inner">
            <div class="table-action query">
                <button type="button" class="btn btn-primary" @click="createItem">新增</button>
                <button type="button" class="btn btn-primary" @click="importItem">批量导入</button>
                <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!bth_check.canDel">删除</button>
                <button type="button" class="btn btn-info" @click="editItem" :disabled="!bth_check.canEdit">编辑</button>
                <!-- <button type="button" class="btn btn-info" @click="bootUpItem" :disabled="!bth_check.canBootUp">开机</button>
                <button type="button" class="btn btn-info" @click="shutdownItem" :disabled="!bth_check.canShutdown">关机</button> -->
                <button type="button" class="btn btn-info" @click="restartItem" :disabled="!bth_check.canRestart">重启</button>
                <button type="button" class="btn btn-info" @click="enableItem" :disabled="!bth_check.canEnable">启用</button>
                <button type="button" class="btn btn-info" @click="prohibitItem" :disabled="!bth_check.canProhibit">禁用</button>
                <!-- <button type="button" class="btn btn-info" @click="shutdownItem" :disabled="!bth_check.canShutdown">重置</button> -->
                <button type="button" class="btn btn-info" @click="appItem" :disabled="!bth_check.canApp">应用管理</button>
                <button type="button" class="btn btn-info" @click="releaseItem" :disabled="!bth_check.canRelease">释放</button>
                <button type="button" class="btn btn-info" @click="registerItem" :disabled="!bth_check.canRegister">注册</button>
                <div class="pull-right">
                    <div class="search-box">
                        <form name="searchForm" novalidate @submit.prevent="applyGlobalSearch()">
                            <button class="btn" type="submit" :disabled="false">
                                <i class="icon-aw-search"></i>
                            </button>
                            <input type="text" class="form-control" autocomplete="off" maxlength="128" placeholder="请输入设备名称/IP" name="searchTerm" v-model="globalSearchTerm"/>
                        </form>
                    </div>
                    <button class="btn btn-renovat btn-search" v-show="!open" @click="openItem()">
                        <i class="icon-aw-gear1"></i>
                    </button>
                    <button class="btn btn-renovat btn-search" v-show="open" @click="retractItem()">
                        <i class="icon-aw-angle-up"></i>
                    </button>
                    <el-tooltip class="item" effect="dark" :content="$t('cn.tabelTips.refreshList')" placement="top">
                        <button class="btn btn-renovat btn-search" @click="refresh()">
                            <i class="icon-aw-refresh"></i>
                        </button>
                    </el-tooltip>
                </div>
                <div class="search" v-show="open">
                    <div class="control-group">
                        <label class="control-label">设备ID：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.id" name="id">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备名称：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.name" name="name">
                        </div>
                    </div>
                    <div class="control-group" v-show="$route.query.status == 2">
                        <label class="control-label">所属集群：</label>
                        <div class="controls">
                            <el-select
                                v-model="queryForm.clusterName"
                                filterable
                                clearable
                                name="clusterName"
                            >
                                <el-option
                                    v-for="item in clusterList"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.name"
                                >
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备MAC：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.innerMac" name="innerMac">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备内网管理IP：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.innerIp" name="innerIp">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备内网管理端口：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.innerPort" name="innerPort">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备外网业务IP：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.ip" name="ip">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备外网业务端口：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.port" name="port">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备所属机房：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.idc" name="idc">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备所属机架号：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.cabinet" name="cabinet">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">服务器机架槽位：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.location" name="location">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">设备所在服务器内部槽位：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.slot" name="slot">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">服务器管理IP：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.managerIp" name="managerIp">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">服务器管理端口：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.managerPort" name="managerPort">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">描述：</label>
                        <div class="controls">
                            <input type="text" class="form-control" v-model="queryForm.description" name="description">
                        </div>
                    </div>
                </div>
                <div class="btn-search" v-show="open">
                    <button type="button" class="btn btn-primary" @click="searchItem">搜索</button>
                    <button type="button" class="btn btn-info" @click="emptyItem">清空</button>
                </div>
            </div>
            <div class="table-content">
                <el-table ref="multipleTable" 
                    :data="dataList" 
                    @filter-change="handleFilterChange"
                    @selection-change="handleSelectionChange" >
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="id" label="设备ID" width="180px">
                        
                    </el-table-column>
                    <el-table-column prop="name" label="设备名称" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.name'>{{scope.row.name}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="innerIp" label="管理IP" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.innerIp'>{{scope.row.innerIp}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="baseType" label="设备类型" column-key="baseType" :filter-multiple="false" :filters="[{text: 'arm', value: 1},{text: 'x86', value: 2}]">
                        <template slot-scope="scope">
                            <span :title='scope.row.baseType'>{{scope.row.baseType}}</span>
                        </template> 
                    </el-table-column>
                    <!-- <el-table-column prop="baseName" label="设备规格名称">
                        <template slot-scope="scope">
                            <span :title='scope.row.baseName'>{{scope.row.baseName}}</span>
                        </template> 
                    </el-table-column> -->
                    <el-table-column prop="status" label="设备运行状态" column-key="status" :filter-multiple="false" :filters="[{text: '空闲', value: 1},{text: '使用中', value: 2},{text: '错误', value: 3},{text: '安装中', value: 4},{text: '重启中', value: 5}]">
                        <template slot-scope="scope">
                            <span :title='scope.row.deviceStatus' :class="{'bg-green':scope.row.status == 1,'bg-blue':scope.row.status != 1&&scope.row.status != 3,'bg-red':scope.row.status == 3}">{{scope.row.deviceStatus}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="schStatus" label="设备启用状态" column-key="schStatus" :filter-multiple="false" :filters="[{text: '正常', value: 1},{text: '禁用', value: 2}]">
                        <template slot-scope="scope">
                            <span :title='scope.row.deviceschStatus' :class="scope.row.schStatus?'bg-green':'bg-red'">{{scope.row.deviceschStatus}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="usedSpace" label="已用空间（MB）" width="150" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.usedSpace'>{{scope.row.usedSpace}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="freeSpace" label="可用空间（MB）" width="150" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.freeSpace'>{{scope.row.freeSpace}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="clusterName" label="所属集群" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.clusterName'>{{scope.row.clusterName}}</span>
                        </template> 
                    </el-table-column>
                    <el-table-column prop="init" label="注册状态" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.deviceInit' :class="scope.row.init?'bg-green':'bg-red'">{{scope.row.deviceInit}}</span>
                        </template> 
                    </el-table-column>
                    <!-- <el-table-column prop="groupName" label="所属设备组" sortable>
                        <template slot-scope="scope">
                            <span :title='scope.row.groupName'>{{scope.row.groupName}}</span>
                        </template> 
                    </el-table-column> -->
                    <el-table-column prop="detail" label="详情" >
                        <template slot-scope="scope">
                            <router-link v-bind:to="'/equipmentlist?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.name) + '&status='+ $route.query.status">
                                <span>查看</span>
                            </router-link>
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
                <span>您确定要删除选择的设备吗？</span>
                <div slot="footer">
                    <button class="btn btn-warning" type="button" :disabled="deleteItemDisabled" @click="confirm('del')">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="deleteDialogVisible = false">{{$t('cn.action.cancel')}}</button>
                </div>
            </el-dialog>
            <el-dialog
                :visible.sync="operationDialogVisible"
                custom-class="small-dialog delete-dialog"
                center
                :close-on-click-modal="false">
                <span>{{operationTitle}}</span>
                <div slot="footer">
                    <button class="btn btn-warning" type="button" :disabled="operationItemDisabled" @click="operation()">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="operationDialogVisible = false">{{$t('cn.action.cancel')}}</button>
                </div>
            </el-dialog>
            <!-- 新建,编辑 -->
            <el-dialog :title="courseTitle" :visible.sync="courseDialogFormVisible" center custom-class="normal-dialog"  v-if="courseDialogFormVisible" :close-on-click-modal="false">
                <!-- <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>设备ID：
                    </label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.id" data-vv-as="设备ID" name="id" v-validate="'required'" :disabled="prohibit">
                        <div class="error">
                            <span v-show="errors.has('id')" class="error">{{ errors.first('id') }}</span>
                        </div>
                    </div>
                </div> -->
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
                    <label class="control-label"><span class="red">*</span>管理IP：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.innerIp" data-vv-as="管理IP" name="innerIp" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('innerIp')" class="error">{{ errors.first('innerIp') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>管理端口：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.innerPort" data-vv-as="管理端口" name="innerPort" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('innerPort')" class="error">{{ errors.first('innerPort') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>设备MAC：
                    </label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.innerMac" data-vv-as="设备MAC" name="innerMac" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('innerMac')" class="error">{{ errors.first('innerMac') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>外网IP：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.ip" data-vv-as="外网IP" name="ip" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('ip')" class="error">{{ errors.first('ip') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>外网端口：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.port" data-vv-as="外网端口" name="port" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('port')" class="error">{{ errors.first('port') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>所属机房：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.idc" data-vv-as="所属机房" name="idc" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('idc')" class="error">{{ errors.first('idc') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>所属机架号：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.cabinet" data-vv-as="所属机架号" name="cabinet" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('cabinet')" class="error">{{ errors.first('cabinet') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>服务器机架槽位：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.location" data-vv-as="服务器机架槽位" name="location" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('location')" class="error">{{ errors.first('location') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>服务器内部槽位：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.slot" data-vv-as="服务器内部槽位" name="slot" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('slot')" class="error">{{ errors.first('slot') }}</span>
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
                    <label class="control-label"><span class="red">*</span>adb连接IP：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.adbIp" data-vv-as="adb连接IP" name="adbIp" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('adbIp')" class="error">{{ errors.first('adbIp') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>adb连接端口：</label>
                    <div class="controls">
                        <input type="text" class="form-control" v-model="equipmentForm.adbPort" data-vv-as="adb连接端口" name="adbPort" v-validate="'required'">
                        <div class="error">
                            <span v-show="errors.has('adbPort')" class="error">{{ errors.first('adbPort') }}</span>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span class="red">*</span>设备规格：</label>
                    <div class="controls">
                        <el-select
                            v-model="equipmentForm.baseId"
                            filterable
                            clearable
                            name="baseId"
                            data-vv-as="设备规格"
                            v-validate="'required'"
                        >
                            <el-option
                                v-for="item in specsList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            >
                            </el-option>
                        </el-select>
                        <div class="error">
                            <span v-show="errors.has('baseId')" class="error">{{ errors.first('baseId') }}</span>
                        </div>
                    </div>
                </div>
                <!-- <div class="control-group">
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
                </div> -->
                <div class="control-group">
                    <label class="control-label">设备描述：</label>
                    <div class="controls">
                        <textarea class="form-controls" name="description" v-model="equipmentForm.description"> </textarea>
                    </div>
                </div>
                <div slot="footer" class="dialog-footer">
                    <button  class="btn btn-primary" @click="commitModel()">{{$t('cn.action.ok')}}</button >
                    <button  @click="courseDialogFormVisible = false" class="btn btn-info">{{$t('cn.action.cancel')}}</button >
                </div>
            </el-dialog>
            <!-- 应用管理 -->
            <el-dialog title="应用管理" :visible.sync="appDialogFormVisible" center custom-class="normal-dialog" class="appManage"  v-if="appDialogFormVisible" :close-on-click-modal="false">
                <p class="app-manager">已安装应用</p>
                <div class="table-content">
                    <el-table ref="multipleTable" 
                        :data="dataList1"
                        height="195" >
                        <el-table-column prop="id" label="应用ID" width="180px">
                            <template slot-scope="scope">
                                <span :title="scope.row.id">{{scope.row.id}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="应用名称">
                            <template slot-scope="scope">
                                <span :title='scope.row.name'>{{scope.row.name}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="size" label="安装大小(MB)">
                            <template slot-scope="scope">
                                <span :title='scope.row.size'>{{scope.row.size}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="enterpriseName" label="所属用户">
                            <template slot-scope="scope">
                                <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="" label="" >
                            <template slot-scope="scope">
                                <button type="button" class="btn btn-text uninstall" @click="uninstall(scope.row.id)" :disabled="uninstall1">卸载</button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div v-if="pageInstan1.showPage">
                        <page-query v-if="pageInstan1.showPage" :currentPage="pageInstan1.currentPage" :total="pageInstan1.Total" :pageSize="pageInstan1.pageSize" @handleSizeChangeSub="handleSizeChange1" @handleCurrentChangeSub="handleCurrentChange1"></page-query>
                    </div>
                </div>
                <p class="app-manager1">可安装应用</p>
                <div class="table-content">
                    <el-table ref="multipleTable" 
                        :data="dataList2" 
                        height="195">
                        <el-table-column prop="id" label="应用ID" width="180px">
                            <template slot-scope="scope">
                                <span :title="scope.row.id">{{scope.row.id}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="应用名称">
                            <template slot-scope="scope">
                                <span :title='scope.row.name'>{{scope.row.name}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="size" label="安装大小(MB)">
                            <template slot-scope="scope">
                                <span :title='scope.row.size'>{{scope.row.size}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="enterpriseName" label="所属用户">
                            <template slot-scope="scope">
                                <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="" label="">
                            <template slot-scope="scope">
                                <button type="button" class="btn btn-text install" @click="install(scope.row.id)" :disabled="install1">安装</button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div v-if="pageInstan2.showPage">
                        <page-query v-if="pageInstan2.showPage" :currentPage="pageInstan2.currentPage" :total="pageInstan2.Total" :pageSize="pageInstan2.pageSize" @handleSizeChangeSub="handleSizeChange2" @handleCurrentChangeSub="handleCurrentChange2"></page-query>
                    </div>
                </div>
            </el-dialog>
            <!-- 批量导入 -->
            <el-dialog
                :visible.sync="importDialogVisible"
                title="批量导入"
                custom-class="normal-dialog"
                center
                v-if="importDialogVisible"
                :close-on-click-modal="false">
                <div class="authSetting">
                    <div class="control-group">
                        <label class="control-label">模板下载：</label>
                        <div class="controls">
                            <a href="/img/deviceImport.xlsx" download="批量导入设备模板">批量导入设备模板.xlsx</a>
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
            <detail-page :props="hasRouteQuery">
                <equipment-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></equipment-detail>
            </detail-page>
        </div>
    </div>
    
</template>

<script>
import {getDevicesList,getGroupDevices,createDevice,updateDevice,deleteDevices,getDevicesBases,getInstallApps,getUnInstallApps,fileUpload,shutdown,reboot,enable,disable,forcerelease,initDeviceInfo} from '@/public/api/equipment.js';
import {getClusters} from '@/public/api/node.js';
import {InstallToDevices,UninstallToDevices} from '@/public/api/application.js';
import detailPage from '@/public/common-tmpl/detail.vue' 
import equipmentDetail from './equipmentDetail.vue'
export default {
    data(){
        return {
            canDel1:false,
            canEdit1:false,
            canApp:false,
            hasRouteQuery:0,
            multipleSelection: [],
            multipleSelection1: [],
            dataList:[],
            dataList1:[],
            dataList2:[],
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
            pageInstan1:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
            pageInstan2:{//表格分页参数
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
            specsList:[],
            appDialogFormVisible:false,
            prohibit:false,
            importDialogVisible:false,
            importItemDisabled:false,
            formData:{},
            error:false,
            operationDialogVisible:false,
            operationItemDisabled:false,
            operationTitle:'',
            install1:false,
            uninstall1:false,
            open:false,
            queryForm:{},
            clusterList:[],
            status:'',
            baseType:'',
            schStatus:''
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
        //切换页码组，每页展示多少条
        handleSizeChange1(val) {
            this.pageInstan1.pageSize = val;
            this.appItem();
        },
        //切换页码
        handleCurrentChange1(val) {
            this.pageInstan1.currentPage = val;
            this.appItem('','pageOne');
        },
        //切换页码组，每页展示多少条
        handleSizeChange2(val) {
            this.pageInstan2.pageSize = val;
            this.appItem();
        },
        //切换页码
        handleCurrentChange2(val) {
            this.pageInstan2.currentPage = val;
            this.appItem('','pageOne');
        },
        handleFilterChange(filters){
            console.log(filters)
            this.pageInstan.pageSize = 50
            this.pageInstan.currentPage = 1
            if(filters.baseType){
                this.baseType = filters.baseType[0]
            }
            if(filters.status){
                this.status = filters.status[0]
            }
            if(filters.schStatus){
                this.schStatus = filters.schStatus[0]
            }
            this.initData()
        },
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize,
            }
            if(this.globalSearchTerm){
                oData.search = this.globalSearchTerm
            }
            if(this.status){
                oData.status = this.status
            }
            if(this.baseType){
                if(this.baseType == 1){
                    oData.baseType = 0
                }else if(this.baseType == 2){
                    oData.baseType = 1
                }
            }
            if(this.schStatus){
                if(this.schStatus == 1){
                    oData.schStatus = 1
                }else if(this.schStatus == 2){
                    oData.schStatus = 0
                }
            }
            if(this.queryForm.id){
                oData.id = this.queryForm.id
            }
            if(this.queryForm.name){
                oData.name = this.queryForm.name
            }
            if(this.queryForm.clusterName){
                oData.clusterName = this.queryForm.clusterName
            }
            if(this.queryForm.groupName){
                oData.groupName = this.queryForm.groupName
            }
            if(this.queryForm.innerIp){
                oData.innerIp = this.queryForm.innerIp
            }
            if(this.queryForm.innerPort){
                oData.innerPort = this.queryForm.innerPort
            }
            if(this.queryForm.innerMac){
                oData.innerMac = this.queryForm.innerMac
            }
            if(this.queryForm.ip){
                oData.ip = this.queryForm.ip
            }
            if(this.queryForm.port){
                oData.port = this.queryForm.port
            }
            if(this.queryForm.idc){
                oData.idc = this.queryForm.idc
            }
            if(this.queryForm.cabinet){
                oData.cabinet = this.queryForm.cabinet
            }
            if(this.queryForm.location){
                oData.location = this.queryForm.location
            }
            if(this.queryForm.slot){
                oData.slot = this.queryForm.slot
            }
            if(this.queryForm.managerIp){
                oData.managerIp = this.queryForm.managerIp
            }
            if(this.queryForm.managerPort){
                oData.managerPort = this.queryForm.managerPort
            }
            if(this.queryForm.description){
                oData.description = this.queryForm.description
            }
            console.log(oData)
            if(this.$route.query.status == 2){
                oData.groupId = this.$route.query.groupId
                getGroupDevices(oData).then((res) =>{
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
            }else{
                oData.clusterId = this.$route.query.clusterId
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
                        this.optionData(this.dataList)
                    }
                })
            }
            getDevicesBases().then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.specsList = res.data;
                }
            }),
            getClusters().then((res) =>{
                this.clusterList = res.data.records
            })
        },
        optionData(data){
            data.forEach(function(item){
                item.baseType = item.baseType == "0"?'arm':'x86';
                item.deviceschStatus = item.schStatus?'正常':'禁用'
                item.deviceInit = item.init?'已注册':'未注册'
                if(item.status == 1){
                    item.deviceStatus = '空闲'
                }else if(item.status == 2){
                    item.deviceStatus = '使用中'
                }else if(item.status == 3){
                    item.deviceStatus = '错误'
                }else if(item.status == 4){
                    item.deviceStatus = '安装中'
                }else if(item.status == 5){
                    item.deviceStatus = '重启中'
                }
            })
        },
        createItem(){
            this.type = "add"
            this.courseTitle = "新增设备",
            this.courseDialogFormVisible = true;
            this.equipmentForm = {
                // "id":"",
                "name":'',
                "innerIp":'',
                "innerPort":'',
                "innerMac":"",
                "ip":'',
                "port":'',
                "idc":'',
                "cabinet":'',
                "location":'',
                "slot":'',
                "managerIp":'',
                "managerPort":'',
                "adbIp":"",
                "adbPort":"",
                "baseId":"",
                "description":"",
            };
        },
        editItem(){
            this.type = "edit"
            this.courseTitle = "编辑设备",
            this.equipmentForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            console.log(this.equipmentForm)
            this.courseDialogFormVisible = true;
            this.prohibit = true
        },
        deleteItem(){
            this.deleteDialogVisible = true
        },
        importItem(){
            this.importDialogVisible = true
            this.formData = {}
            this.error = false
            this.importItemDisabled = false
        },
        bootUpItem(){
            

        },
        shutdownItem(){
            this.type = 'shutdown'
            this.operationDialogVisible = true
            this.operationTitle = '是否确认关闭选择的设备？'
        },
        restartItem(){
            this.type = 'restart'
            this.operationDialogVisible = true
            this.operationTitle = '是否确认重启选择的设备？'
        },
        enableItem(){
            this.type = 'enable'
            this.operationDialogVisible = true
            this.operationTitle = '是否确认启用选择的设备？'
        },
        prohibitItem(){
            this.type = 'prohibit'
            this.operationDialogVisible = true
            this.operationTitle = '是否确认禁用选择的设备？'
        },
        releaseItem(){
            this.type = 'release'
            this.operationDialogVisible = true
            this.operationTitle = '是否确认释放选择的设备？'
        },
        registerItem(){
            this.type = 'register'
            this.operationDialogVisible = true
            this.operationTitle = '是否确认注册选择的设备？'
        },
        appItem(){
            this.install1 = false
            this.uninstall1 = false
            this.appDialogFormVisible = true
            let oData = {
                'pageNum': this.pageInstan1.currentPage,
                'pageSize': this.pageInstan1.pageSize,
            }
            let oData1 = {
                'pageNum': this.pageInstan2.currentPage,
                'pageSize': this.pageInstan2.pageSize,
            }
            var id = this.multipleSelection.map(item=>{return item.id})
            getInstallApps(oData,id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.dataList1 = res.data.records;
                    if(this.dataList1.length > 0){
                        this.pageInstan1.Total = res.data.total;
                        this.pageInstan1.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan1.Total = res.data.total;
                        this.pageInstan1.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                }
            })
            getUnInstallApps(oData1,id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.dataList2 = res.data.records;
                    if(this.dataList2.length > 0){
                        this.pageInstan2.Total = res.data.total;
                        this.pageInstan2.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan2.Total = res.data.total;
                        this.pageInstan2.showPage = false;//判断数据是否为空 为空则不显示分页
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
        commitModel(){
            let Odata = {
                // "id":this.equipmentForm.id,
                "name":this.equipmentForm.name,
                "innerIp":this.equipmentForm.innerIp,
                "innerPort":this.equipmentForm.innerPort,
                "innerMac":this.equipmentForm.innerMac,
                "ip":this.equipmentForm.ip,
                "port":this.equipmentForm.port,
                "idc":this.equipmentForm.idc,
                "cabinet":this.equipmentForm.cabinet,
                "location":this.equipmentForm.location,
                "slot":this.equipmentForm.slot,
                "managerIp":this.equipmentForm.managerIp,
                "managerPort":this.equipmentForm.managerPort,
                "adbIp":this.equipmentForm.adbIp,
                "adbPort":this.equipmentForm.adbPort,
                "baseId":this.equipmentForm.baseId,
                "description":this.equipmentForm.description,
            }
            Odata.clusterId = this.$route.query.clusterId
            Odata.groupId = this.$route.query.groupId
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
        uninstall(id){
            var data = this.multipleSelection.map(item=>{return item.id})
            UninstallToDevices(data,id).then((res) =>{
                console.log(res);
                if(res&&res.code == 200){
                    this.uninstall1 = true
                    this.appDialogFormVisible = false
                    this.initData()
                }
            })
        },
        install(id){
            var data = this.multipleSelection.map(item=>{return item.id})
            console.log(data)
            console.log(id)
            InstallToDevices(data,id).then((res) =>{
                console.log(res);
                if(res&&res.code == 200){
                    this.install1 = true
                    this.appDialogFormVisible = false
                    this.initData()
                }
            })
        },
        operation(){
            let ids = [];
            this.multipleSelection.map(item =>{
                ids.push(item.id)
            })
            let data = ids
            console.log(data)
            // return
            if(this.type == 'shutdown'){
                shutdown(data).then((res) =>{
                    console.log(res)
                    this.operationDialogVisible = false;
                    this.initData()
                })
            }else if(this.type == 'restart'){
                reboot(data).then((res) =>{
                    console.log(res)
                    this.operationDialogVisible = false;
                    this.initData()
                })
            }else if(this.type == 'enable'){
                enable(data).then((res) =>{
                    console.log(res)
                    this.operationDialogVisible = false;
                    this.initData()
                })
            }else if(this.type == 'prohibit'){
                disable(data).then((res) =>{
                    console.log(res)
                    this.operationDialogVisible = false;
                    this.initData()
                })
            }else if(this.type == 'release'){
                forcerelease(data).then((res) =>{
                    console.log(res)
                    this.operationDialogVisible = false;
                    this.initData()
                })
            }else if(this.type == 'register'){
                initDeviceInfo(data).then((res) =>{
                    console.log(res)
                    this.operationDialogVisible = false;
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
            this.pageInstan.pageSize = 50
            this.pageInstan.currentPage = 1
            this.initData()
        },
        // 搜索
        searchItem(){
            this.initData()
        },
        //清空
        emptyItem(){
            this.queryForm = {}
        },
        openItem(){
            this.open = true
        },
        retractItem(){
            this.open = false
            this.queryForm = {}
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
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
        bth_check(){
            // 1 空闲
            // schStatus 启用
            // !schStatus 禁用
            if(this.multipleSelection.length == 1){
                if(this.multipleSelection[0].status == 1){
                    if(this.multipleSelection[0].schStatus){
                        if(this.multipleSelection[0].init){
                            return {
                                canEdit:false,
                                canDel:false,
                                canBootUp:false,
                                canShutdown:false,
                                canRestart:false,
                                canApp:false,
                                canEnable:false,
                                canProhibit:true,
                                canRelease:true,
                                canRegister:false
                            }
                        }else{
                            return {
                                canEdit:false,
                                canDel:false,
                                canBootUp:false,
                                canShutdown:false,
                                canRestart:false,
                                canApp:false,
                                canEnable:false,
                                canProhibit:true,
                                canRelease:false,
                                canRegister:true
                            }
                        }
                    }else{
                        if(this.multipleSelection[0].init){
                            return {
                                canEdit:true,
                                canDel:true,
                                canBootUp:true,
                                canShutdown:true,
                                canRestart:true,
                                canApp:true,
                                canEnable:true,
                                canProhibit:false,
                                canRegister:true
                            }
                        }else{
                            return {
                                canEdit:true,
                                canDel:true,
                                canBootUp:true,
                                canShutdown:true,
                                canRestart:true,
                                canApp:true,
                                canEnable:true,
                                canProhibit:false,
                                canRegister:true
                            }
                        }
                    }
                }else if(this.multipleSelection[0].status == 2){
                    if(this.multipleSelection[0].schStatus){
                        if(this.multipleSelection[0].init){
                            return {
                                canEdit:false,
                                canDel:false,
                                canBootUp:false,
                                canShutdown:false,
                                canRestart:false,
                                canApp:false,
                                canEnable:false,
                                canProhibit:true,
                                canRelease:true,
                                canRegister:true
                            }
                        }else{
                            return {
                                canEdit:false,
                                canDel:false,
                                canBootUp:false,
                                canShutdown:false,
                                canRestart:false,
                                canApp:false,
                                canEnable:false,
                                canProhibit:true,
                                canRelease:true,
                                canRegister:true
                            }
                        }
                    }else{
                        if(this.multipleSelection[0].init){
                            return {
                                canEdit:true,
                                canDel:true,
                                canBootUp:true,
                                canShutdown:true,
                                canRestart:true,
                                canApp:true,
                                canEnable:true,
                                canProhibit:false,
                                canRelease:true,
                                canRegister:true
                            }
                        }else{
                            return {
                                canEdit:true,
                                canDel:true,
                                canBootUp:true,
                                canShutdown:true,
                                canRestart:true,
                                canApp:true,
                                canEnable:true,
                                canProhibit:false,
                                canRelease:true,
                                canRegister:true
                            }
                        }
                    }
                }else{
                    if(this.multipleSelection[0].schStatus){
                        if(this.multipleSelection[0].init){
                            return {
                                canEdit:false,
                                canDel:false,
                                canBootUp:false,
                                canShutdown:false,
                                canRestart:false,
                                canApp:false,
                                canEnable:false,
                                canProhibit:true,
                                canRelease:false,
                                canRegister:true
                            }
                        }else{
                            return {
                                canEdit:false,
                                canDel:false,
                                canBootUp:false,
                                canShutdown:false,
                                canRestart:false,
                                canApp:false,
                                canEnable:false,
                                canProhibit:true,
                                canRelease:false,
                                canRegister:true
                            }
                        }
                    }else{
                        if(this.multipleSelection[0].init){
                            return {
                                canEdit:true,
                                canDel:true,
                                canBootUp:true,
                                canShutdown:true,
                                canRestart:true,
                                canApp:true,
                                canEnable:true,
                                canProhibit:false,
                                canRelease:false,
                                canRegister:true
                            }
                        }else{
                            return {
                                canEdit:true,
                                canDel:true,
                                canBootUp:true,
                                canShutdown:true,
                                canRestart:true,
                                canApp:true,
                                canEnable:true,
                                canProhibit:false,
                                canRelease:false,
                                canRegister:true
                            }
                        }
                    }
                }
            }else if(this.multipleSelection.length >1){
                let startNum = 0,
                    forbidNum = 0,
                    initNum = 0,
                    noInitNum = 0;
                this.multipleSelection.map((item) =>{
                    if(item.schStatus){
                        forbidNum++
                    }else{
                        startNum++
                    };
                    if(item.init){
                        initNum++
                    }else{
                        noInitNum++
                    }
                })
                if(forbidNum == this.multipleSelection.length){
                    if(initNum == this.multipleSelection.length){
                        return {
                            canEdit:false,
                            canDel:false,
                            canBootUp:false,
                            canShutdown:false,
                            canRestart:false,
                            canApp:false,
                            canEnable:false,
                            canProhibit:true,
                            canRelease:false,
                            canRegister:true
                        }
                    }else if(noInitNum == this.multipleSelection.length){
                        return {
                            canEdit:false,
                            canDel:false,
                            canBootUp:false,
                            canShutdown:false,
                            canRestart:false,
                            canApp:false,
                            canEnable:false,
                            canProhibit:true,
                            canRelease:false,
                            canRegister:true
                        }
                    }else{
                        return {
                            canEdit:false,
                            canDel:false,
                            canBootUp:false,
                            canShutdown:false,
                            canRestart:false,
                            canApp:false,
                            canEnable:false,
                            canProhibit:true,
                            canRelease:false,
                            canRegister:true
                        }
                    }
                }else if(startNum == this.multipleSelection.length){
                    if(initNum == this.multipleSelection.length){
                        return {
                            canEdit:false,
                            canDel:true,
                            canBootUp:true,
                            canShutdown:true,
                            canRestart:true,
                            canApp:false,
                            canEnable:true,
                            canProhibit:false,
                            canRelease:false,
                            canRegister:true
                        }
                    }else if(noInitNum == this.multipleSelection.length){
                        return {
                            canEdit:false,
                            canDel:true,
                            canBootUp:true,
                            canShutdown:true,
                            canRestart:true,
                            canApp:false,
                            canEnable:true,
                            canProhibit:false,
                            canRelease:false,
                            canRegister:true
                        }
                    }else{
                        return {
                            canEdit:false,
                            canDel:true,
                            canBootUp:true,
                            canShutdown:true,
                            canRestart:true,
                            canApp:false,
                            canEnable:true,
                            canProhibit:false,
                            canRelease:false,
                            canRegister:true
                        }
                    }
                }else{
                    if(initNum == this.multipleSelection.length){
                        return {
                            canEdit:false,
                            canDel:false,
                            canBootUp:false,
                            canShutdown:false,
                            canRestart:false,
                            canApp:false,
                            canEnable:false,
                            canProhibit:false,
                            canRelease:false,
                            canRegister:true
                        }
                    }else if(noInitNum == this.multipleSelection.length){
                        return {
                            canEdit:false,
                            canDel:false,
                            canBootUp:false,
                            canShutdown:false,
                            canRestart:false,
                            canApp:false,
                            canEnable:false,
                            canProhibit:false,
                            canRelease:false,
                            canRegister:true
                        }
                    }else{
                        return {
                            canEdit:false,
                            canDel:false,
                            canBootUp:false,
                            canShutdown:false,
                            canRestart:false,
                            canApp:false,
                            canEnable:false,
                            canProhibit:false,
                            canRelease:false,
                            canRegister:true
                        }
                    }
                }
            }else{
                return {
                    canEdit:false,
                    canDel:false,
                    canBootUp:false,
                    canShutdown:false,
                    canRestart:false,
                    canApp:false,
                    canEnable:false,
                    canProhibit:false,
                    canRelease:false,
                    canRegister:false
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
