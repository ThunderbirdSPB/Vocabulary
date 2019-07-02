package socket.dictionary;

import db.WordsDAO;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "Dictionary Socket Servlet", urlPatterns = "/dictionary")
public class DictionarySocketServlet extends WebSocketServlet {
    private WordsDAO wordsDAO;

    public DictionarySocketServlet(WordsDAO wordsDAO) {
        this.wordsDAO = wordsDAO;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(new DictionarySocketCreator(wordsDAO));
    }
}
