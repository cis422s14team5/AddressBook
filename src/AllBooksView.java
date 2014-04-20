import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// TODO Break the AllBooksView into different modules: All Address Books, All Contacts, Contact
// TODO Fix the layout of the Contacts tab so the fields fill up all the space left after the labels.
// TODO Validate input in a separate module

/**
 * The address book GUI.
 */
public class AllBooksView extends JFrame {

    private final File BOOKS = new File("addressBooks/books.txt");

    private JList<String> bookList;

    private ArrayList<String> addressBookList;

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
    public AllBooksView() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        tsv = new TSV();
        readWrite = new ReadWrite();

        addressBookList = readWrite.read(BOOKS.toPath());
        Collections.sort(addressBookList);
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
                openNewBookPane();
            }
        });
        fileMenu.add(newMenuItem);


        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //loadAddressBook();
                openBookView(bookList.getSelectedIndex());
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
        bookList = new JList<>();
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateBookList();
        bookList.setSelectedIndex(0);

        bookList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JList list = (JList)e.getSource();
                if (e.getClickCount() == 2) {
                    openBookView(list.locationToIndex(e.getPoint()));
                } else if (e.getClickCount() == 3) {   // Triple-click
                    openBookView(list.locationToIndex(e.getPoint()));

                }
            }
        });

        JScrollPane bookPane = new JScrollPane(bookList);
        //bookPane.setPreferredSize(new Dimension(360, 240));

        // Buttons
        JButton open = new JButton("Open");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //loadAddressBook();
                openBookView(bookList.getSelectedIndex());
            }
        });

        JButton newBook = new JButton("New");
        newBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                openNewBookPane();
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

        GridLayout mainPanelLayout = new GridLayout(2, 0);
        mainPanel.setLayout(mainPanelLayout);
        mainPanel.setBorder(border);
        mainPanel.add(bookPane);
        mainPanel.add(mainButtonPanel);
        add(mainPanel);

        setVisible(true);
    }

    private void openNewBookPane() {
        newFileName = "";
        newFileName = JOptionPane.showInputDialog(null, "What do you want to call this new book?");
        boolean matches = false;
        for (String string : addressBookList) {
            if (newFileName.equals(string + ".tsv")) {
                matches = true;
            }
        }
        if (!newFileName.equals("") && !matches) {
            createNewBook();
        } else {
            JOptionPane.showMessageDialog(this, "That title already exists. Please choose another.");
            openNewBookPane();
        }
    }

    private void createNewBook() {
        addressBook = new ArrayList<>();
        bookList.setSelectedIndex(addressBookList.size());

        // Write to the TSV file.
        tsv.write(new File("addressBooks/" + newFileName + ".tsv"), addressBook);

        // Write to books.txt
        readWrite.write(BOOKS.toPath(), addressBookList);

        // Add to local list of address books and sort.
        addressBookList.add(newFileName);
        Collections.sort(addressBookList);

        createAllAddressBooks();

        updateBookList();
//        for (Observer observer : observers) {
//            observer.update(4);
//        }
    }

    private void createAllAddressBooks() {
        allAddressBooks = new ArrayList<>();

        for (String addressBook : addressBookList) {
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
        if (!bookList.isSelectionEmpty()) {
            file = new File("addressBooks/" + addressBookList.get(index) + ".tsv");
            BookView bookView = new BookView(this, index, file.getName());
            bookView.setTitle(file.getName());
            openBooks.add(file.getName());
        } else if (addressBookList.size() == 0) {
            JOptionPane.showMessageDialog(null, "There are no address books, please create one.");
        } else {
            JOptionPane.showMessageDialog(null, "There are no address books selected, please select one.");
        }
    }

    private void closeAllBooksView() {
        dispose();
    }

    public void closeBook(int index) {
        file = new File("addressBooks/" + addressBookList.get(index) + ".tsv");
        addressBook = allAddressBooks.get(index);
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
        bookList.setListData(addressBookList.toArray(new String[addressBookList.size()]));
    }

    public void setAddressBook(ArrayList<HashMap<String, String>> addressBook) {
        this.addressBook = addressBook;
    }


}
