import java.awt.*;
import java.io.File;

/**
 * Import and export address books.
 */
public class ImportExport {

    private FileDialog fileDialog;

    /**
     * Constructor.
     * @param allBooksView is the window over which the file dialog is created.
     */
    public ImportExport(AllBooksView allBooksView) {
        fileDialog = new FileDialog(allBooksView);
        fileDialog.setFilenameFilter(new TSVFilter());
    }

    /**
     * Launches the load file dialog and tells Main to import the selected file.
     * @return the file to import.
     */
    public File importTSV() {
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() != null) {
            return new File(fileDialog.getFile());
        } else {
            return null;
        }
    }

    /**
     * Launches the save file dialog dialog and tells Main to export the selected file.
     * @return the file to export.
     */
    public File exportTSV() {
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() != null) {
            return new File(fileDialog.getFile());
        } else {
            return null;
        }
    }
}
