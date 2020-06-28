$("#pageUrl").keyup(function() {
	$.post("/validator/pageUrl", {url: $("#pageUrl").val()}).done(function(result) {
		if(result == false) {
			$("#pageUrl").addClass("is-valid");
			$("#pageUrl").removeClass("is-invalid");
			$("#pageUrlSubmit").addClass("btn-primary");
			$("#pageUrlSubmit").removeClass("btn-danger");
			$("#pageUrlSubmit").prop("disabled", false);
		} else {
			$("#pageUrl").addClass("is-invalid");
			$("#pageUrl").removeClass("is-valid");
			$("#pageUrlSubmit").addClass("btn-danger");
			$("#pageUrlSubmit").removeClass("btn-primary");
			$("#pageUrlSubmit").prop("disabled", true);
		}
	});
});

$("#spaceUrl").keyup(function() {
	$.post("/validator/spaceUrl", {url: $("#spaceUrl").val()}).done(function(result) {
		if(result == false) {
			$("#spaceUrl").addClass("is-valid");
			$("#spaceUrl").removeClass("is-invalid");
			$("#spaceUrlSubmit").addClass("btn-primary");
			$("#spaceUrlSubmit").removeClass("btn-danger");
			$("#spaceUrlSubmit").prop("disabled", false);
		} else {
			$("#spaceUrl").addClass("is-invalid");
			$("#spaceUrl").removeClass("is-valid");
			$("#spaceUrlSubmit").addClass("btn-danger");
			$("#spaceUrlSubmit").removeClass("btn-primary");
			$("#spaceUrlSubmit").prop("disabled", true);
		}
	});
});
