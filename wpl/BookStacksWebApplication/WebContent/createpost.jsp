<jsp:include page="header.jsp"></jsp:include>
<style>
.table{
    width: 50%;
    margin-left: 20%;
}
.table tbody tr td{
    color: black;
}
</style>
<!-- products-breadcrumb -->

<%
if(session.getAttribute("user_id") == null || session.getAttribute("user_id").equals("")){
	String uri = request.getRequestURI();
	String pageName = uri.substring(uri.lastIndexOf("/")+1);
	response.sendRedirect("login.jsp?redirect="+pageName);
}

%>
	<div class="products-breadcrumb">
		<div class="container">
			<ul>
				<li><i class="fa fa-home" aria-hidden="true"></i><a href="index.jsp">Home</a><span>|</span></li>
				<li><a class="active" href="createpost.jsp">Create Post</a><span>|</span></li>
			</ul>
		</div>
	</div>
<!-- banner -->
	<div class="banner">
		<div>
			<div class="w3_create">
				<div class="w3_create_module">
					  <div class="create_form">
						<% if (request.getAttribute("creationMsg") != null) { %>
				        <h1 id="creationMsg"> <% out.println(request.getAttribute("creationMsg")); %> </h1><br/>
				        <% } %>
						<form name="postdetails" action="PostCreation" method="post">
						 	<table class="table">
						 	    <tr>
						 	        <td colspan="2" style="text-align: center;"><h2>Create new post</h2></td>
						 	    </tr>
						 	    <tr>
						 	        <td>Book name:</td>
						 	        <td><input type="text" class="form-control" name="bookname" pattern="([A-Za-z0-9][a-z0-9]* *)*" title="Can contain alphabets and numbers and maximum size of 100 character" placeholder = "Name of the Book" maxlength = "100" required></td>
						 	        
						 	    </tr>
						 	    <tr>
						 	        <td>Author name:</td>
						 	        <td><input type="text" class="form-control" name="authorname" title="Can contain only Alphabets separated by space. First letter of each word is a capital letter and maximum size of 40 character" pattern = "([A-Z][a-z]* ?)*" placeholder = "Name of the Author" maxlength = "40" required></td>
						 	        
						 	    </tr>
						 	    
						 	    <tr>
						 	        <td>Year of Publication:</td>
						 	        <td><input type="text" name="year" class="form-control" placeholder = "Year of Publication" pattern="^\d{4}$" required>
						 	        <span id='yearerror' style="display: none; color: red;">Enter a valid year</span>
						 	        </td>
						 	        
						 	    </tr>
						 	    
						 	    <tr>
						 	        <td>Publisher Name:</td>
						 	        <td><input type="text" name="publishername" class="form-control" pattern="([A-Z]?[a-z]* *)*" title="First letter of each word is a capital letter and maximum size of 50 character." placeholder="Name of the Publisher" maxlength = "50" required></td>
						 	        
						 	    </tr>
						 	    <tr>
						 	        <td>Category:</td>
						 	        <td>
							 	        <select name="category" id="category" class="form-control" required>
										<option value="1">Biography</option>
										<option value="2">Business & Leadership</option>
										<option value="3">Fiction & Literature</option>
										<option value="4">Politics & Economy</option>
										<option value="5">Cooking & Food</option>
										<option value="6">Computers & Technology</option>
										<option value="7">Crafts & Hobbies</option>
										<option value="8">Others</option>
									    </select>
								     </td>
						 	        
						 	    </tr>
						 	    <tr>
						 	        <td>Condition:</td>
						 	        <td><input type="radio" id="input1" name="condition" value="new" required="required">
		  						    <label for="input1">New</label> &nbsp;
								    <input type="radio" id="input2" name="condition" value="used">
								    <label for="input2">Used</label> &nbsp;
								    <input type="radio" id="input3" name="condition" value="new/used">
								    <label for="input3">New/Used</label>
								    </td>
						 	        
						 	    </tr>
						  	    <tr style="text-align: center;">
						 	        <td colspan="2"><input type="submit" class="btn btn-primary" name="create" value="Create Post">
						 	        <input type="reset"  class="btn btn-primary" style="margin-left: 20px;"></td>
						 	    </tr>
							</table>
						</form>
					  </div>
				</div>
			</div>
<!-- //login -->
		</div>
		<div class="clearfix"></div>
	</div>
<!-- //banner -->
	<script>
		$(document).ready(function() {
			
				/*$.ajax({
					url : 'PostCreation',
					type: 'get',
					success : function(response) {console.log(response);
						var entry = response.split(".");
						var option = '';
						option += '<option value=""> Select a category </option>';
						for (var i=0;i<entry.length-1;i++){
						   var splitkeyvalue = entry[i].split("/");
						   option += '<option value="'+ splitkeyvalue[0] + '">' + splitkeyvalue[1] + '</option>';
						}
						$('#category').empty().append(option);
					}
			});*/
				
		    $("input[name='year']").blur(function(){
		    	validateForm();
		    });
		});
	</script>
	<script>
	
	function validateForm() {
	    var year = document.forms["postdetails"]["year"].value;
	    var today = new Date();
		var yyyy = today.getFullYear();
	    if (year > yyyy | year < 1) {
	    	$("input[name='year']").val("");
	        $("#yearerror").show();
	        return false;
	    }else{
	    	$("#yearerror").hide();
	    }
	    return true;
	}
	function getYearMax() {
		var today = new Date();
		var yyyy = today.getFullYear();
		document.getElementById("year").setAttribute("max", today);
	}
	</script>
<jsp:include page="footer.jsp"></jsp:include>