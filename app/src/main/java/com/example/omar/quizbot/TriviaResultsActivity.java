package com.example.omar.quizbot;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import static com.example.omar.quizbot.EnterNameActivity.PLAYER_NAME;

public class TriviaResultsActivity extends AppCompatActivity {

    private TextView mTVPercent;
    private Double mPercent;
    private String mName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_results);

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvQuestions = (TextView) findViewById(R.id.tvTotalQuestions);
        TextView tvCorrect = (TextView) findViewById(R.id.tvCorrect);
        TextView tvIncorrect = (TextView) findViewById(R.id.tvIncorrect);
        mTVPercent = (TextView) findViewById(R.id.tvScorePercentage);

        // Display all scoring stuff on the screen
        mName = getIntent().getStringExtra(PLAYER_NAME);
        tvName.setText(mName);

        int numQuestions = getIntent().getIntExtra(QuizActivity.NUM_QUESTIONS, 0);
        int numCorrect = getIntent().getIntExtra(QuizActivity.SCORE, 0);
        mPercent = Math.ceil(((double)numCorrect/(double)numQuestions) * 100);
        System.out.println(mPercent);
        tvQuestions.setText(getString(R.string.label_total_questions, numQuestions));
        tvCorrect.setText(getString(R.string.label_correct, numCorrect));
        tvIncorrect.setText(getString(R.string.label_incorrect, (numQuestions - numCorrect)));

        // Start a new quiz if user presses button
        Button tryAgain = (Button) findViewById(R.id.btnTryAgain);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TriviaResultsActivity.this, QuizActivity.class);
                i.putExtra(PLAYER_NAME, mName);
                startActivity(i);
                finish();
            }
        });

        UpdatePercentTask task = new UpdatePercentTask();
        task.execute();
    }

    /**
     * Fancy AsyncTask that increases percentage score on the screen
     */
    private class UpdatePercentTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i = 0; i < mPercent.intValue() + 1; i += 2) {
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException ex) {
                    Log.e("Ex", "InterruptedException", ex);
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mTVPercent.setText(String.format(Locale.getDefault(), "%d%%", values[0]));
        }
    }

}
