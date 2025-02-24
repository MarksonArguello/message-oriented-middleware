package teste;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class FileWriter {
    private FileOutputStream fileOut;

    public FileWriter(String path) {
        // Create the file
        File file = new File(path);
        try {
            if (file.exists())
                file.delete();

            file.createNewFile();
            fileOut = new FileOutputStream(path, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void write(String txt) {
        try {
            txt += "\n";
            this.fileOut.write(txt.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
