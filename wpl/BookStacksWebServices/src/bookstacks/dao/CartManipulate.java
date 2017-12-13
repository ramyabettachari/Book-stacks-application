package bookstacks.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import bookstacks.conn.DBUtil;

import java.sql.CallableStatement;


@Path("/cartmanipulate")
public class CartManipulate {
	private static Connection conn;
	private CallableStatement calstat; 
	public CartManipulate() {
		conn = DBUtil.getConnection();
		calstat = null;
	}
	@Path("/addItemToCart")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response addItemToCart (MultivaluedMap<String, String> cart) throws Exception {
		int response = -1;
			String insertItemStoreProc = "{call spr_addToCart(?,?,?,?)}";
			calstat = conn.prepareCall(insertItemStoreProc);
			calstat.setInt(1, Integer.parseInt(cart.getFirst("userid")));
			calstat.setInt(2, Integer.parseInt(cart.getFirst("bid_id")));
			calstat.setInt(3, Integer.parseInt(cart.getFirst("quantity")));
			calstat.registerOutParameter(4, Types.INTEGER);
			calstat.executeUpdate();
			response = calstat.getInt(4);
			
			calstat.close();
		
			return Response.status(200).entity(String.valueOf(response)).build();
	}
	@Path("/deleteItemFromCart")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response deleteItemFromCart (MultivaluedMap<String, String> cart) throws Exception {
		int res =-1;
			String deleteSP = "{call spr_deleteCartItem(?,?)}";
			calstat = conn.prepareCall(deleteSP);
			calstat.setInt(1, Integer.parseInt(cart.getFirst("cartid")));
			calstat.registerOutParameter(2, Types.INTEGER);
			calstat.executeUpdate();
			res = calstat.getInt(2);
			
			calstat.close();
		
			return Response.status(200).entity(String.valueOf(res)).build();
	}
	@Path("/updateQuantityInCart")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response updateQuantityInCart (MultivaluedMap<String, String> cart) throws Exception {
		int res = -1;
			String deleteSP = "{call spr_updateQuantity(?,?,?)}";
			calstat = conn.prepareCall(deleteSP);
			calstat.setInt(1, Integer.parseInt(cart.getFirst("cartid")));
			calstat.setInt(2, Integer.parseInt(cart.getFirst("quantity")));
			calstat.registerOutParameter(3, Types.INTEGER);
			calstat.executeUpdate();
			res = calstat.getInt(3);
			calstat.close();
		
			return Response.status(200).entity(String.valueOf(res)).build();
	}
	
	@Path("/checkout")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String checkout(MultivaluedMap<String, String> user) throws Exception {
		ArrayList<Map<String, Object>> checkoutlist = new ArrayList<>();
		String sp = "{call spr_placeOrder(?,?)}";
		calstat = conn.prepareCall(sp);
		calstat.setInt(1, Integer.parseInt(user.getFirst("userid")));
		calstat.registerOutParameter(2, Types.INTEGER);
		calstat.execute();
		int res = calstat.getInt(2);
		if (res == 0)
		{
			ResultSet rs = calstat.getResultSet();
            while(rs.next()) {	
            	Map<String, Object> responseData = new HashMap<String, Object>();
            	responseData.put("bookname",rs.getString("book_name"));
            	responseData.put("biddername",rs.getString("biddername"));
            	responseData.put("email",rs.getString("email"));
            	responseData.put("bidamount",rs.getDouble("bid_amount"));
            	responseData.put("quantity",rs.getInt("quantity"));
            	responseData.put("cartid",rs.getInt("cart_id"));
            	checkoutlist.add(responseData);
            }
			
		}
		calstat.close();
		String mapAsJson = new ObjectMapper().writeValueAsString(checkoutlist);
 		return mapAsJson;
	}
	
	
	@Path("/getBidDetails")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String getBidDetails(MultivaluedMap<String, String> post) throws Exception {
		ArrayList<Map<String, Object>> bidList = new ArrayList<>();

        	String storedProcedure = "{call spr_getBidDetails(?,?)}";
			calstat = conn.prepareCall(storedProcedure);
			calstat.setInt(1, Integer.parseInt(post.getFirst("userid")));
			calstat.setInt(2, Integer.parseInt(post.getFirst("postid")));
			ResultSet rs = calstat.executeQuery();

            while(rs.next()) {	
            	Map<String, Object> responseData = new HashMap<String, Object>();
            	responseData.put("bookname",rs.getString("book_name"));
            	responseData.put("bidder_firstname",rs.getString("firstname"));
            	responseData.put("bidder_lastname",rs.getString("lastname"));
            	responseData.put("bidder_email",rs.getString("email"));
            	responseData.put("bidamount",rs.getDouble("bid_amount"));
            	responseData.put("bid_id",rs.getInt("bid_id"));
            	bidList.add(responseData);
            }
        

            String mapAsJson = new ObjectMapper().writeValueAsString(bidList);
    		return mapAsJson;
    }

	@Path("/getCartDisplayDetails")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String getCartDisplayDetails(MultivaluedMap<String, String> cart) throws Exception {
		ArrayList<Map<String, Object>> cartItems = new ArrayList<>();
            
        	String storedProcedure = "{call spr_cartDisplay(?)}";
			calstat = conn.prepareCall(storedProcedure);
			calstat.setInt(1, Integer.parseInt(cart.getFirst("userid")));			
			ResultSet rs = calstat.executeQuery();

            while(rs.next()) {	
            	Map<String, Object> responseData = new HashMap<String, Object>();
            	
            	responseData.put("bookname",rs.getString("book_name"));
            	responseData.put("biddername",rs.getString("biddername"));
            	responseData.put("bidamount",rs.getDouble("bid_amount"));
            	responseData.put("quantity",rs.getInt("quantity"));
            	responseData.put("cart_id",rs.getInt("cart_id"));
            	cartItems.add(responseData);
            }

            String mapAsJson = new ObjectMapper().writeValueAsString(cartItems);
    		return mapAsJson;
    }
	@Path("/getCartCount")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response getCartCount (MultivaluedMap<String, String> cart) throws Exception {
        int count = 0; 
            
        	String storedProcedure = "{call spr_getCartCount(?,?)}";
			calstat = conn.prepareCall(storedProcedure);
			calstat.setInt(1, Integer.parseInt(cart.getFirst("userid")));			
			calstat.registerOutParameter(2, Types.INTEGER);
			calstat.executeUpdate();
			count = calstat.getInt(2);
			calstat.close();
        
		return Response.status(200).entity(String.valueOf(count)).build();
    }

}
