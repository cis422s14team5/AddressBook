import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class AddressBookView extends JFrame {

    private AllBooksView allBooksView;
    private ArrayList<HashMap<String, String>> addressBook;
    private HashMap<String, String> address;

    // Panels
    private JTabbedPane tabbedPane;

    // All Contacts Panels
    private JPanel allContactsPanel;
    private JPanel allContactsButtonPanel;

    // Contact Panels
    private JPanel contactPanel;
    private JPanel contactButtonPanel;
    private JPanel addressPanel;
    private JPanel lastPanel;
    private JPanel deliveryPanel;
    private JPanel secondPanel;
    private JPanel recipientPanel;
    private JPanel phonePanel;

    // Labels
    private JLabel lastLabel;
    private JLabel cityLabel = new JLabel("City");
    private JLabel stateLabel = new JLabel("State");
    private JLabel zipLabel = new JLabel("Zip");
    private JLabel deliveryLabel = new JLabel("Delivery:");
    private JLabel secondLabel = new JLabel("Second:");
    private JLabel recipientLabel = new JLabel("Recipient: ");
    private JLabel lastNameLabel = new JLabel("Last");
    private JLabel firstNameLabel = new JLabel("First");
    private JLabel phoneLabel = new JLabel("Phone:");

    // Text Fields
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;
    private JTextField deliveryField;
    private JTextField secondField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField phoneField;

    // Buttons
    private JButton close;
    private JButton newContact;
    private JButton edit;
    private JButton remove;
    private JButton clear;
    private JButton save;

    private JList scrollList;

    private boolean isEditing;

    private int index;

    private JDialog addressBookFrame;

    public AddressBookView(AllBooksView allBooksView, int index) {
        this.index = index;
        this.allBooksView = allBooksView;
        addressBook = allBooksView.getAllAddressBooks().get(index);
        isEditing = false;

        // Window
        addressBookFrame = new JDialog();
        addressBookFrame.setTitle("Address Book");
        addressBookFrame.setSize(460, 260);
        setResizable(false);
        setLocationRelativeTo(null); // TODO fix location where address book window opens
        addressBookFrame.setLayout(new BorderLayout());
        addressBookFrame.setVisible(true);

        // Panels
        tabbedPane = new JTabbedPane();

        // All Contacts Panels
        allContactsPanel = new JPanel();
        allContactsButtonPanel = new JPanel();

        // Contact Panels
        contactPanel = new JPanel();
        contactButtonPanel = new JPanel();
        addressPanel = new JPanel();
        lastPanel = new JPanel();
        deliveryPanel = new JPanel();
        secondPanel = new JPanel();
        recipientPanel = new JPanel();
        phonePanel = new JPanel();

        // JList and JScrollPane
        DefaultListModel listModel = new DefaultListModel();
        scrollList = new JList<String>(listModel);
        scrollList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollList.setSelectedIndex(0);
        listModel.clear();

        updateScrollList();

        JScrollPane scrollPane = new JScrollPane(scrollList);
        scrollPane.setPreferredSize(new Dimension(360, 145));

        // Labels
        lastLabel = new JLabel("Last:");
        cityLabel = new JLabel("City");
        stateLabel = new JLabel("State");
        zipLabel = new JLabel("Zip");
        deliveryLabel = new JLabel("Delivery:");
        secondLabel = new JLabel("Second:");
        recipientLabel = new JLabel("Recipient: ");
        lastNameLabel = new JLabel("Last");
        firstNameLabel = new JLabel("First");
        phoneLabel = new JLabel("Phone:");

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
        close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                closeAddressBook();
            }
        });

        newContact = new JButton("New");
        newContact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                newContact();
            }
        });

        edit = new JButton("Edit");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                editContact();
            }
        });

        remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                removeContact();
            }
        });

        // Contact Buttons
        clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                clearFields();
            }
        });

        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                saveContact();
            }
        });

        // Layouts
        addressBookFrame.add(tabbedPane);
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
        allContactsButtonPanel.add(close);

        // Contact Layout
        contactPanel.setBorder(border);
        contactPanel.add(addressPanel);
        contactPanel.add(contactButtonPanel);

        GridLayout addressPanelLayout = new GridLayout(5, 0);
        addressPanel.setLayout(addressPanelLayout);
        addressPanel.setBorder(border);
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

        GridLayout contactButtonPanelLayout = new GridLayout(0, 2);
        contactButtonPanel.setLayout(contactButtonPanelLayout);
        contactButtonPanel.setBorder(border);
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

    /**
     * Switches the allBooksView to the Contact tab and clears the fields.
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
        saveAddressBook();
        JOptionPane.showMessageDialog(null, "Contact Removed.");
    }

    /**
     * Switches the allBooksView to the Contact tab and fills the fields with the selected contact's details.
     */
    private void editContact() {
        if (!scrollList.isSelectionEmpty() && !isEditing) {
            isEditing = true;
            getContact(scrollList.getSelectedIndex());
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
        if (!lastNameField.getText().equals("")) {
            address.put("lastName", lastNameField.getText());
        } else {
            address.put("lastName", " ");
        }
        if (!firstNameField.getText().equals("")) {
            address.put("firstName", firstNameField.getText());
        } else {
            address.put("firstName", " ");
        }
        if (!phoneField.getText().equals("")) {
            address.put("phone", phoneField.getText());
        } else {
            address.put("phone", " ");
        }

        if (cityField.getText().equals("") && stateField.getText().equals("") &&
                zipField.getText().equals("") && deliveryField.getText().equals("") &&
                secondField.getText().equals("") && lastNameField.getText().equals("") &&
                firstNameField.getText().equals("") && phoneField.getText().equals("")) {
            if (isEditing) {
                addressBook.remove(scrollList.getSelectedIndex());
                updateScrollList();
                saveAddressBook();
            }
        } else {
            if (isEditing) {
                addressBook.set(scrollList.getSelectedIndex(), address);
                isEditing = false;
            } else {
                addressBook.add(address);
            }
        }

        clearFields();
        tabbedPane.setSelectedIndex(0);
        updateScrollList();
        saveAddressBook(); // TODO only
        // JOptionPane.showMessageDialog(null, "Contact Saved.");
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
     * Notifies Main that it needs to save the current address book.
     */
    private void saveAddressBook() {
        allBooksView.setAddressBook(addressBook);
        for (Observer observer : allBooksView.observers) {
            observer.update(1);
        }
    }

    private void closeAddressBook() {

        int choice = JOptionPane.showConfirmDialog(null, "Save this address book?", "Save Address Book", JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == 0) { // Yes
            saveAddressBook();
            allBooksView.closeAddressBook(index);
        } else if (choice == 1) {  // No
            allBooksView.closeAddressBook(index);
        }
        if (choice != 2) {
            addressBookFrame.dispose();
        }

    }
}
