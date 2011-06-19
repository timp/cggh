//Shared vars
var urlParams = {};

function initSharedFunctions () {

	initSerializeObjectFunction();
	initURLParamsVar();
	initLogoutLink();
}

function initLogoutLink () {

	$(".logoutLink").click(function() {	

			$.ajax({
				type: 'DELETE',
				data: '',
				contentType: 'application/json',
				url: '/dataMerger/data/users/authentication',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					if (data.success) {
						
						if (data.success = "true") {
							window.location.href = '/dataMerger/pages/shared/login/';
						} else {
							alert("An error occurred while trying to log out.");
						}
		
					} else {
						alert("An error occurred while trying to log out.");
					}
				},
				error: function (jqXHR, textStatus, errorThrown){
		            alert(errorThrown);
		        }
			});

		
	});
}


function initSerializeObjectFunction () {
	
	//Note: If name A and B both have value = "", this constructs a JSON array, instead of just name="".
	
	$.fn.serializeObject = function()
	{
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] || o[this.name] == '') {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};
	
}

function initURLParamsVar() {


	(function () {
	    var e,
	        a = /\+/g,  // Regex for replacing addition symbol with a space
	        r = /([^&=]+)=?([^&]*)/g,
	        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
	        q = window.location.search.substring(1);
	
	    while (e = r.exec(q))
	       urlParams[d(e[1])] = d(e[2]);
	})();
	
	//			urlParams = {
	//			    enc: " Hello ",
	//			    i: "main",
	//			    mode: "front",
	//			    sid: "de8d49b78a85a322c4155015fdce22c4",
	//			    empty: ""
	//			}
	//
	//			alert(urlParams["mode"]);
	//			// -> "front"

}