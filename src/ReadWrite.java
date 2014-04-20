import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ReadWrite {

    protected ArrayList<String> read(Path path) {
        ArrayList<String> input = new ArrayList<>();

        try {
            BufferedReader reader = Files.newBufferedReader(path, Charset.forName("US-ASCII"));
            String line;
            while ((line = reader.readLine()) != null) {
                input.add(line.substring(0, line.length() - 4));
            }
            reader.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return input;
    }

    protected void write(Path path, ArrayList<String> addressBookList) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("US-ASCII"));
            for (String string : addressBookList) {
                writer.write(string + ".tsv\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
