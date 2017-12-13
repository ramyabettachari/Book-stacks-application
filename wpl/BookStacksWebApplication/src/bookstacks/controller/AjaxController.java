package bookstacks.controller;

import java.io.IOException;
import java.util.HashMap;
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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 * Servlet implementation class AjaxController
 */
public class AjaxController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("cmd") != null && request.getParameter("cmd").equals("checkuser")){
			int status = -1;
			try {
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/accountmanipulate/checkEmailPresent");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				formData.add("email", request.getParameter("email"));
				ClientResponse restResponse = webResource
				    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				    .post(ClientResponse.class, formData);
				
				if (restResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
				}
	 
				String statusString = restResponse.getEntity(String.class);
				status = Integer.parseInt(statusString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    if(status == 0){
		    	response.getWriter().append("invalid");
		    }else{
		    	response.getWriter().append("valid");
		    }
		}
		
		if(request.getParameter("cmd") != null && request.getParameter("cmd").equals("updatePassword")){
			int status = -3;
			try {
				HttpSession session = request.getSession();
				if(session.getAttribute("user_id") == null || session.getAttribute("user_id").equals("")){
					String uri = request.getRequestURI();
					String pageName = uri.substring(uri.lastIndexOf("/")+1);
					response.sendRedirect("login.jsp?redirect="+pageName);
				}
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/accountmanipulate/updatePassword");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				formData.add("oldpassword", request.getParameter("oldpassword"));
				formData.add("newpassword", request.getParameter("newpassword"));
				String email = session.getAttribute( "user_email" ).toString();
				formData.add("email",email);
				
				ClientResponse restResponse = webResource
				    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				    .post(ClientResponse.class, formData);
				
				if (restResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
				}
	 
				String statusString = restResponse.getEntity(String.class);
				status = Integer.parseInt(statusString);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		    if(status == 0){
		    	response.getWriter().append("success");
		    }else if(status == -1){
		    	response.getWriter().append("oldinvalid");
		    }else{
		    	response.getWriter().append("failed");
		    }
		}
		if(request.getParameter("cmd") != null && request.getParameter("cmd").equals("updateProfile")){
		    
			int status = -1;
			HttpSession session = request.getSession();
			try {
				if(session.getAttribute("user_id") == null || session.getAttribute("user_id").equals("")){
					String uri = request.getRequestURI();
					String pageName = uri.substring(uri.lastIndexOf("/")+1);
					response.sendRedirect("login.jsp?redirect="+pageName);
				}
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/accountmanipulate/updateProfile");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				formData.add("firstname", request.getParameter("firstname"));
				formData.add("lastname", request.getParameter("lastname"));
				formData.add("phone", request.getParameter("phone"));
				String userid = session.getAttribute( "user_id" ).toString();
				formData.add( "userid",userid);
				ClientResponse restResponse = webResource
				    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				    .post(ClientResponse.class, formData);
				
				if (restResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
				}
	 
				String statusString = restResponse.getEntity(String.class);
				status = Integer.parseInt(statusString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		    if(status == 0){
		    	String firstname = request.getParameter("firstname");
			    String lastname = request.getParameter("lastname");
		    	session.setAttribute("user_firstname",firstname);
		    	session.setAttribute("user_lastname", lastname);
		    	response.getWriter().append("success");
		    }else{
		    	response.getWriter().append("failed");
		    }
		}
		if(request.getParameter("cmd") != null && request.getParameter("cmd").equals("fetchProfile")){
			try {
				HttpSession session = request.getSession();
				Map<String,Object> dataMap;
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/accountmanipulate/getProfile");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				String userid = session.getAttribute( "user_id" ).toString();
				formData.add( "userid",userid);
				
				
				ClientResponse restResponse = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
				
				dataMap = new ObjectMapper().readValue(restResponse.getEntityInputStream(), HashMap.class);
				StringBuilder sb = new StringBuilder();
				
				for (Map.Entry<String, Object> entry : dataMap.entrySet())
				{
				    sb.append(entry.getKey() + "/" + (String)entry.getValue()+"#");
				}
				response.getWriter().append(sb);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}

}
