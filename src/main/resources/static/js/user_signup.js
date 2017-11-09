var app = new Vue({
    el: '#children',
    data: {
        children : [
            { removable : false }
        ]
    },
    methods: {
        removeChild : function(index, child) {
            app.children.splice(index, 1);
        },
        addChild: function() {
            app.children.push({removable : true});
        }
    }
});