var app = new Vue({
	el : '#signup',
	data : {
		children : [ {
			name : "",
			birthday : "",
			numberOfClasses : 0
		} ],
		mobilePhone : "",
		verificationCode : "",
		address : "",
		removable : false,
	},
	methods : {
		removeChild : function(index, child) {
			app.children.splice(index, 1);
			if (app.children.length == 1) {
				app.removable = false;
			}
		},
		addChild : function() {
			app.children.push({
				name : "",
				birthday : "",
				numberOfClasses : 0
			});
			if (app.children.length > 1) {
				app.removable = true;
			}
		},
		onClickGetVerificationCode : function() {
			console.log("get veri code clicked");
		},
		onSubmit : function() {
			var openCode = $('#wxOpenCode').text();
			$.ajax({
				url : '/api/v1/Customer',
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify({
					"openCode" : openCode,
					"name" : "浦东用户",
					"mobilePhone" : app.mobilePhone,
					"address" : "上海市浦东新区",
				}),
			}).done(function() {
				console.log(response);
			});
		},
	}
});