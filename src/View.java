import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

// TODO Break the View into different modules: All Address Books, All Contacts, Contact
// TODO Fix the layout of the Contacts tab so the fields fill up all the space left after the labels.
// TODO Validate input in a separate module

/**
 * The address book GUI.
 */
public class View extends JFrame {

    // Text Fields
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;
    private JTextField deliveryField;
    private JTextField secondField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField phoneField;

    final private JTabbedPane tabbedPane;

    private JList<String> scrollList;

    private ArrayList<HashMap<String, String>> addressBook;
    private HashMap<String, String> address;
    private boolean isEditing;
    private ArrayList<Observer> observers;

    /**
     * Constructor. Sets up the window, panels, labels, buttons, and fields.
     * @param addressList is an addressBook loaded from the TSV file.
     */
    public View(ArrayList<HashMap<String, String>> addressList) {
        addressBook = addressList;
        observers = new ArrayList<>();
        isEditing = false;

        // Window
        setTitle("Address Book");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(460, 260);
        setResizable(false);
        setLocationRelativeTo(null);

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
        scrollList = new JList<>();
        scrollList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollList.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(scrollList);
        scrollPane.setPreferredSize(new Dimension(360, 145));
        updateScrollList();

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
        final JButton newContact = new JButton("New");
        newContact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                newContact();
            }
        });

        JButton edit = new JButton("Edit");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                editContact();
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

        setVisible(true);
    }

    /**
     * Switches the view to the Contact tab and clears the fields.
     */
    private void newContact() {
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
     * Switches the view to the Contact tab and fills the fields with the selected contact's details.
     */
    private void editContact() {
        if (!scrollList.isSelectionEmpty()) {
            isEditing = true;
            getContact(scrollList.getSelectedIndex());
            tabbedPane.setSelectedIndex(1);
        } else if (addressBook.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The address book is empty, create a new contact.");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to edit.");
        }
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
        saveAddressBook();
        JOptionPane.showMessageDialog(null, "Contact Saved.");
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
        for (Observer observer : observers) {
            observer.update(1);
        }
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

    /**
     * Updates the list of addresses in the scroll list.
     */
    public void updateScrollList() {
        ArrayList<String> tempList = new ArrayList<>();
        for (HashMap<String, String> address : addressBook) {
            tempList.add(address.get("lastName") + ", " + address.get("firstName"));
        }
        scrollList.setListData(tempList.toArray(new String[tempList.size()]));
    }

    /**
     * Sets the addressBook and updates the scroll list.
     * @param addressBook is the new address book.
     */
    public void setAddressBook(ArrayList<HashMap<String, String>> addressBook) {
        this.addressBook = addressBook;
        updateScrollList();
    }

    /**
     * Gets the addressBook.
     * @return the current address book.
     */
    public ArrayList<HashMap<String, String>> getAddressBook() {
        return addressBook;
    }

    /**
     * Adds an observer to the observer list.
     * @param observer is the observer to be added.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
}
