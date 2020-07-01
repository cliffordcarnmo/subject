$("#createSpaceUrl").keyup(function() {
	validateUrl("create", "space", $("#createSpaceUrl"), "", $("#createSpaceSubmit"));
});

$("#editSpaceUrl").keyup(function() {
	validateUrl("edit", "space", $("#editSpaceUrl"), $("#editInitialSpaceUrl").val(), $("#editSpaceSubmit"));
});

$("#createPageUrl").keyup(function() {
	validateUrl("create", "page", $("#createPageUrl"), "", $("#createPageSubmit"));
});

$("#editPageUrl").keyup(function() {
	validateUrl("edit", "page", $("#editPageUrl"), $("#editInitialPageUrl").val(), $("#editPageSubmit"));
});

function validateUrl(mode, type, url, initialUrl, button) {
	$.post("/validator/url", {mode: mode, type: type, url: url.val(), initialUrl: initialUrl}).done(function(result) {
		if(result == false) {
			url.addClass("is-valid");
			url.removeClass("is-invalid");
			button.addClass("btn-primary");
			button.removeClass("btn-danger");
			button.prop("disabled", false);
		} else {
			url.addClass("is-invalid");
			url.removeClass("is-valid");
			button.addClass("btn-danger");
			button.removeClass("btn-primary");
			button.prop("disabled", true);
		}
	});
}