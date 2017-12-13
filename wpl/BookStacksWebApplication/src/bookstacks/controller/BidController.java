package bookstacks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 * Servlet implementation class BidController
 */
@WebServlet({ "/BidController", "/BidController.do" })
public class BidController extends HttpServlet {

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BidController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null || session.getAttribute("user_id").equals("")){
			String uri = request.getRequestURI();
			String pageName = uri.substring(uri.lastIndexOf("/")+1);
			response.sendRedirect("login.jsp?redirect="+pageName);
		}
		if(request.getParameter("cmd").equals("placebid")){
			int status = -1;
			
			try {
				String userid = session.getAttribute("user_id").toString();
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/bidmanipulate/placeBid");
				MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
				
				formData.add("userid", userid);
				formData.add("postid", request.getParameter("post_id"));
				formData.add("bidamount", request.getParameter("bidamount"));
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
	}
}
