//获取当前窗口的主机名
// var ip = '';
var host = window.location.host;
console.log(host)
// if(host == 'localhost:10002'){
//     ip = 'http://192.168.1.8:8081'
// }else if(host == '192.168.1.201'){
//     ip = 'http://192.168.1.201:8080'
// }
//API SOURCE
window.GLOBALCONFIG = {
    APIHOST:{
        "API":'http://124.71.5.200:8084',
        //"API":'https://api.yidianwan.cn',
        "IMG":'https://files.yidianwan.cn'
    }
};


//获取当前窗口的端口
// var port = window.location.port;


//global setting
window.SETTING = {
    "pageNum":"10",
};

// STATIC URL
window.STATIC_URL = "./frontend_static/";
var browser=navigator.appName;
var b_version=navigator.appVersion;
var version=b_version.split(";");
var trim_Version;
if(version[1]){
    trim_Version=version[1].replace(/[ ]/g,"");
}
if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0"){
    alert("您的浏览器暂不支持云平台，请使用Internet explorer v10.0 及以上版本浏览器");
    document.execCommand("Stop");
}else{
    (function(){
        var config = document.getElementById("page-config");
        if(config){
            var js,css;
            if(config.getAttribute("data-css")){
                css = config.getAttribute("data-css").replace(/^\s+|\s+$/g, "").split(/\s+/).map(function(i){
                    return "<link rel=\"stylesheet\" href=\"" + STATIC_URL + i + "\">";
                });
                document.write(css.join(""));
            }
            if(config.getAttribute("data-js")){
                js = config.getAttribute("data-js").replace(/^\s+|\s+$/g, "").split(/\s+/).map(function(i){
                    return "<script src=\"" + STATIC_URL + i + "\"></" + "script>";
                });
                document.write(js.join(""));
            }
        }
    })();
}
