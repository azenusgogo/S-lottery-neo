define(function(){
    var Timer = function(options){
        options || (options = {});
        this.range = options.range || [
            86400000, //天
            3600000, //时
            60000, //分
            1000 //秒
        ];
        this.endTime = options.endTime; // 结束时间
        this.url = options.url || null; // 同步url
        this.step = 1000; // 执行速度
        this.isStop = false; // 是否停止
        this.serverTime = options.serverTime || null; // 静态同步时间
        this.render = options.render || function(){}; // 渲染callback 返回格式化后的range
        this.onTimerEnd = options.onTimerEnd || function(){}; // 结束callback
        this.$el = $(options.el);
        this.el = this.$el[0];
    };
    Timer.prototype = {
        constructor:Timer,
        sync:function(url,fn){
            if (typeof url == "function"){
                fn = url;
                url = this.url
            }
            if (url){
                var _this = this;
                $.get(url,function(time){
                    // 同步时间 更新 endTime
                    _this.endTime -= (+new Date - time);
                    fn && fn.call(_this)
                }).error(function() { // 防止意外终止
                    //_this.endTime += 5000 // 测试
                    fn && fn.call(_this)
                })
            }else {
                this.serverTime && (this.endTime -= (this.serverTime - +new Date));
                fn && fn.call(this)
            }
        },
        start:function(){
            var endTime = this.endTime - +new Date;
            if (this.isStop || endTime<0) { // 停止
                this.isStop = false;
                this.onTimerEnd();
                return
            };
            var range = this.range;
            var time = [];
            for (var i=0; i<range.length; i++){
                time.push(Math.floor(endTime/range[i]));
                endTime %= range[i]
            };
            //this.render(time,this.$el,this.el);
            this.render(time);
            var _this = this;
            setTimeout(function(){
                _this.start()
            },this.step)
            
        },
        stop:function(){
            this.isStop = true
        },
        update:function(key,val){
            var attrs;
            if (typeof key === 'string') {
                (attrs = {})[key] = val;
                key = attrs
            }
            _.extend(this,key)
        }
    }
    return Timer
})