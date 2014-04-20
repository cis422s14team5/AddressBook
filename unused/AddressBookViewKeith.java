//import javax.swing.*;
//import javax.swing.border.Border;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class AddressBookViewKeith extends JFrame {
//
//    private JTextField firstNameField;
//    private JTextField lastNameField;
//    private JTextField streetField;
//    private JTextField cityField;
//    private JTextField zipField;
//    private JTextField phoneField;
//    private JTextField emailField;
//
//    public HashMap<String, String> addressMap;
//
//    private ArrayList<Observer> observers;
//
//    private JPanel addressPanel;
//
//    public AddressBookViewKeith() {
//        observers = new ArrayList<Observer>();
//
//        // Window
//        setTitle("Add/AllBooks Address");
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setSize(300, 300);
//        setResizable(false);
//        setLocationRelativeTo(null);
//
//        // Panels
//        JPanel addressBookPanel = new JPanel();
//        addressPanel = new JPanel();
//        JPanel buttonPanel = new JPanel();
//        JPanel labelPanel = new JPanel();
//        JPanel fieldPanel = new JPanel();
//
//        // Labels
//        JLabel firstNameLabel = new JLabel("First Name");
//        JLabel lastNameLabel = new JLabel("Last Name");
//        JLabel streetLabel = new JLabel("Street");
//        JLabel cityLabel = new JLabel("City");
//        JLabel zipLabel = new JLabel("Zip");
//        JLabel phoneLabel = new JLabel("Phone");
//        JLabel emailLabel = new JLabel("Email");
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
//        // Buttons
//        JButton save = new JButton("Save");
//        save.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                addressMap = new HashMap<String, String>();
//                addressMap.put("firstName", firstNameField.getText());
//                addressMap.put("lastName", lastNameField.getText());
//                addressMap.put("street", streetField.getText());
//                addressMap.put("city", cityField.getText());
//                addressMap.put("zip", zipField.getText());
//                addressMap.put("phone", phoneField.getText());
//                addressMap.put("email", emailField.getText());
//                clearFields();
//                JOptionPane.showMessageDialog(null, "Saved!");
//                notifyObservers();
//            }
//        });
//
//        JButton clear = new JButton("Clear");
//        clear.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                clearFields();
//            }
//        });
//
//        JButton open = new JButton("Open");
//        open.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                notifyOpenObservers();
//                firstNameField.setText(addressMap.get("firstName"));
//                lastNameField.setText(addressMap.get("lastName"));
//                streetField.setText(addressMap.get("street"));
//                cityField.setText(addressMap.get("city"));
//                zipField.setText(addressMap.get("zip"));
//                phoneField.setText(addressMap.get("phone"));
//                emailField.setText(addressMap.get("email"));
//            }
//        });
//
//        JButton back = new JButton("Back");
//        open.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                changeView();
//            }
//        });
//
//        // Layout
//        add(addressPanel);
//        add(BorderLayout.SOUTH, buttonPanel);
//
//        Border mainBorder = BorderFactory.createEmptyBorder(0, 10, 10, 10);
//        addressPanel.setBorder(mainBorder);
//        buttonPanel.setBorder(mainBorder);
//
//        GridLayout buttonPanelLayout = new GridLayout(0, 4);
//        buttonPanel.setLayout(buttonPanelLayout);
//        buttonPanel.add(clear);
//        buttonPanel.add(open);
//        buttonPanel.add(save);
//        buttonPanel.add(back);
//
//        GridLayout mainPanelLayout = new GridLayout(0, 2);
//        addressPanel.setLayout(mainPanelLayout);
//        addressPanel.add(labelPanel);
//        addressPanel.add(fieldPanel);
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
//        setVisible(true);
//    }
//
//    private void changeView() {
//        remove(addressPanel);
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
//    public void addObserver(Observer observer) {
//        observers.add(observer);
//    }
//
//    public void notifyObservers() {
//        for (Observer observer : observers) {
//            observer.saveUpdate(addressMap);
//        }
//    }
//
//    public void notifyOpenObservers() {
//        for (Observer observer : observers) {
//            observer.updateOpen();
//        }
//    }
//}
