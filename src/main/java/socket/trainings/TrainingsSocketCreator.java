package socket.trainings;

import db.WordsDAO;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class TrainingsSocketCreator implements WebSocketCreator {
    private WordsDAO wordsDAO;

    public TrainingsSocketCreator(WordsDAO wordsDAO) {
        this.wordsDAO = wordsDAO;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {

        String login = (String)req.getSession().getAttribute("SessionId");

        return new TrainingSocket(wordsDAO,login);
    }
}
