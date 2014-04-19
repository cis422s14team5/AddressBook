
/**
 * Interface for the observer part of the Observer Pattern.
 */
public interface Observer {

    /**
     * Used by AddressBook to receive updates from the AllBooksView.
     */
    public void update(int num);
}
