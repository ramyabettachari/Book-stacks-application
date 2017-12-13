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
				<li>My posts</li>
			</ul>
		</div>
	</div>
<!-- banner -->
	<div class="banner" ng-app="postApp">
	<div>
		<div class="w3_login" ng-controller="postCrtl">
			<h3>My posts</h3>
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
              <div class="alert" id="closepostalert">
                   
              </div>
                <table class="table table-striped table-bordered" style="width: 100%">
                  <thead>
                    <th>Change status to closed</th>
                    <th ng-click="sort_by('bookname');" style="cursor:pointer">Book Name</th>
                    <th ng-click="sort_by('authorname');" style="cursor:pointer">Author Name</th>
                    <th ng-click="sort_by('publishername');" style="cursor:pointer">Publisher Name</th>
                    <th ng-click="sort_by('year');" style="cursor:pointer">Year of publication</th>
                    <th ng-click="sort_by('categoryname');" style="cursor:pointer">Category Name</th>
                    <th ng-click="sort_by('condition');" style="cursor:pointer">Condition</th>
                    <th ng-click="sort_by('status');" style="cursor:pointer">Status</th>
                    <th></th>
                      </thead>
                  <tbody>
                    <tr ng-repeat="data in filtered = (list | filter:search | orderBy : predicate :reverse) | startFrom:(currentPage-1)*entryLimit | limitTo:entryLimit ">
                      <td><span style="text-decoration: underline; cursor: pointer; color: #337ab7" id="closepost_{{data.post_id}}" ng-hide="checkStatus(data)">Close post</span></td>
                      <td>{{data.bookname }}</td>
                      <td>{{data.authorname}}</td>
                      <td>{{data.publishername}}</td>
                      <td>{{data.year}}</td>
                      <td>{{data.categoryname}}</td>
                      <td>{{data.condition}}</td>
                      <td id="status_{{data.post_id}}">{{data.status}}</td>
                      <td><a href="postbiddetails.jsp?postid={{data.post_id}}" class="btn btn-primary" id="viewbids_{{data.post_id}}" style="margin-left: 10px" ng-disabled="checkStatus(data)">View bids</a></td>
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
<script src="js/myposts/app.js"></script>
<script type="text/javascript" src="js/jquery.validate-1.14.0.min.js" /></script>
<script type="text/javascript" src="js/jquery-validate.bootstrap-tooltip.js" /></script>
<script>
$(document).ready(function(){
	$('#mypostform').validate();
	$('body').on('click','[id^=closepost]',function(){
		postid = $(this).attr("id").split("_")[1];
		if(confirm("Are you sure to change the status to closed? Once changed it cannot be revoked.\n Click OK to continue.")){ 
	    	$.ajax({
				url : 'PostsController',
				type: 'post',
				data : {
					cmd : "closePost",
					post_id : postid
				},
				success : function(responseText) {console.log(responseText+"sdsadsd");
				if(responseText == 'success'){
					$("#closepost_"+postid).remove();
					$("#status_"+postid).html("closed");
					$("#viewbids_"+postid).attr("disabled","disabled");
					
				}else{
					$('#closepostalert').removeClass('alert-success');
					$('#closepostalert').html("");
					$('#closepostalert').addClass('alert-danger');
					$('#closepostalert').html("<strong>Failed!</strong> Something went wrong. PLease try again!");
					$('#closepostalert').css("display","block");
				}
				}
			});
		}else{
			return false;
		}
	    
	});
});
</script>
<jsp:include page="footer.jsp"></jsp:include>