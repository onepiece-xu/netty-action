<template>
    <div class="run">
        <div id="videocontainer" hidden>
            <div id="remote">
                <canvas class="cursor" width="48" height="48" id="cursor" style="display:none;"></canvas>
                <video id="remote-video" playsinline muted></video>
                <audio autoplay></audio>
            </div>
            <div class="fp-ui">
                <div class="keyboard_hint_panel_cover" id="keyboard_hint_panel_cover"></div>
            </div>
            <div hidden id="stats">
                <div class="stats-background"></div>
                <div class ="menu">
                    <div class="head">
                        <div class="device-name">{{deviceName}}</div>
                        <div class="delay">延时：<span :class="delay>60?'red':''">{{delay}}ms</span></div>
                        <div class="loss">丢包：<span :class="loss<2?'':'red'">{{loss}}%</span></div>
                        <div class="show-hide"><el-checkbox v-model="checked" @change="showHide()">显示网络状态</el-checkbox></div>
                    </div>
                    <div class="hr"></div>
                </div>
            </div>
            <div class="monitor" v-if="checked">延时：<span :class="delay>60?'red':''">{{delay}}ms</span>&nbsp;&nbsp;&nbsp;丢包：<span :class="loss<2?'':'red'">{{loss}}%</span>&nbsp;&nbsp;&nbsp;码率：<span>{{bit}}Mbps</span>&nbsp;&nbsp;&nbsp;帧率：<span>{{fps}}fps</span></div>
        </div> 
        <div id="run-load" class="run-load">
            <!-- <img :src="loadImg" alt=""> -->
            <div class="run-load-bg"></div>
            <div class="title"><marquee :val='text'></marquee></div>
            <div class="load-text">{{deviceName}}&nbsp;&nbsp;正在连接中，请稍后......</div>
        </div>
        <el-dialog
            title="确定要退出吗？"
            :visible.sync="quitDialogVisible"
            custom-class="quit-dialog"
            append-to-body
            :close-on-click-modal="false"
            :show-close="false"
            style="margin-top: 15vh;"
            >
            <img :src="closeImg" alt="" @click="quitDialogVisible = false">
            <div slot="footer">
                <button class="btn btn-default" type="button"  @click="quitDialogVisible = false">取消</button>
                <button class="btn btn-primary" type="button" @click="commitModel()">确定</button>
                
            </div>
        </el-dialog>
        <el-dialog
            title="提示"
            :visible.sync="errorDialogVisible"
            custom-class="quit-dialog"
            append-to-body
            :close-on-click-modal="false"
            :show-close="false"
            style="margin-top: 15vh;"
            >
            <p>连接失败，请重新打开！</p>
            <div slot="footer">
                <button class="btn btn-primary" type="button" @click="errrorModel()">确定</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data(){
        return {
            checked:false,
            closeImg:"./img/close.png",
            connectTime:"./img/connect-time.png",
            fullScreenImg:"./img/fullScreen.png",
            exitFullScreenImg:"./img/exitFullScreen.png",
            voiceImg:"./img/voice.png",
            qualityImg:"./img/quality.png",
            mouseImg:"./img/mouse.png",
            keyboardImg:"./img/keyboard.png",
            quitImg:"./img/quit.png",
            // loadImg:"./img/load-bg.png",
            reconnectImg:"./img/reconnect.png",
            directionImg:"./img/direction.png",
            keyImg:"./img/key.png",
            delay:0,
            loss:0,
            sum:0,
            fps:0,
            bit:0,
            lineUp:false,
            active:1,
            qualityList:[
                {
                    name:'流畅'
                },
                {
                    name:'标准'
                },
                {
                    name:'高清'
                },
                {
                    name:'超清'
                },
            ],
            deviceName:window.getDeviceConnect().deviceName,
            tipsDialogVisible:false,
            quitDialogVisible:false,
            errorDialogVisible:false,
            configList:[],
            cesuTimer:'',
            errorTimer:'',
            status:3,
            gif:'',
            gifTimer:'',
            refeshTimer:'',
            screen:true,
            text:"温馨提示：可通过Ctrl+Shift+Enter组合键呼出控制面板。   全屏/浏览器模式：强烈建议在[控制面板]设置为[全屏]模式下运行,长按[Esc]退出全屏。    游戏启动：您看到当前页面时,代表画面正在启动中，请耐心等待。       特别说明：当网络带宽不足或延时较大时可能出现卡顿或花屏,此时可降低[画质]尝试改善。如仍无效果,请更换更好的网络环境。"
        }
    },
    components: {

    },
    methods:{
        initData(){            
            $("html").css("height","100%")
            $(".main").css("margin-top","0px")
            window.connectDevice();         
        },
        errrorModel(){
            window.open("about:blank", "_top").close()
        },
        showHide(){
            if(this.checked){
                this.$message({
                    message: '显示网络状态成功',
                    customClass:'run-message',
                    duration:2000,
                    type: 'success'
                });
            }else{
                this.$message({
                    message: '隐藏网络状态成功',
                    customClass:'run-message',
                    duration:2000,
                    type: 'success'
                });
            }
        },       
        //重连
        reconnectItem(){
            location.reload()
        },
        commitModel(){
            window.release()
            let deviceConnect = window.getDeviceConnect();
            let data = {
                "deviceId": deviceConnect.deviceId, 
                "connectId": deviceConnect.connectId
            }
            release(data).then((res) =>{
                if(res&&res.code == 200){
                    window.open("about:blank", "_top").close()
                }else{
                    window.open("about:blank", "_top").close()
                }
            })
            localStorage.removeItem("ConfigList")
        },
        fullScreen(){
            // this.screen = true
            let dE = $('body').get(0);
            //W3C
            if (dE.requestFullscreen) {
                dE.requestFullscreen();
            }
            //FireFox
            else if (dE.mozRequestFullScreen) {
                dE.mozRequestFullScreen();
            }
            //Chrome等
            else if (dE.webkitRequestFullScreen) {
                dE.webkitRequestFullScreen();
            }
            //IE11
            else if (elem.msRequestFullscreen) {
                elem.msRequestFullscreen();
            }
        },
        exitFullScreen(){
            // this.screen = false
            if (document.exitFullscreen) {
                document.exitFullscreen();
            } else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else if (document.msExitFullscreen) {
                document.msExiFullscreen();
            } else if (document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen();

            } else if (document.webkitExitFullscreen) {
                document.webkitExitFullscreen();
            }
        },
        tipsModel(){
            window.open("about:blank", "_top").close()
        },
        key(){
            $("#stats").hide();
            $(".keyboard_setting_panel").css("display","block")
        },
        formatSeconds(times){
            let result = '00:00';
            if (times>0){
                let hour = parseInt(times/3600);
                let minute = parseInt(times%3600/60);
                let second = parseInt((times-60 * minute) % 60);
                if (hour < 10) {
                    hour = '0' + hour;
                }
                if(minute<10){
                    minute = "0" + minute;
                }
                if(second<10){
                    second = "0" + second;
                }
                result = hour + ':' + minute +':' + second;
            }
            return result;
        },
        fullscreenchange(){
            let isFullScreen = document.fullScreen || document.mozFullScreen || document.webkitIsFullScreen;
            console.log(isFullScreen)
            console.log(123456778)
            if(isFullScreen) {
                this.screen = true
            }else {
                this.screen = false
            }
        },
    },
    computed:{
        
    },
    watch:{
        
    },
    mounted(){
        window.name = 'ABC'
        this.initData();
        document.addEventListener('fullscreenchange', this.fullscreenchange);
        document.addEventListener('webkitfullscreenchange', this.fullscreenchange);
        document.addEventListener('mozfullscreenchange', this.fullscreenchange);
        setInterval(() =>{
            if(localStorage.avgRtt){
                this.delay = localStorage.avgRtt
            }
            if(localStorage.avgLost){
                this.loss = parseFloat(localStorage.avgLost).toFixed(2)
            }
            if(localStorage.framefps){
                this.fps = localStorage.framefps
            }
            if(localStorage.avgLost){
                this.bit = parseFloat(localStorage.bitrate).toFixed(2)
            }
        },1000)
        let timer = setInterval(() =>{
            if(localStorage.framesDecodes > 0){
                window.clearInterval(timer);
                window.clearInterval(this.gifTimer)
                window.clearInterval(this.cesuTimer);
                window.clearInterval(this.errorTimer);
                this.sum = 100
                setTimeout(() => {
                    $("#videocontainer").css("display","block")
                    $("#run-load").css("display","none")
                }, 1000);
            }
        },1000)
    }
}
</script>
