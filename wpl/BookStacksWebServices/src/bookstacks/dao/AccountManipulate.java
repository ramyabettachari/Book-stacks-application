package bookstacks.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.ws.rs.Path;
import java.sql.CallableStatement;
import bookstacks.conn.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import javax.ws.rs.PathParam;

@Path("/accountmanipulate")
public class AccountManipulate {
	
	private static Connection conn;
	private CallableStatement calstat; 
	public AccountManipulate() {
		conn = DBUtil.getConnection();
		calstat = null;
	}
	
	@Path("/registerUser")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response registerUser (MultivaluedMap<String, String> user) throws Exception{
		int response = -1;
		String storedProcedure = "{call spr_registerUser(?,?,?,?,?,?,?)}";
		calstat = conn.prepareCall(storedProcedure);
		calstat.setString(1, user.getFirst("firstname"));
		calstat.setString(2, user.getFirst("lastname"));
		calstat.setString(3, user.getFirst("email"));
		calstat.setString(4, user.getFirst("password"));
		calstat.setString(5, user.getFirst("phone"));
		calstat.setString(6, user.getFirst("location"));
		calstat.registerOutParameter(7, Types.INTEGER);
		calstat.executeUpdate();
		int res = calstat.getInt(7);
		calstat.close();
		if (res == 0)
		{
			response = 0;
		}
		else
		{
			response = -1;
		}
		
		return Response.status(200).entity(String.valueOf(response)).build();
	}
	
	@Path("/validateUser")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
	public String validateUser(MultivaluedMap<String, String> user) throws Exception {
	    int flag =-1;
    	String storedProcedure = "{call spr_validateUser(?,?,?,?,?,?,?,?,?)}";
		calstat = conn.prepareCall(storedProcedure);
		calstat.setString(1, user.getFirst("email"));
		calstat.setString(2, user.getFirst("password"));
		calstat.setString(3, user.getFirst("location"));
		calstat.registerOutParameter(4, Types.INTEGER);
		calstat.registerOutParameter(5, Types.TIMESTAMP);
		calstat.registerOutParameter(6, Types.VARCHAR);
		calstat.registerOutParameter(7, Types.VARCHAR);
		calstat.registerOutParameter(8, Types.VARCHAR);
		calstat.registerOutParameter(9, Types.INTEGER);
		calstat.executeQuery();
		int res = calstat.getInt(4);
		HashMap<String,Object> map = new HashMap<>();
		if (res == 0)
		{
			
			Date lst_login = calstat.getTimestamp(5);
			String firstname = calstat.getString(6);
			String lastname = calstat.getString(7);
			String lastlocation = calstat.getString(8);
			int userid = calstat.getInt(9);
			String str_lstlogin = new SimpleDateFormat("MM-dd-yyyy hh:mm a").format(lst_login);
			flag = 0;
			
			map.put("email", user.getFirst("email")); 
			map.put("firstname", firstname); 
			map.put("lastname", lastname);
			map.put("lastlocation", lastlocation); 
			map.put("lastlogin", str_lstlogin);
			map.put("userid", userid);
			map.put("flag", flag);
			
			
		}
		else
		{
			flag = -1;
			map.put("flag", flag);
			
		}
		calstat.close();
	     
		String mapAsJson = new ObjectMapper().writeValueAsString(map);
		return mapAsJson;
     }   
	
	@Path("/checkEmailPresent")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response checkEmailPresent(MultivaluedMap<String, String> email) throws Exception{
		int flag = -1;
		String storedProcedure = "{call spr_getEmail(?)}";
		calstat = conn.prepareCall(storedProcedure);
		calstat.setString(1, email.getFirst("email"));			
		ResultSet resultSet = calstat.executeQuery();
		if (resultSet.next())
		{
			int count = Integer.parseInt(resultSet.getString(1));
			System.out.println(count);
			if (count == 0)
			{
				flag = -1;
			}
			else
			{
				flag = 0;
			}
			
		}
		resultSet.close();
		calstat.close();
		return Response.status(200).entity(String.valueOf(flag)).build();
		
	}
	
	
	@Path("/updatePassword")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response updatePassword(MultivaluedMap<String, String> user) throws Exception{
		int res = -2;
		String storedProcedure = "{call spr_changePassword(?,?,?,?)}";
		calstat = conn.prepareCall(storedProcedure);
		calstat.setString(1, user.getFirst("email"));
		calstat.setString(2, user.getFirst("oldpassword"));
		calstat.setString(3, user.getFirst("newpassword"));
		calstat.registerOutParameter(4, Types.INTEGER);
		calstat.executeQuery();
		res = calstat.getInt(4);
		System.out.println(res);
		calstat.close();
		return Response.status(200).entity(String.valueOf(res)).build();
	}
	
	@Path("/updateProfile")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response updateProfile(MultivaluedMap<String, String> user) throws Exception{
		int res = -1;
		String storedProcedure = "{call spr_updateProfile(?,?,?,?,?)}";
		calstat = conn.prepareCall(storedProcedure);
		calstat.setInt(1, Integer.parseInt(user.getFirst("userid")));
		calstat.setString(2, user.getFirst("firstname"));
		calstat.setString(3, user.getFirst("lastname"));
		calstat.setString(4, user.getFirst("phone"));
		calstat.registerOutParameter(5, Types.INTEGER);
		calstat.executeQuery();
		res = calstat.getInt(5);
		calstat.close();
		return Response.status(200).entity(String.valueOf(res)).build();
	}
	
	@Path("/getProfile")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
	public String getProfile(MultivaluedMap<String, String> user) throws Exception{
		String storedProcedure = "{call spr_getProfile(?)}";
		calstat = conn.prepareCall(storedProcedure);
		calstat.setInt(1, Integer.parseInt(user.getFirst("userid")));
		ResultSet resultSet = calstat.executeQuery();
		HashMap<String,Object> map = new HashMap<>();
		if (resultSet.next())
		{
			map.put("firstname", resultSet.getString("firstname"));
			map.put("lastname", resultSet.getString("lastname"));
			map.put("email", resultSet.getString("email"));
			map.put("phone", resultSet.getString("phone"));
		}
		calstat.close();
		String mapAsJson = new ObjectMapper().writeValueAsString(map);
		return mapAsJson;
	}

}
