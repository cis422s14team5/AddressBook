import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Import and export address books.
 */
public class ImportExport {

    private FileDialog fileDialog;

    /**
     * Constructor.
     * @param book is the window over which the file dialog is created.
     */
    public ImportExport(JFrame book) {
        fileDialog = new FileDialog(book);
        fileDialog.setFilenameFilter(new TSVFilter());
    }

    /**
     * Launches the load file dialog and tells AddressBook to import the selected file.
     * @return the file to import.
     */
    public File importTSV() {
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() != null) {
            String file = fileDialog.getDirectory() + fileDialog.getFile();
            return new File(file);
        } else {
            return null;
        }
    }

    /**
     * Launches the save file dialog dialog and tells AddressBook to export the selected file.
     * @return the file to export.
     */
    public File exportTSV() {
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setFile("untitled.tsv");
        fileDialog.setVisible(true);
        if (fileDialog.getFile() != null) {
            String file = fileDialog.getDirectory() + fileDialog.getFile();
            return new File(file);
        } else {
            return null;
        }
    }
}
