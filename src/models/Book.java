package models;

public class Book {
    private String title;
    private String author;
    private String genre;
    private String status;

    // Constructor
    public Book(String title, String author, String genre, String status) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.status = status;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
