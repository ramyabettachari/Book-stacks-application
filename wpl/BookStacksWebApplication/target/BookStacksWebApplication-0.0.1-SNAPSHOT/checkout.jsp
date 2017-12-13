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

</style> 

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
				<li>Checkout</li>
			</ul>
		</div>
	</div>
<!-- banner -->
	<div class="banner">
	<div>
		<div class="w3_login" style="min-height: 300px;">
		    <h3>Purchase order confirmation</h3>
			<h4 style="margin-top: 30px; text-align: center;">Your order has been placed successfully
			Please check your email for order confirmation</h4>			

		</div>
		</div>
		<div class="clearfix"></div>
	</div>
<!-- //banner -->

<jsp:include page="footer.jsp"></jsp:include>