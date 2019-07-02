package socket.dictionary;


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
public class DictionarySocket{
    private Session session;
    private String login;
    private WordsDAO wordsDAO;

    public DictionarySocket(String login, WordsDAO wordsDAO) {
        this.login = login;
        this.wordsDAO = wordsDAO;
    }

    @OnWebSocketConnect
    public void onOpen(Session session){
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String data) throws Exception {
        if (data.equals("getWords"))
           getWords();
        else if (data.contains("<remove>"))
            remove(data);
        else if (data.contains("<add>"))
            add(data);
        else if (data.trim().endsWith("\"send\"]"))
            sendToTrainings(data);

        else if (data.endsWith("\"deleteAll\"]"))
            deleteCheckedWords(data);
    }

    private void getWords() throws IOException {
        List<String> words = wordsDAO.getAllWords(login);
        if (words.isEmpty())
            return;

        for (int i = 0; i < words.size() - 1; i++)
            session.getRemote().sendPartialString(words.get(i) + "_:_",false);

        session.getRemote().sendPartialString(words.get(words.size() -1),true);
    }

    private void remove(String data){
        String word = data.split("<remove>")[0];

        wordsDAO.removeWord(word,login);
    }

    private void add(String data){
        String word = data.split("<add>")[0];
        String translation = data.split("<add>")[1];

        wordsDAO.addNewWord(word,translation,login);
    }

    private void sendToTrainings(String data) throws Exception {
        String uncompleted[] = data.substring(2,data.length() -2).split("\",\"");
        wordsDAO.setUncompletedWords(new ArrayList<>(Arrays.asList(uncompleted)), login);
    }

    private void deleteCheckedWords(String data){
        String forDelete[] = data.substring(2,data.length() -2).split("\",\"");
        wordsDAO.deleteCheckedWords(new ArrayList<>(Arrays.asList(forDelete)),login);
    }
}




