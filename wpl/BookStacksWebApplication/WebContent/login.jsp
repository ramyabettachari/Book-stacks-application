<jsp:include page="header.jsp"></jsp:include>
<!-- For google maps -->
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCwl-IEPmLd7TKv5K5lS-rR5p9qctbay1g"></script>
<script type="text/javascript" src="js/locator.js"></script> 
<!-- Custom js -->
<script type="text/javascript" src="js/custom.js"></script> 
<%
if(session.getAttribute("user_id") != null ){
	
	response.sendRedirect("index.jsp");
}

%>
	<div class="products-breadcrumb">
		<div class="container">
			<ul>
				<li><i class="fa fa-home" aria-hidden="true"></i><a href="index.jsp">Home</a><span>|</span></li>
				<li>Sign In & Sign Up</li>
			</ul>
		</div>
	</div>
<!-- banner -->
	<div class="banner">
		<div>
		<!-- login -->
		<div class="w3_login">
		<% if (request.getAttribute("registrationMsg") != null) { %>
        <h2 id="loginmsg"> <% out.println(request.getAttribute("registrationMsg")); %> </h2><br/>
        <% } %>
			<h3>Sign In & Sign Up</h3>
			<div class="w3_login_module">
				<div class="module form-module">
				  <div class="toggle"><i class="fa fa-times fa-pencil"></i>
					<div class="tooltip">Click Me to Sign Up</div>
				  </div>
				  <div class="form">
					<h2>Login to your account</h2>
					<form action="Login" method="post">
					  <input type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" name="login_email" placeholder="Email Address" maxlength = "40" required>
					  <input type="password" name="login_password" placeholder="Password" required>
					  <input type="hidden" name="location" value="">
					  <input type="hidden" name="url" value="">
					  <input type="hidden" name="regCmd" value="Login">
					  <input type="submit" name="login" value="Login">
					</form>
				  </div>
				  <div class="form">
					<h2>Create an account</h2>
					<form action="Login" method="post">
					  <input type="text" name="firstname" placeholder="First Name" title="Maximum 40 Characters. Can contain only Alphabets separated by space with first letter caps" pattern = "([A-Z][a-z]* ?)*" maxlength = "40" required>
					  <input type="text" name="lastname" placeholder="Last Name" title="Maximum 40 Characters. Can contain only Alphabets separated by space with first letter caps" pattern = "([A-Z][a-z]* ?)*" maxlength = "40" required>
					  <input type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" name="email" placeholder="Email Address" maxlength = "40" required>
					  <span id='emailerror'></span>
					  <input type="password" name="password" placeholder="Password" title="Minimum 8 characters at least 1 Alphabet, 1 Number and no special characters" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$" maxlength = "40" required>
					  <input type="password" placeholder="Confirm Password" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"  id="confirm_password" required>
					  <span id='passworderror'>Passwords do not match. Try again!</span>
					  <input type="text" name="phone" placeholder="10 Digit Mobile Number" title="Enter 10 Digit mobile number without country code" pattern="^[2-9]{2}[0-9]{8}$" required>
					  <input type="hidden" name="location" value="">
					  <input type="hidden" name="regCmd" value="Register">
					  <input type="submit" name="register" value="Register" >
					</form>
				  </div>
				</div>
			</div>
			
			

		</div>
<!-- //login -->
		</div>
		<div class="clearfix"></div>
	</div>
<!-- //banner -->
<script>
$(document).ready(function(){
	$('input[name="url"]').val(getParameterByName("redirect"));
});
</script>
<jsp:include page="footer.jsp"></jsp:include>