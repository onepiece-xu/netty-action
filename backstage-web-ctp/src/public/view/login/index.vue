<template>
    <div class="login-page">
        <img :src="logoImg"  class="logo"/>
        <span class="title">易点玩管理后台</span>
        <div class="login-form">
            <form name="loginForm" class="sign-box" @submit.prevent="submitForm" novalidate>
                <legend>登录易点玩管理后台</legend>
                <fieldset>
                    <div class="control-group" v-bind:class="{'focus':namefocus}">
                        <input class="form-controls" 
                            @focus="namefocus=true;loginCfmed=false" 
                            @blur="namefocus=false" 
                            autocomplete="off"
                            type="text"
                            name="name"
                            id="input_name"
                            v-validate="'required'"
                            v-model="login.name"
                            :placeholder="$t('cn.auth.user')"
                        />
                        <label class="control-label" v-bind:class="{'focused':focused}"><span class="scroll-bg"></span><span>{{$t('cn.auth.user')}}</span></label>
                        <div class="error-messages" v-show="errors.has('name')">
                            <span class="error">{{$t('cn.auth.user')+'不能为空'}}</span>
                        </div>
                    </div>
                    <div class="control-group" v-bind:class="{'focus':passwordfocus}">
                        <input class="form-controls"
                            @focus="passwordfocus=true;loginCfmed=false;showErrMsg = false" 
                            @blur="passwordfocus=false"
                            type="password"
                            name="password"
                            id="input_password"
                            v-model="login.password"
                            autocomplete="new-password"
                            :placeholder="$t('cn.auth.inputpasswd')"
                            v-validate="'required'"
                        />
                        <label class="control-label" v-bind:class="{'focused':focused}">
                            <span class="scroll-bg"></span>
                            <span>{{$t('cn.auth.passwd')}}</span>
                        </label>
                        <div class="error-messages" v-show="errors.has('password')">
                            <span class="error">{{$t('cn.auth.inputpasswd')}}</span>
                        </div>
                    </div>
                    <!-- <div class="control-group" v-bind:class="{'focus':captchaCode}">
                        <input class="form-controls verication"
                            @focus="captchaCode=true;loginCfmed=false;" 
                            @blur="captchaCode=false;"
                            type="text"
                            name="code"
                            v-model="login.captchaCode"
                            autocomplete="off"
                            :placeholder="$t('cn.auth.inputVerificationCode')"
                            v-validate="'required'"
                        />
                        <label class="control-label" v-bind:class="{'focused':focused}">
                            <span>{{$t('cn.auth.verificationCode')}}</span>
                            <span class="scroll-bg"></span>
                        </label>
                        <span class="code-img" @click="getKaptcha">
                            <img :src="imgUrl" id="codeImg">
                        </span>
                        <div class="error-messages" v-show="errors.has('code')">
                            <span class="error">{{$t('cn.auth.vercode')}}</span>
                        </div>
                        <div v-show="showErrMsg">
                            <span class="error">{{loginErrMessage}}</span>
                        </div>
                    </div> -->
                    <div class="btn-item">
                        <button type="submit" class="btn btn-primary" :disabled="submitted">{{$t('cn.auth.login')}}<span v-show="logining"><i class="icon-aw-load aw-spin"></i></span></button>
                    </div>
                    <div class="signuperror" v-show="loginError" >
                        <div v-if="loginError===1 && loginCfmed">
                            {{loginError1Msg}}
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</template>
<script>
import {userLoginIn} from '@/public/api/login.js';
export default {
    data(){
        return {
            login:{
                name:"",
                password:"",
                captchaCode:"",
            },
            loginError:0,
            loginCfmed:false,
            passwordfocus:false,
            namefocus:false,
            logining:false,
            focused:false,
            submitted:false,
            captchaCode:false,
            imgUrl:"",
            timer:{},
            showErrMsg:false,
            loginErrMessage:"",
            logoImg:require('@/images/static/logo.png')
        }
    },
    components: {
        // optionResourceGroup
    },
    methods:{
        submitForm(){
            const _this = this;
            _this.$validator.validateAll().then((res)=>{
                _this.loginError = 0;
                if (res) {
                    _this.submitted = true;
                    _this.logining = true;
                    _this.loginError = "";
                    _this.showErrMsg = false;
                    _this.loginErrMessage = "";
                    var data = _this.login;
                    userLoginIn(data).then((result)=>{
                        if (result && result.code == "200") {
                            _this.loginInit(result);
                            //_this.close();
                        } else {
                            //登录失败刷新验证码
                            // _this.getKaptcha();
                            //_this.captchaCode = "";
                            _this.showErrMsg = true;
                            _this.loginErrMessage = result.message;
                        }
                        
                    }).finally(function() {
                        _this.logining = false;
                        _this.submitted = false;
                    });
                } else {
                    _this.submitted = false;
                }
            })
        },
        loginInit(result) {
            //var editData = this.editData;
            var _this = this;
            _this.loginError = 0;
            localStorage.$AUTH_TOKEN = result.data;
            localStorage.USER_INFO = JSON.stringify(result.data);
            this.$router.replace({'name':"query"})
        },
        getKaptcha(){
            this.login.captchaCode = "";
            var _this = this;
            var url = "fpga-examstack-management/v1/kaptcha";
            this.$_ajax({
                url: url,
                method: 'GET',
                responseType: 'blob'
            }).then(function(res) {
                if (res && res.data) {
                    let codeData = new FileReader();
                    codeData.onload = function(){
                        _this.imgUrl = this.result;
                    }
                    codeData.readAsDataURL(res.data);
                }
            });
        },
    },
    computed:{
        
    },
    mounted(){
        document.title = this.$i18n.t('cn.menu.login');
        // this.getKaptcha();
        //验证码每个60秒刷新一次
        /* this.timer = setInterval(this.getKaptcha, 60000);
        localStorage.clear();
        $cookies.remove('LIB_LOGINED');
        $cookies.remove('LIB_AUTH_TOKEN'); */
    }
}
</script>