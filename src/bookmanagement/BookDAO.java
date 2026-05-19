package bookmanagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Book operations.
 * Implements Manageable interface (Polymorphism via interface implementation).
 * Subclass of a base DAO concept — demonstrates Inheritance.
 */
public class BookDAO implements Manageable<Book> {

    private Connection conn;

    public BookDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public boolean add(Book book) throws SQLException {
        String sql = "INSERT INTO tblbooks (title, author, category, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setString(4, book.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Book book) throws SQLException {
        String sql = "UPDATE tblbooks SET title=?, author=?, category=?, status=? WHERE bookid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setString(4, book.getStatus());
            ps.setInt(5, book.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM tblbooks WHERE bookid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Book> getAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM tblbooks";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSet(rs));
            }
        }
        return books;
    }

    @Override
    public List<Book> search(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM tblbooks WHERE title LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSet(rs));
                }
            }
        }
        return books;
    }

        /**
     * Checks if a book title already exists in the database.
     */
    public boolean titleExists(String title) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tblbooks WHERE LOWER(title) = LOWER(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Maps a ResultSet row to a Book object.
     */
    private Book mapResultSet(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("bookid"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("category"),
            rs.getString("status")
        );
    }
}
