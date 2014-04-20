import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class BookView extends JFrame {

    private ArrayList<HashMap<String, String>> addressBook;
    private HashMap<String, String> address;

    private ImportExport importExport;
    private TSV tsv;

    private JList<String> scrollList;
    private boolean isEditing;
    private String title;

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

    public BookView(ArrayList<HashMap<String, String>> addressBook, String title) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        this.title = title;
        this.addressBook = addressBook;
        isEditing = false;
        importExport = new ImportExport(this);
        tsv = new TSV();

        // Window
        setTitle("Address Book");
        setSize(760, 260);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        // Menu
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem newMenuItem = new JMenuItem("New Contact");
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        newMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                newContact();
            }
        });
        fileMenu.add(newMenuItem);


        JMenuItem openMenuItem = new JMenuItem("Open Selected Contact");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                editContact(scrollList.getSelectedIndex());
            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
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
                saveBook();
            }
        });
        fileMenu.add(saveAsMenuItem);

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
                JList list = (JList)e.getSource();
                if (e.getClickCount() == 2) {
                    editContact(list.locationToIndex(e.getPoint()));
                } else if (e.getClickCount() == 3) {   // Triple-click
                    editContact(list.locationToIndex(e.getPoint()));

                }
            }
        });

        updateScrollList();

        JScrollPane scrollPane = new JScrollPane(scrollList);
        scrollPane.setPreferredSize(new Dimension(360, 145));

        // Labels
        JLabel lastLabel = new JLabel("Last:");
        JLabel cityLabel = new JLabel("City");
        JLabel stateLabel = new JLabel("State");
        JLabel zipLabel = new JLabel("Zip");
        JLabel deliveryLabel = new JLabel("Delivery:");
        JLabel secondLabel = new JLabel("Second:");
        JLabel recipientLabel = new JLabel("Recipient: ");
        JLabel lastNameLabel = new JLabel("Last");
        JLabel firstNameLabel = new JLabel("First");
        JLabel phoneLabel = new JLabel("Phone:");

        // Text Fields
        cityField = new JTextField();
        stateField = new JTextField();
        zipField = new JTextField();
        deliveryField = new JTextField();
        secondField = new JTextField();
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        phoneField = new JTextField();

        // All Contacts Buttons

        JButton newContact = new JButton("New");
        newContact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                tabbedPane.setEnabledAt(0, false);
                newContact();
            }
        });

        JButton edit = new JButton("Edit");
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

        JButton save = new JButton("Save");
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
        allContactsPanel.setBorder(border);
        allContactsPanel.add(scrollPane);
        allContactsPanel.add(allContactsButtonPanel);

        allContactsButtonPanel.setBorder(border);
        allContactsButtonPanel.add(newContact);
        allContactsButtonPanel.add(edit);
        allContactsButtonPanel.add(remove);

        // Contact Layout
        //contactPanel.setBorder(border);
        //GridLayout contactPanelLayout = new GridLayout(2, 0);
        //contactPanel.setLayout(contactPanelLayout);
        contactPanel.add(addressPanel);
        contactPanel.add(contactButtonPanel);

        GridLayout addressPanelLayout = new GridLayout(5, 0);
        addressPanel.setLayout(addressPanelLayout);
        //addressPanel.setBorder(border);
        addressPanel.add(lastPanel);
        addressPanel.add(deliveryPanel);
        addressPanel.add(secondPanel);
        addressPanel.add(recipientPanel);
        addressPanel.add(phonePanel);

        GridLayout linePanelLayout = new GridLayout(1, 0);
        lastPanel.setLayout(linePanelLayout);
        lastPanel.add(lastLabel);
        lastPanel.add(cityLabel);
        lastPanel.add(cityField);
        lastPanel.add(stateLabel);
        lastPanel.add(stateField);
        lastPanel.add(zipLabel);
        lastPanel.add(zipField);

        deliveryPanel.setLayout(linePanelLayout);
        deliveryPanel.add(deliveryLabel);
        deliveryPanel.add(deliveryField);

        secondPanel.setLayout(linePanelLayout);
        secondPanel.add(secondLabel);
        secondPanel.add(secondField);

        recipientPanel.setLayout(linePanelLayout);
        recipientPanel.add(recipientLabel);
        recipientPanel.add(lastNameLabel);
        recipientPanel.add(lastNameField);
        recipientPanel.add(firstNameLabel);
        recipientPanel.add(firstNameField);

        phonePanel.setLayout(linePanelLayout);
        phonePanel.add(phoneLabel);
        phonePanel.add(phoneField);

        GridLayout contactButtonPanelLayout = new GridLayout(0, 3);
        contactButtonPanel.setLayout(contactButtonPanelLayout);
        //contactButtonPanel.setBorder(border);
        contactButtonPanel.add(cancel);
        contactButtonPanel.add(clear);
        contactButtonPanel.add(save);
    }

    /**
     * Updates the list of addresses in the scroll list.
     */
    public void updateScrollList() {

        //listModel.clear();
        scrollList.setListData(new String[0]);
        ArrayList<String> tempList = new ArrayList<>();
        for (HashMap<String, String> address : addressBook) {
            tempList.add(address.get("lastName") + ", " + address.get("firstName"));
        }
        scrollList.setListData(tempList.toArray(new String[tempList.size()]));
    }

    private void cancelEdit() {
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(0, true);
        isEditing = false;
        clearFields();
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
        addressBook.remove(scrollList.getSelectedIndex());
        updateScrollList();
        saveBook();
        JOptionPane.showMessageDialog(null, "Contact Removed.");
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
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to edit.");
        }
        //isEditing = false;
    }

    /**
     * Fills the contact tab's fields from the selected address. Used by editContact.
     * @param index is the scrollList's selected index.
     */
    private void getContact(int index) {
        address = addressBook.get(index);

        if (!address.get("city").equals(" ")) {
            cityField.setText(address.get("city"));
        } else {
            cityField.setText("");
        }
        if (!address.get("state").equals(" ")) {
            stateField.setText(address.get("state"));
        } else {
            stateField.setText("");
        }
        if (!address.get("zip").equals(" ")) {
            zipField.setText(address.get("zip"));
        } else {
            zipField.setText("");
        }
        if (!address.get("delivery").equals(" ")) {
            deliveryField.setText(address.get("delivery"));
        } else {
            deliveryField.setText("");
        }
        if (!address.get("second").equals(" ")) {
            secondField.setText(address.get("second"));
        } else {
            secondField.setText("");
        }
        if (!address.get("lastName").equals(" ")) {
            lastNameField.setText(address.get("lastName"));
        } else {
            lastNameField.setText("");
        }
        if (!address.get("firstName").equals(" ")) {
            firstNameField.setText(address.get("firstName"));
        } else {
            firstNameField.setText("");
        }
        if (!address.get("phone").equals(" ")) {
            phoneField.setText(address.get("phone"));
        } else {
            phoneField.setText("");
        }
    }

    /**
     * Save the contents of the fields to the addressBook and the TSV file.
     */
    private void saveContact() {
        address = new HashMap<>();


        if (checkLastName() && checkPhone() && checkZip()) {
            address.put("lastName", lastNameField.getText());

            if (!phoneField.getText().equals("")) {
                address.put("phone", phoneField.getText());
            } else {
                address.put("phone", " ");
            }


            if (!cityField.getText().equals("")) {
                address.put("city", cityField.getText());
            } else {
                address.put("city", " ");
            }
            if (!stateField.getText().equals("")) {
                address.put("state", stateField.getText());
            } else {
                address.put("state", " ");
            }
            if (!zipField.getText().equals("")) {
                address.put("zip", zipField.getText());
            } else {
                address.put("zip", " ");
            }
            if (!deliveryField.getText().equals("")) {
                address.put("delivery", deliveryField.getText());
            } else {
                address.put("delivery", " ");
            }
            if (!secondField.getText().equals("")) {
                address.put("second", secondField.getText());
            } else {
                address.put("second", " ");
            }
            if (!firstNameField.getText().equals("")) {
                address.put("firstName", firstNameField.getText());
            } else {
                address.put("firstName", " ");
            }

            if (cityField.getText().equals("") && stateField.getText().equals("") &&
                    zipField.getText().equals("") && deliveryField.getText().equals("") &&
                    secondField.getText().equals("") && lastNameField.getText().equals("") &&
                    firstNameField.getText().equals("") && phoneField.getText().equals("")) {
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
        }
    }

    private boolean checkLastName() {
        boolean valid = false;
        if (!lastNameField.getText().equals("")) {
            valid = true;
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a last name.");
        }

        return valid;
    }

    private boolean checkPhone() {
        boolean valid = false;
        if (!phoneField.getText().equals("") &&
                phoneField.getText().matches("^[0-9]{3}[-][0-9]{3}[-][0-9]{4}$") ||
                phoneField.getText().matches("^[0-9]{3}[-][0-9]{4}$")) {
            valid = true;
            //address.put("phone", phoneField.getText());
        } else if (phoneField.getText().equals("")) {
            valid = true;
           // address.put("phone", " ");
        } else {
            JOptionPane.showMessageDialog(this, "You did not enter a valid phone number. Please try again.");
        }

        return valid;
    }

    private boolean checkZip() {
        boolean valid = false;
        if (!zipField.getText().equals("") && zipField.getText().matches("^[0-9]{5}$") ||
                zipField.getText().matches("^[0-9]{5}[-][0-9]{4}$")) {
            valid = true;
        } else if (zipField.getText().equals("")) {
            valid = true;
        } else {
            JOptionPane.showMessageDialog(this, "You did not enter a valid zip code. Please try again.");
        }

        return valid;
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
    }

    /**
     * Saves the current address book.
     */
    private void saveBook() {
        AddressConverter converter = new AddressConverter();
        tsv.write(new File("addressBooks/" + title + ".tsv"), converter.internalToStandard(addressBook));
    }

    private void closeBook() {
        int choice = JOptionPane.showConfirmDialog(null, "Save this address book?", "Save Address Book", JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == 0) { // Yes
            saveBook();
        }
        if (choice != 2) {
            dispose();
        }

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
}
