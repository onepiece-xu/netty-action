<template>
    <div class="tab-content">
        <div class="detail-row">
            <div class="detail-title">
                <a class="an-close" @click="$router.go(-1)">
                    <i class="icon-aw-mail-reply"></i>
                </a>
                <span>用户审核</span>
            </div>
            <div class="detail-info">
                <table class='table-dex' >
                    <tr>
                        <td>企业名称</td>
                        <td>{{detailInfo.enterpriseName}}</td>
                        <td>企业ID</td>
                        <td>{{detailInfo.identification}}</td>
                    </tr>
                    <tr>
                        <td>企业key</td>
                        <td>{{detailInfo.secretKey}}</td>
                        <td>用户登录名</td>
                        <td>{{detailInfo.loginName}}</td>
                    </tr>
                    <tr>
                        <td>用户类型</td>
                        <td>{{detailInfo.userType}}</td>
                        <td>状态</td>
                        <td>{{detailInfo.status==0?"正常":"禁用"}}</td>
                    </tr>
                    <tr>
                        <td>用户邮箱</td>
                        <td>{{detailInfo.email}}</td>
                        <td>用户手机号</td>
                        <td>{{detailInfo.mobileNumber}}</td>
                    </tr>
                    <tr>
                        <td>电话号码（座机）</td>
                        <td>{{detailInfo.telNumber}}</td>
                        <td>创建时间</td>
                        <td>{{detailInfo.createTime}}</td>
                    </tr>
                    <tr>
                        <td>修改时间</td>
                        <td>{{detailInfo.modifiedTime}}</td>
                        <td>备注</td>
                        <td>{{detailInfo.description}}</td>
                    </tr>
                </table>
            </div>
            <div class="detail-form">
                <div class="control-group">
                    <label class="control-label">
                        <span class="red">*</span>审核结果：
                    </label>
                    <div class="controls">
                        <el-select
                            v-model="userForm.type"
                            filterable
                            clearable
                            name="type"
                            data-vv-as="审核结果"
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
                    <label class="control-label">备注：</label>
                    <div class="controls">
                        <textarea class="form-controls" name="description" placeholder="请输入备注" v-model="userForm.description" > </textarea>
                    </div>
                </div>
                <div class="footer">
                    <button class="btn btn-primary" type="button" @click="commitModel()">{{$t('cn.action.ok')}}</button>
                    <button class="btn btn-default" type="button"  @click="commitModel()">{{$t('cn.action.cancel')}}</button>
                </div>
            </div>
            
        </div>
    </div>
</template>

<script>
import {getUserInfo} from '@/public/api/userManager.js';

export default {
    data(){
        return {
            detailInfo:{},
            userForm:{},
            typeList:[
                {
                    id:1,
                    type:"审核成功",
                },
                {
                    id:0,
                    type:"审核失败",
                },
                // {
                //     id:3,
                //     type:"个人用户",
                // },
            ]
        }
    },
    methods:{
        initData(){
            // let data = {
            //     poolId:this.$route.query.id
            // }
            let id = this.$route.query.id
            getUserInfo(id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.detailInfo = res.data;
                    this.optionData([this.detailInfo])
                }
            })
        },
        optionData(data){
            data.forEach(function(item){
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
        commitModel(){

        },
    },
    mounted(){
        this.initData();
    }
}
</script>
