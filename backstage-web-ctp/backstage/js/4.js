(window.webpackJsonp=window.webpackJsonp||[]).push([[4],{270:function(e,t,n){"use strict";n.r(t);var s=n(289),i=n(280);for(var o in i)"default"!==o&&function(e){n.d(t,e,function(){return i[e]})}(o);var a=n(3),l=Object(a.a)(i.default,s.a,s.b,!1,null,null,null);l.options.__file="src/public/view/run/index.vue",t.default=l.exports},280:function(e,t,n){"use strict";n.r(t);var s=n(281),i=n.n(s);for(var o in s)"default"!==o&&function(e){n.d(t,e,function(){return s[e]})}(o);t.default=i.a},281:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={data:function(){return{checked:!1,closeImg:"./img/close.png",connectTime:"./img/connect-time.png",fullScreenImg:"./img/fullScreen.png",exitFullScreenImg:"./img/exitFullScreen.png",voiceImg:"./img/voice.png",qualityImg:"./img/quality.png",mouseImg:"./img/mouse.png",keyboardImg:"./img/keyboard.png",quitImg:"./img/quit.png",reconnectImg:"./img/reconnect.png",directionImg:"./img/direction.png",keyImg:"./img/key.png",delay:0,loss:0,sum:0,fps:0,bit:0,lineUp:!1,active:1,qualityList:[{name:"流畅"},{name:"标准"},{name:"高清"},{name:"超清"}],deviceName:window.getDeviceConnect().deviceName,tipsDialogVisible:!1,quitDialogVisible:!1,errorDialogVisible:!1,configList:[],cesuTimer:"",errorTimer:"",status:3,gif:"",gifTimer:"",refeshTimer:"",screen:!0,text:"温馨提示：可通过Ctrl+Shift+Enter组合键呼出控制面板。   全屏/浏览器模式：强烈建议在[控制面板]设置为[全屏]模式下运行,长按[Esc]退出全屏。    游戏启动：您看到当前页面时,代表画面正在启动中，请耐心等待。       特别说明：当网络带宽不足或延时较大时可能出现卡顿或花屏,此时可降低[画质]尝试改善。如仍无效果,请更换更好的网络环境。"}},components:{},methods:{initData:function(){$("html").css("height","100%"),$(".main").css("margin-top","0px"),window.connectDevice()},errrorModel:function(){window.open("about:blank","_top").close()},showHide:function(){this.checked?this.$message({message:"显示网络状态成功",customClass:"run-message",duration:2e3,type:"success"}):this.$message({message:"隐藏网络状态成功",customClass:"run-message",duration:2e3,type:"success"})},reconnectItem:function(){location.reload()},commitModel:function(){window.release();var e=window.getDeviceConnect(),t={deviceId:e.deviceId,connectId:e.connectId};release(t).then(function(e){e&&e.code,window.open("about:blank","_top").close()}),localStorage.removeItem("ConfigList")},fullScreen:function(){var e=$("body").get(0);e.requestFullscreen?e.requestFullscreen():e.mozRequestFullScreen?e.mozRequestFullScreen():e.webkitRequestFullScreen?e.webkitRequestFullScreen():elem.msRequestFullscreen&&elem.msRequestFullscreen()},exitFullScreen:function(){document.exitFullscreen?document.exitFullscreen():document.mozCancelFullScreen?document.mozCancelFullScreen():document.msExitFullscreen?document.msExiFullscreen():document.webkitCancelFullScreen?document.webkitCancelFullScreen():document.webkitExitFullscreen&&document.webkitExitFullscreen()},tipsModel:function(){window.open("about:blank","_top").close()},key:function(){$("#stats").hide(),$(".keyboard_setting_panel").css("display","block")},formatSeconds:function(e){var t="00:00";if(e>0){var n=parseInt(e/3600),s=parseInt(e%3600/60),i=parseInt((e-60*s)%60);n<10&&(n="0"+n),s<10&&(s="0"+s),i<10&&(i="0"+i),t=n+":"+s+":"+i}return t},fullscreenchange:function(){var e=document.fullScreen||document.mozFullScreen||document.webkitIsFullScreen;console.log(e),console.log(123456778),this.screen=!!e}},computed:{},watch:{},mounted:function(){var e=this;window.name="ABC",this.initData(),document.addEventListener("fullscreenchange",this.fullscreenchange),document.addEventListener("webkitfullscreenchange",this.fullscreenchange),document.addEventListener("mozfullscreenchange",this.fullscreenchange),setInterval(function(){localStorage.avgRtt&&(e.delay=localStorage.avgRtt),localStorage.avgLost&&(e.loss=parseFloat(localStorage.avgLost).toFixed(2)),localStorage.framefps&&(e.fps=localStorage.framefps),localStorage.avgLost&&(e.bit=parseFloat(localStorage.bitrate).toFixed(2))},1e3);var t=setInterval(function(){localStorage.framesDecodes>0&&(window.clearInterval(t),window.clearInterval(e.gifTimer),window.clearInterval(e.cesuTimer),window.clearInterval(e.errorTimer),e.sum=100,setTimeout(function(){$("#videocontainer").css("display","block"),$("#run-load").css("display","none")},1e3))},1e3)}}},289:function(e,t,n){"use strict";var s=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"run"},[n("div",{attrs:{id:"videocontainer",hidden:""}},[n("div",{attrs:{id:"remote"}},[n("canvas",{staticClass:"cursor",staticStyle:{display:"none"},attrs:{width:"48",height:"48",id:"cursor"}}),e._v(" "),n("video",{attrs:{id:"remote-video",playsinline:"",muted:""},domProps:{muted:!0}}),e._v(" "),n("audio",{attrs:{autoplay:""}})]),e._v(" "),e._m(0),e._v(" "),n("div",{attrs:{hidden:"",id:"stats"}},[n("div",{staticClass:"stats-background"}),e._v(" "),n("div",{staticClass:"menu"},[n("div",{staticClass:"head"},[n("div",{staticClass:"device-name"},[e._v(e._s(e.deviceName))]),e._v(" "),n("div",{staticClass:"delay"},[e._v("延时："),n("span",{class:e.delay>60?"red":""},[e._v(e._s(e.delay)+"ms")])]),e._v(" "),n("div",{staticClass:"loss"},[e._v("丢包："),n("span",{class:e.loss<2?"":"red"},[e._v(e._s(e.loss)+"%")])]),e._v(" "),n("div",{staticClass:"show-hide"},[n("el-checkbox",{on:{change:function(t){return e.showHide()}},model:{value:e.checked,callback:function(t){e.checked=t},expression:"checked"}},[e._v("显示网络状态")])],1)]),e._v(" "),n("div",{staticClass:"hr"})])]),e._v(" "),e.checked?n("div",{staticClass:"monitor"},[e._v("延时："),n("span",{class:e.delay>60?"red":""},[e._v(e._s(e.delay)+"ms")]),e._v("   丢包："),n("span",{class:e.loss<2?"":"red"},[e._v(e._s(e.loss)+"%")]),e._v("   码率："),n("span",[e._v(e._s(e.bit)+"Mbps")]),e._v("   帧率："),n("span",[e._v(e._s(e.fps)+"fps")])]):e._e()]),e._v(" "),n("div",{staticClass:"run-load",attrs:{id:"run-load"}},[n("div",{staticClass:"run-load-bg"}),e._v(" "),n("div",{staticClass:"title"},[n("marquee",{attrs:{val:e.text}})],1),e._v(" "),n("div",{staticClass:"load-text"},[e._v(e._s(e.deviceName)+"  正在连接中，请稍后......")])]),e._v(" "),n("el-dialog",{staticStyle:{"margin-top":"15vh"},attrs:{title:"确定要退出吗？",visible:e.quitDialogVisible,"custom-class":"quit-dialog","append-to-body":"","close-on-click-modal":!1,"show-close":!1},on:{"update:visible":function(t){e.quitDialogVisible=t}}},[n("img",{attrs:{src:e.closeImg,alt:""},on:{click:function(t){e.quitDialogVisible=!1}}}),e._v(" "),n("div",{attrs:{slot:"footer"},slot:"footer"},[n("button",{staticClass:"btn btn-default",attrs:{type:"button"},on:{click:function(t){e.quitDialogVisible=!1}}},[e._v("取消")]),e._v(" "),n("button",{staticClass:"btn btn-primary",attrs:{type:"button"},on:{click:function(t){return e.commitModel()}}},[e._v("确定")])])]),e._v(" "),n("el-dialog",{staticStyle:{"margin-top":"15vh"},attrs:{title:"提示",visible:e.errorDialogVisible,"custom-class":"quit-dialog","append-to-body":"","close-on-click-modal":!1,"show-close":!1},on:{"update:visible":function(t){e.errorDialogVisible=t}}},[n("p",[e._v("连接失败，请重新打开！")]),e._v(" "),n("div",{attrs:{slot:"footer"},slot:"footer"},[n("button",{staticClass:"btn btn-primary",attrs:{type:"button"},on:{click:function(t){return e.errrorModel()}}},[e._v("确定")])])])],1)},i=[function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"fp-ui"},[t("div",{staticClass:"keyboard_hint_panel_cover",attrs:{id:"keyboard_hint_panel_cover"}})])}];s._withStripped=!0,n.d(t,"a",function(){return s}),n.d(t,"b",function(){return i})}}]);