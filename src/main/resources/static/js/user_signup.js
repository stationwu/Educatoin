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
        onSubmit: function() {
            var openCode = $('#wxOpenCode').text();
            $.ajax({
                url: '/api/v1/Customer',
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify({
                          "openCode"     : openCode+"abc",
                          "name"         : openCode,
                          "mobilePhone"  : app.mobilePhone,
                          "address"      : app.address,
                      }),
            }).done(function() {
                console.log(response);
            });
        },
    }
});