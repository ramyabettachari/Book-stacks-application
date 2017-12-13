package bookstacks.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
/**
 * Servlet implementation class CartController
 */
@WebServlet({ "/CartController", "/CartController.do" })
public class CartController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartController() {
        super();

        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null || session.getAttribute("user_id").equals("")){
			String uri = request.getRequestURI();
			String pageName = uri.substring(uri.lastIndexOf("/")+1);
			response.sendRedirect("login.jsp?redirect="+pageName);
		}
		if(request.getParameter("cmd").equals("postbids")){
			try {
				
				ArrayList<String> list;
				String userid= session.getAttribute("user_id").toString();
				String postid = request.getParameter("postid").toString();
				
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/cartmanipulate/getBidDetails");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add("userid", userid);
				formData.add("postid", postid);
				ClientResponse restResponse = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
				list = new ObjectMapper().readValue(restResponse.getEntityInputStream(), ArrayList.class);
				Gson gson = new Gson();
				JsonElement element = gson.toJsonTree(list, new TypeToken<List<Map<String,Object>>>() {}.getType());

				JsonArray jsonArray = element.getAsJsonArray();
				response.setContentType("application/json");
				response.getWriter().print(jsonArray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

		if(request.getParameter("cmd").equals("cartdetails")){
			
			String userid = session.getAttribute("user_id").toString();
			ArrayList<String> list;
			try {
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/cartmanipulate/getCartDisplayDetails");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add("userid", userid);
				ClientResponse restResponse = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
				list = new ObjectMapper().readValue(restResponse.getEntityInputStream(), ArrayList.class);
				Gson gson = new Gson();
				JsonElement element = gson.toJsonTree(list, new TypeToken<List<Map<String,Object>>>() {}.getType());

				JsonArray jsonArray = element.getAsJsonArray();
				response.setContentType("application/json");
				response.getWriter().print(jsonArray);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(request.getParameter("cmd").equals("cartCount")){
			
			String userid = session.getAttribute("user_id").toString();
			int count = 0;
			try {
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/cartmanipulate/getCartCount");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add("userid", userid);
				ClientResponse restResponse = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
				if (restResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
				}
	 
				String countString = restResponse.getEntity(String.class);
				count = Integer.parseInt(countString);
				response.getWriter().print(count);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		if(request.getParameter("cmd").equals("addItem")){
			int status = -1;
			HttpSession session = request.getSession();
			try {
				String userid = session.getAttribute("user_id").toString();
				String bid_id = request.getParameter("bid_id").toString();
				String quantity = request.getParameter("quantity").toString();
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/cartmanipulate/addItemToCart");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add("userid", userid);
				formData.add("bid_id", bid_id);
				formData.add("quantity", quantity);
				ClientResponse restResponse = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
				if (restResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
				}
 
				String statusString = restResponse.getEntity(String.class);
				status = Integer.parseInt(statusString);

				if(status == 0){
					response.getWriter().append("inserted");
				}else if(status == 1){
					response.getWriter().append("updated");
				}
				else{
					response.getWriter().append("failed");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		if(request.getParameter("cmd").equals("deleteItem")){
			int status = -1;
			try {
				String cartid = request.getParameter("cart_id").toString();
				
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/cartmanipulate/deleteItemFromCart");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add("cartid", cartid);
				ClientResponse restResponse = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
				if (restResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
				}
 
				String statusString = restResponse.getEntity(String.class);
				status = Integer.parseInt(statusString);

				if(status == 0){
					response.getWriter().append("success");
				}
				else{
					response.getWriter().append("failed");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(request.getParameter("cmd").equals("updateQuantity")){
			int status = -1;
			try {
				String cartid = request.getParameter("cart_id").toString();
				String quantity = request.getParameter("quantity").toString();
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/cartmanipulate/updateQuantityInCart");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add("cartid", cartid);
				formData.add("quantity", quantity);
				ClientResponse restResponse = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
				if (restResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
				}
 
				String statusString = restResponse.getEntity(String.class);
				status = Integer.parseInt(statusString);

				if(status == 0){
					response.getWriter().append("success");
				}
				else{
					response.getWriter().append("failed");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
