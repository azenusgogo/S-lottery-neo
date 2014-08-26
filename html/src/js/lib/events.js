define(function() {
    var eventTarget = {
        handlers : {},
        on: function(type, handler) {
            if (typeof this.handlers[type] == "undefined") {
                this.handlers[type] = []
            }
            this.handlers[type].push(handler)
        },
        off: function(type, handler) {
            if (this.handlers[type] instanceof Array) {
                var handlers = this.handlers[type];
                for (var i = 0, len = handlers.length; i < len; i++) {
                    if (handlers[i] === handler) {
                        break
                    }
                }
                Handlers.splice(i, 1)
            }
        },
        trigger: function(e) {
            if (!e.target) {
                e.target = this
            }
            if (this.handlers[e.type] instanceof Array) {
                var handlers = this.handlers[e.type];
                for (var i = 0, len = handlers.length; i < len; i++) {
                    handlers[i](e)
                }
            }
        }
    }
    return eventTarget
})