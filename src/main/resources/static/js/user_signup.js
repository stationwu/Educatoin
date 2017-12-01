var app = new Vue({
    el: '#signup',
    data: {
        children: [
            {
                name: "",
                birthday: ""
            }
        ],
        mobilePhone: "",
        verificationCode: "",
        address: "",
        removable: false,
    },
    methods: {
        removeChild: function (index, child) {
            app.children.splice(index, 1);
            if (app.children.length == 1) {
                app.removable = false;
            }
        },
        saveBasic: function () {
    		var mobilePhone = $("#mobilePhone")[0].value;
    		var address = $("#address")[0].value;
    		if(mobilePhone === ""){
    			mobilePhone=$("#mobilePhone")[0].placeholder;
    		}
    		if(address === ""){
    			address=$("#address")[0].placeholder;
    		}
    		$.post("/user/center?mobilePhone="+mobilePhone,"address="+address).done(function(data){
    			alert(data);
    		});
        },
        addChild: function () {
            app.children.push({
                name: "",
                birthday: ""
            });
            if (app.children.length > 1) {
                app.removable = true;
            }
        },
        
        onClickGetVerificationCode: function () {
        	var mobileNumber   = $('#mobileNumber').text();
        	var oSMSObject = {
        		mobile: mobileNumber,
        		type: "R"
        	};
        	var wait = 5 * 60;
        	var timer = function(o) {
        		if (wait == 0) {
	        		o.removeAttribute("disabled");   
	        		o.value="获取验证码";
	        		wait = 5 * 60;
        		} else { 
	        		o.setAttribute("disabled", true);
	        		o.value="重新发送(" + wait + ")";
	        		wait--;
	        		setTimeout(function() {
	        			timer(o);
	        		},1000);
        		}
        	};
        	
        	$.ajax({
                url: '/verify',
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify(oSMSObject),
            }).done(function (response) {
            	timer($('#verifyCodeBtn'));
            	$('#verifyCodeBtn').value = response;
            });
        },
        onAddStudents: function () {
            var openCode   = $('#wxOpenCode').text();
            var customerid = $('#customerid').text();
            var students   = [];
            var bHasError  = false;

            for (var i = 0; i < app.children.length; ++i) {
                var sChildName     = app.children[i].name.trim();
                var sChildBirthday = app.children[i].birthday.trim();

                if (sChildName === '' || sChildBirthday === '') {
                    $.alert("请填写学员的姓名，生日信息", "提示!");
                    bHasError = true;
                    break;
                } else {
                    students.push({
                        "childName": app.children[i].name,
                        "birthday": app.children[i].birthday
                    });
                }
            }

            if (!bHasError || students.length === 0) {
                $.ajax({
                    url: '/api/v1/Customer/AddChild',
                    contentType: 'application/json',
                    type: 'POST',
                    data: JSON.stringify(students),
                }).done(function (response) {
                    window.location.href = "/user/center";
                });
            }
        },
        onSubmit: function () {
            var openCode = $('#wxOpenCode').text();
            var verifyCodeId = $('#wxVerifyCode').text();
            var customer = {
                "openCode": openCode,
                "name": app.children[0].name + "家长",
                "mobilePhone": app.mobilePhone,
                "address": app.address,
                "verifyCodeId": verifyCodeId,
                "children": []
            };
            for (var i = 0; i < app.children.length; ++i) {
                customer.children.push({
                    "childName": app.children[i].name,
                    "birthday": app.children[i].birthday
                });
            }
            
            $.ajax({
                url: '/api/v1/Customer/SignUp',
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify(customer),
            }).done(function (response) {
                window.location.href = "/user/center";
            });
        },
    }
});