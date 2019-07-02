package servlets;

import db.UserDAO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {
    private UserDAO userDAO;

    public SignUpServlet(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        userDAO.addNewUser(login,req.getParameter("password"));
        req.getSession(true).setAttribute("SessionId",login);
        resp.sendRedirect("/trainings.html");
    }
}
