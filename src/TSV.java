import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A TSV processor built using methods from the Super CSV 3rd-party library. Reads and writes TSV files in the USPS
 * Standard Format.
 *
 * http://supercsv.sourceforge.net/index.html
 */
public class TSV {

    public ArrayList<HashMap<String, String>> addressList;

    /**
     * Initializes the addressList which will hold a copy of the current address book read from the read method and written
     * two from the write method.
     */
    public TSV() {
        addressList = new ArrayList<>();
    }

    /**
     * Creates and returns the cell processors that are used in read and write methods.  The code below is a
     * modification of the default CsvMapReader implementation provided by the developer. It has been modified
     * to suit the context.
     */
    private CellProcessor[] getProcessors() {
        return new CellProcessor[] {
            new NotNull(), // Last
            new NotNull(), // Delivery
            new NotNull(), // Second
            new NotNull(), // Recipient
            new NotNull() // Phone
        };
    }

    /**
     * Reads a tsv file using CsvMapReader. The code below is a modification of the default CsvMapReader implementation
     * provided by the developer. It has been modified to suit the context.
     */
    public ArrayList<HashMap<String, String>> read(File file) {
        ArrayList<HashMap<String, String>> addressList = new ArrayList<>();
        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new FileReader(file), CsvPreference.TAB_PREFERENCE);

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader(true);

            final CellProcessor[] processors =  new CellProcessor[header.length];
            for (int i = 0; i < header.length; i++) {
                if (header[i] != null) {
                    processors[i] = new NotNull();
                }
            }

            Map<String, Object> address;
            while ((address = mapReader.read(header, processors)) != null) {
                addressList.add((HashMap) address);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mapReader != null) {
                try {
                    mapReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return addressList;
    }

    /**
     * Writes to a tsv file using CsvMapWriter. The code below is a modification of the default CsvMapWriter
     * implementation provided by the developer. It has been modified to suit the context.
     */
    public void write(File file, ArrayList<HashMap<String, String>> mapList) {
        final String[] header = new String[] {"Last", "Delivery", "Second", "Recipient", "Phone",
                null, null, null, null, null, null, null};
        this.addressList = mapList;

        ICsvMapWriter mapWriter = null;
        try {
            mapWriter = new CsvMapWriter(new FileWriter(file), CsvPreference.TAB_PREFERENCE);

            final CellProcessor[] processors = {
                    new NotNull(), // Last
                    new NotNull(), // Delivery
                    new NotNull(), // Second
                    new NotNull(), // Recipient
                    new NotNull(),  // Phone
                    null, null, null, null, null, null, null
            };;

            // write the header
            mapWriter.writeHeader(header);

            for (HashMap<String, String> address : this.addressList) {
                // write the addressList
                mapWriter.write(address, header, processors);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mapWriter != null) {
                try {
                    mapWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
