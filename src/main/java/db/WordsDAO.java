package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class WordsDAO {
    private QueryExecutor executor;

    public WordsDAO(QueryExecutor executor) {
        this.executor = executor;
    }


    /**
     *
     INSERT INTO public.admin(
        word, translation, "completedTrainings")
        VALUES ('Hello', 'Привет', '{}');
     */
    public void addNewWord(String word, String translation, String login){
        executor.execUpdate(String.format("INSERT INTO %s (word, translation, \"completedTrainings\")\n" +
                "VALUES ('%s', '%s', '{re,er,writing,cards}');",login, word, translation));
    }

    /**
     * Method for dictionary action
     * @return All words for particular user
     */
    public List<String> getAllWords(String login){
        List<String> words = new ArrayList<>();
        return executor.execQuery(String.format("SELECT word, translation FROM public.%s",login),(rs) ->{
            while (rs.next())
                words.add(rs.getString("word") + "=" + rs.getString("translation"));
            return words;
        });
    }

    public List<String> getUncompletedWords(String tr, String login){
        List<String> words = new ArrayList<>();
        return executor.execQuery(String.format("SELECT word, translation FROM %s WHERE '%s' = ANY(\"completedTrainings\");",login,tr),(rs) ->{
            while (rs.next())
                words.add(rs.getString("word") + "=" + rs.getString("translation") + "<separator>");
            return words;
        });
    }

    public void setCompletedTrainings(List<String> words, String login, String tr) throws Exception {
        Connection con = executor.getConnection();
        PreparedStatement stmt = null;
        switch (tr){
            case "re":
                stmt = con.prepareStatement(String.format("UPDATE %s SET \"completedTrainings\"[1] = 'null' WHERE word = ?;",login));

            break;
            case "er":
                stmt = con.prepareStatement(String.format("UPDATE %s SET \"completedTrainings\"[2] = 'null' WHERE word = ?;",login));
            break;
            case "writing":
                stmt = con.prepareStatement(String.format("UPDATE %s SET \"completedTrainings\"[3] = 'null' WHERE word = ?;",login));
            break;
            case "cards":
                stmt = con.prepareStatement(String.format("UPDATE %s SET \"completedTrainings\"[4] = 'null' WHERE word = ?;",login));
            break;
            default:
                throw new Exception("Wrong training type");
        }

        for (String s : words) {
            stmt.setString(1,s);
            stmt.addBatch();
        }
        stmt.executeBatch();
    }

    public void setUncompletedWords(List<String> words, String login) throws Exception{
        for (String s : words)
            executor.execUpdate(String.format("UPDATE %s SET \"completedTrainings\" =  ARRAY['re','er','writing','cards'] WHERE word = '%s';",login,s));
    }

    public void deleteCheckedWords(List<String> words, String login){
        for (String s : words)
            executor.execUpdate(String.format("DELETE FROM %s WHERE word = '%s';",login,s));
    }


    public void removeWord(String word, String login){
        executor.execUpdate(String.format("DELETE FROM %s WHERE word='%s'",login,word));
    }

    public List<String> getNRandomEngWords(int n){
        List<String> words = new ArrayList<>();
        executor.execQuery("SELECT word FROM \"engWords\"",(rs) ->{
            while (rs.next()) {
                words.add(rs.getString("word")+ "<separator>");
            }
            return words;
        });

        List<String> w = new ArrayList<>();
        for (int i = 0; i < n; i++){
            w.add(words.get((int) (Math.random() * 660)));
        }

        return w;
    }

    public List<String> getNRandomRusWords(int n){
        List<String> words = new ArrayList<>();
        executor.execQuery("SELECT word FROM \"russWords\"",(rs) ->{
            while (rs.next()) {
                words.add(rs.getString("word")+ "<separator>");
            }
            return words;
        });

        List<String> w = new ArrayList<>();
        for (int i = 0; i < n; i++){
            w.add(words.get((int) (Math.random() * 513)));
        }

        return w;
    }
}
