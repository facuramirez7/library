import models.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//DAO = Data Access Object
public class BookDAO {
    private static final String INSERT_BOOK = "INSERT INTO books(title, author, genre, status) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BOOKS = "SELECT * FROM books";
    private static final String SEARCH_BOOKS = "SELECT * FROM books WHERE title LIKE ?";
    private static final String DELETE_BOOK = "DELETE FROM books WHERE title = ?";


    //Inserta uhn libro en la bd
    public void insertBook(Book book) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getGenre());
            preparedStatement.setString(4, book.getStatus());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Trae todos los libros de la bd
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOKS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                String status = resultSet.getString("status");

                // Utiliza el constructor de Book con argumentos
                Book book = new Book(title, author, genre, status);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    //Función para buscar por el titulo
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BOOKS)) {

            preparedStatement.setString(1, "%" + title + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String bookTitle = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String genre = resultSet.getString("genre");
                    String status = resultSet.getString("status");

                    // Utiliza el constructor de Book con argumentos
                    Book book = new Book(bookTitle, author, genre, status);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Función para actualizar un libro en la bd
    public void updateBook(String currentTitle, String currentAuthor, Book updatedBook) {
        try (Connection connection = DatabaseConnector.connect()) {
            // Construir la sentencia SQL UPDATE
            String updateQuery = "UPDATE books SET title=?, author=?, genre=?, status=? " +
                    "WHERE title=? AND author=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                // Establecer los valores en la sentencia preparada
                preparedStatement.setString(1, updatedBook.getTitle());
                preparedStatement.setString(2, updatedBook.getAuthor());
                preparedStatement.setString(3, updatedBook.getGenre());
                preparedStatement.setString(4, updatedBook.getStatus());
                preparedStatement.setString(5, currentTitle);
                preparedStatement.setString(6, currentAuthor);

                // Ejecutar la actualización
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(String title) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK)) {

            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
