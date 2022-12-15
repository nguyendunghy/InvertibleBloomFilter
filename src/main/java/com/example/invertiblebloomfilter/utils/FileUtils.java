package com.example.invertiblebloomfilter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {

    public static String readFile(String filePath) {
        try {
            byte[] data = Files.readAllBytes(new File(filePath).toPath());
            return new String(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
