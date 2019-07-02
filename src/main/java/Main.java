import db.QueryExecutor;
import db.UserDAO;
import db.WordsDAO;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.SignInServlet;
import servlets.SignUpServlet;
import socket.dictionary.DictionarySocketServlet;
import socket.trainings.TrainingsSocketServlet;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/";
        String login = "postgres";
        String password = "0000";
        Connection con = DriverManager.getConnection(url, login, password);

        Server server = new Server(8080);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("static/temp");
        resourceHandler.setWelcomeFiles(new String[]{"signIn.html"});

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder(new DictionarySocketServlet(new WordsDAO(new QueryExecutor(con)))), "/dictionary");
        contextHandler.addServlet(new ServletHolder(new SignInServlet(new UserDAO(new QueryExecutor(con)))),"/signin");
        contextHandler.addServlet(new ServletHolder(new SignUpServlet(new UserDAO(new QueryExecutor(con)))),"/signup");
        contextHandler.addServlet(new ServletHolder(new TrainingsSocketServlet(new WordsDAO(new QueryExecutor(con)))),"/training");
        contextHandler.setSessionHandler(new SessionHandler());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler,contextHandler});

        server.setHandler(handlers);

        server.start();
        server.join();
    }
}


