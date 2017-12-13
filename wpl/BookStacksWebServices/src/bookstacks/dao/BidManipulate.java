package bookstacks.dao;

import java.sql.Connection;
import java.sql.Types;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.sql.CallableStatement;


import bookstacks.conn.*;

@Path("/bidmanipulate")
public class BidManipulate {
	
	private Connection conn;
	private CallableStatement calstat; 
	public BidManipulate() {
		conn = DBUtil.getConnection();
		calstat = null;
	}
	
	
	@Path("/placeBid")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
	public Response placeBid (MultivaluedMap<String, String> bid ) throws Exception {
		
		int res = -1;
		String storeProc = "{call spr_placeBid(?,?,?,?)}";
		calstat = conn.prepareCall(storeProc);
		calstat.setInt(1, Integer.parseInt(bid.getFirst("userid")));
		calstat.setInt(2, Integer.parseInt(bid.getFirst("postid")));
		calstat.setDouble(3, Double.parseDouble(bid.getFirst("bidamount")));
		calstat.registerOutParameter(4, Types.INTEGER);
		calstat.executeUpdate();
		res = calstat.getInt(4);
		calstat.close();
			
		return Response.status(200).entity(String.valueOf(res)).build();
	}
	
}
