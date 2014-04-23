import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Gets information from the contact tab and verifies key elements to be correct and valid information before saving.
 */
public class CheckInput {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField zipField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField emailField;
    private Book book;
    private HashMap<String, String> stateMap;

    public CheckInput(Book book, JTextField firstNameField, JTextField lastNameField, JTextField phoneField, JTextField zipField,
                      JTextField cityField, JTextField stateField, JTextField emailField) {
        this.book = book;
        this.firstNameField = firstNameField;
        this.lastNameField = lastNameField;
        this.phoneField = phoneField;
        this.zipField = zipField;
        this.cityField = cityField;
        this.stateField = stateField;
        this.emailField = emailField;
        stateMap = new HashMap<>();
        initializeStateMap();
    }

    /**
     * Checks the last name field for valid input.
     * @return true if valid.
     */
    public boolean checkLastName() {
        boolean valid = false;
        if (!lastNameField.getText().equals("")) {
            valid = true;
        } else {
            JOptionPane.showMessageDialog(book, "Please enter a last name.");
        }

        return valid;
    }

    /**
     * Checks the phone field for valid input.
     * @return true if valid.
     */
    public boolean checkPhone() {
        boolean valid = false;
        if (!phoneField.getText().equals("") &&
                phoneField.getText().matches("^[0-9]{10}$") ||                              // ##########
                phoneField.getText().matches("^[(][0-9]{3}[)][ ][0-9]{3}[-][0-9]{4}$") ||   // (###) ###-####
                phoneField.getText().matches("^[(][0-9]{3}[)][ ][0-9]{3}[.][0-9]{4}$") ||   // (###) ###.####
                phoneField.getText().matches("^[(][0-9]{3}[)][0-9]{3}[-][0-9]{4}$") ||      // (###)###-####
                phoneField.getText().matches("^[(][0-9]{3}[)][0-9]{3}[.][0-9]{4}$") ||      // (###)###.####
                phoneField.getText().matches("^[0-9]{3}[-][0-9]{3}[-][0-9]{4}$") ||         // ###-###-####
                phoneField.getText().matches("^[0-9]{3}[.][0-9]{3}[.][0-9]{4}$") ||         // ###.###.####
                phoneField.getText().matches("^[0-9]{7}$") ||                               // #######
                phoneField.getText().matches("^[0-9]{3}[.][0-9]{4}$") ||                    // ###.####
                phoneField.getText().matches("^[0-9]{3}[-][0-9]{4}$")) {                    // ###-####
            valid = true;
        } else if (phoneField.getText().equals("")) {
            valid = true;
        } else {
            JOptionPane.showMessageDialog(book, "You did not enter a valid phone number. Please try again.");
        }

        return valid;
    }

    /**
     * Checks the zip field for valid input.
     * @return true if valid.
     */
    public boolean checkZip() {
        boolean valid = false;
        if (!zipField.getText().equals("") &&
                zipField.getText().matches("^[0-9]{5}$") ||
                zipField.getText().matches("^[0-9]{5}[-][0-9]{4}$")) {
            valid = true;
        } else if (zipField.getText().equals("")) {
            valid = true;
        } else {
            JOptionPane.showMessageDialog(book, "You did not enter a valid zip code. Please try again.");
        }

        return valid;
    }

    /**
     * Initializes the HashMap of states with the names of the states and their respective state codes.
     */
    private void initializeStateMap() {
        stateMap.put("Alabama", "AL");
        stateMap.put("Alaska", "AK");
        stateMap.put("Arizona", "AZ");
        stateMap.put("Arkansas", "AR");
        stateMap.put("California", "CA");
        stateMap.put("Colorado", "CO");
        stateMap.put("Connecticut", "CT");
        stateMap.put("Delaware", "DE");
        stateMap.put("District of Columbia", "DC");
        stateMap.put("Florida", "FL");
        stateMap.put("Georgia", "GA");
        stateMap.put("Hawaii", "HI");
        stateMap.put("Idaho", "ID");
        stateMap.put("Illinois", "IL");
        stateMap.put("Indiana", "IN");
        stateMap.put("Iowa", "IA");
        stateMap.put("Kansas", "KS");
        stateMap.put("Kentucky", "KY");
        stateMap.put("Louisiana", "LA");
        stateMap.put("Maine", "ME");
        stateMap.put("Montana", "MT");
        stateMap.put("Nebraska", "NE");
        stateMap.put("Nevada", "NV");
        stateMap.put("New Hampshire", "NH");
        stateMap.put("New Jersey", "NJ");
        stateMap.put("New Mexico", "NM");
        stateMap.put("New York", "NY");
        stateMap.put("North Carolina", "NC");
        stateMap.put("North Dakota", "ND");
        stateMap.put("Ohio", "OH");
        stateMap.put("Oklahoma", "OK");
        stateMap.put("Oregon", "OR");
        stateMap.put("Maryland", "MD");
        stateMap.put("Massachusetts", "MA");
        stateMap.put("Michigan", "MI");
        stateMap.put("Minnesota", "MN");
        stateMap.put("Mississippi", "MS");
        stateMap.put("Missouri", "MO");
        stateMap.put("Pennsylvania", "PA");
        stateMap.put("Rhode Island", "RI");
        stateMap.put("South Carolina", "SC");
        stateMap.put("South Dakota", "SD");
        stateMap.put("Tennessee", "TN");
        stateMap.put("Texas", "TX");
        stateMap.put("Utah", "UT");
        stateMap.put("Vermont", "VT");
        stateMap.put("Virginia", "VA");
        stateMap.put("Washington", "WA");
        stateMap.put("West Virginia", "WV");
        stateMap.put("Wisconsin", "WI");
        stateMap.put("Wyoming", "WY");
    }

    /**
     * Checks the state field for valid input.
     * @return true if valid.
     */
    public boolean checkState(){
        boolean valid = false;
        if (stateMap.containsKey(stateField.getText()) ||
                stateMap.containsValue(stateField.getText()) ||
                stateField.getText().equals("")){
            valid = true;
        } else {
            JOptionPane.showMessageDialog(book, "Please enter a valid US state.");
        }

        return valid;
    }

    /**
     * Checks the state field for whether it was a full state name, or its state code. If it is a full state name
     * change it into its state code.
     * @param state is the string to transform.
     * @return a string that is the correct state code.
     */
    public String transformState(String state){
        String newState = state;
        if (stateMap.containsKey(state)){
            for(Map.Entry<String, String> entry: stateMap.entrySet()) {
                if (entry.getKey().equals(state)){
                    newState = entry.getValue();
                }
            }
        }
        return newState;
    }

    /**
     * Checks the email field for valid input.
     * @return true if valid.
     */
    public boolean checkEmail(){
        boolean valid = false;
        if (!emailField.getText().equals("") &&
                emailField.getText().matches("^[a-zA-Z0-9.-_,]+[@]{1}[a-zA-Z0-9]+[.]{1}[a-zA-Z0-9]+$")) {
            valid = true;
        } else if (emailField.getText().equals("")) {
            valid = true;
        } else {
            JOptionPane.showMessageDialog(book, "You did not enter a valid Email. Please try again.");
        }
        return valid;
    }

    /**
     * Checks the city field for valid input.
     * @return true if valid.
     */
    public boolean checkFirstName() {
        boolean valid = false;
        if (!firstNameField.getText().equals("") && firstNameField.getText().matches("[a-zA-Z]+")) {
            valid = true;
        } else if (firstNameField.getText().equals("")) {
            valid = true;
        } else {
            JOptionPane.showMessageDialog(book, "You did not enter a valid first name. Please try again.");
        }
        return valid;
    }

//    /**
//     * Checks the city field for valid input.
//     * @return true if valid.
//     */
//    public boolean checkCity() {
//        boolean valid = false;
//        if (!cityField.getText().equals("") && cityField.getText().matches("[a-zA-Z\\s]")) {
//            valid = true;
//        } else if (cityField.getText().equals("")) {
//            valid = true;
//        } else {
//            JOptionPane.showMessageDialog(book, "You did not enter a valid city. Please try again.");
//        }
//        return valid;
//    }
}