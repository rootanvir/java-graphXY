import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class FileHandle {
    private String filename = "pointer.txt";

    public FileHandle() {
        this.filename = "pointer.txt";
    }
    public String readWholeFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            System.err.println("Error occurred while reading from the file: " + e.getMessage());
            return null;
        }
    }

    public void saveDoubleValues() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.printf("");
            // System.out.println("Values saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error occurred while writing to the file: " + e.getMessage());
        }
    }

    public void saveDoubleValues(double x, double y) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {

            writer.printf("%s,%s%n", x + "f", y + "f");
            // System.out.println("Values saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error occurred while writing to the file: " + e.getMessage());
        }
    }

    public void deleteFile(String deleteFile) {
        File file = new File(deleteFile);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully: " + deleteFile);
            } else {
                System.err.println("Unable to delete the file: " + deleteFile);
            }
        } else {
            System.err.println("File doesn't exist: " + deleteFile);
        }
    }

    

}
