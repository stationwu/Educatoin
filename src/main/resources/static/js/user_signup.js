var app = new Vue({
    el: '#signup',
    data: {
        children : [
            { name: "", birthday: "", numberOfClasses: 0 }
        ],
        mobilePhone : "",
        verificationCode : "",
        address: "",
        removable : false,
    },
    methods: {
        removeChild : function(index, child) {
            app.children.splice(index, 1);
            if (app.children.length == 1) {
                app.removable = false;
            }
        },
        addChild: function() {
            app.children.push({ name: "", birthday: "", numberOfClasses: 0 });
            if (app.children.length > 1) {
                app.removable = true;
            }
        },
        onClickGetVerificationCode : function() {
            console.log("get veri code clicked");
        },
        onAddStudent: function(){
        	var openCode = $('#wxOpenCode').text();
        	var customer = {
                "openCode"     : openCode,
                "children"     : []
            };
        	
            for(var i = 0; i < app.children.length; ++i) {
                customer.children.push({
                    "childName"   : app.children[i].name,
                    "birthday"    : app.children[i].birthday,
                    "classPeriod" : app.children[i].numberOfClasses
                });
            }
            
            $.ajax({
                url: '/api/v1/Student',
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify(customer),
            }).done(function(response) {
                //window.location.href = "/user/center";
            });
        },
        onSubmit: function() {
            var openCode = $('#wxOpenCode').text();
            var customer = {
                "openCode"     : openCode,
                "name"         : app.children[0].name + "家长",
                "mobilePhone"  : app.mobilePhone,
                "address"      : app.address,
                "children"     : []
            };
            for(var i = 0; i < app.children.length; ++i) {
                customer.children.push({
                    "childName"   : app.children[i].name,
                    "birthday"    : app.children[i].birthday,
                    "classPeriod" : app.children[i].numberOfClasses
                });
            }

            $.ajax({
                url: '/api/v1/Customer/SignUp',
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify(customer),
            }).done(function(response) {
                //window.location.href = "/user/center";
            });
        },
    }
});