package org.example;

public class FileService {
    private final FileReaderService r;
    private final FileWriterService w;

    public FileService(FileReaderService r, FileWriterService w) {
        this.r = r;
        this.w = w;
    }

    public String processFile() {
        String d = r.read();
        w.write("Processed " + d);
        return "Processed " + d;
    }
}