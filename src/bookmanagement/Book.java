package bookmanagement;

/**
 * Represents a Book entity mapped to tblbooks.
 * Extends Entity (Inheritance) and encapsulates book data.
 */
public class Book extends Entity {

    private String title;
    private String author;
    private String category;
    private String status;

    public Book() {
        super();
    }

    public Book(int bookid, String title, String author, String category, String status) {
        super(bookid);
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
    }

    public Book(String title, String author, String category, String status) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
    }

    // Getters and Setters (Encapsulation)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean isValid() {
        return title != null && !title.trim().isEmpty()
            && author != null && !author.trim().isEmpty()
            && category != null && !category.trim().isEmpty()
            && status != null && !status.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author
               + "', category='" + category + "', status='" + status + "'}";
    }
}
