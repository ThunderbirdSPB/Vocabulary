package socket.trainings;

import db.WordsDAO;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "TrainingsSocketServlet", urlPatterns = {"/training"})
public class TrainingsSocketServlet extends WebSocketServlet {
    private WordsDAO wordsDAO;

    public TrainingsSocketServlet(WordsDAO wordsDAO) {
        this.wordsDAO = wordsDAO;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(new TrainingsSocketCreator(wordsDAO));
    }
}
