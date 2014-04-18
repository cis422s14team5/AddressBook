import java.util.ArrayList;
import java.util.HashMap;

// TODO in standardToInternal, if Last.length < 3: use regex to figure out which is a state, city, and zip instead of
// arbitrarily choosing the indexes, since user could have inputted any of the three

/**
 * A class to convert addresses between the internal format and the USPS standard format.
 *
 * Internal Format:
 *      city <tab> state <tab> zip <tab> delivery <tab> second <tab> firstName <tab> lastName <tab> phone <NL>
 *
 * USPS Standard Format:
 *      Last <tab> Delivery <tab> Second <tab> Recipient <tab> Phone <NL>
 */
public class AddressConverter {

    /**
     * Converts addresses in an address book from the internal format to the USPS standard format.
     * @param addressBook the book of addresses to convert.
     * @return the converted address book.
     */
    public ArrayList<HashMap<String, String>> internalToStandard(ArrayList<HashMap<String, String>> addressBook) {
        ArrayList<HashMap<String, String>> tempBook = new ArrayList<>();

        for (HashMap<String, String> address : addressBook) {
            HashMap<String, String> tempAddress = new HashMap<>();

            tempAddress.put("Last", address.get("city") + " " + address.get("state") + " " + address.get("zip"));
            tempAddress.put("Delivery", address.get("delivery"));
            tempAddress.put("Second", address.get("second"));
            tempAddress.put("Recipient", address.get("firstName") + " " + address.get("lastName"));
            tempAddress.put("Phone", address.get("phone"));

            tempBook.add(tempAddress);
        }

        return tempBook;
    }

    /**
     * Converts addresses in an address book from the USPS standard format to the internal format.
     * @param addressBook the book of addresses to convert.
     * @return the converted address book.
     */
    public ArrayList<HashMap<String, String>> standardToInternal(ArrayList<HashMap<String, String>> addressBook) {
        ArrayList<HashMap<String, String>> tempBook = new ArrayList<>();

        for (HashMap<String, String> address : addressBook) {
            HashMap<String, String> tempAddress = new HashMap<>();

            String[] tempLast = address.get("Last").split("\\s+");

            if (tempLast.length == 0) {
                tempLast = new String[] {" ", " ", " "};
            } else if (tempLast.length == 1) {
                tempLast = new String[] {tempLast[0], " ", " "};
            } else if (tempLast.length == 2) {
                tempLast = new String[] {tempLast[0], tempLast[1], " "};
            } else {
                tempLast = new String[] {tempLast[0], tempLast[1], tempLast[2]};
            }

            String[] last = new String[] {" ", " ", " "};
            for (String string : tempLast) {
                if (checkLast(string) == 0) {
                    last[0] = string;
                }
                if (checkLast(string) == 1) {
                    last[1] = string;
                }
                if (checkLast(string) == 2) {
                    last[2] = string;
                }
            }

            String delivery = address.get("Delivery");
            String second = address.get("Second");
            String[] recipient = address.get("Recipient").split("\\s+");
            String phone = address.get("Phone");

            tempAddress.put("city", last[0]);
            tempAddress.put("state", last[1]);
            tempAddress.put("zip", last[2]);
            tempAddress.put("delivery", delivery);
            tempAddress.put("second", second);
            tempAddress.put("firstName", recipient[0]);
            tempAddress.put("lastName", recipient[1]);
            tempAddress.put("phone", phone);

            tempBook.add(tempAddress);
        }

        return tempBook;
    }

    private int checkLast(String string) {
        if (string.matches("[a-zA-Z]+")) {
            return 0;
        } else if (string.matches("[A-Z]{2}")) {
            return 1;
        } else if (string.matches("[0-9]{5}") || string.matches("[0-9]{5}[-][0-9]{4}")) {
            return 2;
        }

        return -1;
    }
}
