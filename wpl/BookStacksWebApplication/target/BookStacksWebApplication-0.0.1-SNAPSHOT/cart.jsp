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

.glyphicon{
    cursor: pointer;
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
				<li>My Cart</li>
			</ul>
		</div>
	</div>
<!-- banner -->
	<div class="banner" ng-app="cartApp">
	<div>
		<div class="w3_login" ng-controller="cartCrtl">
			<h3>My Cart</h3>
			<div class="container">
            
            <br/>
            <div class="row">
              <div class="col-md-13" ng-show="filteredItems > 0">
              <form id="cartform">
              <div class="alert" id="cartalert">
                   
              </div>
                <table class="table table-striped table-bordered" style="width: 100%">
                  <thead>
                    <th></th>
                    <th>Book Name</th>
                    <th>Bidder Name</th>
                    <th>Quantity</th>
                    <th>Bid amount($)</th>
                    <th>Sub Total($)</th>
                      </thead>
                  <tbody>
                    <tr ng-repeat="data in filtered = (list | filter:search | orderBy : predicate :reverse) | startFrom:(currentPage-1)*entryLimit | limitTo:entryLimit ">
                      <td><span class="glyphicon glyphicon-remove" id="deleteitem_{{data.cart_id}}"></span></td>
                      <td>{{data.bookname}}</td>
                      <td>{{data.biddername}}</td>
                      <td style="text-align: center;"><span class="glyphicon glyphicon-minus" cart-id="{{data.cart_id}}" ng-click="qtyDecrement(data)"></span><span id="itemQty_{{data.cart_id}}" style="padding-left: 10px; padding-right: 10px;">{{data.quantity}}</span><span class="glyphicon glyphicon-plus" cart-id="{{data.cart_id}}" ng-click="qtyIncrement(data)"></span>
                                 
                      </td>
                      <td>{{data.bidamount}}</td>
                      <td>{{ data.quantity * data.bidamount }}</td>
                      
                    </tr>
                    <tr>
                        <td colspan="6"><strong style="float:right">Total: $<span ng-bind="getTotal()"></span></strong></td>
                    </tr>
                    <tr>
                        <td colspan="6"><a href="CheckoutController" class="btn btn-primary" style="float: right;">Place Order</a></td>
                    </tr>
                  </tbody>
                </table>
                </form>
              </div>
              <div class="col-md-12" ng-show="filteredItems == 0">
                <div class="col-md-12">
                  <h4>No items found in your cart. </h4><a href="myposts.jsp">My Posts</a>
                </div>
              </div>
              <div class="col-md-12" ng-show="filteredItems > 0" style="margin-top: 20px;
    text-align: center;">
                <div pagination="" page="currentPage" on-select-page="setPage(page)" boundary-links="true" total-items="filteredItems" items-per-page="entryLimit" class="pagination-small" previous-text="&laquo;" next-text="&raquo;"></div>
              </div>
              <div ng-show="showLoader" ng-Cloak>
                  <h4>Loading!! Please wait...</h4> 
              </div>
            </div>
          </div>			

		</div>
		</div>
		<div class="clearfix"></div>
	</div>
<!-- //banner -->
<script src="js/ui-bootstrap-tpls-0.10.0.min.js"></script>
<script src="js/cart/app.js"></script>
<script type="text/javascript" src="js/jquery.validate-1.14.0.min.js" /></script>
<script type="text/javascript" src="js/jquery-validate.bootstrap-tooltip.js" /></script>
<script>
$(document).ready(function(){

	
	$('#cartform').validate();
	// Delete Item
	$('body').on('click','[id^=deleteitem]',function(){
		cartid = $(this).attr("id").split("_")[1];
		   	$.ajax({
				url : 'CartController.do',
				type: 'post',
				data : {
					cmd : "deleteItem",
					cart_id : cartid
				},
				success : function(responseText) {
					if(responseText == 'success'){
						console.log("Item removed");
						$("#deleteitem_"+cartid).parent().parent().hide();
						var $scope =  angular.element(document.getElementById('.w3_login')).scope();
						angular.element('.w3_login').scope().reload();
						angular.element('.w3_login').scope().getTotal();
						angular.element('.w3_login').scope().$apply()
						var cartvalue = parseInt($('#viewcart').val().replace(/[^0-9\.]/g, ''), 10)-1;
						$('#viewcart').val("Your Cart ("+cartvalue+")")
					}else{
						$('#cartalert').removeClass('alert-success');
						$('#cartalert').html("");
						$('#cartalert').addClass('alert-danger');
						$('#cartalert').html("<strong>Failed!</strong> Something went wrong. PLease try again!");
						$('#cartalert').css("display","block");
					}
				}
			});
	});
	
	
	$('body').on('click','.glyphicon-minus,.glyphicon-plus',function(){
		cartid = $(this).attr("cart-id");
		itemQty =  $("#itemQty_"+cartid).html();
	    	$.ajax({
				url : 'CartController.do',
				type: 'post',
				data : {
					cmd : "updateQuantity",
					cart_id : cartid,
					quantity : itemQty
				},
				success : function(responseText) {
					if(responseText == 'success'){
						console.log("Quantity updated");
					}else{
						$('#cartalert').removeClass('alert-success');
						$('#cartalert').html("");
						$('#cartalert').addClass('alert-danger');
						$('#cartalert').html("<strong>Failed!</strong> Something went wrong. PLease try again!");
						$('#cartalert').css("display","block");
					}
				}
			});
	});
});
</script>
<jsp:include page="footer.jsp"></jsp:include>