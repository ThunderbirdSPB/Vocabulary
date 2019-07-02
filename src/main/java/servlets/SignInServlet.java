package servlets;

import db.UserDAO;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInServlet extends HttpServlet {
    private UserDAO userDAO;

    public SignInServlet(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");

        if (userDAO.exists(login)){
            req.getSession(true).setAttribute("SessionId",login);
            resp.sendRedirect("/dictionary.html");
        }else {
            resp.sendRedirect("/signIn.html");
        }
    }
}
