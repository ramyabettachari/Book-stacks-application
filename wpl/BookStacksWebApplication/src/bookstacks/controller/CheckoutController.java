package bookstacks.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import bookstacks.mailer.Mailer;


/**
 * Servlet implementation class CheckoutController
 */
@WebServlet({ "/CheckoutController", "/CheckoutController.do" })
public class CheckoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    Mailer mail = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckoutController() {
        super();
        mail = new Mailer();
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
		try {
			ArrayList<Map<String,Object>> list;
			String userid= session.getAttribute("user_id").toString();
			String useremail = (String)session.getAttribute("user_email");
			String userfirstname = (String)session.getAttribute("user_firstname");
			String userlastname = (String)session.getAttribute("user_lastname");
			
			Client client = Client.create();
			WebResource webResource = client.resource("http://localhost:8080/BookStacksWebServices/cartmanipulate/checkout");
			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			formData.add("userid", userid);
			ClientResponse restResponse = webResource
				    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				    .post(ClientResponse.class, formData);
			list = new ObjectMapper().readValue(restResponse.getEntityInputStream(), ArrayList.class);
			if(!list.isEmpty())
			{
				String bidderemail_title = "";
				String bidderemail_body = "";
				double total = 0;
				String orderinfo = "";
				String bidinfo = "";
				for(Map<String,Object> l: list)
				{
					double bamt = Double.parseDouble(l.get("bidamount").toString());
					int qty = Integer.parseInt(l.get("quantity").toString());
					orderinfo += "Book name: "+l.get("bookname").toString()+"\n";
					orderinfo += "Bidder name: "+l.get("biddername").toString()+"\n";
					orderinfo += "Bid amount: "+l.get("bidamount").toString()+"\n";
					orderinfo += "Quantity: "+l.get("quantity").toString()+"\n";
					orderinfo += "-------------------------------------------------------------------------------------\n\n";
					
					bidinfo += "Book name: "+l.get("bookname").toString()+"\n";
					bidinfo += "Bid amount: "+l.get("bidamount").toString()+"\n";
					bidinfo += "Quantity: "+l.get("quantity").toString()+"\n";
					bidinfo += "Purchaser name: "+userfirstname+" "+userlastname;
					total += bamt*qty;
			
					bidderemail_title += "Book Stacks - Your bid has been successfully checked out";
					bidderemail_body += "Dear "+l.get("biddername").toString()+",\n\n";
					bidderemail_body += "Please find the details below: \n\n";
					bidderemail_body += bidinfo;
					
					try {
						Mailer.Send(l.get("email").toString(), bidderemail_title, bidderemail_body);
					} catch (AddressException e) {
						e.printStackTrace();
					} catch (MessagingException e) {
						e.printStackTrace();
					}

					
				}
				orderinfo += "Total: $"+total+"\n";
				String purchaseremail_title = "Book Stacks - Order Confirmation";
				String purchaseremail_body = "Dear "+userfirstname+" "+userlastname+",\n\n";
				purchaseremail_body += "Please find your order details below: \n\n";
				purchaseremail_body += orderinfo;
				// Send email to Purchaser
				try {
					Mailer.Send(useremail, purchaseremail_title, purchaseremail_body);
				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}

			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.getRequestDispatcher("checkout.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
