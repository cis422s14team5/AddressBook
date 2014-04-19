import java.io.*;
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
public class AddressBook implements Observer {

    private final File BOOKS = new File("addressBooks/books.txt");

    private ImportExport importExport;
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
        importExport = new ImportExport(allBooksView);
    }

    private void createAllAddressBooks() {
        allAddressBooks = new ArrayList<>();

        for (String addressBook : addressBookList) {
            File file = new File ("addressBooks/" + addressBook);

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
                tsv.saveTSV(allBooksView.getFile(), allBooksView.getAddressBook());
                break;
            case 2:  // Import
                file = importExport.importTSV();
                if (file != null) {

                    /*
                    1. read file
                    2. write file into addressBooks
                    3. add name to


                     */
                    //AddressConverter convert = new AddressConverter();
                    tsv.loadTSV(file);
                    tsv.saveTSV(new File("addressBooks/" + file.getName()), allBooksView.getAddressBook());
                    addressBookList.add(file.getName());
                    readWrite.write(BOOKS.toPath(), addressBookList);
                    allBooksView.setAddressBookList(addressBookList);

                    createAllAddressBooks();
                    allBooksView.setAllAddressBooks(allAddressBooks);

                    allBooksView.updateBookList();

                   // saveTSV(STORE);
                    //allBooksView.setAddressBook(addressBook);
                }
                break;
            case 3:  // Export
                file = importExport.exportTSV();
                if (file != null) {
                    tsv.saveTSV(file, allBooksView.getAddressBook());
                }
                break;
            case 4:  // New Book
                tsv.saveTSV(new File("addressBooks/" + allBooksView.getNewFileName()), allBooksView.getAddressBook());

                addressBookList.add(allBooksView.getNewFileName());
                readWrite.write(BOOKS.toPath(), addressBookList);
                allBooksView.setAddressBookList(addressBookList);

                createAllAddressBooks();
                allBooksView.setAllAddressBooks(allAddressBooks);

                allBooksView.updateBookList();
                break;
            case 5:  // Load
//                //file = allBooksView.getAddressBookFile();
//                if (file != null) {
//                    //loadTSV();
//                    //view.setAddressBook(addressBook);
//                }
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