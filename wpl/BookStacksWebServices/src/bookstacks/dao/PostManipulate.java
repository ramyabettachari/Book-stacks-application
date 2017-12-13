package bookstacks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.sql.CallableStatement;
import bookstacks.conn.*;

@Path("/postmanipulate")
public class PostManipulate {
	
	private Connection conn;
	private CallableStatement calstat; 
	public PostManipulate() {
		conn = DBUtil.getConnection();
		calstat = null;
	}
	
	@Path("/createPost")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response createPost (MultivaluedMap<String, String> post ) throws Exception {
		
		int response = -1;
			String storeProc = "{call spr_createPost(?,?,?,?,?,?,?,?)}";
			calstat = conn.prepareCall(storeProc);
			calstat.setInt(1, Integer.parseInt(post.getFirst("userid")));
			calstat.setInt(2, Integer.parseInt(post.getFirst("category")));
			calstat.setString(3, post.getFirst("bookname"));
			calstat.setInt(4, Integer.parseInt(post.getFirst("year")));
			calstat.setString(5, post.getFirst("pubname"));
			calstat.setString(6, post.getFirst("condition"));
			calstat.setString(7, post.getFirst("authname"));
			calstat.registerOutParameter(8, Types.INTEGER);
			calstat.executeUpdate();
			int res = calstat.getInt(8);
			calstat.close();
			if (res == 0)
			{
				response =  0;
			}
			else
			{
				response = -1;
			}	
			return Response.status(200).entity(String.valueOf(response)).build();
	}
	
	
	
	@Path("/getPostDetails")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String getPostDetails(MultivaluedMap<String, String> user) throws Exception {
		ArrayList<Map<String, Object>> postList = new ArrayList<>();
		String query = user.getFirst("query");
        if(query.equals("null")){
        	query = "";;
        }
    	String storedProcedure = "{call spr_getPostDetails(?,?)}";
		calstat = conn.prepareCall(storedProcedure);
		calstat.setInt(1, Integer.parseInt(user.getFirst("userid")));
		calstat.setString(2, query);
		ResultSet rs = calstat.executeQuery();

        while(rs.next()) {	
        	Map<String, Object> responseData = new HashMap<String, Object>();
        	responseData.put("post_id",rs.getInt("post_id"));
        	responseData.put("user_id",rs.getInt("user_id"));
        	responseData.put("ownername",rs.getString("ownername"));
        	responseData.put("bookname",rs.getString("book_name"));
        	responseData.put("authorname",rs.getString("author_name"));
        	responseData.put("year",rs.getInt("year"));
        	responseData.put("categoryname",rs.getString("category_name"));
        	responseData.put("publishername",rs.getString("publisher_name"));
        	responseData.put("condition",rs.getString("condition"));
        	postList.add(responseData);
        }
        String mapAsJson = new ObjectMapper().writeValueAsString(postList);
		return mapAsJson;

    }
	
	
	@Path("/getMyPosts")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String getMyPosts(MultivaluedMap<String, String> user) throws Exception {
		
        ArrayList<Map<String, Object>> myposts = new ArrayList<>();     
    	String storedProcedure = "{call spr_getMyPosts(?)}";
		calstat = conn.prepareCall(storedProcedure);
		calstat.setInt(1, Integer.parseInt(user.getFirst("userid")));			
		ResultSet rs = calstat.executeQuery();

        while(rs.next()) {	
        	Map<String, Object> responseData = new HashMap<String, Object>();
        	Integer postid = rs.getInt("post_id");
        	Integer year = rs.getInt("year");
        	responseData.put("post_id",postid.toString());
        	responseData.put("bookname",rs.getString("book_name"));
        	responseData.put("authorname",rs.getString("author_name"));
        	responseData.put("year",year.toString());
        	responseData.put("categoryname",rs.getString("category_name"));
        	responseData.put("publishername",rs.getString("publisher_name"));
        	responseData.put("condition",rs.getString("condition"));
        	responseData.put("status",rs.getString("status"));
			myposts.add(responseData);
        	
        }
        

        String mapAsJson = new ObjectMapper().writeValueAsString(myposts);
		return mapAsJson;
    }
	@Path("/setStatus")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response setPostStatus (MultivaluedMap<String, String> post) throws Exception {
		
		int response = -1;
			String storeProc = "{call spr_updatePostStatus(?,?)}";
			calstat = conn.prepareCall(storeProc);
			calstat.setInt(1, Integer.parseInt(post.getFirst("postid")));
			calstat.registerOutParameter(2, Types.INTEGER);
			calstat.executeUpdate();
			int res = calstat.getInt(2);
			calstat.close();
			if (res == 0)
			{
				response =  0;
			}
			else
			{
				response = -1;
			}	
			return Response.status(200).entity(String.valueOf(response)).build();
		
	}
}
