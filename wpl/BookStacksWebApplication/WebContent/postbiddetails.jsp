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
				<li>Bid Details</li>
			</ul>
		</div>
	</div>
<!-- banner -->
	<div class="banner" ng-app="postbidApp">
	<div>
		<div class="w3_login" ng-controller="postbidCrtl">
			<h3>Bid Details</h3>
			<div class="container">
            <div class="row">
              <div class="col-md-2"> <span style="display:inline-block">Filtered {{ filtered.length }} of {{ totalItems}}</span> </div>
              <div class="col-md-1">Filter: </div>
              <div class="col-md-2">
                <input type="text" ng-model="search" ng-change="filter()" placeholder="Filter" class="form-control" />
              </div>
              <div class="col-md-2" style="float:right">PageSize:
                <select ng-model="entryLimit" class="form-control" style="display:inline-block;width:95px">
                  <option>10</option>
                  <option>20</option>
                  <option>50</option>
                  <option>100</option>
                </select>
              </div>
            </div>
            <br/>
            <div class="row">
              <div class="col-md-13" ng-show="filteredItems > 0">
              <form id="mypostform">
              <div class="alert" id="addtocartalert">
                   
              </div>
                <table class="table table-striped table-bordered" style="width: 100%">
                  <thead>
                  <th ng-click="sort_by('bookname');" style="cursor:pointer">Book Name</th>
                    <th ng-click="sort_by('bidder_firstname');" style="cursor:pointer">Bidder First Name</th>
                    <th ng-click="sort_by('bidder_lastname');" style="cursor:pointer">Bidder Last Name</th>
                    <th ng-click="sort_by('bidder_email');" style="cursor:pointer">Bidder Email </th>
                    <th ng-click="sort_by('bidamount');" style="cursor:pointer">Bid amount</th>
                    <th></th>
                      </thead>
                  <tbody>
                    <tr ng-repeat="data in filtered = (list | filter:search | orderBy : predicate :reverse) | startFrom:(currentPage-1)*entryLimit | limitTo:entryLimit ">
                      <td>{{data.bookname}}</td>
                      <td>{{data.bidder_firstname }}</td>
                      <td>{{data.bidder_lastname}}</td>
                      <td>{{data.bidder_email}}</td>
                      <td>{{data.bidamount}}</td>
                      <td><input type="number" class="form-control" id="qty_{{data.bid_id}}"  style="width: 70px; display: inline-block" placeholder="Qty" /><button class="btn btn-primary" id="addtocart_{{data.bid_id}}" style="margin-left: 10px">Add to cart</button></td>
                    </tr>
                  </tbody>
                </table>
                </form>
              </div>
              <div class="col-md-12" ng-show="filteredItems == 0">
                <div class="col-md-12">
                  <h4>No Records found</h4>
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
<script src="js/postbiddetails/app.js"></script>
<script type="text/javascript" src="js/jquery.validate-1.14.0.min.js" /></script>
<script type="text/javascript" src="js/jquery-validate.bootstrap-tooltip.js" /></script>
<script>
$(document).ready(function(){
	if(getParameterByName("postid") == "" || getParameterByName("postid") == "null"){
		window.location.href= "myposts.jsp";
		return false;
	}
	function checkNumber(val){
		if(val == ""){
			return "This field is required";
		}
		if(val <=0 ){
			return "Please enter a number greater than 0";
		}
		else if (!/^(?!\.?$)\d{0,3}(\.\d{0,2})?$/.test(+val)){
			return "Please match the number format xxx.xx";
		}else{
			return "valid";
		}
	}
	
	
	$('body').on('keypress','[id^=qty_]',function(event){
		var regex = new RegExp("^[a-zA-Z0-9]+$");
	    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
	    if (!regex.test(key)) {
	       event.preventDefault();
	       return false;
	    }
	});
	
	$('body').on('click','[id^=addtocart]',function(){
		bidid = $(this).attr("id").split("_")[1];
		 
	    	qty = $('#qty_'+bidid).val();
	    	$.ajax({
				url : 'CartController.do',
				type: 'post',
				data : {
					cmd : "addItem",
					bid_id : bidid,
					quantity : qty
				},
				success : function(responseText) {console.log(responseText);
					if(responseText == 'inserted'){
						$.get('CartController.do?cmd=cartCount').success(function(data){
					        $("#viewcart").val("Your Cart ("+data+")");
					    });
						alert("Success! Item has been added to the cart successfully.");
							
					}else if(responseText == 'updated'){
						alert("Success! You have already added this item to cart. Quantity has been updated successfully.");	
					}
					else{
						alert("Failed! Something went wrong. PLease try again!");

					}
					return false;
				}
				
			});
	});
});
</script>
<jsp:include page="footer.jsp"></jsp:include>