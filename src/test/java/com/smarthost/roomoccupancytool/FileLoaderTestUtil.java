package com.smarthost.roomoccupancytool;

import lombok.SneakyThrows;

import java.io.File;

public final class FileLoaderTestUtil {

    @SneakyThrows
    public static File loadResourceFile(String fileName) {
        return new File(FileLoaderTestUtil.class.getClassLoader().getResource(fileName).toURI());
    }

    private FileLoaderTestUtil() {
    }
}
