import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class FileHandle {
    private String filename = "pointer.txt";
    private String header="glColor3f(0.404f, 0.404f, 0.404f); \nglBegin(GL_POLYGON);\n";
    private String footer="glEnd();";
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
            
            String formattedX = String.format("%.2f", x);
            String formattedY = String.format("%.2f", y);
            writer.printf("glVertex2f(%.2ff, %.2ff);\n", Double.parseDouble(formattedX), Double.parseDouble(formattedY));
            
            
            
            //writer.printf("%s,%s%n","glVertex2f("+ x + "f", y + "f);");
            // System.out.println("Values saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error occurred while writing to the file: " + e.getMessage());
        }
    }

    public void deleteFile(String deleteFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(deleteFile))) {
            writer.printf("");
            // System.out.println("Values saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error occurred while writing to the file: " + e.getMessage());
        }
    }

    

}