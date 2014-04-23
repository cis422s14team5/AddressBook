import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.HashMap;

public class PrintBook implements Printable {

    private ArrayList<HashMap<String, String>> addressBook;
    int[] pageBreaks;  // array of page break line positions.
    String[] textLines;

    public PrintBook(ArrayList<HashMap<String, String>> addressBook) {
        this.addressBook = addressBook;
        initTextLines();
    }

    private void initTextLines() {
        ArrayList<String> textTemp = new ArrayList<>();

        for (HashMap<String, String> address : addressBook) {
            if (address.get("firstName") != null && !address.get("firstName").equals("")) {
                textTemp.add(address.get("firstName") + " " + address.get("lastName"));
            } else {
                textTemp.add(address.get("lastName"));
            }
            if (address.get("delivery") != null && !address.get("delivery").equals("")) {
                textTemp.add(address.get("delivery"));
            }
            if (address.get("second") != null && !address.get("second").equals("")
                                              && !address.get("second").equals(" ")) {
                textTemp.add(address.get("second"));
            }


            if (address.get("city") != null && !address.get("city").equals("") && // All available
                    address.get("state") != null && !address.get("state").equals("") &&
                    address.get("zip") != null && !address.get("zip").equals("")) {
                textTemp.add(address.get("city") + " " +  address.get("state") + " " +  address.get("zip"));
            } else if (address.get("city") != null && !address.get("city").equals("") && // No zip
                    address.get("state") != null && !address.get("state").equals("") &&
                    address.get("zip") == null || address.get("zip").equals("")) {
                textTemp.add(address.get("city") + " " +  address.get("state"));
            } else if (address.get("city") == null || address.get("city").equals("") && // No city
                    address.get("state") != null && !address.get("state").equals("") &&
                    address.get("zip") != null && !address.get("zip").equals("")) {
                textTemp.add(address.get("state") + " " + address.get("zip"));
            } else if (address.get("city") != null && !address.get("city").equals("") && // No state
                    address.get("state") == null || address.get("state").equals("") &&
                    address.get("zip") != null && !address.get("zip").equals("")) {
                if (address.get("city") == null || address.get("city").equals("")) {
                    textTemp.add(address.get("zip"));
                } else {
                    textTemp.add(address.get("city") + " " + address.get("zip"));
                }
            } else if (address.get("city") != null && !address.get("city").equals("") && // No state or zip
                    address.get("state") == null || address.get("state").equals("") &&
                    address.get("zip") == null || address.get("zip").equals("")) {
                textTemp.add(address.get("city"));
            } else if (address.get("city") == null || address.get("city").equals("") && // No state or city
                    address.get("state") == null || address.get("state").equals("") &&
                    address.get("zip") != null && !address.get("zip").equals("")) {
                textTemp.add(address.get("zip"));
            } else if (address.get("city") == null || address.get("city").equals("") && // No city or zip
                    address.get("state") != null && !address.get("state").equals("") &&
                    address.get("zip") == null || address.get("zip").equals("")) {
                textTemp.add(address.get("state"));
            } else if (address.get("city") == null || address.get("city").equals("") && // No city, state, or zip
                    address.get("state") == null || address.get("state").equals("") &&
                    address.get("zip") == null || address.get("zip").equals("")) {
                textTemp.add(address.get(" "));
            }
            textTemp.add("");
        }

        textLines = textTemp.toArray(new String[textTemp.size()]);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Font font = new Font("Serif", Font.PLAIN, 10);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int lineHeight = metrics.getHeight();

        if (pageBreaks == null) {
            initTextLines();
            int linesPerPage = (int)(pageFormat.getImageableHeight()/lineHeight);
            int numBreaks = (textLines.length - 1) / linesPerPage;
            pageBreaks = new int[numBreaks];
            for (int b = 0; b < numBreaks; b++) {
                pageBreaks[b] = (b + 1) * linesPerPage;
            }
        }

        if (pageIndex > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }

        // User (0,0) is typically outside the
        // imageable area, so we must translate
        // by the X and Y values in the PageFormat
        // to avoid clipping.
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         */
        int y = 0;
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
        int end   = (pageIndex == pageBreaks.length)
                ? textLines.length : pageBreaks[pageIndex];
        for (int line=start; line < end; line++) {
            y += lineHeight;
            graphics.drawString(textLines[line], 0, y);
        }

        // tell the caller that this page is part
        // of the printed document
        return PAGE_EXISTS;
    }
}
