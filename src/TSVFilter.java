import java.io.File;
import java.io.FilenameFilter;

/**
 * Filters TSV files for the address book's file dialog.
 */
public class TSVFilter implements FilenameFilter {

    public final static String tsv = "tsv";

    /**
     * Allows only TSV files to be selected in the FileDialog.
     * @param dir is the file's directory.
     * @param name is the file's name.
     * @return true if the file is selectable and false if it is not.
     */
    @Override
    public boolean accept(File dir, String name) {
        String extension = getExtension(name);
        return extension != null && (extension.equals(tsv));
    }

    /**
     * Gets the extension from a file name string.
     * @param fileName is the file for which an extension must be retrieved.
     * @return the extension retrieved.
     */
    public static String getExtension(String fileName) {
        String extension = null;
        int i = fileName.lastIndexOf('.');

        if (i > 0 &&  i < fileName.length() - 1) {
            extension = fileName.substring(i+1).toLowerCase();
        }
        return extension;
    }


}
