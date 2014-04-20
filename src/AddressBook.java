import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// TODO get the current store from the address books file.
// TODO export address books to a default folder
// TODO encrypt the STORE
// TODO add button to save the imported address book as the default address book?

/**
 * AddressBook is an address management program. It takes addresses in USPS standard format and stores them in an
 * address book.
 */
public class AddressBook implements Observer {

    private final File BOOKS = new File("addressBooks/books.txt");

    //private ImportExport importExport;
    private TSV tsv;
    private AllBooksView allBooksView;
    private ArrayList<ArrayList<HashMap<String, String>>> allAddressBooks;
    private ArrayList<String> addressBookList;
    private ReadWrite readWrite;

    /**
     * Constructor. Loads the saved address book from the TSV file and starts the allBooksView.
     */
    public AddressBook() {
        tsv = new TSV();
        readWrite = new ReadWrite();
        addressBookList = readWrite.read(BOOKS.toPath());

        createAllAddressBooks();

        allBooksView = new AllBooksView(addressBookList, allAddressBooks);
        allBooksView.addObserver(this);
        //importExport = new ImportExport(allBooksView);
    }

    private void createAllAddressBooks() {
        allAddressBooks = new ArrayList<>();

        for (String addressBook : addressBookList) {
            File file = new File ("addressBooks/" + addressBook + ".tsv");

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
     * Initiates the save, import, or export of an address book. Called by the AllBooksView.
     * @param num is the switch case.
     */
    @Override
    public void update(int num) {
        File file;

        switch (num) {
            case 1:  // Save
                tsv.saveTSV(new File("addressBooks/" + allBooksView.getNewFileName() + ".tsv"),
                        allBooksView.getAddressBook());
                break;
            case 2:  // Import
//                file = importExport.importTSV();
//                if (file != null) {
//                    tsv.loadTSV(file);
//                }
                break;
            case 3:  // Export
//                file = importExport.exportTSV();
//                if (file != null) {
//                    tsv.saveTSV(file, allBooksView.getAddressBook());
//                }
                break;
            case 4:  // New Book
                tsv.saveTSV(new File("addressBooks/" + allBooksView.getNewFileName() + ".tsv"),
                        allBooksView.getAddressBook());

                addressBookList.add(allBooksView.getNewFileName());
                Collections.sort(addressBookList);
                readWrite.write(BOOKS.toPath(), addressBookList);
                allBooksView.setAddressBookList(addressBookList);

                createAllAddressBooks();
                allBooksView.setAllAddressBooks(allAddressBooks);

                allBooksView.updateBookList();
                break;
            default:
                break;
        }
    }



    /**
     * AddressBook method.
     * @param args is the command line arguments array.
     */
    public static void main(String[] args) {
        new AddressBook();
    }
}