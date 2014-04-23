import java.util.ArrayList;
import java.util.HashMap;

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

    String[] tempLast;
    String city;
    ArrayList<String> tempCity;

    /**
     * Converts addresses in an address book from the internal format to the USPS standard format.
     * @param addressBook the book of addresses to convert.
     * @return the converted address book.
     */
    public ArrayList<HashMap<String, String>> internalToStandard(ArrayList<HashMap<String, String>> addressBook) {
        ArrayList<HashMap<String, String>> tempBook = new ArrayList<>();

        for (HashMap<String, String> address : addressBook) {
            HashMap<String, String> tempAddress = new HashMap<>();

            String last = null;
            if (address.get("city") == null && address.get("state") == null && address.get("zip") == null) {
                last = null;
            } else if (address.get("city") == null && address.get("state") == null) {
                last = " " + " " + address.get("zip");
            } else if (address.get("city") == null && address.get("zip") == null) {
                last = " " + address.get("state") + " ";
            } else if (address.get("zip") == null && address.get("state") == null) {
                last = address.get("city") + " " + " ";
            } else if (address.get("city") == null) {
                last =  " " + address.get("state") + " " + address.get("zip");
            }  else if (address.get("state") == null) {
                last = address.get("city") + " " + address.get("zip");
            } else if (address.get("zip") == null) {
                last = address.get("city") + " " + address.get("state") + " ";
            } else {
                last = address.get("city") + " " + address.get("state") + " " + address.get("zip");
            }

            tempAddress.put("Last", last);
            tempAddress.put("Delivery", address.get("delivery"));
            tempAddress.put("Second", address.get("second"));
            tempAddress.put("Recipient", address.get("firstName") + " " + address.get("lastName"));
            tempAddress.put("Phone", address.get("phone"));
            tempAddress.put("Email", address.get("email"));
            tempAddress.put("Note", address.get("note"));

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
            ArrayList<String> last = new ArrayList<>();

            if (address.get("Last") != null) {
                tempLast = address.get("Last").split("\\s+");

                for (int i = 0; i < 10; i++) {
                    last.add("");
                }

                int size = tempLast.length;
                tempCity = new ArrayList<>();
                for (int i = size - 1; i >= 0; i--) {
                    String string = tempLast[i];
                    if (string != null && !string.equals("")) {
                        if (checkLast(string) == 0) {
                            last.set(0, "");
                            last.set(1, "");
                            last.set(2, "");
                        }
                        if (checkLast(string) == 3) {
                            last.set(2, string);
                        }
                        if (checkLast(string) == 2) {
                            last.set(1, string);
                        }
                        if (checkLast(string) == 1) {
                            tempCity.add(string);
                        }
                    }
                }

                city = "";
                for (int i = tempCity.size() - 1; i >= 0; i--) {
                    city += tempCity.get(i);
                    if (i != 0) {
                        city += " ";
                    }
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    last.set(i, "");
                }
            }

            String delivery = address.get("Delivery");
            String second = address.get("Second");
            String[] recipient = address.get("Recipient").split("\\s+");
            String phone = address.get("Phone");
            String email = address.get("Email");
            String note = address.get("Note");


            tempAddress.put("city", city);
            tempAddress.put("state", last.get(1));
            tempAddress.put("zip", last.get(2));
            tempAddress.put("delivery", delivery);
            tempAddress.put("second", second);
            tempAddress.put("firstName", recipient[0]);
            tempAddress.put("lastName", recipient[1]);
            tempAddress.put("phone", phone);
            tempAddress.put("email", email);
            tempAddress.put("note", note);

            tempBook.add(tempAddress);
        }

        return tempBook;
    }

    private int checkLast(String string) {
        if (string == null) {
            return 0;
        } else if (string.matches("[A-Z]{2}")) {
            return 2;
        } else if (string.matches("[a-zA-Z]+") && !(string.matches("[n][u][l][l]"))) {
            return 1;
        } else if (string.matches("[0-9]{5}") || string.matches("[0-9]{5}[-][0-9]{4}")) {
            return 3;
        }

        return -1;
    }
}
