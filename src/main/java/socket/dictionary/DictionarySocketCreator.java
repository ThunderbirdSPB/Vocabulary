package socket.dictionary;

import db.WordsDAO;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class DictionarySocketCreator implements WebSocketCreator {
    private WordsDAO wordsDAO;

    public DictionarySocketCreator(WordsDAO wordsDAO) {
        this.wordsDAO = wordsDAO;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        return new DictionarySocket((String) req.getSession().getAttribute("SessionId"),wordsDAO);
    }
}
