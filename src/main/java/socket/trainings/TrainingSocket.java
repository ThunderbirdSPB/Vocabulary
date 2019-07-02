package socket.trainings;

import db.WordsDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@WebSocket
public class TrainingSocket {
    private WordsDAO wordsDAO;
    private String login;
    private Session session;
    private String tr;

    public TrainingSocket(WordsDAO wordsDAO, String login) {
        this.wordsDAO = wordsDAO;
        this.login = login;
    }

    @OnWebSocketConnect
    public void onOpen(Session session){
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String data) throws Exception {
        if (data.equals("getWords"))
            getWords();

        else if (data.equals("getRandomEngWords"))
           getRandomEngWords();

        else if (data.equals("getRandomRusWords"))
            getRandomRusWords();

        else if(data.startsWith("["))
            setCompletedWords(data);

        else {
            switch (data){
                case "rus_eng":
                    tr = "re";
                    break;
                case "eng_rus":
                    tr = "er";
                    break;
                case "writing":
                    tr = "writing";
                    break;
                case "cards":
                    tr = "cards";
                    break;
            }
        }
    }

    private void getWords() throws IOException {
        List<String> words = wordsDAO.getUncompletedWords(tr, login);
        int maxSize = words.size() > 10 ? 10:words.size();
        if (words.size() == 1){
            session.getRemote().sendString(words.get(0));
            return;
        }
        if (words.isEmpty()) {
            session.getRemote().sendString("endWords");
            return;
        }

        for (int i = 0; i < maxSize -1; i++)
            session.getRemote().sendPartialString(words.get(i),false);
        session.getRemote().sendPartialString(words.get(maxSize -1).trim(),true);
    }

    private void getRandomEngWords() throws IOException {
        List<String> randomWords = wordsDAO.getNRandomEngWords(40);
        for (int i = 0; i < 39; i++)
            session.getRemote().sendPartialString(randomWords.get(i),false);
        session.getRemote().sendPartialString(randomWords.get(39),true);
    }

    private void getRandomRusWords() throws IOException {
        List<String> randomWords = wordsDAO.getNRandomRusWords(40);
        for (int i = 0; i < 39; i++)
            session.getRemote().sendPartialString(randomWords.get(i),false);
        session.getRemote().sendPartialString(randomWords.get(39),true);
    }

    private void setCompletedWords(String data) throws Exception {
        String completed[] = data.substring(2,data.length() -2).split("\",\"");
        wordsDAO.setCompletedTrainings(new ArrayList<>(Arrays.asList(completed)),login,completed[0]);
    }
}
