$("#email").keyup(function() {
	validateEmail($("#email"), $("#userSubmit"));
});

function validateEmail(email, button) {
	$.post("/validator/email", {email: email.val()}).done(function(result) {
		if(result == false) {
			email.addClass("is-valid");
			email.removeClass("is-invalid");
			button.addClass("btn-primary");
			button.removeClass("btn-danger");
			button.prop("disabled", false);
		} else {
			email.addClass("is-invalid");
			email.removeClass("is-valid");
			button.addClass("btn-danger");
			button.removeClass("btn-primary");
			button.prop("disabled", true);
		}
	});
}