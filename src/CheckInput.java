import javax.swing.*;

public class CheckInput {

    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField zipField;
    private Book book;

    public CheckInput(Book book, JTextField lastNameField, JTextField phoneField, JTextField zipField) {
        this.book = book;
        this.lastNameField = lastNameField;
        this.phoneField = phoneField;
        this.zipField = zipField;
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

}
