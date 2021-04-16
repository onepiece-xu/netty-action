// const { setInterval } = require("stompjs");
var p2p;
var locked = 0;
var mouseX;
var mouseY;
var connected = false;
var relativeMovementMode = false;
//全屏
var panel = false
var keyboard = navigator.keyboard
//移动端定义
var screen = 1
var startTime = 0;
var endTime = 0;
var arr = new Array();
var brr = new Array();
var pid = 0           ///全局 pid，队列为空时清0，重新从0 开始
var mouseleftbutton = 1000 ;         ///鼠标左键
var step = 20         ///移动步长
var data = {
    a:"",
    ps:"",
    xy:[],
    i:"",
    pid:""
}

var framesDecodes = 0       ///解码帧数，当>0时，表明视频已经连接成功，此时，可以去掉游戏蒙层
var framefps = 0            ///帧率
var bytesReceive = 0        ///接收字节数，单位 M
var bitrate      = 0        ///带宽  /s
var packetsReceive = 0      ///接收的包数
var totLost      = 0        ///总丢包数
var totDecodeTime = 0       ///总解码时长
var avgDecodeTime = 0       ///平均解码时长
var avgLost      = 0        ///当前丢包率 %
var avgRtt       = 0        ///平均时延，ms
var timer;
var down = false;
var framesDecode = false
var keyCode = []

//设置deviceConnect
window.setDeviceConnect = function(deviceConnect) {
    localStorage.setItem("deviceConnect", deviceConnect);
}

//清除连接
window.delDeviceConnect = function() {
    localStorage.removeItem("deviceConnect");
}

//获取deviceConnect
window.getDeviceConnect = function() {
    return JSON.parse(localStorage.getItem("deviceConnect"));
}

//连接设备
window.connectDevice = function() {
    let device = window.getDeviceConnect();
    console.log(device)
    console.log("正在连接设备，请稍后！");
    let signaling = new SignalingChannel();
    p2p = new Owt.P2P.P2PClient({
        audioEncodings: true,
        videoEncodings: [{
            codec: {
                name: 'h264'
            }
        }
        , {
            codec: {
                name: 'vp9'
            }
        }, {
            codec: {
                name: 'vp8'
            }
        }],
        rtcConfiguration: {
            iceServers: [{
                urls: device.turnServer,
                credential: device.turnPassword,
                username: device.turnUser
            }]
        },
    }, signaling);
    p2p.connect({
        host: device.signalServer,
        token: device.token
    }).then(() => {
        p2pConnect();
    }, error => {
        console.log('Failed to connect to the signaling server.');
    });
}
function p2pConnect() {
    // $("html").css("height","100%")
    // $(".main").css("margin-top","0px")
    console.log("开始连接，请稍后！");
    brr = []
    let deviceConnect = window.getDeviceConnect();
    p2p.allowedRemoteIds = deviceConnect.connectId;
    p2p.send(deviceConnect.connectId, 'start'); // Send data to remote endpoint.
    // Automatically switch to full screen.
    p2p.addEventListener('streamadded', function(e) { // A remote stream is available.
        console.log(1111)
        console.log(e)
        connected = true;
        relativeMovementMode = true;
        //获取按键
        setTimeout(function () {
            keyConfig()
        }, 100);
        e.stream.addEventListener('ended', () => {
            $('#remote-video').get(0).srcObject = undefined;
            $('#remote-video').hide();
            console.log('Stream is removed.');
            // if(connected){
            //     alert(111)
            //     $('#tips').show();
            // }
            // window.open("about:blank", "_top").close()
            // $('#tips').show();
        });
        if(e.stream.mediaStream.getVideoTracks().length === 0) {
            if (e.stream.mediaStream.getAudioTracks().length > 0) {
                $('#remote audio').get(0).srcObject = e.stream.mediaStream;
                $('#remote audio').get(0).play();
            }
            return;
        }
        if(e.stream.source.video === 'screen-cast') {
            $('#remote-video').show();
            $('#remote-video').get(0).srcObject = e.stream.mediaStream;
            $('#remote-video').get(0).play();
        }else if(e.stream.source.audio || e.stream.source.video) {
            $('#remote-video').show();
            $('#remote-video').get(0).srcObject = e.stream.mediaStream;
            $('#remote-video').get(0).play()
        }

        setInterval(function () {
            getStats();
        }, 1000);
    });


    function getStats () {
        let device = window.getDeviceConnect();

        p2p.getStats(device.connectId).then((stats)=>{
            stats.forEach(function (stat) {
                if (stat.type == "inbound-rtp"){
                    if (stat.mediaType === "video") {
                        let  bytes = stat.bytesReceived     ///接收字节数
                        let  frames = stat.framesDecoded
                        if (bytesReceive> 0 ){
                            bitrate = (bytes - bytesReceive) * 8 /1000000  ///单位 M
                            localStorage.bitrate = bitrate
                        }
                        bytesReceive = bytes                              
                        if ( framesDecodes > 0){
                            window.clearInterval(timer)
                            framefps = frames - framesDecodes;     ///帧率
                            localStorage.framefps = framefps
                            localStorage.framesDecodes = framesDecodes
                            setTimeout(function () {
                                framesDecode = true
                            }, 1000)
                        }
                        framesDecodes = frames 

                        let packets = stat.packetsReceived     ///接收包数
                        let curPackets = packets - packetsReceive;   ///当前统计时间内的包数，用于统计丢包率
                        let lost = stat.packetsLost  -  totLost;   /// 当前丢包数
                        if (curPackets >0 ){
                            avgLost = lost /curPackets
                        }
                        totLost = stat.packetsLost
                        packetsReceive = packets;
                        if (framefps > 0 ) {
                            avgDecodeTime = (stat.totalDecodeTime  - totDecodeTime)* 1000/ framefps 
                        }
                        totDecodeTime = stat.totalDecodeTime

                        localStorage.avgLost = avgLost
                        // console.log('stats: frames :'+ frames  +  " fps:" + framefps + " bytes:" + bytesReceive + " BM:" + bitrate + "  decode time:" +   avgDecodeTime +  " lost packet:" + totLost + " lost:" + avgLost)
                    }

                }else if (stat.type == "candidate-pair" ) {  //iceserver
                    if ( stat.state == "succeeded"){
                        // console.log(stat)
                        avgRtt = 1000 * stat.currentRoundTripTime;
                        localStorage.avgRtt = avgRtt
                        // console.log ("current rtt:" + avgRtt + " round time :" + stat.currentRoundTripTime)
                    }
                }
            });

          });

    //    console.log(stats)
    }

    p2p.addEventListener('messagereceived', function (e) { // Received data from datachannel.
        console.log(e)
        let deviceConnect = window.getDeviceConnect();
        if(deviceConnect.deviceType == 1){
            var message = JSON.parse(e.message);
            if (message.type === 'cursor') {
                updateCursor(message);
            }else if(message.type == 'shape'){
                var shape = base64ToUint8Array(message.cursor)
                updateCursorShape(shape,message.width,message.heigth)
            }
        }else if(deviceConnect.deviceType == 0){
            if(e.message == "orientation_landscape"){
                screen = 1
                $("#remote video").css("width",'100%').css("height",'100%').css("margin-left",0)
                $("#remote video").css("object-fit","fill")
            }else if(e.message == "orientation_portrait"){
                screen = 2
                let width = document.documentElement.clientWidth;
                let height = document.documentElement.clientHeight;
                $("#remote video").css("width",(720/1280)*height).css("height",height).css("margin-left",(width - (720/1280)*height)/2)
                $("#remote video").css("object-fit","contain")
            }
        }
    });
    $('#keyboard_hint_panel_cover').get(0).addEventListener("mousemove", event => {
        if(framesDecode){
            let deviceConnect = window.getDeviceConnect();
            if(deviceConnect.deviceType == 1){
                let width = $('#remote video').width();
                let height = $('#remote video').height();
                mouseX = event.offsetX;
                mouseY = event.offsetY;
                let parameters = {};
                parameters.x = mouseX;
                parameters.y = mouseY;
                parameters.movementX = event.movementX;
                parameters.movementY = event.movementY;
                parameters.width = width;
                parameters.heigth = height;
                parameters.locked = locked;
                window.sendControlData('mousemove', parameters);
            }else if(deviceConnect.deviceType == 0){
                if(down){
                    startTime = new Date().getTime()

                    if(startTime - endTime >= 50){

                        let index = -1
                        ///找到鼠标按下的索引
                        for ( var i = 0 ; i < brr.length; i ++)
                        {
                            if (brr[i].value == mouseleftbutton)  
                            {
                                index = i 
                                break
                            }
                        }
                        let width = document.documentElement.clientWidth;
                        let height = document.documentElement.clientHeight;
                        let width1 = $('#remote-video').width();
                        let height1 = $('#remote-video').height();
                        ///更新坐标
                        if(screen == 1){
                            brr[index].x = event.pageX/width
                            brr[index].y = event.pageY/height

                        }else if(screen == 2){
                            brr[index].x =(event.pageX - (width - (720/1280)*height)/2)/width1
                            brr[index].y = event.pageY/height1
                        }

                        endTime = startTime


                        data.xy = []
                        let xy = {}
                        data.a = 2
                        data.ps = brr.length


                        for(var i = 0; i < brr.length; i++){
                            data.i = i 
                            data.pid = i  //brr[i].pid 
                            if(screen == 1){
                                xy = {
                                    x:Math.round(brr[i].x*1280) + brr[i].offsetX ,      ///需要加上偏移坐标
                                    y:Math.round(brr[i].y*720) + brr[i].offsetY
                                }
                            }else if(screen == 2){
                                xy = {
                                    x:Math.round(brr[i].x*720) + brr[i].offsetX ,      ///需要加上偏移坐标
                                    y:Math.round(brr[i].y*1280) + brr[i].offsetY
                                }
                            }
                            data.xy.push(xy)
                        }
                        window.sendControlData(data)    ///触控
                    }
                }
            }
        }
        
    });

    
    //键盘按下
    // arr数组 防止多次触发
    // brr数组 存储按键信息 
    $(document).keydown(e => {
        if(framesDecode){
            if(e.ctrlKey&&e.shiftKey&&e.keyCode === 13){
                console.log('呼出菜单')
                $("#stats").show();
                document.exitPointerLock();
            }
            // if(!panel){
            //     if(e.ctrlKey&&e.shiftKey&&e.keyCode === 13){
            //         console.log('呼出菜单')
            //         $("#stats").show();
            //         document.exitPointerLock();
            //     }
            //     // e.preventDefault();
            //     // e.returnValue=false;
            // }
            e.preventDefault();
            e.returnValue=false;
            let deviceConnect = window.getDeviceConnect();
            if(deviceConnect.deviceType == 1){
                window.sendControlData('keydown', {
                    which: e.which
                });
            }else if(deviceConnect.deviceType == 0){
                let width = document.documentElement.clientWidth;
                let height = document.documentElement.clientHeight;
                let width1 = $('#remote-video').width();
                let height1 = $('#remote-video').height();
                let item = e
                let touch = false
                let dir = false                   ///是否为方向键
                let curkey = {
                    "count":0,
                    "type" :1,
                    "value":"",
                    "x":0,
                    "y":0,
                    "offsetX":0,
                    "offsetY":0,
                    "pid":0
                }
                curkey.count = 0                  ///键个数，方向键最多可能是4个
                for(var j =0;j<keyCode.length;j++){
                    if(keyCode[j].type == 1){
                        console.log(111)
                        if(keyCode[j].value == item.which) { 
                            curkey.type = 1            ///普通键
                            curkey.value = item.which
                            curkey.x = keyCode[j].x
                            curkey.y = keyCode[j].y 
                            curkey.offsetX = 0
                            curkey.offsetY = 0
    
                            curkey.count = 1
                            touch = true
                            break;
                        }
                    }else if(keyCode[j].type == 2){
    
                        if( keyCode[j].sub_value_0 == item.which ||
                            keyCode[j].sub_value_1 == item.which ||
                            keyCode[j].sub_value_2 == item.which ||
                            keyCode[j].sub_value_3 == item.which ) {    ///当前按下的是方向键
    
                            dir  = true        ///是方向键
                            curkey.type = 2            
                            curkey.value = item.which
                            curkey.x = keyCode[j].x
                            curkey.y = keyCode[j].y 
    
                            curkey.count = 1       
    
                            if(keyCode[j].sub_value_0 == item.which  ) {///方向 W
                                curkey.offsetX   = 0
                                curkey.offsetY   = 0 - step
                            }
                            else if (keyCode[j].sub_value_1 == item.which ) {///方向A
                                curkey.offsetX   = 0 - step
                                curkey.offsetY   = 0
                            }
                            else if (  keyCode[j].sub_value_2 == item.which ) {  /// 方向 S
                                curkey.offsetX   = 0
                                curkey.offsetY   = step
                            }else if ( keyCode[j].sub_value_3 == item.which) {  /// 方向 D
                                curkey.offsetX   = step
                                curkey.offsetY   = 0
                            }
                            break;
                        }
                    }
                }
                if(!touch && !dir){   ///非触控和方向键
                    return
                }
              
                for(var i = 0;i < arr.length;i++){   ///去除长按一个键的操作
                    if(arr[i].which === item.which){ 
                        return
                    }
                }
                arr.push(item)
    
                if ( touch == true) {  ///   
                    pid += 1
                    curkey.pid = pid   ///记住每一个键的pid
                    brr.push (curkey)
                }
                else if (dir == true){
                    let index = -1
                    for (var k = 0; k < brr.length  ; k ++ ) {  
                        if(brr[k].type == 2) {
                            index = k;
                            break;
                        }
                    }
                    if ( index < 0) { ///没找到方向键
                         curkey.first = true
                         pid = pid +1
                         curkey.pid = pid 
                         brr.push ( curkey)
                    }
                    else { ///找到方向键
                        brr[index].count += curkey.count
                        brr[index].first = false
                        brr[index].offsetX += curkey.offsetX     ///修改方向偏移
                        brr[index].offsetY += curkey.offsetY    
                    }
                }
    
    
                data.xy = []
    
    
    
                let xy = {}
                let len = brr.length       ///当前队列长度
                if ( touch == true){    ///当前是普通键，则按键肯定是加在队列尾部
                    if ( len > 1) { //多指触控
                        data.a = 5
                    }
                    else {
                        data.a = 0   ///单指触控
                    }
                    data.ps  = len ;
                    for(var i = 0; i < len; i++){
                        data.i = i ;
                        data.pid = i;  ///brr[i].pid 
                        xy = {
                            x:Math.round(brr[i].x*1280) + brr[i].offsetX ,      ///需要加上偏移坐标
                            y:Math.round(brr[i].y*720) + brr[i].offsetY
                        }
                        data.xy.push(xy)
                    }
                    window.sendControlData(data)
                }
                else {    ///当前键是一个方向键，需要判断是移动还是点击
                    if (brr[len -1].type == 2 ) {   //最后一个键是方向键
                        if (brr[len -1].first == true ) {   ///第一次按方向键，需要先发点击，再发移动
                            if (len > 1)
                            {
                                data.a = 5
                            }
                            else {
                                data.a = 0
                            }
                            data.ps  = len ;
                            for(var i = 0; i < len; i++){
                                data.i = i ;
                                data.pid = i ; //brr[i].pid 
                                xy = {
                                    x:Math.round(brr[i].x*1280)  ,      ///点击不需要加上偏移坐标
                                    y:Math.round(brr[i].y*720) 
                                }
                                data.xy.push(xy)
                            }
                            window.sendControlData(data)    ///触控
                        } 
                    }
                    ///如果方向键非最后一个键，则方向键肯定已经加入到内存表中，所以只能是移动事件，改变方向
                    ///移动事件
                    data.a = 2 
                    data.ps = len 
                    data.xy = [] 
                    for(var i = 0; i < len; i++){
                        data.i = i 
                        data.pid = i  //brr[i].pid 
                        xy = {
                            x:Math.round(brr[i].x*1280) + brr[i].offsetX ,      ///需要加上偏移坐标
                            y:Math.round(brr[i].y*720) + brr[i].offsetY
                        }
                        data.xy.push(xy)
                    }
                    window.sendControlData(data)    ///触控
                }
            }
        }
        
    });
    //键盘松开
    $(document).keyup(e => {
        if(framesDecode){
            let deviceConnect = window.getDeviceConnect();
            if(deviceConnect.deviceType == 1){
                window.sendControlData('keyup', {
                    which: e.which
                });
            }else if(deviceConnect.deviceType == 0){
                let item = e 
                ///先找到是那个键按弹起
                let moveW = false 
                let moveA = false
                let moveS = false
                let moveD = false

                let touch = false
                let dir = false                   ///是否为方向键
                for(var j =0;j<keyCode.length;j++){
                    if(keyCode[j].type == 1){
                        if(keyCode[j].value == item.which) { 
                            touch = true
                            break;
                        }
                    }else if(keyCode[j].type == 2){

                        if( keyCode[j].sub_value_0 == item.which ||
                            keyCode[j].sub_value_1 == item.which ||
                            keyCode[j].sub_value_2 == item.which ||
                            keyCode[j].sub_value_3 == item.which ) {    ///当前按下的是方向键

                            dir  = true        ///是方向键
                            if(keyCode[j].sub_value_0 == item.which  ) {///方向 W
                                moveW = true
                            }
                            else if (keyCode[j].sub_value_1 == item.which ) {///方向A
                                moveA = true
                            }
                            else if (  keyCode[j].sub_value_2 == item.which ) {  /// 方向 S
                                moveS = true
                            }else if ( keyCode[j].sub_value_3 == item.which) {  /// 方向 D
                                moveD = true
                            }
                            break;
                        }
                    }
                }
                if(!touch && !dir){    ///不是触控键和方向键，则直接返回
                    return
                }

                let index = -1
                for (var i = 0 ; i < brr.length; i ++) {    ///查找在列表中的键
                    if (touch == true ){
                        if ( brr[i].value == item.which){
                            index = i;
                        }
                    }
                    else {
                        if ( brr[i].type == 2){  ///方向键
                            index = i
                        }
                    }
                }
                if ( index < 0){   ///没有找到键，直接返回
                    return 
                }

                for(var i = 0; i < arr.length; i++){     ///从列表中清楚当前键
                    if(e.which == arr[i].which){
                        arr.splice(i,1)
                    }
                }


                data.xy = []
                let xy = {}
                let len =  brr.length

                if ( touch == true)  { ///触控键
                    if (len > 1){
                        data.a = 6
                    }
                    else {
                        data.a = 1
                    }
                    data.ps  = len 
                    data.pid = index // brr[index].pid    ///按下时的pid
                    data.i   = index 
                    for(var i = 0; i < brr.length; i++){
                        xy = {
                            x:Math.round(brr[i].x*1280) + brr[i].offsetX,
                            y:Math.round(brr[i].y*720) + brr[i].offsetY
                        }
                        data.xy.push(xy)
                    }
                    
                    window.sendControlData(data)
                    brr.splice(index,1)   ///从列表中删除

                }else if ( dir == true) {   ///方向键，比较复杂，如果当前按下的是多个方向键，则弹起一个后，其它还是move操作，如果最后一个方向键弹起，则是正常弹起操作
                
                    brr[index].count -= 1    ///方向键减少1个

                    console.log("dir key count is:" + brr[index].count)

                    if (brr[index].count > 0) { //多个方向键弹出一个，还是move，只是改变方向
                        if (moveW == true){
                            brr[index].offsetY += step 
                        } 
                        else if ( moveA == true){
                            brr[index].offsetX += step 
                        }
                        else if (moveS == true) {
                            brr[index].offsetY -= step  
                        }
                        else if ( moveD == true){
                            brr[index].offsetX -= step 
                        }
                        data.a = 2             ///移动


                    }else {       ///最后一个方向键弹起
                        if (len > 1){
                            data.a = 6
                        }
                        else {
                            data.a = 1
                        }
                    }
                    data.ps = brr.length
                    data.i =  index
                    data.pid = index     /// brr[index].pid
                    for(var i = 0; i < brr.length; i++){
                        xy = {
                            x:Math.round(brr[i].x*1280) + brr[i].offsetX,
                            y:Math.round(brr[i].y*720) + brr[i].offsetY
                        }
                        data.xy.push(xy)
                    }

                
                    window.sendControlData(data)
        
                    if (brr[index].count == 0 ) {   ///没有方向键按下，需要删除内存键
                        brr.splice(index,1)  ///删除当前键
                    }
                }
                if (brr.length == 0){   ///队列已经空了
                    pid = 0             /// pid 从头开始计数
                }
            }
        }
        
    });

    document.addEventListener('wheel', e => {
        // console.log(e)
        window.sendControlData('wheel', {
            deltaX: e.deltaX,
            deltaY: e.deltaY,
            deltaZ: e.deltaZ
        });
    });
    //左键按下 
    $('#keyboard_hint_panel_cover').mousedown(e => {
        if(framesDecode){
            if(locked == 1){
                let canvas = $("#keyboard_hint_panel_cover").get(0);
                canvas.requestPointerLock = canvas.requestPointerLock || canvas.mozRequestPointerLock || canvas.webkitRequestPointerLock; 
                canvas.requestPointerLock();
            }
            let deviceConnect = window.getDeviceConnect();
            if(deviceConnect.deviceType == 1){
                let vdo = $("#remote-video")[0]; //jquery
                vdo.muted = false;
                $('#remote-video').bind('dragstart',function() { return false; });
                $('#remote-video').bind('contextmenu',function() { return false; });
                window.sendControlData('mousedown', {
                    which: e.which,
                    x: mouseX,
                    y: mouseY
                });
            }else if(deviceConnect.deviceType == 0){
                down = true
                let vdo1 = $("#remote-video")[0]; //jquery
                vdo1.muted = false;
                $('#remote-video').bind('dragstart',function() { return false; });
                $('#remote-video').bind('contextmenu',function() { return false; });
    
                if(e.button ==0){
                    let width = document.documentElement.clientWidth;
                    let height = document.documentElement.clientHeight;
                    let width1 = $('#remote-video').width();
                    let height1 = $('#remote-video').height();

                    let curkey = {
                        "count":1,
                        "type" :1,
                        "value":mouseleftbutton,
                        "x":0,
                        "y":0,
                        "offsetX":0,
                        "offsetY":0,
                        "pid":0
                    }
                    
                    ///计算坐标
                    if(screen == 1){
                        curkey.x = e.pageX/width
                        curkey.y = e.pageY/height
                    }else if(screen == 2){
                        curkey.x = (e.pageX - (width - (720/1280)*height)/2)/width1
                        curkey.y = e.pageY/height1
                    }

                    brr.push (curkey) 
                    console.log(brr)
                    let len = brr.length       ///当前队列长度
                    console.log(len)
                    if ( len > 1) { //多指触控
                        data.a = 5
                    }
                    else {
                        data.a = 0   ///单指触控
                    }
                    data.ps  = len ;
                    data.xy =[]

                    for(var i = 0; i < len; i++){
                        data.i = i ;
                        data.pid = i;  ///brr[i].pid 
                        if(screen == 1){
                            xy = {
                                x:Math.round(brr[i].x*1280) + brr[i].offsetX ,      ///需要加上偏移坐标
                                y:Math.round(brr[i].y*720) + brr[i].offsetY
                            }
                        }else if(screen == 2){
                            xy = {
                                x:Math.round(brr[i].x*720) + brr[i].offsetX ,      ///需要加上偏移坐标
                                y:Math.round(brr[i].y*1280) + brr[i].offsetY
                            }
                        }
                        data.xy.push(xy)
                    }
                    console.log('x：'+e.pageX/width,'y：' +e.pageY/height)
                    window.sendControlData(data)
                    // console.log(JSON.stringify(data))

                }
            }
        }
        
        
    });
    //左键松开
    $('#keyboard_hint_panel_cover').mouseup(e => {
        if(framesDecode){
            let deviceConnect = window.getDeviceConnect();
            if(deviceConnect.deviceType == 1){
                window.sendControlData('mouseup', {
                    which: e.which,
                    x: mouseX,
                    y: mouseY
                });
            }else if(deviceConnect.deviceType == 0){
                down = false
                if(e.button == 0){
                    let index = -1   ///鼠标在列表中的位置
                    for ( var i = 0 ; i < brr.length; i ++)
                    {
                        if ( brr[i].value == mouseleftbutton )   ///查找鼠标左键
                        {
                            index = i 
                            break
                        }
                    }
                    if ( index < 0 )   ///没有找到鼠标，直接退出
                    {
                        return 
                    }

                    let width = document.documentElement.clientWidth;
                    let height = document.documentElement.clientHeight;
                    let width1 = $('#remote-video').width();
                    let height1 = $('#remote-video').height();

                    if(screen == 1){
                        brr[i].x =  e.pageX/width
                        brr[i].y =  e.pageY/height
                    }else if(screen == 2){
                        brr[i].x =  (e.pageX - (width - (720/1280)*height)/2)/width1 
                        brr[i].y =  e.pageY/height1
                    }

                    data.xy = []
                    let xy = {}

                    data.i = index 
                    data.pid = index 
                    data.ps = brr.length
                    if(brr.length == 1){
                        data.a = 1
                    }else if(arr.length > 1){
                        data.a = 6
                    }

                    for(var i = 0; i < brr.length; i++){
                        data.i = i ;
                        data.pid = i;  ///brr[i].pid 
                        if(screen == 1){
                            xy = {
                                x:Math.round(brr[i].x*1280) + brr[i].offsetX ,      ///需要加上偏移坐标
                                y:Math.round(brr[i].y*720) + brr[i].offsetY
                            }
                        }else if(screen == 2){
                            xy = {
                                x:Math.round(brr[i].x*720) + brr[i].offsetX ,      ///需要加上偏移坐标
                                y:Math.round(brr[i].y*1280) + brr[i].offsetY
                            }
                        }
                        
                        data.xy.push(xy)
                    }
                    console.log(111111)
                    console.log('x：'+e.pageX/width,'y：' +e.pageY/height)
                    window.sendControlData(data)
                    brr.splice(index,1)
                    console.log(brr)
                }
            }
        }
        
       
    });
    $(".stats-background").bind("click", function(){
        $("#stats").hide();
    })
    document.addEventListener('contextmenu', event => event.preventDefault()); // Disable context menu.

}
window.onbeforeunload = function() {
    if(connected){
        let deviceConnect = window.getDeviceConnect();
        p2p.stop(deviceConnect.connectId);
    }
};
function keyConfig(){
    if(localStorage.ConfigList){
        $("#keyboard").empty()
        keyCode = JSON.parse(JSON.parse(localStorage.ConfigList))
        let width = document.documentElement.clientWidth;
        let height = document.documentElement.clientHeight;
        for (let i = 0; i < keyCode.length; i++) {
            if(keyCode[i].type == 1){
                $("#keyboard").append('<span class="circle" style="top:' + (keyCode[i].y*height - 21) + 'px;left: ' + (keyCode[i].x*width - 21) + 'px;">'
                +'<span class="textBtn">'
                + keyCode[i].name
                +'</span>'
                +'</span>');
            }else if(keyCode[i].type == 2){
                $("#keyboard").append('<span class="joypad" style="top:' + (keyCode[i].y*height - 60) + 'px;left: ' + (keyCode[i].x*width - 60) + 'px;">'
                +'</span>');
            }
        }
    }else{
        $("#keyboard").css("display","none")
    }
}
//释放设备
window.release = function(){
    connected = false;
    p2p.disconnect();
}

window.sendControlData = function(event, parameters) {
    let deviceConnect = window.getDeviceConnect();
    if(deviceConnect.deviceType == 1){
        if (connected)
            p2p.send(deviceConnect.connectId, JSON.stringify({
                type: 'control',
                data: {
                    event: event,
                    parameters: parameters
                }
            }));
            // console.log(JSON.stringify(parameters))
    }else if(deviceConnect.deviceType == 0){
        if(connected){
            p2p.send(deviceConnect.connectId, JSON.stringify(event));
            // let a = encodeURI(JSON.stringify(event))
            // let b = btoa(a)
            // console.log(b)
            console.log(JSON.stringify(event))
        }
    }
};
//全屏
window.fullScreen = function() {
    let dE = $('.run').get(0);
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
}
//退出全屏方法
window.exitFullScreen = function() {
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
}
//数据整理
function base64ToUint8Array(base64String) {
    let padding = '='.repeat((4 - base64String.length % 4) % 4);
    let base64 = (base64String + padding).replace(/\-/g, '+').replace(/_/g, '/');
    let rawData = window.atob(base64);
    let outputArray = new Uint8Array(rawData.length);

    for (let i = 0; i < rawData.length; ++i) {
        outputArray[i] = rawData.charCodeAt(i);
    }
    return outputArray;
}
function updateCursorShape(shape, col, row) {
    let bgraArray = shape; // Server side doesn't do base64 encoding for now.
    let canvas = document.getElementById('cursor');
    let ctx = canvas.getContext('2d');
    ctx.canvas.width = col;
    ctx.canvas.height = row;
    ctx.clearRect(0, 0, row, col);
    for (let i = 0; i < row; i++) {
        for (let j = 0; j < col; j++) {
        let bgraIndex = (i * row + j) * 4;
        ctx.fillStyle = 'rgba(' + bgraArray[bgraIndex + 2] + ',' + bgraArray[bgraIndex + 1] + ',' + bgraArray[bgraIndex] + ',' + bgraArray[bgraIndex + 3] + ')';
        ctx.fillRect(j, i, 1, 1);
        }
    }
    let pngUrl = canvas.toDataURL('image/png');
    document.getElementById('keyboard_hint_panel_cover').style.cursor = 'url(' + pngUrl + '),auto';
    // document.getElementById('remote-video').css('background-blend-mode','color-dodge')
};
function updateCursor(message) {
    if(message.cursor === 20){
        let canvas = $("#keyboard_hint_panel_cover").get(0);
        canvas.requestPointerLock = canvas.requestPointerLock || canvas.mozRequestPointerLock || canvas.webkitRequestPointerLock;
        canvas.requestPointerLock();
        locked = 1
    }else{
        let element = document.pointerLockElement;
        if(element){
            document.exitPointerLock();
        }
        locked = 0
        if(message.cursor === 1){
            $("#keyboard_hint_panel_cover").css("cursor","text")
        }else if(message.cursor === 2){
            $("#keyboard_hint_panel_cover").css("cursor","wait")
        }else if(message.cursor === 3){
            $("#keyboard_hint_panel_cover").css("cursor","crosshair")
        }else if(message.cursor === 4){
            $("#keyboard_hint_panel_cover").css("cursor","nw-resize")
        }else if(message.cursor === 5){
            $("#keyboard_hint_panel_cover").css("cursor","ne-resize")
        }else if(message.cursor === 6){
            $("#keyboard_hint_panel_cover").css("cursor","e-resize")
        }else if(message.cursor === 7){
            $("#keyboard_hint_panel_cover").css("cursor","n-resize")
        }else if(message.cursor === 8){
            $("#keyboard_hint_panel_cover").css("cursor","move")
        }else if(message.cursor === 10){
            $("#keyboard_hint_panel_cover").css("cursor","pointer")
        }else if(message.cursor === 12){
            $("#keyboard_hint_panel_cover").css("cursor","help")
        }else if(message.cursor === 22){
            $("#keyboard_hint_panel_cover").css("cursor","none")
        }else if(message.cursor === 21 || message.cursor === 24){
            
        }else{
            $("#keyboard_hint_panel_cover").css("cursor","auto")
        }
    }
};

document.addEventListener('fullscreenchange', fullscreenchange);
document.addEventListener('webkitfullscreenchange', fullscreenchange);
document.addEventListener('mozfullscreenchange', fullscreenchange);
function fullscreenchange(){
    let isFullScreen = document.fullScreen || document.mozFullScreen || document.webkitIsFullScreen;
    // console.log(keyboard)
    if(isFullScreen) {
        console.log('全屏')
        panel = true
        setTimeout(function () {
            keyConfig()
        }, 100);
        if(keyboard){
            keyboard.lock(['Escape','F11'])
        }
        if(locked == 1){
            console.log(11111111)
            let canvas = $("#keyboard_hint_panel_cover").get(0);
            canvas.requestPointerLock = canvas.requestPointerLock || canvas.mozRequestPointerLock || canvas.webkitRequestPointerLock; 
            canvas.requestPointerLock();
        }
        $("#stats").hide();
    }else {
        console.log('非全屏')
        panel = false
        setTimeout(function () {
            keyConfig()
        }, 100);
        if(keyboard){
            keyboard.unlock();
        }
        $("#stats").show();
        // if(relativeMovementMode) {
        //     $('#remote video').get(0).requestPointerLock(); 
        // }
    }
}
//手柄
var hasGP = false;
var repGP;
function canGame() {
    return "getGamepads" in navigator;
}
var keyStatus = {
    a:{index:0,val:1, status:false, time:0},
    b:{index:1,val:2, status:false, time:0},
    x:{index:2,val:3, status:false, time:0},
    y:{index:3,val:4, status:false, time:0},
    LB:{index:4,val:5, status:false, time:0},
    RB:{index:5,val:6, status:false, time:0},
    back:{index:8,val:7, status:false, time:0},
    start:{index:9,val:8, status:false, time:0},
    lefttrue:{index:10,val:9, status:false, time:0},
    righttrue:{index:11,val:10, status:false, time:0},
    up:{index:12,val:11, status:false, value:10, time:0},
    down:{index:13,val:12, status:false, value:11,time:0},
    left:{index:14,val:13, status:false, value:12, time:0},
    right:{index:15,val:14, status:false, value:13,time:0},


    LT:{index:6,val:15, status:false, value:0, time:0},
    RT:{index:7,val:16, status:false, value:1,time:0},
    
    
    l_left:{status:false, time:0},
    l_right:{status:false, time:0},
    l_up:{status:false, time:0},
    l_down:{status:false, time:0},
    r_left:{status:false, time:0},
    r_right:{status:false, time:0},
    r_up:{status:false, time:0},
    r_down:{status:false, time:0},
    lt:{status:false,time:0},
    rt:{status:false,time:0}
};
function applyDeadZone(number, threshold) {
    var percentage = (Math.abs(number) - threshold) / (1 - threshold);
    if( percentage < 0){
        percentage = 0;
    }
    return percentage* (number > 0 ? 1: -1);
}
function getStickStatus(x, y, threshold){
    var status = {left:false, right:false, up:false, down:false};
    var stickX = applyDeadZone(x, threshold);
    if (stickX > 0){
        status.right = true;
    }
    else if (stickX < 0) {
        status.left = true;
    }
    var stickY = applyDeadZone(y, threshold);
    if (stickY > 0){
        status.down = true;
    }
    else if (stickY < 0) {
        status.up = true;
    }
    return status;
}
function reportOnGamepad() {
    var parameters;
    var ltvalue;
    var rtvalue;
    var gp = navigator.getGamepads()[0];
    var html = "";
    html += "id: " + gp.id + "<br/>";
    var curDate = new Date();
    var time = curDate.getTime();
    var d_status = getStickStatus(gp.axes[0], gp.axes[1], 0.2);
    var f_status = getStickStatus(gp.axes[2], gp.axes[3], 0.2);

    if (keyStatus.l_left.status && !(d_status.left)) {
        parameters = {};
        parameters.lx = 0;
        parameters.ly = 0;
        // window.sendControlData('vjoykeyup', {which: keyStatus.left.val});
        window.sendControlData('vjoylstick', parameters);
        //window.sendControlData('vjoylstick', parameters);
        console.log("left key up event");
        console.log(parameters)
        keyStatus.l_left.status = d_status.left;
        // alert("l_left/up"+gp.axes[0]*10000+gp.axes[1]*10000+keyStatus.left.val);
    }
    if (keyStatus.l_right.status && !(d_status.right)) {
        parameters = {};
        parameters.lx = 0;
        parameters.ly = 0;
        // window.sendControlData('vjoykeyup', {which: keyStatus.right.val});
        window.sendControlData('vjoylstick', parameters);
        console.log("right key up event");
        console.log(parameters)
        keyStatus.l_right.status = d_status.right;
        // alert("l_right/down"+keyStatus.right.val);
    }
    if (d_status.left) {
        parameters = {};
        parameters.lx = gp.axes[0];
        parameters.ly = gp.axes[1];
        
        // window.sendControlData('vjoykeydown', {which: keyStatus.left.val});
        window.sendControlData('vjoylstick', parameters);
        keyStatus.l_left.status = d_status.left;
        // keyStatus.r_left.status = d_status.left;
        console.log("left key down event");
        console.log(parameters)
        // alert("left/up"+keyStatus.left.val);
    }
    if (d_status.right) {
        parameters = {};
        parameters.lx = gp.axes[0];
        parameters.ly = gp.axes[1];
        console.log(parameters)
        window.sendControlData('vjoylstick', parameters);
        // window.sendControlData('vjoykeydown', {which: keyStatus.right.val});
        keyStatus.l_right.status = d_status.right;
        //keyStatus.r_right.status = d_status.right;
        console.log("right key down event");
        console.log(parameters)
        // alert("right/down"+keyStatus.right.val);
    }
    if (keyStatus.l_up.status && !(d_status.up)) {
        parameters = {};
        parameters.lx = 0;
        parameters.ly = 0;
        // window.sendControlData('vjoykeyup', {which: keyStatus.up.val});
        window.sendControlData('vjoylstick', parameters);
        console.log("up key up event");
        console.log(parameters)
        keyStatus.l_up.status = d_status.up;
    }
    if (keyStatus.l_down.status && !(d_status.down)) {
        parameters = {};
        parameters.lx = 0;
        parameters.ly = 0;
        // window.sendControlData('vjoykeyup', {which: keyStatus.down.val});
        window.sendControlData('vjoylstick', parameters);
        console.log("down key up event");
        console.log(parameters)
        keyStatus.l_down.status = d_status.down;
        // alert("l_down/down"+keyStatus.down.val);
    }
    if (d_status.up) {
        parameters = {};
        parameters.lx = gp.axes[0];
        parameters.ly = gp.axes[1];
        // window.sendControlData('vjoykeydown', {which: keyStatus.up.val});
        window.sendControlData('vjoylstick', parameters);
        keyStatus.l_up.status = d_status.up;
        //keyStatus.r_up.status = d_status.up;
        console.log("up key down event");
        console.log(parameters)
        // alert("left/up"+keyStatus.up.val);
    }
    if (d_status.down) {
        parameters = {};
        parameters.lx = gp.axes[0];
        parameters.ly = gp.axes[1];
        // window.sendControlData('vjoykeydown', {which: keyStatus.down.val});
        window.sendControlData('vjoylstick', parameters);
        keyStatus.l_down.status = d_status.down;
        // keyStatus.r_down.status = d_status.down;
        console.log("down key down event");
        console.log(parameters)
        //alert("right/down"+keyStatus.down.val);
    }
    if (keyStatus.r_left.status && !(f_status.left)) {
        parameters = {};
        // parameters.rx = 0.2;
        // parameters.ry = 0.2;
        parameters.rx = 0;
        parameters.ry = 0;
        // window.sendControlData('vjoykeyup', {which: keyStatus.left.value});
        window.sendControlData('vjoyrstick', parameters);
        console.log("left key up event");
        console.log(parameters)
        keyStatus.r_left.status = f_status.left;
    }
    if (keyStatus.r_right.status && !(f_status.right)) {
        parameters = {};
        // parameters.rx = 0.2;
        // parameters.ry = 0.2;
        parameters.rx = 0;
        parameters.ry = 0;
        // window.sendControlData('vjoykeyup', {which: keyStatus.right.value});
        window.sendControlData('vjoyrstick', parameters);
        console.log("right key up event");
        console.log(parameters)
        keyStatus.r_right.status = f_status.right;

    }
    if (f_status.left) {
        parameters = {};
        parameters.rx = gp.axes[2];
        parameters.ry = gp.axes[3];
        window.sendControlData('vjoyrstick', parameters);
        // window.sendControlData('vjoykeydown', {which: keyStatus.left.value});
        keyStatus.r_left.status = f_status.left;
        console.log("left key down event");
        console.log(parameters)
    }
    if (f_status.right) {
        parameters = {};
        parameters.rx = gp.axes[2];
        parameters.ry = gp.axes[3];
        window.sendControlData('vjoyrstick', parameters);
        // window.sendControlData('vjoykeydown', {which: keyStatus.right.value});
        console.log("right key down event");
        console.log(parameters)
        keyStatus.r_right.status = f_status.right;
    }
    if (keyStatus.r_up.status && !(f_status.up)) {
        parameters = {};
        // parameters.rx = 0.2;
        // parameters.ry = 0.2;
        parameters.rx = 0;
        parameters.ry = 0;
        // window.sendControlData('vjoykeyup', {which: keyStatus.up.value});
        window.sendControlData('vjoyrstick', parameters);
        console.log("up key up event");
        console.log(parameters)
        keyStatus.r_up.status = f_status.up;
    }
    if (keyStatus.r_down.status && !(f_status.down)) {
        parameters = {};
        // parameters.rx = 0.2;
        // parameters.ry = 0.2;
        parameters.rx = 0;
        parameters.ry = 0;
        // window.sendControlData('vjoykeyup', {which: keyStatus.down.value});
        window.sendControlData('vjoyrstick', parameters);
        console.log("down key up event");
        console.log(parameters)
        keyStatus.r_down.status = f_status.down;
    }
    if (f_status.up) {
        parameters = {};
        parameters.rx = gp.axes[2];
        parameters.ry = gp.axes[3];
        // window.sendControlData('vjoykeydown', {which: keyStatus.up.value});
        window.sendControlData('vjoyrstick', parameters);
        keyStatus.r_up.status = f_status.up;
        console.log("up key down event");
        console.log(parameters)
    }
    if (f_status.down) {
        parameters = {};
        parameters.rx = gp.axes[2];
        parameters.ry = gp.axes[3];
        // window.sendControlData('vjoykeydown', {which: keyStatus.down.value});
        window.sendControlData('vjoyrstick', parameters);
        console.log("down key down event");
        console.log(parameters)
        keyStatus.r_down.status = f_status.down;
    }

    for (var i = 0; i < gp.buttons.length; i++) {
        html += "Button " + (i + 1) + ": ";
        if (gp.buttons[i].pressed) console.log("button " + i + " pressed");
        var keyVal = null;
        switch (i) {
        case keyStatus.a.index:
            keyVal = keyStatus.a;
            break;
        case keyStatus.b.index:
            keyVal = keyStatus.b;
            break;
        case keyStatus.x.index:
            keyVal = keyStatus.x;
            break;
        case keyStatus.y.index:
            keyVal = keyStatus.y;
            break;
        case keyStatus.LB.index:
            keyVal = keyStatus.LB;
            break;
        case keyStatus.RB.index:
            keyVal = keyStatus.RB;
            break;
        case keyStatus.back.index:
            keyVal = keyStatus.back;
            break;
        case keyStatus.start.index:
            keyVal = keyStatus.start;
            break;
        case keyStatus.LT.index:
            keyVal = keyStatus.LT;
            break;
        case keyStatus.RT.index:
            keyVal = keyStatus.RT;
            break;
        case keyStatus.lefttrue.index:
            keyVal = keyStatus.lefttrue;
            break;
        case keyStatus.righttrue.index:
            keyVal = keyStatus.righttrue;
            break;
        case keyStatus.up.index:
            keyVal = keyStatus.up;
            break;
        case keyStatus.down.index:
            keyVal = keyStatus.down;
            break;
        case keyStatus.left.index:
            keyVal = keyStatus.left;
            break;
        case keyStatus.right.index:
            keyVal = keyStatus.right;
            break;
        default:
            break;
        }
        if (keyVal && keyVal.status == true && gp.buttons[i].pressed == true) {
            if (keyVal.val === 15){
                ltvalue =gp.buttons[6].value;
                // rtvalue =gp.buttons[7].value;
                keyVal.time = time;
                window.sendControlData('vjoyltrigger',{trigger: ltvalue});
                // window.sendControlData('vjoyrtrigger',{trigger: rtvalue});
                // window.sendControlData('vjoykeydown', {which: keyVal.val});
                keyVal.status = gp.buttons[i].pressed;
                //console.log("key up event");
            }else if(keyVal.val === 16){
                rtvalue =gp.buttons[7].value;
                window.sendControlData('vjoyrtrigger',{trigger: rtvalue});
                keyVal.time = time;
                keyVal.status = gp.buttons[i].pressed;
            } else {
                keyVal.time = time;
                window.sendControlData('vjoykeydown', {which: keyVal.val});
                console.log(1)
                console.log(keyVal.val)
                keyVal.status = gp.buttons[i].pressed;
                //console.log("key up event");
            }
        }

        if (keyVal && keyVal.status == true && gp.buttons[i].pressed == false) {
            if (keyVal.val === 15) {
                ltvalue =gp.buttons[6].value;
                // rtvalue =gp.buttons[7].value;
                keyVal.time = time;
                window.sendControlData('vjoyltrigger',{trigger: ltvalue});
                // window.sendControlData('vjoyrtrigger',{trigger: rtvalue});
                // window.sendControlData('vjoykeydown', {which: keyVal.val});
                keyVal.status = gp.buttons[i].pressed;
                //console.log("key up event");
            }else if(keyVal.val === 16){
                rtvalue =gp.buttons[7].value;
                window.sendControlData('vjoyrtrigger',{trigger: rtvalue});
                keyVal.time = time;
                keyVal.status = gp.buttons[i].pressed;
            }else {
                window.sendControlData('vjoykeyup', {which: keyVal.val});
                keyVal.status = gp.buttons[i].pressed;
                keyVal.time = time;
                console.log(2)
                console.log(keyVal.val)
                //console.log("key up event");
            }
        }

        if (keyVal && keyVal.status == false && gp.buttons[i].pressed == true) {
            if (keyVal.val === 15) {
                ltvalue =gp.buttons[6].value;
                // rtvalue =gp.buttons[7].value;
                keyVal.time = time;
                window.sendControlData('vjoyltrigger',{trigger: ltvalue});
                // window.sendControlData('vjoyrtrigger',{trigger: rtvalue});
                // window.sendControlData('vjoykeydown', {which: keyVal.val});
                keyVal.status = gp.buttons[i].pressed;
                //console.log("key down event");
            } else if(keyVal.val === 16){
                rtvalue =gp.buttons[7].value;
                window.sendControlData('vjoyrtrigger',{trigger: rtvalue});
                keyVal.time = time;
                keyVal.status = gp.buttons[i].pressed;
            }else {
                window.sendControlData('vjoykeydown', {which: keyVal.val});
                keyVal.status = gp.buttons[i].pressed;
                keyVal.time = time;
                console.log(3)
                console.log(keyVal.val)
                //console.log("key down event");
            }
        }
    }
    for (var j = 0; j < gp.axes.length; j += 2) {
        html += "Stick " + (Math.ceil(j / 2) + 1) + ": " + gp.axes[j] + "," + gp.axes[j + 1] + "<br/>";
    }
}
if(canGame()) {
    var prompt = "To begin using your gamepad, connect it and press any button!";
    $('#gamepadPrompt').text(prompt);
    $(window).on("gamepadconnected", function() {
        hasGP = true;
        console.log("connection event");
        window.clearInterval(repGP);
        repGP = window.setInterval(reportOnGamepad,150);
    });
    $(window).on("gamepaddisconnected", function() {
        console.log("disconnection event");
        window.clearInterval(repGP);
    });
    //setup an interval for Chrome
    var checkGP = window.setInterval(function() {
        //console.log('checkGP');
        if(navigator.getGamepads()[0]) {
            if(hasGP) $(window).trigger("gamepadconnected");
            window.clearInterval(checkGP);
        }
    }, 500);
}