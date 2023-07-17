package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private SeekBar sliderSeekBar;
    private TextView sliderValueTextView;
    private Button submitButton;

    private Question[] quizQuestions;
    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private int currentSliderValue = 50; // Set an initial value for the slider
    private int previousAnswerScore = 0; // Variable to store the score of the previous answer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        sliderSeekBar = findViewById(R.id.sliderSeekBar);
        sliderValueTextView = findViewById(R.id.sliderValueTextView);
        submitButton = findViewById(R.id.submitButton);

        // Create quiz questions here, e.g.:
        quizQuestions = new Question[]{
                new Question("What is the capital of France?",
                        new String[]{"London", "Paris", "Berlin", "Madrid"},
                        new int[]{0, 10, 0, 0}),
                new Question("On a scale of 1 to 10, how much do you like pizza?", 1, 10),

                new TrueFalseQuestion("Is the sky blue?", true), // Diverging path for True
                new TrueFalseQuestion("Is water wet?", false),   // Diverging path for False

                // Add more questions here
        };

        // Restore the slider value and previous answer score if they exist
        if (savedInstanceState != null) {
            currentSliderValue = savedInstanceState.getInt("currentSliderValue", 50); // Use the initial value if not saved
            previousAnswerScore = savedInstanceState.getInt("previousAnswerScore", 0);
        }

        showQuestion(currentQuestionIndex);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question currentQuestion = quizQuestions[currentQuestionIndex];

                if (currentQuestion.isTrueFalseQuestion()) {
                    int selectedAnswerIndex = answersRadioGroup.getCheckedRadioButtonId();
                    if (selectedAnswerIndex != -1) {
                        boolean userAnswer = selectedAnswerIndex == 0; // 0 for True, 1 for False
                        TrueFalseQuestion trueFalseQuestion = (TrueFalseQuestion) currentQuestion;
                        if (trueFalseQuestion.isCorrectAnswer(userAnswer)) {
                            previousAnswerScore = trueFalseQuestion.getScores()[selectedAnswerIndex];
                        } else {
                            previousAnswerScore = 0; // If the true/false answer is incorrect, set the previous score to 0
                        }
                        currentQuestionIndex++;
                        if (currentQuestionIndex < quizQuestions.length) {
                            showQuestion(currentQuestionIndex);
                        } else {
                            finishQuiz();
                        }
                    }
                } else if (currentQuestion.getAnswers().length > 1) {
                    int selectedAnswerIndex = answersRadioGroup.getCheckedRadioButtonId();
                    if (selectedAnswerIndex != -1) {
                        int score = currentQuestion.getScores()[selectedAnswerIndex];
                        totalScore += score * previousAnswerScore;
                    }
                    currentQuestionIndex++;
                    if (currentQuestionIndex < quizQuestions.length) {
                        showQuestion(currentQuestionIndex);
                    } else {
                        finishQuiz();
                    }
                } else {
                    int sliderValue = sliderSeekBar.getProgress();
                    int sliderMinValue = currentQuestion.getSliderMinValue();
                    int sliderMaxValue = currentQuestion.getSliderMaxValue();
                    int score = calculateSliderScore(sliderValue, sliderMinValue, sliderMaxValue);
                    totalScore += score * previousAnswerScore;

                    currentQuestionIndex++;
                    if (currentQuestionIndex < quizQuestions.length) {
                        showQuestion(currentQuestionIndex);
                    } else {
                        finishQuiz();
                    }
                }
            }
        });
    }

    private int calculateSliderScore(int sliderValue, int sliderMinValue, int sliderMaxValue) {
        // You can implement your custom logic here to calculate the score based on the slider value
        // For example, you can convert the slider value to a score based on some range
        // For simplicity, we will use a linear scaling formula here
        float range = sliderMaxValue - sliderMinValue;
        float normalizedSliderValue = (float) (sliderValue - sliderMinValue) / range;
        int maxScore = 10; // You can adjust this based on your desired maximum score
        int score = Math.round(normalizedSliderValue * maxScore);

        return score;
    }


    private void showQuestion(int questionIndex) {
        Question currentQuestion = quizQuestions[questionIndex];
        questionTextView.setText(currentQuestion.getQuestionText());

        if (currentQuestion.isTrueFalseQuestion()) {
            answersRadioGroup.setVisibility(View.VISIBLE);
            sliderSeekBar.setVisibility(View.GONE);
            sliderValueTextView.setVisibility(View.GONE);

            answersRadioGroup.removeAllViews();
            for (int i = 0; i < currentQuestion.getAnswers().length; i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(currentQuestion.getAnswers()[i]);
                radioButton.setId(i);
                answersRadioGroup.addView(radioButton);
            }
        } else if (currentQuestion.getAnswers().length > 1) {
            answersRadioGroup.setVisibility(View.VISIBLE);
            sliderSeekBar.setVisibility(View.GONE);
            sliderValueTextView.setVisibility(View.GONE);

            answersRadioGroup.removeAllViews();
            for (int i = 0; i < currentQuestion.getAnswers().length; i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(currentQuestion.getAnswers()[i]);
                radioButton.setId(i);
                answersRadioGroup.addView(radioButton);
            }
        } else {
            answersRadioGroup.setVisibility(View.GONE);
            sliderSeekBar.setVisibility(View.VISIBLE);
            sliderValueTextView.setVisibility(View.VISIBLE);

            int sliderMinValue = currentQuestion.getSliderMinValue();
            int sliderMaxValue = currentQuestion.getSliderMaxValue();
            sliderSeekBar.setMax(sliderMaxValue - sliderMinValue);
            sliderSeekBar.setProgress(currentSliderValue - sliderMinValue);
            sliderValueTextView.setText(String.valueOf(currentSliderValue));

            sliderSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    currentSliderValue = progress + currentQuestion.getSliderMinValue();
                    sliderValueTextView.setText(String.valueOf(currentSliderValue));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }
    }

    private void finishQuiz() {
        questionTextView.setText("Quiz Completed! Your Total Score: " + totalScore);
        answersRadioGroup.setVisibility(View.GONE);
        sliderSeekBar.setVisibility(View.GONE);
        sliderValueTextView.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentSliderValue", currentSliderValue);
        outState.putInt("previousAnswerScore", previousAnswerScore);
    }
}
