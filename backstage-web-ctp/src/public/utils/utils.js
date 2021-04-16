const dateTimeUtils = function dateUtils(dateTime){
    var dateTimeData="";
    var h = Math.floor(dateTime / 60);
    dateTime -= h * 60;
    if (dateTime == 0) {
        dateTimeData = h ? h + "小时" : "0分钟";
    } else {
        if (dateTime < 10) {
            dateTime = dateTime;
        }
        dateTimeData =  (h ? h + "小时" : "") + (dateTime ? dateTime + "分钟" : "");
    }
    return dateTimeData;
}
export default dateTimeUtils;