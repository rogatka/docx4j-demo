package com.example.docx4j.utils;

import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.util.Objects;

@UtilityClass
public class FileUtils {

    public static InputStream getFileContentAsStream(String filePath) {
        Objects.requireNonNull(filePath);
        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        Objects.requireNonNull(inputStream);
        return inputStream;
    }
}
