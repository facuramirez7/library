import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import models.Book;

public class LibraryApp {
    private static final BookDAO bookDAO = new BookDAO();
    private static final DefaultTableModel tableModel = new DefaultTableModel();
    private static final String[] estados = {"En la biblioteca", "Prestado"};
    private static JTextField searchField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Biblioteca App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);

            JPanel panel = new JPanel(new BorderLayout());

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            JButton showBooksButton = new JButton("Mostrar Libros");
            showBooksButton.addActionListener(e -> {
                List<Book> libros = bookDAO.getBooks();
                actualizarTabla(libros);
            });

            // Panel de búsqueda
            JPanel searchPanel = new JPanel();
            searchField = new JTextField(20);
            JButton searchButton = new JButton("Search");
            searchButton.addActionListener(e -> {
                String searchTerm = searchField.getText();
                List<Book> books = bookDAO.searchBooksByTitle(searchTerm);
                updateTable(books);
            });
            searchPanel.add(new JLabel("Search by Title:"));
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            JButton insertBooksButton = getjButton(showBooksButton);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(showBooksButton);
            buttonPanel.add(insertBooksButton);

            panel.add(buttonPanel, BorderLayout.PAGE_START);
            panel.add(searchPanel, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

            frame.getContentPane().add(panel);
            frame.setVisible(true);
        });
    }

    private static JButton getjButton(JButton showBooksButton) {
        JButton insertBooksButton = new JButton("Insertar Libro");
        insertBooksButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Ingrese el título del libro:");
            String author = JOptionPane.showInputDialog("Ingrese el autor del libro:");
            String genre = JOptionPane.showInputDialog("Ingrese el género del libro:");

            // Usar JComboBox para seleccionar el estado
            JComboBox<String> estadoComboBox = new JComboBox<>(estados);
            int result = JOptionPane.showConfirmDialog(null, estadoComboBox, "Seleccione el estado", JOptionPane.OK_CANCEL_OPTION);

            String status = result == JOptionPane.OK_OPTION ? (String) estadoComboBox.getSelectedItem() : "";

            Book newBook = new Book(title, author, genre, status);
            bookDAO.insertBook(newBook);

            // Actualizar la tabla después de la inserción
            showBooksButton.doClick();
        });
        return insertBooksButton;
    }

    private static void updateTable(List<Book> books) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        tableModel.addColumn("Titulo");
        tableModel.addColumn("Autor");
        tableModel.addColumn("Género");
        tableModel.addColumn("Estado");

        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getGenre(), book.getStatus()});
        }
    }


    private static void actualizarTabla(List<Book> libros) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        tableModel.addColumn("Título");
        tableModel.addColumn("Autor");
        tableModel.addColumn("Género");
        tableModel.addColumn("Estado");

        for (Book libro : libros) {
            tableModel.addRow(new Object[]{libro.getTitle(), libro.getAuthor(), libro.getGenre(), libro.getStatus()});
        }
    }
}
