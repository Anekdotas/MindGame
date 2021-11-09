package com.example.bruhtest;

public class Message {
    private String author;
    private String text;
    private int profPicSource;
    private int questionPicture;

    public Message(String author, String text, int profPicSource, int questionPicture) {
        this.author = author;
        this.text = text;
        this.profPicSource = profPicSource;
        this.questionPicture = questionPicture;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuestionPicture() {
        return questionPicture;
    }

    public void setQuestionPicture(int questionPicture) {
        this.questionPicture = questionPicture;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getProfPicSource() {
        return profPicSource;
    }

    public void setProfPicSource(int profPicSource) {
        this.profPicSource = profPicSource;
    }
}