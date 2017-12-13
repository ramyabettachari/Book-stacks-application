package bookstacks.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Servlet implementation class PostsController
 */
@WebServlet({ "/PostsController", "/PostsController.do" })
public class PostsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostsController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null || session.getAttribute("user_id").equals("")){
			String uri = request.getRequestURI();
			String pageName = uri.substring(uri.lastIndexOf("/")+1);
			response.sendRedirect("login.jsp?redirect="+pageName);
		}
		if(request.getParameter("cmd").equals("postDetails")){
			try {
				
				ArrayList<String> list;
				String userid= session.getAttribute("user_id").toString();
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/postmanipulate/getPostDetails");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				formData.add("userid", userid);
				formData.add("query", request.getParameter("query"));
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
		if(request.getParameter("cmd").equals("myposts")){
			try {

				ArrayList<String> list;
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/postmanipulate/getMyPosts");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				String userid = session.getAttribute( "user_id" ).toString();
				formData.add( "userid",userid);
				
				
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
		if(request.getParameter("cmd").equals("closePost")){
			int status = -1;
			try {
				String postid = request.getParameter("post_id");
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/postmanipulate/setStatus");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add( "postid",postid );
				
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
