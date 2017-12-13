package bookstacks.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 * Servlet implementation class Register
 */

public class Register extends HttpServlet {
	

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// For registration
		if(!request.getParameter( "regCmd" ).equals("") && request.getParameter( "regCmd" ).equals("Register")){
			int status = -1;
			
			try {
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/accountmanipulate/registerUser");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add("firstname", request.getParameter( "firstname" ));
				formData.add("lastname", request.getParameter( "lastname" ));
				formData.add("email", request.getParameter( "email" ));
				formData.add("password", request.getParameter( "password" ));
				formData.add("phone",  request.getParameter( "phone" ));
				formData.add("location", request.getParameter( "location" ));
				
				
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
				request.setAttribute("registrationMsg", "Registration successful! Login to continue");
			    request.getRequestDispatcher("login.jsp").forward(request, response);
			}
			else{
				request.setAttribute("registrationMsg", "Registration failed! Please try again");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
		
		
		else if(!request.getParameter( "regCmd" ).equals("") && request.getParameter( "regCmd" ).equals("Login")){
			// For login
			int status =-1;
			HashMap<String, Object> dataMap = null;
			try {
				
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/accountmanipulate/validateUser");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add( "email",request.getParameter( "login_email" ) );
				formData.add( "password",request.getParameter( "login_password" ) );
				formData.add( "location",request.getParameter( "location" ) );
				
				ClientResponse restResponse = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
				response.setContentType(MediaType.APPLICATION_JSON_TYPE.toString());
				dataMap = new ObjectMapper().readValue(restResponse.getEntityInputStream(), HashMap.class);

				status = (int)dataMap.get("flag");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
	        if(status == 0)
	        {
	        	String lastlogin = (String)dataMap.get("lastlogin")+" "+(String)dataMap.get("lastlocation");
	        	HttpSession session = request.getSession();
	        	session.setAttribute("lastlogin", lastlogin);
	        	session.setAttribute("user_email", (String)dataMap.get("email"));
	        	session.setAttribute("user_firstname", (String)dataMap.get("firstname"));
	        	session.setAttribute("user_lastname", (String)dataMap.get("lastname"));
	        	session.setAttribute("user_id", (int)dataMap.get("userid"));
	        	if(request.getParameter( "url" ) != null && !request.getParameter( "url" ).equals("")){
	        		request.getRequestDispatcher(request.getParameter( "url" )).forward(request, response);
	        	}else{
	                request.getRequestDispatcher("index.jsp").forward(request, response);
	        	}
	        }
	        else
	        {
	        	request.setAttribute("registrationMsg", "Wrong username and/or password. Try again!");
	        	if(request.getParameter( "url" ) != null && !request.getParameter( "url" ).equals("")){
	        		request.getRequestDispatcher("login.jsp?redirect="+request.getParameter( "url" )).forward(request, response);
	        	}else{
	        		request.getRequestDispatcher("login.jsp").forward(request, response);
	        	}
	        	
	        }
			
		}
		else{
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		
		
	}

}
