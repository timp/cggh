function initSharedFunctions () {

	initSerializeObjectFunction();

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