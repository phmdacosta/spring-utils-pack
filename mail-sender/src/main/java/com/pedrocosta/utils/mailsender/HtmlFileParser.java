package com.pedrocosta.utils.mailsender;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HtmlFileParser extends FileParser {

    private String content;
    private Map<String, String> params;

    public HtmlFileParser(String htmlFilePath) {
        super(htmlFilePath);
    }

    public HtmlFileParser(File file) throws IOException {
        super(file);
    }

    public HtmlFileParser(InputStream inputStream) {
        super(inputStream);
        this.params = new HashMap<>();
        this.content = getRawContent();
    }

    @Override
    public String read() {
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            String paramHtmlName = "${" + entry.getKey() + "}";
            if (this.content.contains(paramHtmlName)) {
                paramHtmlName = paramHtmlName.replace("$", "\\$").replace("{", "\\{");
                this.content = this.content.replaceAll(paramHtmlName, entry.getValue());
            }
        }
        return this.content;
    }

    public HtmlFileParser addParameter(String name, String value) {
        this.params.put(name, value);
        return this;
    }
}
