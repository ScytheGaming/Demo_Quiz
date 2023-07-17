package com.example.quiz;

public class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(String questionText, boolean correctAnswer) {
        super(questionText, new String[]{"True", "False"}, new int[]{10, 0});
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrectAnswer(boolean userAnswer) {
        return userAnswer == correctAnswer;
    }
}
