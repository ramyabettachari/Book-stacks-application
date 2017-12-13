/* For login page*/

$(document).ready(function() {
	initialize();
	$('.toggle').click(function(){
		  // Switches the Icon
		  $(this).children('i').toggleClass('fa-pencil');
		  // Switches the forms  
		  $('.form').animate({
			height: "toggle",
			'padding-top': 'toggle',
			'padding-bottom': 'toggle',
			opacity: "toggle"
		  }, "slow");
		  if ($('.tooltip').html() == 'Click Me to Sign Up'){
	    	  $('.tooltip').html('Click Me to Sign In');
	      }
	      else{
	    	  $('.tooltip').html('Click Me to Sign Up');
	      }
		});

	$('input[name="email"]').blur(function() {
		if($(this).val() != ""){
			emailid = $(this).val();
			$.ajax({
				url : 'AjaxController',
				type: 'post',
				data : {
					cmd : "checkuser",
					email : $(this).val()
				},
				success : function(responseText) {
					if(responseText == 'valid'){
						$('#emailerror').css('display','none');
					}else{
					    $('#emailerror').html(emailid+" already exists.Try another Email ID!");
					    $('#emailerror').css('display','block');
					}
				}
			});
		}
	});
	
	$('#confirm_password').blur(function() {
		if($(this).val() == "" || $(this).val().length != $('input[name="password"]').val().length || $(this).val() != $('input[name="password"]').val()){
			$(this).val("");
			$('#passworderror').css('display','block');
		}else{
			$('#passworderror').css('display','none');
		}
		
	});
});

/* For User Profile page*/
$(document).ready(function(){
	$('#passwordform').submit(function(event){
		event.preventDefault();
		oldpass = $('#old_password').val();
		newpass = $('#password').val();
		confirmpass = $('#confirm_password').val();
		$.ajax({
			url : 'AjaxController',
			type: 'post',
			data : {
				cmd : "updatePassword",
				oldpassword : oldpass,
				newpassword : newpass,
				confirmpassword : confirmpass
			},
			success : function(responseText) {
				if(responseText == 'success'){
					$('#passwordalert').removeClass('alert-danger');
					$('#passwordalert').html("");
					$('#passwordalert').addClass('alert-success');
					$('#passwordalert').html("<strong>Success!</strong> Password has been updated successfully.");
					$('#passwordalert').css("display","block");
				}else if(responseText == 'oldinvalid'){
					$('#passwordalert').removeClass('alert-success');
					$('#passwordalert').html("");
					$('#passwordalert').addClass('alert-danger');
					$('#passwordalert').html("<strong>Failed!</strong> Incorrect old password, try again");
					$('#passwordalert').css("display","block");
				}else{
					$('#passwordalert').removeClass('alert-success');
					$('#passwordalert').html("");
					$('#passwordalert').addClass('alert-danger');
					$('#passwordalert').html("<strong>Failed!</strong> Something went wrong. PLease try again!");
					$('#passwordalert').css("display","block");
				}
			}
		});	
	});	
	
	$('#profileform').submit(function(event){
		event.preventDefault();
		fname = $('#firstname').val();
		lname = $('#lastname').val();
		pno = $('#phone').val();
		$.ajax({
			url : 'AjaxController',
			type: 'post',
			data : {
				cmd : "updateProfile",
				firstname : fname,
				lastname : lname,
				phone : pno
			},
			success : function(responseText) {
				if(responseText == 'success'){
					$('#profilealert').removeClass('alert-danger');
					$('#profilealert').html("");
					$('#profilealert').addClass('alert-success');
					$('#profilealert').html("<strong>Success!</strong> Profile has been updated successfully.");
					$('#profilealert').css("display","block");
				}else{
					$('#profilealert').removeClass('alert-success');
					$('#profilealert').html("");
					$('#profilealert').addClass('alert-danger');
					$('#profilealert').html("<strong>Failed!</strong> Something went wrong. PLease try again!");
					$('#profilealert').css("display","block");
				}
			}
		});	
	});
});
function getParameterByName(name, url) {
    if (!url) {
      url = window.location.href;
    }
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
