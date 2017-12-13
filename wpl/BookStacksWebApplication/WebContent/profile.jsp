<jsp:include page="header.jsp"></jsp:include>
<!-- For google maps -->
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCwl-IEPmLd7TKv5K5lS-rR5p9qctbay1g"></script>
<script type="text/javascript" src="js/locator.js"></script> 
<!-- Custom js -->
<script type="text/javascript" src="js/custom.js"></script>
<style>
.table{
    width: 55%;
    max-width: 100%;
    margin-top: 50px;
    margin: 0 auto;
}
.table tbody tr td{
    color: black;
}
#updateprofile{
    margin-left: 20px;
}
</style> 

<script>
$(document).ready(function(){
	$('#edit').click(function(){
		$('#firstname,#lastname,#phone').prop("disabled",false);
	});
});
</script>
<%
if(session.getAttribute("user_id") == null || session.getAttribute("user_id").equals("")){
	String uri = request.getRequestURI();
	String pageName = uri.substring(uri.lastIndexOf("/")+1);
	response.sendRedirect("login.jsp?redirect="+pageName);
}

%>
<!-- products-breadcrumb -->
	<div class="products-breadcrumb">
		<div class="container">
			<ul>
				<li><i class="fa fa-home" aria-hidden="true"></i><a href="index.jsp">Home</a><span>|</span></li>
				<li>Profile</li>
			</ul>
		</div>
	</div>
<!-- banner -->
	<div class="banner">
	<div>
		<div class="w3_login">
			<h3>User Information</h3>
			<div class="alert" id="profilealert">
                   
            </div>
			<form id="profileform">
			<table class="table">
			    <tr>
			        <td>First Name</td>
			        <td><input type="text" name="firstname" id="firstname" class="form-control" disabled="disabled" title="Can contain only Alphabets separated by space. First letter of each word is a capital letter and maximum size of 40 character" pattern = "([A-Z][a-z]* ?)*" maxlength = "40" required></td>
			    </tr>
			    <tr>
			        <td>Last Name</td>
			        <td><input type="text" name="lastname" id="lastname" class="form-control" disabled="disabled" title="Can contain only Alphabets separated by space. First letter of each word is a capital letter and maximum size of 40 character" pattern = "([A-Z][a-z]* ?)*" maxlength = "40" required></td>
			    </tr>
			    <tr>
			        <td>Email</td>
			        <td><input type="text" name="email" id="email" class="form-control" disabled="disabled"></td>
			    </tr>
			    <tr>
			        <td>Phone Number</td>
			        <td><input type="text" name="phone" id="phone" class="form-control" disabled="disabled" title="Enter 10 Digit mobile number without country code" pattern="^[2-9]{2}[0-9]{8}$" required></td>
			    </tr>
			    <tr>
			        <td colspan="2"><input type="button" id="edit" value="Edit" class="btn btn-primary"><input type="submit" id="updateprofile" value="Save changes" class="btn btn-primary"></td>
			    </tr>
			    </table>
			    </form>
			    <div class="alert" id="passwordalert">
                   
                </div>
			    <form id="passwordform">
			    <table class="table">
			    <tr>
			        <td>Old Password</td>
			        <td><input type="password" id="old_password" name="old_password" class="form-control" ></td>
			    </tr>
			    <tr>
			        <td>New Password</td>
			        <td><input type="password" id="password" name="password" class="form-control" title="Minimum 8 characters at least 1 Alphabet, 1 Number and no special characters" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$" ></td>
			    </tr>
			    <tr>
			        <td>Confirm password</td>
			        <td><input type="password" id="confirm_password" name="confirm_password" class="form-control" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$" ><span id='passworderror'>Passwords do not match. Try again!</span></td>
			    </tr>
			    <tr>
			        <td colspan="2"><input type="submit" id="updatepassword" value="Update Password" class="btn btn-primary"></td>
			    </tr>
		
			</table>
			</form>
			
			

		</div>
		</div>
		<div class="clearfix"></div>
	</div>
<!-- //banner -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script>
	$(document).ready(function() {
		
		$.ajax({
			url : 'AjaxController',
			type: 'post',
			data : {
				cmd : "fetchProfile"
			},
			success : function(response) {
				var entry = response.split("#");
				for (var i=0;i<entry.length-1;i++){
					var splitkeyvalue = entry[i].split("/");
					if (splitkeyvalue[0]=='firstname'){
						$('#firstname').val(splitkeyvalue[1])
					}
					else if (splitkeyvalue[0]=='lastname'){
						$('#lastname').val(splitkeyvalue[1])
					}
					else if (splitkeyvalue[0]=='email'){
						$('#email').val(splitkeyvalue[1])
					}
					else if (splitkeyvalue[0]=='phone'){
						$('#phone').val(splitkeyvalue[1])
					}
				}
			}
		});
	});
	</script>
<jsp:include page="footer.jsp"></jsp:include>