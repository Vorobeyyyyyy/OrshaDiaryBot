package com.alexander.orshadiarybot.parser;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class HtmlParserTest {
    private HtmlParser htmlParser = new HtmlParser();
    private String rawHtml;

    {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            rawHtml = readFromInputStream(inputStream);
        } catch (IOException e) {
        }
    }

    @Test
    void parseMarks() {
        System.out.println(htmlParser.parseMarks(rawHtml));
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}