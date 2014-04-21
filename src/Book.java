import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An address book.
 */
public class Book extends JFrame {

    private ArrayList<HashMap<String, String>> addressBook;
    private HashMap<String, String> address;

    private ImportExport importExport;
    private TSV tsv;

    private JList<String> scrollList;
    private boolean isEditing;
    private String title;
    private boolean modified;
    boolean sortedByName;

    // Panels
    private JTabbedPane tabbedPane;

    // Text Fields
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;
    private JTextField deliveryField;
    private JTextField secondField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextArea noteField;

    private AllBooks allBooks;
    private SortBooks sortBooks;

    /**
     * Constructor. Creates an address book window.
     * @param allBooks is a copy of the all books view
     * @param addressBook is the address book for this window.
     * @param title is the title of the address book.
     */
    public Book(AllBooks allBooks, ArrayList<HashMap<String, String>> addressBook, String title) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        this.title = title;
        this.addressBook = addressBook;
        this.allBooks = allBooks;

        importExport = new ImportExport(this);
        tsv = new TSV();
        sortBooks = new SortBooks();

        modified = false;
        isEditing = false;
        sortedByName = true;

        // Window
        setTitle("Address Book");
        setSize(560, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeBook();
            }
        });

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
                newContact();
            }
        });
        fileMenu.add(newMenuItem);


        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                editContact(scrollList.getSelectedIndex());
            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        closeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                closeBook();
            }
        });
        fileMenu.add(closeMenuItem);

        fileMenu.addSeparator();

        JMenuItem importMenuItem = new JMenuItem("Import");
        importMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        importMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                importBook();
            }
        });
        fileMenu.add(importMenuItem);

        JMenuItem exportMenuItem = new JMenuItem("Export");
        exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        exportMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                exportBook();
            }
        });
        fileMenu.add(exportMenuItem);

        fileMenu.addSeparator();

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                saveBook();
            }
        });
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        saveAsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                saveAs();
            }
        });
        fileMenu.add(saveAsMenuItem);

        fileMenu.addSeparator();

        JMenuItem printMenuItem = new JMenuItem("Print...");
        printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        printMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                print();
            }
        });
        fileMenu.add(printMenuItem);

        setJMenuBar(menuBar);

        // Panels
        tabbedPane = new JTabbedPane();

        // All Contacts Panels
        JPanel allContactsPanel = new JPanel();
        JPanel allContactsButtonPanel = new JPanel();

        // Contact Panels
        JPanel contactPanel = new JPanel();
        JPanel contactButtonPanel = new JPanel();
        JPanel addressPanel = new JPanel();
        JPanel lastPanel = new JPanel();
        JPanel deliveryPanel = new JPanel();
        JPanel secondPanel = new JPanel();
        JPanel recipientPanel = new JPanel();
        JPanel phonePanel = new JPanel();
        JPanel zipPanel = new JPanel();
        JPanel notePanel = new JPanel();
        JPanel emailPanel = new JPanel();

        // JList and JScrollPane
        DefaultListModel<String> listModel = new DefaultListModel<>();
        scrollList = new JList<>(listModel);
        scrollList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollList.setSelectedIndex(0);
        listModel.clear();

        scrollList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tabbedPane.setEnabledAt(0, false);
                JList list = (JList)e.getSource();
                if (e.getClickCount() == 2) {
                    editContact(list.locationToIndex(e.getPoint()));
                } else if (e.getClickCount() == 3) {   // Triple-click
                    editContact(list.locationToIndex(e.getPoint()));

                }
            }
        });

        if (allBooks.isMerging) {
            addressBook.addAll(allBooks.mergeBook);
            allBooks.isMerging = false;
        }

        updateScrollList();

        JScrollPane scrollPane = new JScrollPane(scrollList);
        //scrollPane.setPreferredSize(new Dimension(360, 145));

        // Drop-down
        String[] sortOptions = {"Last Name", "Zip Code"};
        JComboBox<String> sortDropDown = new JComboBox<>(sortOptions);
        sortDropDown.setSelectedIndex(0);
        sortDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                if (cb.getSelectedIndex() == 0) {
                    sortByLastName();
                    sortedByName = true;
                    updateScrollList();
                } else {
                    sortByZip();
                    sortedByName = false;
                    updateScrollList();
                }
            }
        });

        // Labels
        JLabel cityLabel = new JLabel("City:");
        JLabel stateLabel = new JLabel("      State:");
        JLabel zipLabel = new JLabel("Zip:");
        JLabel deliveryLabel = new JLabel("Delivery:");
        JLabel secondLabel = new JLabel("Second:");
        JLabel recipientLabel = new JLabel("Name: ");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel noteLabel = new JLabel("Note:");

        // Text Fields
        cityField = new JTextField();
        stateField = new JTextField();
        zipField = new JTextField();
        deliveryField = new JTextField();
        secondField = new JTextField();
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        noteField = new JTextArea();
        JScrollPane notePane = new JScrollPane(noteField);
        noteField.setLineWrap(true);
        noteField.setWrapStyleWord(true);
        notePane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        // All Contacts Buttons

        JButton newContact = new JButton("New");
        newContact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                tabbedPane.setEnabledAt(0, false);
                newContact();
            }
        });

        JButton edit = new JButton("Open");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                tabbedPane.setEnabledAt(0, false);
                editContact(scrollList.getSelectedIndex());
            }
        });

        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                removeContact();
            }
        });

        // Contact Buttons
        JButton clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                clearFields();
            }
        });

        JButton save = new JButton("Done");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                saveContact();
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                cancelEdit();
            }
        });

        // Layouts
        add(tabbedPane);
        tabbedPane.addTab("All Contacts", allContactsPanel);
        tabbedPane.addTab("Contact", contactPanel);

        Border border = BorderFactory.createEmptyBorder(0, 10, 10, 10);

        // All Contacts Layout
        GridLayout allContactsPanelLayout = new GridLayout(3, 0);
        allContactsPanel.setLayout(allContactsPanelLayout);
        allContactsPanel.setBorder(border);
        allContactsPanel.add(sortDropDown);
        allContactsPanel.add(scrollPane);
        allContactsPanel.add(allContactsButtonPanel);

        allContactsButtonPanel.setBorder(border);
        allContactsButtonPanel.add(newContact);
        allContactsButtonPanel.add(edit);
        allContactsButtonPanel.add(remove);


        // Contact Layout

        // GridLayout contactPanelLayout = new GridLayout(2, 0);
        // contactPanel.setLayout(contactPanelLayout);
        // contactPanel.setBorder(border);
        contactPanel.add(addressPanel);
        contactPanel.add(contactButtonPanel);

        GridLayout addressPanelLayout = new GridLayout(9, 0);
        addressPanel.setLayout(addressPanelLayout);
        addressPanel.setBorder(border);
        addressPanel.add(recipientPanel);
        addressPanel.add(deliveryPanel);
        addressPanel.add(secondPanel);
        addressPanel.add(lastPanel);
        addressPanel.add(zipPanel);
        addressPanel.add(phonePanel);
        addressPanel.add(emailPanel);
        addressPanel.add(notePanel);
        addressPanel.add(contactButtonPanel);

        GridLayout linePanelLayout = new GridLayout(1, 0);
        lastPanel.setLayout(linePanelLayout);
        //lastPanel.add(lastLabel);
        lastPanel.add(cityLabel);
        lastPanel.add(cityField);
        lastPanel.add(stateLabel);
        lastPanel.add(stateField);

        GridLayout linePanel2Layout = new GridLayout(0, 2);
        deliveryPanel.setLayout(linePanel2Layout);
        deliveryPanel.add(deliveryLabel);
        deliveryPanel.add(deliveryField);

        zipPanel.setLayout(linePanel2Layout);
        zipPanel.add(zipLabel);
        zipPanel.add(zipField);

        secondPanel.setLayout(linePanel2Layout);
        secondPanel.add(secondLabel);
        secondPanel.add(secondField);

        GridLayout linePanel3Layout = new GridLayout(0, 3);
        recipientPanel.setLayout(linePanel3Layout);
        recipientPanel.add(recipientLabel);
        recipientPanel.add(firstNameField);
        //recipientPanel.add(lastNameLabel);
        recipientPanel.add(lastNameField);
        //recipientPanel.add(firstNameLabel);

        phonePanel.setLayout(linePanel2Layout);
        phonePanel.add(phoneLabel);
        phonePanel.add(phoneField);

        GridLayout contactButtonPanelLayout = new GridLayout(0, 3);
        contactButtonPanel.setLayout(contactButtonPanelLayout);
        contactButtonPanel.setBorder(border);
        contactButtonPanel.add(cancel);
        contactButtonPanel.add(clear);
        contactButtonPanel.add(save);

        notePanel.setLayout(linePanel2Layout);
        //notePanel.setBorder(border);
        notePanel.add(noteLabel);
        notePanel.add(notePane);

        emailPanel.setLayout(linePanelLayout);
        //emailPanel.setBorder(border);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
    }

    // All Addresses Methods

    /**
     * Updates the list of addresses in the scroll list.
     */
    public void updateScrollList() {
        scrollList.setListData(new String[0]);
        ArrayList<String> tempList = new ArrayList<>();
        checkSort();
        for (HashMap<String, String> address : addressBook) {
            tempList.add(address.get("lastName") + ", " + address.get("firstName"));
        }
        scrollList.setListData(tempList.toArray(new String[tempList.size()]));
        scrollList.setSelectedIndex(0);
    }

    /**
     * Switches to the Contact tab and clears the fields.
     */
    private void newContact() {
        isEditing = false;
        clearFields();
        tabbedPane.setSelectedIndex(1);
    }

    /**
     * Removes the selected contact from the addressBook and the TSV file.
     */
    private void removeContact() {
        int choice = JOptionPane.showConfirmDialog(null,
                "If you remove this contact it will be gone forever.\nAre you sure?",
                "Remove Address Book",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == 0) {
            addressBook.remove(scrollList.getSelectedIndex());
            updateScrollList();
            saveBook();
        }
    }

    /**
     * Switches the allBooksView to the Contact tab and fills the fields with the selected contact's details.
     */
    private void editContact(int index) {
        if (!scrollList.isSelectionEmpty() && !isEditing) {
            isEditing = true;
            getContact(index);
            tabbedPane.setSelectedIndex(1);
        } else if (addressBook.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The address book is empty, create a new contact.");
            tabbedPane.setEnabledAt(0, true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to edit.");
            tabbedPane.setEnabledAt(0, true);
        }
    }

    /**
     * Fills the contact tab's fields from the selected address. Used by editContact.
     * @param index is the scrollList's selected index.
     */
    private void getContact(int index) {
        address = addressBook.get(index);

        if (address.get("city") != null) {
            cityField.setText(address.get("city"));
        } else {
            cityField.setText("");
        }
        if (address.get("state") != null) {
            stateField.setText(address.get("state"));
        } else {
            stateField.setText("");
        }
        if (address.get("zip") != null) {
            zipField.setText(address.get("zip"));
        } else {
            zipField.setText("");
        }
        if (address.get("delivery") != null) {
            deliveryField.setText(address.get("delivery"));
        } else {
            deliveryField.setText("");
        }
        if (address.get("second") != null) {
            secondField.setText(address.get("second"));
        } else {
            secondField.setText("");
        }
        if (address.get("lastName") != null) {
            lastNameField.setText(address.get("lastName"));
        } else {
            lastNameField.setText("");
        }
        if (address.get("firstName") != null) {
            firstNameField.setText(address.get("firstName"));
        } else {
            firstNameField.setText("");
        }
        if (address.get("phone") != null) {
            phoneField.setText(address.get("phone"));
        } else {
            phoneField.setText("");
        }
        if (address.get("email") != null) {
            emailField.setText(address.get("email"));
        } else {
            emailField.setText("");
        }
        if (address.get("note") != null) {
            noteField.setText(address.get("note"));
        } else {
            noteField.setText("");
        }
    }

    // Contact Methods

    /**
     * Save the contents of the fields to the addressBook and the TSV file.
     */
    private void saveContact() {
        address = new HashMap<>();
        CheckInput checkInput = new CheckInput(this, lastNameField, phoneField, zipField);

        if (checkInput.checkLastName() && checkInput.checkPhone() && checkInput.checkZip()) {
            address.put("lastName", lastNameField.getText());

            if (!phoneField.getText().equals("")) {
                address.put("phone", phoneField.getText());
            } else {
                address.put("phone", null);
            }

            if (!cityField.getText().equals("")) {
                address.put("city", cityField.getText());
            } else {
                address.put("city", null);
            }

            if (!stateField.getText().equals("")) {
                address.put("state", stateField.getText());
            } else {
                address.put("state", null);
            }

            if (!zipField.getText().equals("")) {
                address.put("zip", zipField.getText());
            } else {
                address.put("zip", null);
            }

            if (!deliveryField.getText().equals("")) {
                address.put("delivery", deliveryField.getText());
            } else {
                address.put("delivery", null);
            }

            if (!secondField.getText().equals("")) {
                address.put("second", secondField.getText());
            } else {
                address.put("second", null);
            }

            if (!firstNameField.getText().equals("")) {
                address.put("firstName", firstNameField.getText());
            } else {
                address.put("firstName", null);
            }

            if (!emailField.getText().equals("")) {
                address.put("email", emailField.getText());
            } else {
                address.put("email", null);
            }

            if (!noteField.getText().equals("")) {
                address.put("note", noteField.getText());
            } else {
                address.put("note", null);
            }

            if (cityField.getText().equals("") && stateField.getText().equals("") &&
                    zipField.getText().equals("") && deliveryField.getText().equals("") &&
                    secondField.getText().equals("") && lastNameField.getText().equals("") &&
                    firstNameField.getText().equals("") && phoneField.getText().equals("") &&
                    emailField.getText().equals("") && noteField.getText().equals("")) {
                if (isEditing) {
                    addressBook.remove(scrollList.getSelectedIndex());
                    updateScrollList();
                    saveBook();
                }
            } else {
                if (isEditing) {
                    addressBook.set(scrollList.getSelectedIndex(), address);
                    isEditing = false;

                    tabbedPane.setEnabledAt(0, true);
                } else {
                    addressBook.add(address);
                }
            }

            clearFields();
            tabbedPane.setSelectedIndex(0);
            updateScrollList();

            modified = true;
            getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
        }
    }

    /**
     * Clears all the fields in the contact tab.
     */
    private void clearFields() {
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
        deliveryField.setText("");
        secondField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        noteField.setText("");
    }

    /**
     * Closes the address book.
     */
    private void closeBook() {
        if (modified) {
            int choice = JOptionPane.showConfirmDialog(null, "Save this address book?",
                    "Save Address Book", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == 0) { // Yes
                saveBook();
                dispose();
            } else if (choice == 1) { // No
                dispose();
            }
        } else {
            dispose();
        }
    }

    /**
     * Saves the current address book.
     */
    private void saveBook() {
        AddressConverter converter = new AddressConverter();
        tsv.write(new File(allBooks.saveDir + allBooks.slash + title + ".tsv"),
                converter.internalToStandard(addressBook));
        modified = false;
        getRootPane().putClientProperty("Window.documentModified", Boolean.FALSE);
    }

    /**
     * Lets the user supply a new name and makes a copy of the address book.
     */
    private void saveAs() {
        AddressConverter converter = new AddressConverter();
        allBooks.openNewBookDialog(converter.internalToStandard(addressBook));
    }

    /**
     * Imports all the addresses from the selected TSV file into addressBook.
     */
    private void importBook() {
        AddressConverter converter = new AddressConverter();
        File file = importExport.importTSV();
        if (file != null) {
            addressBook.addAll(converter.standardToInternal(tsv.read(file)));
            saveBook();
            updateScrollList();
        }
    }

    /**
     * Imports all the addresses from addressBook into the selected TSV file.
     */
    private void exportBook() {
        AddressConverter converter = new AddressConverter();
        File file = importExport.exportTSV();
        if (file != null) {
            tsv.write(file, converter.internalToStandard(addressBook));
        }
    }

    /**
     * Cancels the edits and goes back to the contact list.
     */
    private void cancelEdit() {
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(0, true);
        isEditing = false;
        clearFields();
    }

    public void print() {
        try {
            String cn = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(cn); // Use the native L&F
        } catch (Exception cnf) {
        }
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new PrintBook(addressBook));
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                System.out.println("The job did not successfully complete");
            }
        }
    }

    public void sortByLastName() {
        addressBook = sortBooks.sortByName(addressBook);
    }

    public void sortByZip() {
        addressBook = sortBooks.sortByZip(addressBook);
    }

    public void checkSort() {
        if (sortedByName) {
            sortByLastName();
        } else {
            sortByZip();
        }
    }
}
