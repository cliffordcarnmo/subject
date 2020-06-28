$("#email").keyup(function() {
	$.post("/validator/userEmail", {email: $("#email").val()}).done(function(result) {
		if(result == false) {
			$("#email").addClass("is-valid");
			$("#email").removeClass("is-invalid");
			$("#userSubmit").addClass("btn-primary");
			$("#userSubmit").removeClass("btn-danger");
			$("#userSubmit").prop("disabled", false);
		} else {
			$("#email").addClass("is-invalid");
			$("#email").removeClass("is-valid");
			$("#userSubmit").addClass("btn-danger");
			$("#userSubmit").removeClass("btn-primary");
			$("#userSubmit").prop("disabled", true);
		}
	});
});