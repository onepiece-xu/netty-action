<template>
    <div class="page-inner">
        <div class="table-action">
            <button type="button" class="btn btn-primary" @click="createItem">新增</button>
            <button type="button" class="btn btn-primary" @click="importItem">批量导入</button>
            <button type="button" class="btn btn-info" @click="editItem" :disabled="!bth_check.canEdit">编辑</button>
            <button type="button" class="btn btn-info" @click="imgItem" :disabled="!bth_check.canImg">上传图片</button>
            <button type="button" class="btn btn-info" @click="apkItem" :disabled="!bth_check.canApk">上传安装包</button>
            <button type="button" class="btn btn-info" @click="deleteItem" :disabled="!bth_check.canDel">删除</button>
            <button type="button" class="btn btn-info" @click="enableItem" :disabled="!bth_check.canEnable">启用</button>
            <button type="button" class="btn btn-info" @click="prohibitItem" :disabled="!bth_check.canProhibit">禁用</button>
            <button type="button" class="btn btn-info" @click="installItem" :disabled="!bth_check.canInstall">批量安装</button>
            <button type="button" class="btn btn-info" @click="uninstallItem" :disabled="!bth_check.canUninstall">批量卸载</button>
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
                <el-table-column prop="id" label="应用ID" sortable>
                    
                 </el-table-column>
                <el-table-column prop="name" label="应用名称" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.name'>{{scope.row.name}}</span>
                    </template>
                 </el-table-column>
                <!-- <el-table-column prop="packageName" label="安装包名称">
                    <template slot-scope="scope">
                        <span :title='scope.row.packageName'>{{scope.row.packageName}}</span>
                    </template>
                </el-table-column> -->
                <!-- <el-table-column prop="packageName" label="安装包文件名称">
                    <template slot-scope="scope">
                        <span :title='scope.row.packageName'>{{scope.row.packageFileName}}</span>
                    </template>
                </el-table-column> -->
                <!-- <el-table-column prop="size" label="安装包大小(MB)">
                    <template slot-scope="scope">
                        <span :title='scope.row.size'>{{scope.row.size}}</span>
                    </template> 
                </el-table-column> -->
                <el-table-column prop="enterpriseName" label="所属企业用户" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="type" label="应用类型" column-key="appType" :filter-multiple="false" :filters="[{text: 'arm', value: 1},{text: 'x86', value: 2},{text: '云电脑', value: 4},{text: '云手机', value: 5}]">
                    <template slot-scope="scope">
                        <span :title='scope.row.appType'>{{scope.row.appType}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="schStatus" label="应用状态" column-key="schStatus" :filter-multiple="false" :filters="[{text: '正常', value: 1},{text: '禁用', value: 2}]">
                    <template slot-scope="scope">
                        <span :title='scope.row.appStatus' :class="scope.row.schStatus?'bg-green':'bg-red'">{{scope.row.appStatus}}</span>
                    </template> 
                </el-table-column>
                <!-- <el-table-column prop="strategyName" label="流服务策略">
                    <template slot-scope="scope">
                        <span :title='scope.row.strategyName'>{{scope.row.strategyName}}</span>
                    </template> 
                </el-table-column> -->
                <el-table-column prop="uploadTime" label="上传时间" sortable>
                    <template slot-scope="scope">
                        <span :title='scope.row.uploadTime'>{{scope.row.uploadTime}}</span>
                    </template> 
                </el-table-column>
                <el-table-column prop="detail" label="详情" >
                    <template slot-scope="scope">
                        <router-link v-bind:to="'/application?id='+scope.row.id+'&name='+encodeURIComponent(scope.row.name)">
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
            :visible.sync="courseDialogFormVisible"
            custom-class="small-dialog delete-dialog"
            center
            :close-on-click-modal="false">
            <span>{{deleteTips}}</span>
            <div slot="footer">
                <button class="btn btn-warning" type="button" :disabled="deleteItemDisabled" @click="confirm1()">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="courseDialogFormVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 批量安装 -->
        <el-dialog
            :visible.sync="installDialogVisible"
            title="批量安装"
            custom-class="large-dialog"
            center
            v-if="installDialogVisible"
            :close-on-click-modal="false">
            <div class="table-action">
                <div class="control-group" style="float:left;margin-right:10px">
                    <label class="control-label">选择集群：</label>
                    <div class="controls">
                        <el-select
                            v-model="node"
                            filterable
                            clearable
                            name="node"
                            @change="nodeModel()"
                        >
                            <el-option
                                v-for="item in nodeList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <!-- <div class="control-group" style="float:left;margin-right:10px">
                    <label class="control-label">选择设备组：</label>
                    <div class="controls">
                        <el-select
                            v-model="cluster"
                            filterable
                            clearable
                            name="cluster"
                            :disabled="clusterDisabled"
                        >
                            <el-option
                                v-for="item in clusterList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </div> -->
                <button type="button" class="btn btn-primary" @click="searchItem" style="height:30px">搜索</button>
            </div>
            <div class="table-content batch">
                <el-table ref="multipleTable" 
                    :data="dataList1" 
                    @selection-change="handleSelectionChange1" 
                    height="545">
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="id" label="设备ID" width="180px">
                        
                    </el-table-column>
                    <el-table-column prop="name" label="设备名称">
                        <template slot-scope="scope">
                            <span :title='scope.row.name'>{{scope.row.name}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="clusterName" label="所属集群">
                        <template slot-scope="scope">
                            <span :title='scope.row.clusterName'>{{scope.row.clusterName}}</span>
                        </template>
                    </el-table-column>
                    <!-- <el-table-column prop="groupName" label="所属设备组">
                        <template slot-scope="scope">
                            <span :title='scope.row.groupName'>{{scope.row.groupName}}</span>
                        </template>
                    </el-table-column> -->
                    <el-table-column prop="ip" label="设备内部IP">
                        <template slot-scope="scope">
                            <span :title='scope.row.ip'>{{scope.row.ip}}</span>
                        </template>
                    </el-table-column>
                </el-table>
                <div v-if="pageInstan1.showPage">
                    <page-query v-if="pageInstan1.showPage" :currentPage="pageInstan1.currentPage" :total="pageInstan1.Total" :pageSize="pageInstan1.pageSize" @handleSizeChangeSub="handleSizeChange1" @handleCurrentChangeSub="handleCurrentChange1"></page-query>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" :disabled='!install' @click="confirm('install')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="installDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 批量卸载 -->
        <el-dialog
            :visible.sync="uninstallDialogVisible"
            title="批量卸载"
            custom-class="large-dialog"
            center
            v-if="uninstallDialogVisible"
            :close-on-click-modal="false">
            <div class="table-action">
                <div class="control-group" style="float:left;margin-right:10px">
                    <label class="control-label">选择集群：</label>
                    <div class="controls">
                        <el-select
                            v-model="node"
                            filterable
                            clearable
                            name="node"
                            @change="nodeModel()"
                        >
                            <el-option
                                v-for="item in nodeList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <!-- <div class="control-group" style="float:left;margin-right:10px">
                    <label class="control-label">选择设备组：</label>
                    <div class="controls">
                        <el-select
                            v-model="cluster"
                            filterable
                            clearable
                            name="cluster"
                            :disabled="clusterDisabled"
                        >
                            <el-option
                                v-for="item in clusterList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            >
                            </el-option>
                        </el-select>
                    </div>
                </div> -->
                <button type="button" class="btn btn-primary" @click="searchItem1" style="height:30px">搜索</button>
            </div>
            <div class="table-content batch">
                <el-table ref="multipleTable" 
                    :data="dataList2" 
                    @selection-change="handleSelectionChange2"
                    height="545" >
                    <el-table-column type="selection" width="40"> </el-table-column>
                    <el-table-column prop="id" label="设备ID" width="180px">
                        
                    </el-table-column>
                    <el-table-column prop="name" label="设备名称">
                        <template slot-scope="scope">
                            <span :title='scope.row.name'>{{scope.row.name}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="clusterName" label="所属集群">
                        <template slot-scope="scope">
                            <span :title='scope.row.clusterName'>{{scope.row.clusterName}}</span>
                        </template>
                    </el-table-column>
                    <!-- <el-table-column prop="groupName" label="所属设备组">
                        <template slot-scope="scope">
                            <span :title='scope.row.groupName'>{{scope.row.groupName}}</span>
                        </template>
                    </el-table-column> -->
                    <el-table-column prop="ip" label="设备内部IP">
                        <template slot-scope="scope">
                            <span :title='scope.row.ip'>{{scope.row.ip}}</span>
                        </template>
                    </el-table-column>
                </el-table>
                <div v-if="pageInstan2.showPage">
                    <page-query v-if="pageInstan2.showPage" :currentPage="pageInstan2.currentPage" :total="pageInstan2.Total" :pageSize="pageInstan2.pageSize" @handleSizeChangeSub="handleSizeChange2" @handleCurrentChangeSub="handleCurrentChange2"></page-query>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" :disabled='!uninstall' @click="confirm('uninstall')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="uninstallDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 批量安装与卸载 -->
        <el-dialog
            :visible.sync="relevanceDialogVisible"
            title="批量安装与卸载"
            custom-class="normal-dialog"
            center
            v-if="relevanceDialogVisible"
            :close-on-click-modal="false">
            <div class="select-tree">
                <div class="tags">
                    <el-tag
                    :key="tag.id"
                    v-for="tag in addTreeData"
                    closable
                    :disable-transitions="false"
                    @close="handleClose(tag)">
                    {{tag.label}}
                    </el-tag>
                </div>
                <radio-tree :dataList="treeData" @nodeCheck="nodeCheck"></radio-tree>
            </div>
            
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="confirm('relevance')">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="relevanceDialogVisible = false">{{$t('cn.action.cancel')}}</button>
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
                    <span class="red">*</span>应用英文名称：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.englishName" data-vv-as="应用英文名称" name="englishName" v-validate="'required'" placeholder="请填写应用英文名称">
                    <div class="error">
                        <span v-show="errors.has('englishName')" class="error">{{ errors.first('englishName') }}</span>
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
                        <el-radio :label="3">云电脑</el-radio>
                        <el-radio :label="4">云手机</el-radio>
                    </el-radio-group>
                    <div class="error">
                        <span v-show="errors.has('type')" class="error">{{ errors.first('type') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>付费类型：
                </label>
                <div class="controls">
                    <el-radio-group v-model="userAppForm.free">
                        <el-radio :label="0">付费</el-radio>
                        <el-radio :label="1">免费</el-radio>
                    </el-radio-group>
                    <div class="error">
                        <span v-show="errors.has('free')" class="error">{{ errors.first('free') }}</span>
                    </div>
                </div>
            </div>
            
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>应用显示类型：
                </label>
                <div class="controls">
                    <el-radio-group v-model="userAppForm.screen">
                        <el-radio :label="1">横屏</el-radio>
                        <el-radio :label="2">竖屏</el-radio>
                    </el-radio-group>
                    <div class="error">
                        <span v-show="errors.has('screen')" class="error">{{ errors.first('screen') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>平台类型：
                </label>
                <div class="controls">
                    <el-select
                        v-model="userAppForm.platform"
                        filterable
                        clearable
                        name="platform"
                        data-vv-as="平台类型"
                        v-validate="'required'"
                    >
                        <el-option
                            v-for="item in platformList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                    <div class="error">
                        <span v-show="errors.has('platform')" class="error">{{ errors.first('platform') }}</span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">
                    <span class="red">*</span>ARM端流策略名称：
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
                <label class="control-label">
                    <span class="red">*</span>PC端流策略名称：
                </label>
                <div class="controls">
                    <el-select
                        v-model="userAppForm.pcStrategyId"
                        filterable
                        clearable
                        name="pcStrategyId"
                        data-vv-as="流策略名称"
                        v-validate="'required'"
                    >
                        <el-option
                            v-for="item in pcStrategyList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        >
                        </el-option>
                    </el-select>
                    <div class="error">
                        <span v-show="errors.has('pcStrategyId')" class="error">{{ errors.first('pcStrategyId') }}</span>
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
                    <input type="text" class="form-control" v-model="userAppForm.accountPath" name="accountPath">
                    <!-- <div class="error">
                        <span v-show="errors.has('savePath')" class="error">{{ errors.first('savePath') }}</span>
                    </div> -->
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">存档数据路径：
                </label>
                <div class="controls">
                    <input type="text" class="form-control" v-model="userAppForm.savePath" name="savePath">
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
                <button class="btn btn-primary" type="button" @click="commitModel()">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 批量导入 -->
        <el-dialog
            :visible.sync="importDialogVisible"
            title="批量导入应用信息"
            custom-class="normal-dialog"
            center
            v-if="importDialogVisible"
            :close-on-click-modal="false">
            <div class="authSetting">
                <div class="control-group">
                    <label class="control-label">模板下载：</label>
                    <div class="controls">
                        <a href="/img/appImport.xlsx" download="批量导入应用信息模板">批量导入应用信息模板.xlsx</a>
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
        <!-- 批量导入 -->
        <el-dialog
            :visible.sync="apkDialogVisible"
            title="上传安装包"
            custom-class="normal-dialog"
            center
            v-if="apkDialogVisible"
            v-loading="load"
            element-loading-text="正在上传中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :close-on-click-modal="false">
            <div class="authSetting">
                <!-- <div class="control-group">
                    <label class="control-label">模板下载：</label>
                    <div class="controls">
                        <a href="/img/批量导入应用信息模板.xlsx" download="批量导入应用信息模板">批量导入应用信息模板.xlsx</a>
                    </div>
                </div> -->
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>上传安装包：</label>
                    <div class="controls">
                        <div class="file-box">
                            <input class="form-controls" data-vv-as="上传安装包" v-validate="'required'" name="upload" disabled
                            v-model="formData1.name"/>
                            <input type="file" @change="selectFile1($event)" ref="clearFile"/>
                            <button type="button" class="choose">
                                选择文件
                            </button>
                            <div class="error">
                                <span v-show="error" class="help is-danger">{{errorTip}}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="confirm('apk')" :disabled='!apkItemDisabled'>{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="apkDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <!-- 游戏图片 -->
        <el-dialog
            :visible.sync="imgDialogVisible"
            title="上传图片"
            custom-class="normal-dialog"
            center
            v-if="imgDialogVisible"
            :close-on-click-modal="false">
            <div class="control-group">
                <label class="control-label">logo图(200×200)：</label>
                <div class="controls">
                    <div class="img-show" v-if="!imgChecked3 && imgUrl3">
                        <a :href="imgUrl3" target="_blank"><img :src="imgUrl3" id="imgContent"></a>
                        <!-- <img :src="imgUrl3" id="imgContent"> -->
                    </div>
                    <div class="upload-file-btn" v-if="!imgChecked3 && imgUrl3">
                        <span class="btn-file">
                            <input type="file" accept="image/png,image/gif,image/jpeg" name="file" data-vv-as="图片" class="file" @change="changeImg3($event)"  />
                            <span class="blue-hand">修改图片</span>
                        </span>
                    </div>
                    <div class="upload-file-btn" v-if="imgChecked3 && !imgUrl3">
                        <span class="btn-file">
                            <input type="file" accept="image/png,image/gif,image/jpeg" name="file" data-vv-as="图片" class="file" @change="changeImg3($event)"  />
                            <span class="blue-hand">上传图片</span>
                        </span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">小图(200×200)：</label>
                <div class="controls">
                    <div class="img-show" v-if="!imgChecked && imgUrl">
                        <a :href="imgUrl" target="_blank"><img :src="imgUrl" id="imgContent"></a>
                        <!-- <img :src="imgUrl" id="imgContent"> -->
                    </div>
                    <div class="upload-file-btn" v-if="!imgChecked && imgUrl">
                        <span class="btn-file">
                            <input type="file" accept="image/png,image/gif,image/jpeg" name="file" data-vv-as="图片" class="file" @change="changeImg($event)"  />
                            <span class="blue-hand">修改图片</span>
                        </span>
                    </div>
                    <div class="upload-file-btn" v-if="imgChecked && !imgUrl">
                        <span class="btn-file">
                            <input type="file" accept="image/png,image/gif,image/jpeg" name="file" data-vv-as="图片" class="file" @change="changeImg($event)"  />
                            <span class="blue-hand">上传图片</span>
                        </span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">中图(500×280)：</label>
                <div class="controls">
                    <div class="img-show1" v-if="!imgChecked1 && imgUrl1">
                        <a :href="imgUrl1" target="_blank"><img :src="imgUrl1" id="imgContent"></a>
                        <!-- <img :src="imgUrl1" id="imgContent"> -->
                    </div>
                    <div class="upload-file-btn" v-if="!imgChecked1 && imgUrl1">
                        <span class="btn-file">
                            <input type="file" accept="image/png,image/gif,image/jpeg" name="file" data-vv-as="图片" class="file" @change="changeImg1($event)"  />
                            <span class="blue-hand">修改图片</span>
                        </span>
                    </div>
                    <div class="upload-file-btn" v-if="imgChecked1 && !imgUrl1">
                        <span class="btn-file">
                            <input type="file" accept="image/png,image/gif,image/jpeg" name="file" data-vv-as="图片" class="file" @change="changeImg1($event)"  />
                            <span class="blue-hand">上传图片</span>
                        </span>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">大图(650×428)：</label>
                <div class="controls">
                    <div class="img-show2" v-if="!imgChecked2 && imgUrl2">
                        <a :href="imgUrl2" target="_blank"><img :src="imgUrl2" id="imgContent"></a>
                        <!-- <img :src="imgUrl2" id="imgContent"> -->
                    </div>
                    <div class="upload-file-btn" v-if="!imgChecked2 && imgUrl2">
                        <span class="btn-file">
                            <input type="file" accept="image/png,image/gif,image/jpeg" name="file" data-vv-as="图片" class="file" @change="changeImg2($event)"  />
                            <span class="blue-hand">修改图片</span>
                        </span>
                    </div>
                    <div class="upload-file-btn" v-if="imgChecked2 && !imgUrl2">
                        <span class="btn-file">
                            <input type="file" accept="image/png,image/gif,image/jpeg" name="file" data-vv-as="图片" class="file" @change="changeImg2($event)"  />
                            <span class="blue-hand">上传图片</span>
                        </span>
                    </div>
                </div>
            </div>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="commitImgModel()">{{$t('cn.action.ok')}}</button>
                <button class="btn btn-default" type="button"  @click="imgDialogVisible = false">{{$t('cn.action.cancel')}}</button>
            </div>
        </el-dialog>
        <detail-page :props="hasRouteQuery">
            <application-detail :detailProps="{id:$route.query.id,name:$route.query.name}" v-if='hasRouteQuery'></application-detail>
        </detail-page>
    </div>
</template>

<script>
import {getUserApps,createUserApp,getPlatformList,updateUserApp,deleteUserApp,enableApps,disableApps,getCanInstallDevicesByAppId,getCanUninstallDevicesByAppId,InstallToDevices,UninstallToDevices,fileUpload,apkUpload,upload} from '@/public/api/application.js';
import {getuserList} from '@/public/api/userManager.js';
import {getStrategyList} from '@/public/api/streamService.js';
import {getClusters} from '@/public/api/node.js';
import {getDeviceGroupList} from '@/public/api/colony.js';
import detailPage from '@/public/common-tmpl/detail.vue' 
import applicationDetail from './applicationDetail.vue'
import radioTree from '@/public/common-tmpl/radio-tree.vue'
export default {
    data(){
        return {
            // canEdit:false,
            // canDel:false,
            // canInstall:false,
            // canUninstall:false,
            // canEnable:false,
            // canProhibit:false,
            hasRouteQuery:0,
            multipleSelection: [],
            multipleSelection1: [],
            editData:{
                opentype:'',
                optionWrap:false,
                content:this.multipleSelection
            },
            dataList:[],
            dataList1:[],
            dataList2:[],
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
            relevanceDialogVisible:false,
            addTreeData:[],
            treeData:[],
            addTitle:'新建应用',
            addDialogVisible:false,
            type:'add',
            userAppForm:{},
            userList:[],
            platformList:[],
            strategyList:[],
            pcStrategyList:[],
            importDialogVisible:false,
            formData:{},
            error:false,
            importItemDisabled:false,
            prohibit:false,
            installDialogVisible:false,
            nodeList:[],
            clusterList:[],
            node:'',
            cluster:'',
            clusterDisabled:true,
            uninstallDialogVisible:false,
            install:false,
            uninstall:false,
            dispatch:false,
            appType:'',
            schStatus:'',
            apkDialogVisible:false,
            formData1:{},
            apkItemDisabled:false,
            errorTip:'',
            load:false,
            imgDialogVisible:false,
            imgUrl:'',
            imgChecked:true,
            imgUrl1:'',
            imgChecked1:true,
            imgUrl2:'',
            imgChecked2:true,
            imgUrl3:'',
            imgChecked3:true,
        }
    },
    components: {
        detailPage,applicationDetail,radioTree
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
            this.searchItem();
        },
        //切换页码
        handleCurrentChange1(val) {
            this.pageInstan1.currentPage = val;
            this.searchItem('','pageOne');
        },
        //切换页码组，每页展示多少条
        handleSizeChange2(val) {
            this.pageInstan2.pageSize = val;
            this.searchItem1();
        },
        //切换页码
        handleCurrentChange2(val) {
            this.pageInstan2.currentPage = val;
            this.searchItem1('','pageOne');
        },
        handleFilterChange(filters){
            console.log(filters)
            this.pageInstan.pageSize = 50
            this.pageInstan.currentPage = 1
            if(filters.appType){
                this.appType = filters.appType[0]
            }
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
            if(this.appType){
                if(this.appType == 1){
                    oData.type = 0
                }else if(this.appType == 2){
                    oData.type = 1
                }else if(this.appType == 4){
                    oData.type = 3
                }else if(this.appType == 5){
                    oData.type = 4
                }
            }
            if(this.schStatus){
                if(this.schStatus == 1){
                    oData.schStatus = 1
                }else if(this.schStatus == 2){
                    oData.schStatus = 0
                }
            }
            getUserApps(oData).then((res) =>{
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
            getStrategyList().then((res) =>{
                console.log(res);
                this.strategyList = res.data.records
                this.pcStrategyList = res.data.records
            })
            getPlatformList().then((res) =>{
                if(res&&res.data){
                    this.platformList = res.data
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
                // item.appType = item.type == 0?'arm':'x86';
                if(item.type == 0){
                    item.appType = 'arm'
                }else if(item.type == 1){
                    item.appType = 'x86'
                }else if(item.type == 2){
                    item.appType = '云游戏'
                }else if(item.type == 3){
                    item.appType = '云电脑'
                }else if(item.type == 4){
                    item.appType = '云手机'
                }
                item.uploadTime = item.uploadTime?moment(item.uploadTime).format("YYYY-MM-DD HH:mm:ss"):'';
                if(item.schStatus){
                    item.appStatus = '正常'
                }else{
                    item.appStatus = '禁用'
                }
            })
        },
        nodeCheck(node){
            console.log(node);
            let node_data = node.node.data
            if(node_data.checked){
                let data = {
                    'value':node_data.id,
                    'label':node_data.loginName
                }
                // 先删再添加
                this.addTreeData.pop()
                this.addTreeData.push(data)
                this.resurion(this.treeData,true,node_data.id)
            }else{
                this.addTreeData.map((item,index) =>{
                    if(item.value == node_data.id){
                        this.addTreeData.splice(index,1)
                    }
                })
            }
        },
        createItem(){
            this.type = 'add',
            this.addTitle = "新建应用",
            this.dispatch = false
            this.addDialogVisible = true;
            this.userAppForm = {
                // "id":"",
                "name":"",
                "englishName":"",
                "accessId":"",
                "packageName":"",
                "packageFileName":"",
                "size":"",
                "userId":"",
                "path":"",
                "realSize":"",
                "type":0,
                "free":0,
                "platform":"",
                "screen":1,
                "strategyId":"",
                "pcStrategyId":"",
                "schInstall":1,
                "installMaxNumber":"",
                "savePath":"",
                "accountPath":"",
                "startShell":"",
                "closeShell":"",
                "description":""
            };
            
        },
        importItem(){
            this.importDialogVisible = true
            this.formData = {},
            this.error = false,
            this.importItemDisabled = false
        },
        editItem(){
            this.type = "edit";
            this.addDialogVisible = true,
            this.addTitle = "编辑应用",
            this.userAppForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            if(this.userAppForm.schInstall){
                this.userAppForm.schInstall = 1
            }else{
                this.userAppForm.schInstall = 2
            }
            this.prohibit = true
            console.log(this.userAppForm)
        },
        deleteItem(){
            this.courseDialogFormVisible = true
            this.type = 'del'
            this.deleteTips = '您确定要删除选择的应用吗？'
        },
        enableItem(){
            this.courseDialogFormVisible = true
            this.deleteTips = "您确定要启用选择的应用吗？"
            this.type = 'enable'
        },
        prohibitItem(){
            this.courseDialogFormVisible = true
            this.deleteTips = "您确定要禁用选择的应用吗？"
            this.type = 'prohibit'
        },
        installItem(){
            this.installDialogVisible = true;
            this.clusterDisabled = true;
            this.pageInstan1.showPage = false;
            this.dataList1 = [];
            this.node = '';
            this.cluster = '';
            this.install = false
            getClusters().then((res) =>{
                this.nodeList = res.data.records
            })
        },
        nodeModel(){
            this.cluster = ''
            if(this.node){
                this.clusterDisabled = false
                let oData = {
                    'pageNum': '',
                    'pageSize': ''
                }
                let id = this.node
                console.log(id)
                getDeviceGroupList(oData,id).then((res) =>{
                    if(res&&res.data){
                        this.clusterList = res.data.records
                    }
                })
                
            }else{
                this.clusterDisabled = true
                this.cluster = ''
            }
            
        },
        uninstallItem(){
            this.uninstallDialogVisible = true;
            this.clusterDisabled = true;
            this.pageInstan2.showPage = false;
            this.dataList2 = [];
            this.node = '';
            this.cluster = '';
            this.uninstall = false
            getClusters().then((res) =>{
                this.nodeList = res.data.records
            })
        },
        searchItem(){
            let oData = {
                'pageNum': this.pageInstan1.currentPage,
                'pageSize': this.pageInstan1.pageSize
            }
            if(this.node){
                oData.clusterId = this.node
                if(this.cluster){
                    oData.groupId = this.cluster
                }
            }
            let id = this.multipleSelection.map(item=>{return item.id})
            getCanInstallDevicesByAppId(oData,id).then((res) =>{
                console.log(res);
                this.dataList1 = res.data.records
                if(this.dataList1.length > 0){
                    this.pageInstan1.Total = res.data.total;
                    this.pageInstan1.showPage = true;//判断数据是否为空 为空则不显示分页
                }else{
                    this.pageInstan1.Total = res.data.total;
                    this.pageInstan1.showPage = false;//判断数据是否为空 为空则不显示分页
                }
            })
        },
        searchItem1(){
            let oData = {
                'pageNum': this.pageInstan2.currentPage,
                'pageSize': this.pageInstan2.pageSize
            }
            if(this.node){
                oData.clusterId = this.node
                if(this.cluster){
                    oData.groupId = this.cluster
                }
            }
            let id = this.multipleSelection.map(item=>{return item.id})
            getCanUninstallDevicesByAppId(oData,id).then((res) =>{
                console.log(res);
                this.dataList2 = res.data.records
                if(this.dataList2.length > 0){
                    this.pageInstan2.Total = res.data.total;
                    this.pageInstan2.showPage = true;//判断数据是否为空 为空则不显示分页
                }else{
                    this.pageInstan2.Total = res.data.total;
                    this.pageInstan2.showPage = false;//判断数据是否为空 为空则不显示分页
                }
            })
        },
        installItem1(){
            this.addTreeData = [];
            this.relevanceDialogVisible = true;
            deleteUserApp().then((res) =>{
                console.log(res);
                if(this.multipleSelection[0].userInfo){
                    this.addTreeData = [{
                        value:this.multipleSelection[0].userInfo.id,
                        label:this.multipleSelection[0].userInfo.loginName
                    }]
                }
                res.data.map(i =>{
                    if(i.users.length >0){
                        i.users.map(y =>{
                            if(this.multipleSelection[0].userInfo){
                                if(y.id == this.multipleSelection[0].userInfo.id){
                                    y.checked = true;
                                    
                                }else{
                                    y.checked = false;
                                }
                            }else{
                                y.checked = false;
                            }
                            
                        })
                    }
                })
                this.treeData = res.data
            })
        },
        commitModel(){
            let data = {
                // "id":this.userAppForm.id,
                "name":this.userAppForm.name,
                "englishName":this.userAppForm.englishName,
                "accessId":this.userAppForm.accessId,
                "packageName":this.userAppForm.packageName,
                "packageFileName":this.userAppForm.packageFileName,
                "size":this.userAppForm.size,
                "identification":this.userAppForm.identification,
                "path":this.userAppForm.path,
                "realSize":this.userAppForm.realSize,
                "type":this.userAppForm.type,
                'free':this.userAppForm.free,
                'platform':this.userAppForm.platform,
                "screen":this.userAppForm.screen,
                "strategyId":this.userAppForm.strategyId,
                "pcStrategyId":this.userAppForm.pcStrategyId,
                "savePath":this.userAppForm.savePath,
                "accountPath":this.userAppForm.accountPath,
                "startShell":this.userAppForm.startShell,
                "closeShell":this.userAppForm.closeShell,
                "description":this.userAppForm.description
            }
            if(this.userAppForm.schInstall == 1){
                data.schInstall = 1
            }else if(this.userAppForm.schInstall ==2){
                data.schInstall = 0
            }
            // data.schInstall = this.userAppForm.schInstall
            data.installMaxNumber = this.userAppForm.installMaxNumber
            console.log(data)
            // return
            this.$validator.validateAll().then((result)=>{
                if (result) {
                    this.addDialogVisible = false;
                    if(this.type == 'add'){
                        createUserApp(data).then((res) =>{
                            console.log(res)
                            this.initData()
                        })
                    }else if(this.type == 'edit'){
                        data.id = this.userAppForm.id;
                        updateUserApp(data).then(() =>{
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
            case 'relevance':
                let userIds = [];
                this.addTreeData.map((item) =>{
                    userIds.push(item.value)
                })
                let data2 = {
                    "userId":userIds.join(''),
                    "deskId":this.multipleSelection[0].deskId
                }
                relevanceUser(data2).then((res) =>{
                    console.log(res)
                    this.relevanceDialogVisible = false;
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
            case 'apk':
                this.load = true
                let data4 = new FormData();
                data4.append('file', this.file1);
                var id2 = this.multipleSelection.map(item=>{return item.id})
                this.$validator.validateAll().then((result)=>{
                    if(result){
                        apkUpload(data4,id2).then(res =>{
                            console.log(res);
                            if(res&&res.code == 200){
                                this.load = false
                                this.$refs.clearFile.value = '';
                                this.file1 = null;
                                this.formData1= {
                                    name:""
                                };
                                this.apkDialogVisible = false;
                                this.initData()
                            }
                        })
                    }
                })
                break;
            case 'install':
                let ids = [];
                this.multipleSelection1.map(item =>{
                    ids.push(item.id)
                })
                let data = ids
                var id = this.multipleSelection.map(item=>{return item.id})
                InstallToDevices(data,id).then((res) =>{
                    console.log(res);
                    if(res&&res.code == 200){
                        this.install = false
                        this.installDialogVisible = false;
                        this.initData()
                    }
                })
                break
            case 'uninstall':
                let ids1 = [];
                this.multipleSelection2.map(item =>{
                    ids1.push(item.id)
                })
                let data1 = ids1
                var id1 = this.multipleSelection.map(item=>{return item.id})
                UninstallToDevices(data1,id1).then((res) =>{
                    console.log(res);
                    if(res&&res.code == 200){
                        this.uninstall = false
                        this.uninstallDialogVisible = false;
                        this.initData()
                    }
                })
                break
            }
        },
        //删除，启用，禁用
        confirm1(){
            if(this.type == 'del'){
                let ids = [];
                this.multipleSelection.map(item =>{
                    ids.push(item.id)
                })
                let data = ids
                deleteUserApp(data).then((res) =>{
                    console.log(res)
                    this.courseDialogFormVisible = false;
                    this.initData()
                })
            }else if(this.type == 'enable'){
                var id = this.multipleSelection.map(item=>{return item.id})
                enableApps(id).then((res) =>{
                    console.log(res)
                    this.courseDialogFormVisible = false
                    this.initData()
                })
            }else if(this.type == 'prohibit'){
                var id1 = this.multipleSelection.map(item=>{return item.id})
                disableApps(id1).then((res) =>{
                    console.log(res)
                    this.courseDialogFormVisible = false;
                    this.initData()
                })
            }
        },
        //上传图片
        imgItem(){
            this.imgDialogVisible = true
            this.userAppForm =JSON.parse(JSON.stringify(this.multipleSelection[0]));
            if(this.userAppForm.smallPic){
                this.imgUrl = this.userAppForm.smallPic
                this.imgChecked = false
            }else{
                this.imgUrl = ''
                this.imgChecked = true
                this.fileData = ''
            }
            if(this.userAppForm.midPic){
                this.imgUrl1 = this.userAppForm.midPic
                this.imgChecked1 = false
            }else{
                this.imgUrl1 = ''
                this.imgChecked1 = true
                this.fileData1 = ''
            }
            if(this.userAppForm.bigPic){
                this.imgUrl2 = this.userAppForm.bigPic
                this.imgChecked2 = false
            }else{
                this.imgUrl2 = ''
                this.imgChecked2 = true
                this.fileData2 = ''
            }
            if(this.userAppForm.logoPic){
                this.imgUrl3 = this.userAppForm.logoPic
                this.imgChecked3 = false
            }else{
                this.imgUrl3 = ''
                this.imgChecked3 = true
                this.fileData3 = ''
            }
        },
        changeImg(e){ 
            console.log(e)       
            let file = e.target.files[0]
            this.fileData = file;
            var reader = new FileReader();
            reader.onload = (ev) =>{
                this.imgUrl = ev.target.result;
                this.imgChecked = false;
            }
            reader.readAsDataURL(file);
        },
        changeImg1(e){ 
            console.log(e)       
            let file = e.target.files[0]
            this.fileData1 = file;
            var reader = new FileReader();
            reader.onload = (ev) =>{
                this.imgUrl1 = ev.target.result;
                this.imgChecked1 = false;
            }
            reader.readAsDataURL(file);
        },
        changeImg2(e){ 
            console.log(e)       
            let file = e.target.files[0]
            this.fileData2 = file;
            var reader = new FileReader();
            reader.onload = (ev) =>{
                this.imgUrl2 = ev.target.result;
                this.imgChecked2 = false;
            }
            reader.readAsDataURL(file);
        },
        changeImg3(e){ 
            console.log(e)       
            let file = e.target.files[0]
            this.fileData3 = file;
            var reader = new FileReader();
            reader.onload = (ev) =>{
                this.imgUrl3 = ev.target.result;
                this.imgChecked3 = false;
            }
            reader.readAsDataURL(file);
        },
        commitImgModel(){
            let id = this.multipleSelection.map(item=>{return item.id})
            let formdata = new FormData();
            formdata.append('appId',id)
            formdata.append('smallPic',this.fileData?this.fileData:"");
            formdata.append('midPic',this.fileData1?this.fileData1:"");
            formdata.append('bigPic',this.fileData2?this.fileData2:"");
            formdata.append('logoPic',this.fileData3?this.fileData3:"");
            upload(formdata,id).then((res) =>{
                if(res&&res.code == 200){
                    this.imgDialogVisible = false
                    this.initData()
                }
            })
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
        handleClose(data){
            console.log(data);
            this.addTreeData.map((item,index) =>{
                if(item.value == data.value){
                    this.addTreeData.splice(index,1)
                }
            })
            this.resurion(this.treeData,false,data.value)
            console.log(this.treeData)
        },
        // 递归方法
        resurion(data,status,val){
            // 递归改变元数据
            function queryList(json) {
                for (var i = 0; i < json.length; i++) {
                    var sonList = json[i].users?json[i].users:[];
                    if(json[i].id == val){
                        json[i].checked = status;
                    }
                    if (sonList.length == 0) {
                    } else {
                        queryList(sonList);
                    }
                }
            }
            queryList(data);
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleSelectionChange1(val){
            this.multipleSelection1 = val;
            if(val.length == 0){
                this.install = false
            }else if(val.length == 1){
                this.install = true
            }else if(val.length > 1){
                this.install = true
            }
        },
        handleSelectionChange2(val){
            this.multipleSelection2 = val;
            if(val.length == 0){
                this.uninstall = false
            }else if(val.length == 1){
                this.uninstall = true
            }else if(val.length > 1){
                this.uninstall = true
            }
        },
        //上传安装包
        apkItem(){
            this.apkDialogVisible = true
            this.formData1 = {},
            this.error = false,
            this.apkItemDisabled = false
        },
        selectFile1(e){
            console.log(e)
            let packageName = this.multipleSelection[0].packageName
            let file1 = e.target.files[0];
            if (file1 && ["application/vnd.android.package-archive"].indexOf(file1.type) > -1) {
                const parser = new AppInfoParser(file1)
                parser.parse().then(result => {
                    console.log('app info ----> ', result)
                    console.log('icon base64 ----> ', result.icon)
                    if(result.package == packageName){
                        this.file1 = e.target.files[0];
                        this.formData1= {
                            name:this.file1.name
                        };
                        this.error = false
                        this.apkItemDisabled = true
                    }else{
                        this.$refs.clearFile.value = '';
                        this.file1 = null;
                        this.formData1= {
                            name:""
                        };
                        this.error = true
                        this.errorTip = result.package + '  当前应用包名不正确'
                        this.apkItemDisabled = false
                    }
                }).catch(err => {
                    console.log('err ----> ', err)
                })
            }else{
                this.$refs.clearFile.value = '';
                this.file1 = null;
                this.formData1= {
                    name:""
                };
                this.error = true
                this.errorTip = '升级包格式不正确'
                this.apkItemDisabled = false
            }
        },
    },
    computed:{
        bth_check(){
            // schStatus 正常
            if(this.multipleSelection.length ==1){
                if(this.multipleSelection[0].schStatus){
                    if(this.multipleSelection[0].type == 0){
                        return {
                            canApk:true,
                            canImg:true,
                            canEdit:true,
                            canDel:true,
                            canEnable:false,
                            canProhibit:true,
                            canInstall:true,
                            canUninstall:true
                        }
                    }else{
                        return {
                            canApk:false,
                            canImg:true,
                            canEdit:true,
                            canDel:true,
                            canEnable:false,
                            canProhibit:true,
                            canInstall:true,
                            canUninstall:true
                        }
                    }
                }else{
                    if(this.multipleSelection[0].type == 0){
                        return {
                            canApk:true,
                            canImg:true,
                            canEdit:true,
                            canDel:true,
                            canEnable:true,
                            canProhibit:false,
                            canInstall:true,
                            canUninstall:true
                        }
                    }else{
                        return {
                            canApk:false,
                            canImg:true,
                            canEdit:true,
                            canDel:true,
                            canEnable:true,
                            canProhibit:false,
                            canInstall:true,
                            canUninstall:true
                        }
                    }
                }
                
            }else if(this.multipleSelection.length > 1){
                let startNum = 0,
                    forbidNum = 0;
                this.multipleSelection.map((item) =>{
                    if(item.schStatus){
                        startNum++
                    }else{
                        forbidNum++
                    }
                })
                if(startNum == this.multipleSelection.length){
                    return {
                        canApk:false,
                        canImg:false,
                        canDel:true,
                        canEdit:false,
                        canEnable:false,
                        canProhibit:true,
                        canInstall:false,
                        canUninstall:false
                    }
                }else if(forbidNum == this.multipleSelection.length){
                    return {
                        canApk:false,
                        canImg:false,
                        canDel:true,
                        canEdit:false,
                        canEnable:true,
                        canProhibit:false,
                        canInstall:false,
                        canUninstall:false
                    }
                }else{
                    return {
                        canApk:false,
                        canImg:false,
                        canDel:true,
                        canEdit:false,
                        canEnable:false,
                        canProhibit:false,
                        canInstall:false,
                        canUninstall:false
                    }
                }
            }else{
                return {
                    canApk:false,
                    canImg:false,
                    canEdit:false,
                    canDel:false,
                    canEnable:false,
                    canProhibit:false,
                    canInstall:false,
                    canUninstall:false
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
