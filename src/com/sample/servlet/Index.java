package com.sample.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sample.neo4j.GraphDatabaseDAO;

/**
 * Servlet implementation class Index
 */
@WebServlet("/Index")
public class Index extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Index() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String submit = (String)request.getParameter("submit");
		String myName = (String)request.getParameter("myName");
		String friendName = (String)request.getParameter("friendName");
		String distance = (String)request.getParameter("distance");
		
		if (submit.equals("Add")) {
			
			if ((myName != null && !myName.equals("")) 
					&& (friendName != null && !friendName.equals(""))) {
				
				try (GraphDatabaseDAO dao = new GraphDatabaseDAO("bolt://localhost:11006", "neo4j", "password")) {
					dao.addFriend(myName, friendName);
					request.setAttribute("message", "Successfully added friend!");
		        } catch(Exception e) {
		        		request.setAttribute("message", "Exception thrown when adding friend!");
				}
				
			}
			
		} else if (submit.equals("List")) {
			
			if ((myName != null && !myName.equals("")) 
					&& (distance != null && !distance.equals(""))) {
				
				try (GraphDatabaseDAO dao = new GraphDatabaseDAO("bolt://localhost:11006", "neo4j", "password")) {
					HashMap<String, String> result = dao.listFriends(myName, Integer.parseInt(distance));
					request.setAttribute("result", result);
		        } catch(Exception e) {
		        		request.setAttribute("message", "Exception thrown when listing friends!");
				}
			}
			
		}
		
		String nextJSP = "/index.jsp";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
