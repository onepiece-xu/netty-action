<template>
<div>
    <header>
        <div class="inner clearfix">
            <div class="logo">
                <a href="#/query">
                    <img src="/img/logo.png" alt="">
                    <span>易点玩管理后台</span>
                </a>
            </div>
            <ul class="user-mes-box clearfix">
                <!-- <li class="admin-center-info" v-if="checkAuth"> -->
                <li class="admin-center-info">
                    <a><i class="iconfont"></i></a>
                    <div class="dropdown-layer admin-center">
                        <ul class="admin-center-menu">
                            <li hidden>
                                <a @click="changPassword()">修改密码</a>
                            </li>
                            <li>
                                <a @click="loginOut()">退出</a>
                            </li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
    </header>
    <!-- 新建 -->
    <el-dialog
        :visible.sync="addDialogVisible"
        title="修改密码"
        custom-class="normal-dialog info-dialog"
        center
        :close-on-click-modal="false">
        <div class="control-group">
            <label class="control-label">
                <span class="red">*</span>原密码：
            </label>
            <div class="controls">
                <input type="password" class="form-control form-control-50" v-model="baseForm.oldPWD" data-vv-as="原密码" placeholder="请输入原密码" name="oldpass" v-validate="'required|max:20'" >
                <div class="error">
                    <span v-show="errors.has('oldpass')" class="error">{{ errors.first('oldpass') }}</span>
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">
                <span class="red">*</span>新密码：
            </label>
            <div class="controls">
                <input type="password" class="form-control form-control-50" v-model="baseForm.newPWD" data-vv-as="新密码" placeholder="请输入新密码" name="newPass" v-validate="'required|max:20'" >
                <div class="error">
                    <span v-show="errors.has('newPass')" class="error">{{ errors.first('newPass') }}</span>
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">
                <span class="red">*</span>确认新密码：
            </label>
            <div class="controls">
                <input type="password" class="form-control form-control-50" v-model="baseForm.newPWDad" data-vv-as="确认新密码" placeholder="请输入确认密码" name="newpassAg" >
                <div class="error" v-show="compareStatus">
                    <span>两次密码不同</span>
                </div>
            </div>
        </div>
        <div slot="footer">
            <button class="btn btn-primary" type="button" @click="confirm">{{$t('cn.action.ok')}}</button>
            <button class="btn btn-default" type="button"  @click="addDialogVisible = false">{{$t('cn.action.cancel')}}</button>
        </div>
    </el-dialog>
</div>
</template>
<script>
import {userLoginOut,updatePassword} from '@/public/api/login.js';
export default {
    data() {
        return {
            logoUrl:"",
            addDialogVisible:false,
            baseForm:{},
        }
    },
    computed: {
        compareStatus(){
            return this.baseForm.newPWD != this.baseForm.newPWDad
        }
    },
    props:[],
    methods:{
        changPassword(){
            this.addDialogVisible = true;
            this.baseForm = {}
        },
        confirm(){
            let data = {"oldPassword":this.baseForm.oldPWD,"newPassword":this.baseForm.newPWD}
            this.$validator.validateAll().then((result)=>{
                if(result&&!this.compareStatus){
                    updatePassword(data).then(() =>{
                        this.addDialogVisible = false;
                        this.baseForm={}
                    })
                }
            })
        },
        loginOut(){
            userLoginOut().then((res) =>{
                if(res&&res.code == 200){
                    localStorage.clear();
                    this.$router.replace({'name':"login"})
                }
            })
        },
    },
    watch:{
       
    },
    mounted(){
        
    }
}
</script>
