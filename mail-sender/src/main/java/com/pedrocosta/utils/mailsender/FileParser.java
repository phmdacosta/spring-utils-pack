package com.pedrocosta.utils.mailsender;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

public class FileParser {
    private InputStream stream;

    public FileParser(String htmlFilePath) {
        this(FileParser.class.getClassLoader().getResourceAsStream(htmlFilePath));
    }

    public FileParser(File file) throws IOException {
        this(Files.newInputStream(file.toPath()));
    }

    public FileParser(InputStream inputStream) {
        this.stream = inputStream;
    }

    public String read() {
        return getRawContent();
    }

    protected String getRawContent() {
        String result = null;
        if (this.stream != null) {
            Scanner s = new Scanner(this.stream).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : null;
        }
        return result;
    }
}
