function initResolutionsFunctions () {


	initEditJoinFunction();	

}

function initEditJoinFunction () {
	
	$(".edit-join").click(function() {
		
		if (urlParams["merge_id"] != null) {
		
			//TODO: Refactor this URL
			window.location.href = '/dataMerger/pages/merges/joins?merge_id=' + urlParams["merge_id"];
			
		} else {
			
			alert("The merge has not been specified ID.");
		}
		
	});
	
}