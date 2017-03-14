package com.example.omar.quizbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnterNameActivity extends AppCompatActivity {

    public static final String PLAYER_NAME = "player_name";

    private EditText etName;
    private TextView tvWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        etName = (EditText) findViewById(R.id.etName);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                // Clear warning if name field is not empty
                if (!editable.toString().isEmpty()) {
                    tvWarning.setVisibility(View.INVISIBLE);
                }
            }
        });
        tvWarning = (TextView) findViewById(R.id.tvWarning);

        Button begin = (Button) findViewById(R.id.btnBegin);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make sure name field isn't empty.
                if (etName.getText().toString().isEmpty()) {
                    tvWarning.setVisibility(View.VISIBLE);
                }
                else {
                    tvWarning.setVisibility(View.INVISIBLE);
                    String name = etName.getText().toString();
                    Intent i = new Intent(EnterNameActivity.this, QuizActivity.class);
                    i.putExtra(PLAYER_NAME, name);
                    startActivity(i);
                    finish();
                }

            }
        });
    }
}
