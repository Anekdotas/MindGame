package anekdotas.mindgameapplication.java;

import anekdotas.mindgameapplication.R;
public class Message {
    private String author;
    private String text;
    private int profPicSource;
    private String questionPicture;
    private int audio;

    public Message(String author, String text, int profPicSource, String questionPicture) {
        this.author = author;
        this.text = text;
        this.profPicSource = profPicSource;
        this.questionPicture = questionPicture;
//        if (questionPicture == 123) {                         //I am not sure can I delete this, some tests needed
//            this.questionPicture = R.drawable.bred;
//        }
    }

    public Message(String author, String text, int profPicSource) {
        this.author = author;
        this.text = text;
        this.profPicSource = profPicSource;
    }

    public Message(String author, String text, int profPicSource, String questionPicture, int audio) {
        this.author = author;
        this.text = text;
        this.profPicSource = profPicSource;
        this.questionPicture = questionPicture;
        this.audio = audio;
    }

    public String getAuthor() {
        return author;
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

    public String getQuestionPicture() {
        return questionPicture;
    }

    public void setQuestionPicture(String questionPicture) {
        this.questionPicture = questionPicture;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }
}