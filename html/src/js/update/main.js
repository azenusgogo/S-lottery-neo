define([
    "update/common",
    "update/highfrequency"
],function(common, highfrequency){
  
    return function(){
        common(); // 普通彩
        highfrequency(); // 高频彩
    }
})