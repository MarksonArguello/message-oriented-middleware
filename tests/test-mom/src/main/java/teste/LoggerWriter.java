package teste;



public class LoggerWriter {

    private FileWriter fileWriter;

    public LoggerWriter(String fileName) {
        this.fileWriter = new FileWriter(fileName);
    }

    public void info(String log) {
        // Write the log to the file
        System.out.println(log);
        fileWriter.write(log);
    }

    public void close() {
        fileWriter.close();
    }
}
