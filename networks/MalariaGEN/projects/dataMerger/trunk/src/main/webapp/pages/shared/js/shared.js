function initSharedFunctions () {

	initSerializeObjectFunction();

}

function initSerializeObjectFunction () {
	
	//FIXME: if select 1 and 2 both have value "", does not construct a JSON array, but name="" instead.
	
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