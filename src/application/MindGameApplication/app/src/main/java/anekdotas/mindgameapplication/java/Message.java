package anekdotas.mindgameapplication.java;

public class Message {
    private String author;
    private String text;
    private int profPicSource;

    public Message(String author, String text, int profPicSource) {
        this.author = author;
        this.text = text;
        this.profPicSource = profPicSource;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) { this.author = author; }

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