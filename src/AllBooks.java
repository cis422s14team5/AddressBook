import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// TODO Break the AllBooks into different modules: All Address Books, All Contacts, Contact
// TODO Fix the layout of the Contacts tab so the fields fill up all the space left after the labels.
// TODO Validate input in a separate module

/**
 * The address book GUI.
 */
public class AllBooks extends JFrame {

    private final File BOOKS = new File("addressBooks/books.txt");

    private JList<String> scrollList;

    private ArrayList<String> bookList;

    private ArrayList<ArrayList<HashMap<String, String>>> allAddressBooks;

    private ArrayList<HashMap<String, String>> addressBook;

    private File file;

    private String newFileName;

    private ArrayList<String> openBooks;

    private TSV tsv;
    private ReadWrite readWrite;

    /**
     * Constructor. Sets up the window, panels, labels, buttons, and fields.
     */
    public AllBooks() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        tsv = new TSV();
        readWrite = new ReadWrite();

        bookList = readWrite.read(BOOKS.toPath());
        Collections.sort(bookList);
        createAllAddressBooks();

        openBooks = new ArrayList<>();

        // Window
        setTitle("Address Book");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(460, 260);
        setResizable(false);
        setLocationRelativeTo(null);

        // Menu
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        newMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                openNewBookDialog();
            }
        });
        fileMenu.add(newMenuItem);


        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //loadAddressBook();
                openBookView(scrollList.getSelectedIndex());
            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        closeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                closeAllBooksView();
                System.exit(0);
            }
        });
        fileMenu.add(closeMenuItem);

        setJMenuBar(menuBar);

        // Book List
        scrollList = new JList<>();
        scrollList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateBookList();
        scrollList.setSelectedIndex(0);

        scrollList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    openBookView(list.locationToIndex(e.getPoint()));
                } else if (e.getClickCount() == 3) {   // Triple-click
                    openBookView(list.locationToIndex(e.getPoint()));

                }
            }
        });

        JScrollPane bookPane = new JScrollPane(scrollList);
        //bookPane.setPreferredSize(new Dimension(360, 240));

        // Buttons
        JButton open = new JButton("Open");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //loadAddressBook();
                openBookView(scrollList.getSelectedIndex());
            }
        });

        JButton removeBook = new JButton("Remove");
        removeBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                removeBook();
            }
        });

        JButton newBook = new JButton("New");
        newBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                openNewBookDialog();
            }
        });

        // Layout
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(border);
        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setBorder(border);

        // Button Layout
        mainButtonPanel.add(open);
        mainButtonPanel.add(newBook);
        mainButtonPanel.add(removeBook);

        GridLayout mainPanelLayout = new GridLayout(2, 0);
        mainPanel.setLayout(mainPanelLayout);
        mainPanel.setBorder(border);
        mainPanel.add(bookPane);
        mainPanel.add(mainButtonPanel);
        add(mainPanel);

        setVisible(true);
    }

    private void createAllAddressBooks() {
        allAddressBooks = new ArrayList<>();

        for (String addressBook : bookList) {
            File file = new File ("addressBooks/" + addressBook + ".tsv");

            AddressConverter convert = new AddressConverter();
            try {
                ArrayList<HashMap<String, String>> addressList = tsv.read(file);
                allAddressBooks.add(convert.standardToInternal(addressList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openBookView(int index) {
        if (!scrollList.isSelectionEmpty()) {
            file = new File("addressBooks/" + bookList.get(index) + ".tsv");
            Book book = new Book(allAddressBooks.get(index), bookList.get(index));
            book.setTitle(bookList.get(index));
            openBooks.add(file.getName());
        } else if (bookList.size() == 0) {
            JOptionPane.showMessageDialog(null, "There are no address books, please create one.");
        } else {
            JOptionPane.showMessageDialog(null, "There are no address books selected, please select one.");
        }
    }

    private void openNewBookDialog() {
        newFileName = "";
        newFileName = JOptionPane.showInputDialog(null, "What do you want to call this new book?");
        boolean matches = false;
        for (String string : bookList) {
            if (newFileName.equals(string + ".tsv")) {
                matches = true;
            }
        }
        if (!newFileName.equals("") && !matches) {
            createNewBook();
        } else {
            JOptionPane.showMessageDialog(this,
                    "An address book with that name already exists. Please choose another.");
            openNewBookDialog();
        }
    }

    private void createNewBook() {
        addressBook = new ArrayList<>();
        scrollList.setSelectedIndex(bookList.size());

        // Write to the TSV file.
        tsv.write(new File("addressBooks/" + newFileName + ".tsv"), addressBook);

        // Add to address bookList.
        bookList.add(newFileName);
        Collections.sort(bookList);

        // Write to books.txt
        readWrite.write(BOOKS.toPath(), bookList);

        createAllAddressBooks();
        updateBookList();
        scrollList.setSelectedIndex(0);

        // TODO Open newly created address book here.
    }

    private void removeBook() {
        Path path = FileSystems.getDefault().getPath(
                "addressBooks/" + bookList.get(scrollList.getSelectedIndex()) + ".tsv");
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bookList.remove(scrollList.getSelectedIndex());
        readWrite.write(BOOKS.toPath(), bookList);
        createAllAddressBooks();
        updateBookList();
    }

    private void closeAllBooksView() {
        dispose();
        System.exit(0);
    }

    private void getDialog(String name) {
        JDialog dialog = new JDialog((Window)null, name);
        for (Window window : JDialog.getWindows()) {
            if (window instanceof JDialog) {
                System.out.println(((JDialog)window).getTitle());
            }
        }
    }

    public void updateBookList() {
        scrollList.setListData(bookList.toArray(new String[bookList.size()]));
    }
}
