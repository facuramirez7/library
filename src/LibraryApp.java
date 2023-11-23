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
    private static JTable table;

    public static void main(String[] args) {
        //swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Biblioteca App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());

            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            JButton showBooksButton = new JButton("Mostrar Libros");
            showBooksButton.addActionListener(e -> {
                List<Book> libros = bookDAO.getBooks();
                updateTable(libros);
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
            JButton editBooksButton = getEditButton(showBooksButton);
            JButton deleteBookButton = getDeleteButton(showBooksButton);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(showBooksButton);
            buttonPanel.add(insertBooksButton);
            buttonPanel.add(editBooksButton);
            buttonPanel.add(deleteBookButton);

            panel.add(buttonPanel, BorderLayout.PAGE_START);
            panel.add(searchPanel, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

            frame.getContentPane().add(panel);
            frame.setVisible(true);
        });
    }

    private static JButton getEditButton(JButton showBooksButton) {
        JButton editBookButton = new JButton("Editar Libro");
        editBookButton.addActionListener(e -> {
            // Obtener la fila seleccionada en la tabla
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un libro para editar.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // No hay fila seleccionada, salir del método
            }

            // Obtener los valores actuales de la fila seleccionada
            String currentTitle = (String) tableModel.getValueAt(selectedRow, 0);
            String currentAuthor = (String) tableModel.getValueAt(selectedRow, 1);
            String currentGenre = (String) tableModel.getValueAt(selectedRow, 2);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);

            // Mostrar un cuadro de diálogo para editar los valores
            String newTitle = JOptionPane.showInputDialog("Editar título:", currentTitle);
            String newAuthor = JOptionPane.showInputDialog("Editar autor:", currentAuthor);
            String newGenre = JOptionPane.showInputDialog("Editar género:", currentGenre);

            // Verifica que los campos no estén vacíos
            if (newTitle.isEmpty() || newAuthor.isEmpty() || newGenre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // No continúes con la inserción si algún campo está vacío
            }

            // JComboBox para seleccionar el nuevo estado
            JComboBox<String> estadoComboBox = new JComboBox<>(estados);
            estadoComboBox.setSelectedItem(currentStatus);
            int result = JOptionPane.showConfirmDialog(null, estadoComboBox, "Editar estado", JOptionPane.OK_CANCEL_OPTION);

            String newStatus = result == JOptionPane.OK_OPTION ? (String) estadoComboBox.getSelectedItem() : "";

            // Actualizar el libro en la base de datos
            Book updatedBook = new Book(newTitle, newAuthor, newGenre, newStatus);
            bookDAO.updateBook(currentTitle, currentAuthor, updatedBook);

            // Actualizar la tabla después de la edición
            showBooksButton.doClick();
        });
        return editBookButton;
    }


    private static JButton getjButton(JButton showBooksButton) {
        JButton insertBooksButton = new JButton("Insertar Libro");
        insertBooksButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Ingrese el título del libro:");
            String author = JOptionPane.showInputDialog("Ingrese el autor del libro:");
            String genre = JOptionPane.showInputDialog("Ingrese el género del libro:");

            // Verifica que los campos no estén vacíos
            if (title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // No continúes con la inserción si algún campo está vacío
            }

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

    private static JButton getDeleteButton(JButton showBooksButton) {
        JButton deleteBookButton = new JButton("Eliminar Libro");
        deleteBookButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String titleToDelete = (String) tableModel.getValueAt(selectedRow, 0); // Título está en la columna 0
                bookDAO.deleteBook(titleToDelete);
                showBooksButton.doClick(); // Actualizar la tabla después de la eliminación
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un libro para eliminar.");
            }
        });
        return deleteBookButton;
    }


    // Actualizar la tabla después de la inserción
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


}
