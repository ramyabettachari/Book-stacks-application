<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Book Stacks | Online bidding book store</title>
<!-- for-mobile-apps -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Grocery Store Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
		function hideURLbar(){ window.scrollTo(0,1); } </script>
<!-- //for-mobile-apps -->
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
<!-- font-awesome icons -->
<link href="css/font-awesome.css" rel="stylesheet" type="text/css" media="all" /> 
<!-- //font-awesome icons -->
<!-- js -->
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/angular.min.js"></script>
<script src="js/ui-bootstrap-tpls-0.9.0.js"></script>

<!-- //js -->
<link href='//fonts.googleapis.com/css?family=Ubuntu:400,300,300italic,400italic,500,500italic,700,700italic' rel='stylesheet' type='text/css'>
<link href='//fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic' rel='stylesheet' type='text/css'>
<!-- start-smoth-scrolling -->
<script type="text/javascript" src="js/move-top.js"></script>
<script type="text/javascript" src="js/easing.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function($) {
		$(".scroll").click(function(event){		
			event.preventDefault();
			$('html,body').animate({scrollTop:$(this.hash).offset().top},1000);
		});
	});
</script>
<style>
.agileits_header {
  width: 100%;
  position: relative;
  text-align:center;
}

.block {
  display:inline-block;
  margin: 10px;
}
.w3l_offers div{
  font-size: 14px;
}
.w3l_search,.product_list_header,.w3l_header_right{
  float: none;
  margin-left: 6em;
}

</style>
</head>
<body>
<!-- header -->
	<div class="agileits_header">
		<div class="w3l_offers block">
			<div id="lastlogin">
			    <%if (session.getAttribute("lastlogin") != null) { %>
                Last login: <% out.println(session.getAttribute("lastlogin")); %>
                <% } %> 
			</div>
		</div>
		
		<div class="w3l_search block">
			<form action="searchpost.jsp" method="get">
				<input type="text" name="query" value="Search a book..."  onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Search a product...';}" required="">
				<input type="submit" value=" ">
			</form>
		</div>
		<div class="product_list_header block">
		<%if (session.getAttribute("user_id") != null) { %>  
			<form action="#" method="post" class="last">
                <fieldset>
                    <input type="hidden" name="cmd" value="_cart" />
                    <input type="hidden" name="display" value="1" />
                    <input type="submit" name="submit" id="viewcart" value="" class="button" />
                </fieldset>
            </form>
            <script>
            $(document).ready(function(){
            	$.get('CartController.do?cmd=cartCount').success(function(data){
		            $("#viewcart").val("Your Cart ("+data+")");
		        });
            });
            </script>
        <% }%>
        
		</div>
		<div class="w3l_header_right block">
			<ul>
				<li class="dropdown profile_details_drop">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user" aria-hidden="true"></i><span class="caret"></span></a>
					<div class="mega-dropdown-menu">
						<div class="w3ls_vegetables">
							<ul class="dropdown-menu drp-mnu">
							   <%if (session.getAttribute("user_email") != null) { %>
							   <li><a><% out.print(session.getAttribute("user_firstname")+" "+session.getAttribute("user_lastname"));%></a></li>
                               <li><a href="profile.jsp">User Profile</a></li>
                               <li><a href="Logout">Logout</a></li>
                               <% }else{ %> 
								<li><a href="login.jsp">Login/ Sign up</a></li> 
								<% } %>
							</ul>
						</div>                  
					</div>	
				</li>
			</ul>
		</div>
		
		<div class="clearfix"> </div>
	</div>
<!-- script-for sticky-nav -->
	<script>
	$(document).ready(function() {
		 var navoffeset=$(".agileits_header").offset().top;
		 $("#viewcart").click(function(event){
			event.preventDefault();
			window.location = "cart.jsp";
		 });
		 $(window).scroll(function(){
			var scrollpos=$(window).scrollTop(); 
			if(scrollpos >=navoffeset){
				$(".agileits_header").addClass("fixed");
			}else{
				$(".agileits_header").removeClass("fixed");
			}
		 });
		 
		   
		 
	});
	</script>
<!-- //script-for sticky-nav -->
	<div class="logo_products">
		<div class="container">
			<div class="w3ls_logo_products_left">
				<h1><a href="index.jsp"><span>Book</span> Stacks</a></h1>
			</div>
			<%if (session.getAttribute("user_id") != null) { %>  
			<div class="w3ls_logo_products_left1">
				<ul class="special_items">
					<li><a href="createpost.jsp">Create Post</a><i>/</i></li>
					<li><a href="myposts.jsp">My Posts</a><i>/</i></li>
					<li><a href="searchpost.jsp">Search Post</a></li>
				</ul>
			</div>
			<%}%>

			<div class="clearfix"> </div>
		</div>
	</div>
<!-- //header -->
