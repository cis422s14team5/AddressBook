import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private final File ADDRESSBOOKLIST = new File("addressBooks/books.txt");

    private ImportExport importExport;
    private File file;
    private TSV tsv;
    private AllBooksView allBooksView;
    private ArrayList<ArrayList<HashMap<String, String>>> allAddressBooks;
    private ArrayList<String> addressBookList;

    /**
     * Constructor. Loads the saved address book from the TSV file and starts the allBooksView.
     */
    public Main() {
        tsv = new TSV();
        file = ADDRESSBOOKLIST;

        allAddressBooks = new ArrayList<>();

        addressBookList = read(ADDRESSBOOKLIST.toPath());

        createAllAddressBooks();

        allBooksView = new AllBooksView(addressBookList, allAddressBooks);
        allBooksView.addObserver(this);
        importExport = new ImportExport(allBooksView);
    }

    private void createAllAddressBooks() {
        for (String addressBook : addressBookList) {
            file = new File ("addressBooks/" + addressBook);

            AddressConverter convert = new AddressConverter();
            try {
                ArrayList<HashMap<String, String>> addressList = tsv.read(file);
                allAddressBooks.add(convert.standardToInternal(addressList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the contents of the addressBook to the TSV file.
     */
    private void saveTSV(File file) {
        AddressConverter convert = new AddressConverter();
        try {
            tsv.write(file, convert.internalToStandard(allBooksView.getAddressBook()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Loads the contents of the TSV file into the addressBook.
//     */
//    private void loadTSV() {
//        AddressConverter convert = new AddressConverter();
//        try {
//            tsv.read(file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        addressBook = convert.standardToInternal(tsv.addressList);
//        //allBooksView.setAddressBook(addressBook);
//    }

    private ArrayList<String> read(Path path) {
        ArrayList<String> input = new ArrayList<>();

        try {
            BufferedReader reader = Files.newBufferedReader(path, Charset.forName("US-ASCII"));
            String line;
            while ((line = reader.readLine()) != null) {
                input.add(line);
            }
            reader.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return input;
    }

    private void write(ArrayList<String> addressBookList) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(ADDRESSBOOKLIST.toPath(), Charset.forName("US-ASCII"));
            for (String string : addressBookList) {
                writer.write(string + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    /**
     * Initiates the save, import, or export of an address book. Called by the AllBooksView.
     * @param num is the switch case.
     */
    @Override
    public void update(int num) {
        switch (num) {
            case 1:  // Save
                saveTSV(allBooksView.getFile());
                break;
            case 2:  // Import
                file = importExport.importTSV();
                if (file != null) {
                    //loadTSV();
                   // saveTSV(STORE);
                    //allBooksView.setAddressBook(addressBook);
                }
                break;
            case 3:  // Export
                file = importExport.exportTSV();
                if (file != null) {
                    saveTSV(file);
                }
                break;
            case 4:  // New Book
                saveTSV(new File("addressBooks/" + allBooksView.getNewFileName()));

                addressBookList.add(allBooksView.getNewFileName());
                write(addressBookList);
                allBooksView.setAddressBookList(addressBookList);

                createAllAddressBooks();
                allBooksView.setAllAddressBooks(allAddressBooks);

                allBooksView.updateBookList();
                break;
            case 5:  // Load
                //file = allBooksView.getAddressBookFile();
                if (file != null) {
                    //loadTSV();
                    //view.setAddressBook(addressBook);
                }
                break;
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