import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

// TODO get the current store from the address books file.
// TODO export address books to a default folder
// TODO encrypt the STORE
// TODO add button to save the imported address book as the default address book?

/**
 * AddressBook is an address management program. It takes addresses in USPS standard format and stores them in an
 * address book.
 */
public class Main implements Observer {

    private final File STORE = new File("store.tsv");

    private TSV tsv;
    private View view;
    private ImportExport importExport;
    private AddressConverter convert;
    private File file;
    private ArrayList<HashMap<String, String>> addressBook;

    /**
     * Constructor. Loads the saved address book from the TSV file and starts the view.
     */
    public Main() {
        tsv = new TSV();
        convert = new AddressConverter();
        file = STORE;
        loadTSV();
        view = new View(addressBook);
        view.addObserver(this);
        importExport = new ImportExport(view);
    }

    /**
     * Saves the contents of the addressBook to the TSV file.
     */
    private void saveTSV(File file) {
        try {
            tsv.write(file, convert.internalToStandard(addressBook));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the contents of the TSV file into the addressBook.
     */
    private void loadTSV() {
        try {
            tsv.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addressBook = convert.standardToInternal(tsv.addressList);
    }

    /**
     * Initiates the save, import, or export of an address book. Called by the View.
     * @param num is the switch case.
     */
    @Override
    public void update(int num) {
        switch (num) {
            case 1:  // Save
                addressBook = view.getAddressBook();
                saveTSV(file);
                break;
            case 2:  // Import
                file = importExport.importTSV();
                if (file != null) {
                    loadTSV();
                    saveTSV(STORE);
                    view.setAddressBook(addressBook);
                }
                break;
            case 3:  // Export
                file = importExport.exportTSV();
                if (file != null) {
                    saveTSV(file);
                }
            default:
                break;
        }
    }

    /**
     * Main method.
     * @param args is the command line arguments array.
     */
    public static void main(String[] args) {
        new Main();
    }
}