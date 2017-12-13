package bookstacks.controller;

import java.io.IOException;
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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Servlet implementation class PostCreation
 */
@WebServlet("/PostCreation")

public class PostCreation extends HttpServlet {
	

	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostCreation() {
        super();
        // TODO Auto-generated constructor stub
 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int status = -1;
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null || session.getAttribute("user_id").equals("")){
			String uri = request.getRequestURI();
			String pageName = uri.substring(uri.lastIndexOf("/")+1);
			response.sendRedirect("login.jsp?redirect="+pageName);
		}
		try {
			Client client = Client.create();
			WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/postmanipulate/createPost");
			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			String userid = session.getAttribute( "user_id" ).toString();
			formData.add("userid",userid);
			formData.add( "bookname",request.getParameter( "bookname" ) );
			formData.add("category",request.getParameter( "category" ));
			formData.add( "pubname",request.getParameter( "publishername" ) );
			formData.add("year", request.getParameter( "year" ));
			formData.add( "condition",request.getParameter( "condition" ) );
			formData.add("authname",request.getParameter( "authorname" ));
			
			
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
			
		if(status==0)
        {
			request.setAttribute("creationMsg", "Post was created successfully! Want to create another post?");
        	request.getRequestDispatcher("createpost.jsp").forward(request, response);
        }
        else
        {
        	request.setAttribute("creationMsg", "Sorry! Error in creating your post. Try again!");
        	request.getRequestDispatcher("createpost.jsp").forward(request, response);
        }
		
	}

}
