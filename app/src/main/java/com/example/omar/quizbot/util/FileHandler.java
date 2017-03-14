package com.example.omar.quizbot.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Class in charge of loading file using fileIO.
 */
public class FileHandler {

    private static final String TOKEN = "Ex";
    private static final String FILE_NAME = "quiz.txt";
    private static final String DELIMITER = "#";

    private FileHandler() {}

    /**
     * Loads the quiz from the quiz.txt file and splits the quiz data using
     * a delimiter.
     * @param context Application context
     * @return A hash map containing the quiz questions (Keys) and answers (Values)
     */
    public static HashMap<String,String> loadQuizFile(Context context) {
        AssetManager am = context.getAssets();
        InputStream is;
        HashMap<String, String> quizMap = new HashMap<>();

        try {
            is = am.open(FILE_NAME);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String s;

            while((s = br.readLine()) != null) {
                String[] tokens = s.split(DELIMITER);

                quizMap.put(tokens[0], tokens[1]);
            }

            br.close();
            is.close();
        }
        catch (IOException e) {
            Log.e(TOKEN, "IOException", e);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TOKEN, "ArrayIndexOutOfBoundsException", e);
        }
        catch (Exception e) {
            Log.e(TOKEN, "Exception", e);
        }

        return quizMap;
    }
}
