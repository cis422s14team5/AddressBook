import com.sun.codemodel.internal.JOp;

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


/*
TODO Fix the layout

TODO Likely Changes
- Add additional fields to the entries: for example, fields for additional email address,
  mobile phone number, business phone, birthday, etc. .
- TODO Search: As the number of entries gets larger, we will want to be able to search the address book.
- Ability to keep customized address books with different types of entries in each.
- TODO Merge: Ability to merge address books.


- Simple version that only keeps name and phone number.
- Allow user-defined fields
- Support multiple address books with different fields
 */

/**
 * All address books.
 */
public class AllBooks extends JFrame {

    protected File saveDir;
    private File books;
    protected String slash;
    private JList<String> scrollList;
    private ArrayList<String> bookList;
    private ArrayList<ArrayList<HashMap<String, String>>> allAddressBooks;
    private String newFileName;
    private TSV tsv;
    private ReadWrite readWrite;

    public boolean isMerging;
    public ArrayList<HashMap<String, String>> mergeBook;

    /**
     * Constructor. Sets up the window, panels, labels, buttons, and fields.
     */
    public AllBooks() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        tsv = new TSV();
        readWrite = new ReadWrite();
        isMerging = false;

        checkOS();
        checkForBooks();
        bookList = readWrite.read(books.toPath());
        Collections.sort(bookList);
        createAllAddressBooks();

        // Window
        setTitle("Address Books");
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
                openNewBookDialog(new ArrayList<HashMap<String, String>>());
            }
        });
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                openBookView(scrollList.getSelectedIndex());
            }
        });
        fileMenu.add(openMenuItem);

        fileMenu.addSeparator();

        JMenuItem mergeMenuItem = new JMenuItem("Merge");
        mergeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        mergeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                mergeBooks();
            }
        });
        fileMenu.add(mergeMenuItem);

        fileMenu.addSeparator();

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
        scrollList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //scrollList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateBookList();
        int[] selected = {0};
        scrollList.setSelectedIndices(selected);

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

        // Panels
        JPanel panel = new JPanel();
        JPanel buttonPanel = new JPanel();

        JScrollPane bookPane = new JScrollPane(scrollList);
        bookPane.setPreferredSize(new Dimension(360, 150));

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
                openNewBookDialog(new ArrayList<HashMap<String, String>>());
            }
        });

        // Layout
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        add(panel);

        // Panel Layout
        panel.setBorder(border);
        panel.add(bookPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.PAGE_END);

        // Button Layout
        buttonPanel.add(newBook);
        buttonPanel.add(open);
        buttonPanel.add(removeBook);

        setVisible(true);
    }

    private void createAllAddressBooks() {
        allAddressBooks = new ArrayList<>();

        for (String addressBook : bookList) {
            File file = new File(saveDir + slash +  addressBook + ".tsv");

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
            Book book = new Book(this, allAddressBooks.get(index), bookList.get(index));
            book.setTitle(bookList.get(index));
        } else if (bookList.size() == 0) {
            JOptionPane.showMessageDialog(null, "There are no address books, please create one.");
        } else {
            JOptionPane.showMessageDialog(null, "There are no address books selected, please select one.");
        }
    }

    public void openNewBookDialog(ArrayList<HashMap<String, String>> addressBook) {
        newFileName = "";
        newFileName = JOptionPane.showInputDialog(null, "What do you want to call this new book?");
        boolean matches = false;
        for (String string : bookList) {
            if (newFileName.equals(string + ".tsv")) {
                matches = true;
            }
        }
        if (newFileName != null) {
            if (!newFileName.equals("") && !matches) {
                createNewBook(addressBook);
            } else {
                JOptionPane.showMessageDialog(this,
                        "An address book with that name already exists. Please choose another.");
                openNewBookDialog(addressBook);
            }
        }
    }

    private void createNewBook(ArrayList<HashMap<String, String>> addressBook) {
        //scrollList.setSelectedIndex(bookList.size());

        // Write to the TSV file.
        tsv.write(new File(saveDir + slash + newFileName + ".tsv"), addressBook);

        // Add to address bookList.
        bookList.add(newFileName);
        Collections.sort(bookList);

        // Write to books.txt
        readWrite.write(books.toPath(), bookList);

        createAllAddressBooks();
        updateBookList();
        scrollList.setSelectedIndex(0);


        for (int i = 0; i < bookList.size(); i ++){
            if (bookList.get(i).equals(newFileName)){
                Book book = new Book(this, allAddressBooks.get(i), bookList.get(i));
                book.setTitle(bookList.get(i));
            }
        }
    }

    private void removeBook() {
        int choice = JOptionPane.showConfirmDialog(null,
                "If you remove an address book it will be gone forever.\nAre you sure?",
                "Remove Address Book",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == 0) { // Yes
            Path path = FileSystems.getDefault().getPath(
                    saveDir + slash +  bookList.get(scrollList.getSelectedIndex()) + ".tsv");
            getDialog(bookList.get(scrollList.getSelectedIndex()));
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            bookList.remove(scrollList.getSelectedIndex());
            readWrite.write(books.toPath(), bookList);
            createAllAddressBooks();
            updateBookList();
        }
    }

    /**
     * Quits the program.
     */
    private void closeAllBooksView() {
        dispose();
        System.exit(0);
    }

    /**
     * Updates the book list.
     */
    public void updateBookList() {
        scrollList.setListData(bookList.toArray(new String[bookList.size()]));
        scrollList.setSelectedIndex(0);
    }

    /**
     * Check if books exists, if it doesn't create it.
     */
    private void checkForBooks() {
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        if (!books.exists()) {
            try {
                books.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mergeBooks() {
        if (scrollList.getSelectedIndices().length > 1) {
            mergeBook = new ArrayList<>();
            isMerging = true;
            for (int index : scrollList.getSelectedIndices()) {
                mergeBook.addAll(allAddressBooks.get(index));
            }
            openNewBookDialog(new ArrayList<HashMap<String, String>>());
        } else {
            JOptionPane.showMessageDialog(this, "You must select more than one address book to merge.");
        }
    }

    /**
     * Check the OS to know where to read/write the save files.
     */
    private void checkOS() {
        String os = System.getProperty("os.name");
        if (os.equals("Windows")) {
            saveDir = new File(System.getProperty("user.home"), "Application Data\\AddressBooks");
            slash = "\\";
        } else {
            saveDir = new File(System.getProperty("user.home") + "/AddressBooks");
            slash = "/";
        }
        books = new File(saveDir + slash +  "books.txt");
    }

    private void getDialog(String name) {
        setTitle("");
        for (Window window : JFrame.getWindows()) {

            if (window instanceof JFrame) {
                if (((JFrame)window).getTitle().equals(name)) {
                    window.dispose();
                }
            }
        }
        setTitle("Address Books");
    }
}
