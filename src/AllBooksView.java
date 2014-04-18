import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

// TODO Break the AllBooksView into different modules: All Address Books, All Contacts, Contact
// TODO Fix the layout of the Contacts tab so the fields fill up all the space left after the labels.
// TODO Validate input in a separate module

/**
 * The address book GUI.
 */
public class AllBooksView extends JFrame {

    private JList bookList;

    private ArrayList<String> addressBookList;

    private ArrayList<ArrayList<HashMap<String, String>>> allAddressBooks;

    public ArrayList<Observer> observers;

    private ArrayList<HashMap<String, String>> addressBook;

    private File file;

    private String newFileName;

    /**
     * Constructor. Sets up the window, panels, labels, buttons, and fields.
     * @param addressBookList is the list of address books.
     */
    public AllBooksView(final ArrayList<String> addressBookList,
                        ArrayList<ArrayList<HashMap<String, String>>> allAddressBooks) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        this.addressBookList = addressBookList;
        this.allAddressBooks = allAddressBooks;
        observers = new ArrayList<>();
        //bookViews = new ArrayList<>();

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
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(openMenuItem);

        fileMenu.addSeparator();

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(closeMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.setMnemonic(KeyEvent.VK_S);
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        fileMenu.add(saveAsMenuItem);

        setJMenuBar(menuBar);

        // Book List
        bookList = new JList();
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.setSelectedIndex(0);
        updateBookList();

        JScrollPane bookPane = new JScrollPane(bookList);
        bookPane.setPreferredSize(new Dimension(360, 145));

        // Buttons
        JButton open = new JButton("Open");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //loadAddressBook();
                openAddressBookView();
            }
        });

        JButton newBook = new JButton("New");
        newBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                openNewBookPane();
            }
        });

        JButton exportBook = new JButton("Export");
        exportBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                exportTSV();
            }
        });

        JButton importBook = new JButton("Import");
        importBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                importTSV();
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
        mainButtonPanel.add(importBook);
        mainButtonPanel.add(exportBook);

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
        newFileName = JOptionPane.showInputDialog(null, "What do you want to call this new book?") + ".tsv";
        if (!newFileName.equals("")) {
            createNewBook();
        }
    }

    private void createNewBook() {
        addressBook = new ArrayList<>();
        for (Observer observer : observers) {
            observer.update(4);
        }
    }

    private void openAddressBookView() {
        //System.out.println(allAddressBooks);
        System.out.println(allAddressBooks.get(bookList.getSelectedIndex()));
        new AddressBookView(this, bookList.getSelectedIndex());
    }

    /**
     * Notifies Main that it needs to import an address book.
     */
    private void importTSV() {
        for (Observer observer : observers) {
            observer.update(2);
        }
    }

    /**
     * Notifies Main that it needs to export an address book.
     */
    private void exportTSV() {
        for (Observer observer : observers) {
            observer.update(3);
        }
    }

    public void closeAddressBook(int index) {
        // TODO allAddressBooks is getting appended instead of replaced, making allAddressBooks.get(index) wrong
        file = new File("addressBooks/" + addressBookList.get(index));
        System.out.println(addressBookList.get(index));
        addressBook = allAddressBooks.get(index);
        System.out.println(addressBook);
    }

    /**
     * Notifies Main that it needs to import an address book.
     */
    private void loadAddressBook() {
        for (Observer observer : observers) {
            observer.update(4);
        }
    }

    public void updateBookList() {
        bookList.setListData(addressBookList.toArray(new String[addressBookList.size()]));
    }

    public void setAddressBookList(ArrayList<String> addressBookList) {
        this.addressBookList = addressBookList;
    }

    public ArrayList<ArrayList<HashMap<String, String>>> getAllAddressBooks() {
        return allAddressBooks;
    }

    public void setAllAddressBooks(ArrayList<ArrayList<HashMap<String, String>>> allAddressBooks) {
        this.allAddressBooks = allAddressBooks;
    }

    public ArrayList<HashMap<String, String>> getAddressBook() {
        return addressBook;
    }

    public File getFile() {
        return file;
    }

    public String getNewFileName() {
        return newFileName;
    }

    /**
     * Adds an observer to the observer list.
     * @param observer is the observer to be added.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

}
