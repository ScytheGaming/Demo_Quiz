package com.example.quiz;

// Question.java
public class Question {
    private String questionText;
    private String[] answers;
    private int[] scores;
    private boolean isSliderQuestion; // Indicates if the question is a slider question
    private int sliderMinValue; // The minimum value of the slider scale
    private int sliderMaxValue; // The maximum value of the slider scale

    // Constructor for multiple-choice questions
    public Question(String questionText, String[] answers, int[] scores) {
        this.questionText = questionText;
        this.answers = answers;
        this.scores = scores;
        this.isSliderQuestion = false;
    }

    // Constructor for slider questions
    public Question(String questionText, int sliderMinValue, int sliderMaxValue) {
        this.questionText = questionText;
        this.sliderMinValue = sliderMinValue;
        this.sliderMaxValue = sliderMaxValue;
        this.isSliderQuestion = true;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int[] getScores() {
        return scores;
    }

    public boolean isSliderQuestion() {
        return isSliderQuestion;
    }

    public int getSliderMinValue() {
        return sliderMinValue;
    }

    public int getSliderMaxValue() {
        return sliderMaxValue;
    }

    public boolean isTrueFalseQuestion() {
        return false;
    }
}
