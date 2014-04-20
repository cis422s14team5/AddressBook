//import javax.swing.*;
//import javax.swing.border.Border;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
//public class Book extends JFrame {
//
//    private JTextField firstNameField;
//    private JTextField lastNameField;
//    private JTextField streetField;
//    private JTextField cityField;
//    private JTextField zipField;
//    private JTextField phoneField;
//    private JTextField emailField;
//    private int page;
//    private int count;
//    private boolean editBool;
//
//    public MapAddress mapAddress;
//    public ArrayList<MapAddress> mapAddressList;
//
//    private ArrayList<Observer> observers;
//
//    // Radio Buttons
//
//    JRadioButton contact1;
//    JRadioButton contact2;
//    JRadioButton contact3;
//    JRadioButton contact4;
//    JRadioButton contact5;
//
//    JButton next;
//    JButton prev;
//
//    public Book(final ArrayList<MapAddress> mapAddressList) {
//        this.mapAddressList = mapAddressList;
//        observers = new ArrayList<Observer>();
//        mapAddress = new MapAddress();
//        page = 0;
//        count = 0;
//        editBool = false;
//
//        // Window
//
//        final JTabbedPane tabbedPane = new JTabbedPane();
//        getContentPane().add(tabbedPane);
//
//        setTitle("Address Book");
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setSize(700, 375);
//        setResizable(false);
//        setLocationRelativeTo(null);
//
//        // Panels
//        JPanel newContactPanel = new JPanel();//This will create the second tab
//        JPanel mainPanel = new JPanel();
//        JPanel buttonPanel = new JPanel();
//        JPanel labelPanel = new JPanel();
//        JPanel fieldPanel = new JPanel();
//
//        JPanel contactsPanel = new JPanel();//This will create the first tab
//        JPanel list = new JPanel();
//        JPanel rButtonPanel = new JPanel();
//        JPanel pgNum = new JPanel();
//
//        tabbedPane.addTab("Contacts", contactsPanel);
//        tabbedPane.addTab("Add Contact", newContactPanel);
//
//        // Labels
//        JLabel firstNameLabel = new JLabel("First Name");
//        JLabel lastNameLabel = new JLabel("Last Name");
//        JLabel streetLabel = new JLabel("Street");
//        JLabel cityLabel = new JLabel("City");
//        JLabel zipLabel = new JLabel("Zip");
//        JLabel phoneLabel = new JLabel("Phone");
//        JLabel emailLabel = new JLabel("Email");
//        final JLabel pageNumber = new JLabel("Page: "+ (page + 1));
//
//        // Text Fields
//        firstNameField = new JTextField();
//        lastNameField = new JTextField();
//        streetField = new JTextField();
//        cityField = new JTextField();
//        zipField = new JTextField();
//        phoneField = new JTextField();
//        emailField = new JTextField();
//
//        // Radio Buttons
//
//        ArrayList<String> contactText = initializeContacts();
//
//        contact1 = new JRadioButton(contactText.get(0));
//        contact2 = new JRadioButton(contactText.get(1));
//        contact3 = new JRadioButton(contactText.get(2));
//        contact4 = new JRadioButton(contactText.get(3));
//        contact5 = new JRadioButton(contactText.get(4));
//
//
//        // Add Radio Buttons to ButtonGroup (Allows only one to be selected at a time)
//
//        final ButtonGroup bg = new ButtonGroup();
//        bg.add(contact1);
//        bg.add(contact2);
//        bg.add(contact3);
//        bg.add(contact4);
//        bg.add(contact5);
//
//        // Buttons
//        JButton clear = new JButton("Clear");
//        clear.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                clearFields();
//            }
//        });
//
//        JButton edit = new JButton("Edit");
//        edit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                int index = findSelected();
//                if (index != -1) {
//                    tabbedPane.setSelectedIndex(1);
//                    editContact(index);
//                    editBool = true;
//                } else {
//                    JOptionPane.showMessageDialog(null, "No Contact selected.");
//                }
//            }
//        });
//
//
//
//        prev = new JButton("Previous Page");
//        prev.setEnabled(false);
//        prev.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                //prev.setEnabled(false);
//                if(page > 0) {
//                    //prev.setEnabled(true);
//                    count -= 5;
//                    contact1.setSelected(true);
//                    getContacts();
//                    page--;
//                    pageNumber.setText("Page: "+ (page + 1));
//                    if(page == 0) {
//                        prev.setEnabled(false);
//                    }
//                    if(mapAddressList.size() > (5 + count)) {
//                        next.setEnabled(true);
//                    }
//                }
//            }
//        });
//
//        JButton remove = new JButton("Remove");
//        remove.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                if (findSelected() != -1) {
//                    mapAddressList.remove(findSelected());
//                    if (mapAddressList.size() <= count) {
//                        count -= 5;
//                        page--;
//                        pageNumber.setText("Page: " + (page + 1));
//                        if (page == 0) {
//                            prev.setEnabled(false);
//                        }
//                    }
//                    notifyObservers();
//                    getContacts();
//                    JOptionPane.showMessageDialog(null, "Contact Removed");
//                } else{
//                    JOptionPane.showMessageDialog(null, "No Contact selected.");
//                }
//            }
//        });
//
//        next = new JButton("Next Page");
//        if (mapAddressList.size() < 5) {
//            next.setEnabled(false);
//        }
//        else {
//            next.setEnabled(true);
//        }
//        next.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                count += 5;
//                contact1.setSelected(true);
//                getContacts();
//                page++;
//                pageNumber.setText("Page: "+ (page + 1));
//                if(mapAddressList.size() > (5 + count)) {
//                    next.setEnabled(true);
//                }
//                else{
//                    next.setEnabled(false);
//                }
//                if(page > 0) {
//                    prev.setEnabled(true);
//                }
//            }
//        });
//
//        JButton save = new JButton("Save");
//        save.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                mapAddress = new MapAddress();
//                mapAddress.put("firstName", firstNameField.getText());
//                mapAddress.put("lastName", lastNameField.getText());
//                mapAddress.put("street", streetField.getText());
//                mapAddress.put("city", cityField.getText());
//                mapAddress.put("zip", zipField.getText());
//                mapAddress.put("phone", phoneField.getText());
//                mapAddress.put("email", emailField.getText());
//                if (!editBool) {
//                    if(mapAddressList.size() >= 5) {
//                        next.setEnabled(true);
//                    }
//                    mapAddressList.add(mapAddress);
//                } else {
//                    mapAddressList.set(findSelected(), mapAddress);
//                    tabbedPane.setSelectedIndex(0);
//                }
//                getContacts();
//                clearFields();
//                notifyObservers();
//                JOptionPane.showMessageDialog(null, "Saved!");
//            }
//        });
//
//
//        // Layout of "Add Contact" Tab
//
//        newContactPanel.add(mainPanel);
//        newContactPanel.add(BorderLayout.SOUTH, buttonPanel);
//
//        Border mainBorder = BorderFactory.createEmptyBorder(0, 10, 10, 10);
//        mainPanel.setBorder(mainBorder);
//        buttonPanel.setBorder(mainBorder);
//
//        GridLayout buttonPanelLayout = new GridLayout(2, 0);
//        buttonPanel.setLayout(buttonPanelLayout);
//        buttonPanel.add(clear);
//        buttonPanel.add(save);
//
//        GridLayout mainPanelLayout = new GridLayout(0, 2);
//        mainPanel.setLayout(mainPanelLayout);
//        mainPanel.add(labelPanel);
//        mainPanel.add(fieldPanel);
//
//        GridLayout labelPanelLayout = new GridLayout(0, 1);
//        labelPanel.setLayout(labelPanelLayout);
//        labelPanel.add(firstNameLabel);
//        labelPanel.add(lastNameLabel);
//        labelPanel.add(streetLabel);
//        labelPanel.add(cityLabel);
//        labelPanel.add(zipLabel);
//        labelPanel.add(phoneLabel);
//        labelPanel.add(emailLabel);
//
//        GridLayout fieldPanelLayout = new GridLayout(0, 1);
//        fieldPanel.setLayout(fieldPanelLayout);
//        fieldPanel.add(firstNameField);
//        fieldPanel.add(lastNameField);
//        fieldPanel.add(streetField);
//        fieldPanel.add(cityField);
//        fieldPanel.add(zipField);
//        fieldPanel.add(phoneField);
//        fieldPanel.add(emailField);
//
//
//        // Layout of "Add Contact" Tab
//
//        GridLayout viewContactsPanel = new GridLayout(3, 0);
//        contactsPanel.setLayout(viewContactsPanel);
//        contactsPanel.add(list, BorderLayout.NORTH);
//        contactsPanel.add(rButtonPanel, BorderLayout.CENTER);
//        contactsPanel.add(pgNum, BorderLayout.SOUTH);
//
//
//
//        list.setBorder(mainBorder);
//        rButtonPanel.setBorder(mainBorder);
//
//        buttonPanelLayout = new GridLayout(2, 2);
//        rButtonPanel.setLayout(buttonPanelLayout);
//        rButtonPanel.add(edit);
//        rButtonPanel.add(remove);
//        rButtonPanel.add(prev);
//        rButtonPanel.add(next);
//
//        list.setLayout(labelPanelLayout);
//        list.add(contact1);
//        list.add(contact2);
//        list.add(contact3);
//        list.add(contact4);
//        list.add(contact5);
//
//        pgNum.add(pageNumber);
//
//        setVisible(true);
//    }
//
//    private void clearFields() {
//        firstNameField.setText("");
//        lastNameField.setText("");
//        streetField.setText("");
//        cityField.setText("");
//        zipField.setText("");
//        phoneField.setText("");
//        emailField.setText("");
//    }
//
//    public void getContacts() {
//        ArrayList<String> contactText = initializeContacts();
//
//        contact1.setText(contactText.get(count));
//        contact2.setText(contactText.get(1 + count));
//        contact3.setText(contactText.get(2 + count));
//        contact4.setText(contactText.get(3 + count));
//        contact5.setText(contactText.get(4 + count));
//    }
//
//    private int findSelected() {
//        if(contact1.isSelected()){
//            return count + 0;
//        }
//        else if(contact2.isSelected()){
//            return count + 1;
//        }
//        else if(contact3.isSelected()){
//            return count + 2;
//        }
//        else if(contact4.isSelected()){
//            return count + 3;
//        }
//        else if(contact5.isSelected()){
//            return count + 4;
//        }
//        return -1;
//    }
//
//    public void editContact(int index) {//going to need the map as input
//        MapAddress address = mapAddressList.get(index);
//        firstNameField.setText(address.map.get("firstName"));
//        lastNameField.setText(address.map.get("lastName"));
//        streetField.setText(address.map.get("street"));
//        cityField.setText(address.map.get("city"));
//        zipField.setText(address.map.get("zip"));
//        phoneField.setText(address.map.get("phone"));
//        emailField.setText(address.map.get("email"));
//    }
//
//    public void addObserver(Observer observer) {
//        observers.add(observer);
//    }
//
//    public void notifyObservers() {
//        for (Observer observer : observers) {
//            observer.saveUpdate(mapAddressList);
//        }
//    }
//
//    public ArrayList<String> initializeContacts() {
//        int contactCount = 0;
//        if (mapAddressList.size() < 5 + count) {
//            contactCount = 5 + count - mapAddressList.size();
//        }
//
//        ArrayList<String> contactText = new ArrayList<String>();
//
//        for (MapAddress address : mapAddressList) {
//            contactText.add(address.toString());
//        }
//
//        while (contactCount > 0) {
//            contactText.add("");
//            contactCount--;
//        }
//
//        return contactText;
//    }
////    public void notifyOpenObservers() {
////        for (Observer observer : observers) {
////            observer.updateOpen();
////        }
////    }
//}
