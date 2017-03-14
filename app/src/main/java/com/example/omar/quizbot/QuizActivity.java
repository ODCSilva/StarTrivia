package com.example.omar.quizbot;

//TODO icon

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.omar.quizbot.util.FileHandler;
import com.example.omar.quizbot.util.R2AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    public static final String SCORE = "score";
    public static final String NUM_QUESTIONS = "num_questions";
    private static final long DIALOG_DURATION = 1200;

    private HashMap<String, String> quizQA = new HashMap<>();
    private List<String> mAnswerList = new ArrayList<>();
    private List<String> mQuestionList = new ArrayList<>();

    // Controls
    TextView mTVPosition;
    TextView mTVScore;
    TextView mTVQuestion;
    Button mA1;
    Button mA2;
    Button mA3;
    Button mA4;

    // Member variables
    private int mNumQuestions;
    private int mCurrentQuestion;
    private String mCorrectAnswer;
    private String mPlayerName;
    private int mScore = 0;

    /**
     * On Create
     * @param savedInstanceState Saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize controls
        mTVQuestion = (TextView) findViewById(R.id.tvQuestion);
        mTVPosition = (TextView) findViewById(R.id.tvPosition);
        mTVScore = (TextView) findViewById(R.id.tvScore);
        mA1 = (Button) findViewById(R.id.btnAns1);
        mA2 = (Button) findViewById(R.id.btnAns2);
        mA3 = (Button) findViewById(R.id.btnAns3);
        mA4 = (Button) findViewById(R.id.btnAns4);

        mPlayerName = getIntent().getStringExtra(EnterNameActivity.PLAYER_NAME);

        loadQuizData();
    }

    /**
     * Loads the quiz data from quiz.txt asset file
     */
    private void loadQuizData() {
        quizQA = FileHandler.loadQuizFile(this);

        prepareQuiz();
    }

    /**
     * Prepares the quiz, populates HashMap and Lists
     */
    private void prepareQuiz() {
        // Populate HashMap
        for(HashMap.Entry<String, String> entry: quizQA.entrySet() ) {
            mAnswerList.add(entry.getValue());
            mQuestionList.add(entry.getKey());
        }

        mNumQuestions = mQuestionList.size();
        mCurrentQuestion = 1;

        updateDisplay();

        Collections.shuffle(mQuestionList, new Random(System.nanoTime()));
        Collections.shuffle(mAnswerList, new Random(System.nanoTime()));

        prepareQuestion();
    }

    /**
     * Prepares a question, selecting it from the list
     * and populates option buttons with the correct (and
     * incorrect) answers.
     */
    private void prepareQuestion() {
        int aIndex;
        Random r = new Random();
        String[] buttonAnswers = new String[4];

        // Get new question from list
        String mQuestion = mQuestionList.get(0);

        //Remove the question from the list;
        mQuestionList.remove(0);

        mTVQuestion.setText(mQuestion);
        mCorrectAnswer = quizQA.get(mQuestion);

        // Assign correct response randomly to one of the buttons
        aIndex = r.nextInt(4);
        buttonAnswers[aIndex] = mCorrectAnswer;

        for (int i = 0; i < buttonAnswers.length; i++) {
            if (i == aIndex) {
                continue;
            }

            String s;

            do {
                s = mAnswerList.get(r.nextInt(mAnswerList.size()));
            } while(mCorrectAnswer.equals(s) || Arrays.asList(buttonAnswers).contains(s));

            buttonAnswers[i] = s;
        }

        // Assign answers to buttons
        mA1.setText(buttonAnswers[0]);
        mA2.setText(buttonAnswers[1]);
        mA3.setText(buttonAnswers[2]);
        mA4.setText(buttonAnswers[3]);

    }

    /**
     * Goes to the next question. If there are no more questions left,
     * take user to the results screen to show score, etc.
     */
    private void nextQuestion() {

        mCurrentQuestion++;

        if (mCurrentQuestion > mNumQuestions) {
            Intent i = new Intent(QuizActivity.this, TriviaResultsActivity.class);
            i.putExtra(EnterNameActivity.PLAYER_NAME, mPlayerName);
            i.putExtra(SCORE, mScore);
            i.putExtra(NUM_QUESTIONS, mNumQuestions);
            startActivity(i);
            finish();
        }
        else {
            updateDisplay();
            clearButtons();
            prepareQuestion();
        }
    }

    /**
     * Try to answer the current question and display custom
     * dialog indicating whether the answer is correct or not.
     * @param v The Button view that was clicked.
     */
    public void tryAnswer(View v) {

        String chosenAnswer;


        chosenAnswer = ((Button)v).getText().toString();


        if (chosenAnswer.equals(mCorrectAnswer)) {

            // Correct answer
            R2AlertDialog r2AlertDialog = new R2AlertDialog();
            r2AlertDialog.setOnDialogDismissed(new R2AlertDialog.OnDialogDismissedListener() {
                @Override
                public void onDismiss() {
                    nextQuestion();
                }
            });

            int color = ResourcesCompat.getColor(getResources(), android.R.color.holo_green_dark, null);
            r2AlertDialog.show(this, getString(R.string.answer_correct), color, DIALOG_DURATION);

            playSoundFx(true);
            mScore++;
        }
        else {

            // Incorrect answer.
            R2AlertDialog r2AlertDialog = new R2AlertDialog();
            r2AlertDialog.setOnDialogDismissed(new R2AlertDialog.OnDialogDismissedListener() {
                @Override
                public void onDismiss() {
                    nextQuestion();
                }
            });

            int color = ResourcesCompat.getColor(getResources(), android.R.color.holo_red_dark, null);
            r2AlertDialog.show(this, getString(R.string.answer_wrong), color, DIALOG_DURATION);
            playSoundFx(false);
        }
    }

    /**
     * Plays a sound effect based on whether the question was answered
     * correctly or not.
     * @param correctAnswer boolean Was the question answered correctly?
     */
    private void playSoundFx(boolean correctAnswer) {
        final MediaPlayer fx;

        if (correctAnswer) {
            fx = MediaPlayer.create(this, R.raw.r2_correct);
            fx.start();
        }
        else {
            fx = MediaPlayer.create(this, R.raw.r2_incorrect);
            fx.start();
        }
    }

    /**
     * Clear all text from the Buttons
     */
    public void clearButtons() {
        mA1.setText("");
        mA2.setText("");
        mA3.setText("");
        mA4.setText("");
    }

    /**
     * Updates the display, showing the current question number and the score.
     */
    private void updateDisplay() {
        mTVScore.setText(String.format(getString(R.string.quiz_score), String.valueOf(mScore)));
        mTVPosition.setText(String.format(getString(R.string.quiz_position),
                String.valueOf(mCurrentQuestion),
                String.valueOf(mNumQuestions)));
    }
}
