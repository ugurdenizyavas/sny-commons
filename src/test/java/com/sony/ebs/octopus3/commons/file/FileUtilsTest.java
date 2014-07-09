package com.sony.ebs.octopus3.commons.file;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Test
    public void writeFile() throws Exception {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath = Paths.get(basePath.toString() + "/file.txt");
        assertTrue(FileUtils.writeFile(filePath, "test".getBytes(), true, true));
    }

    @Test
    public void writeFile_unableToWrite_alreadyExists() {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath = Paths.get(basePath.toString() + "/file.txt");
        assertTrue(FileUtils.writeFile(filePath, "test".getBytes(), true, true));

        assertFalse(FileUtils.writeFile(filePath, "test".getBytes(), false, true));
    }

    @Test
    public void writeFile_unableToWrite_missingFolders() throws Exception {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath = Paths.get(basePath.toString() + "/file.txt");
        assertFalse(FileUtils.writeFile(filePath, "test".getBytes(), true, false));
    }

    @Test
    public void deleteDirectory() throws Exception {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath1 = Paths.get(basePath.toString() + "/file1.txt");
        Path filePath2 = Paths.get(basePath.toString() + "/file2.txt");
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);
        FileUtils.writeFile(filePath2, "test".getBytes(), true, true);
        List<Path> filesDeleted = FileUtils.delete(basePath);

        assertFalse(basePath.toFile().exists());
        assertEquals(filePath1, filesDeleted.get(0));
        assertEquals(filePath2, filesDeleted.get(1));
    }

    @Test
    public void deleteDirectory_unableToDelete_missingFolder() throws Exception {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath = Paths.get(basePath.toString() + "/file.txt");
        FileUtils.writeFile(filePath, "test".getBytes(), true, true);

        List<Path> filesDeleted = FileUtils.delete(basePath);
        assertEquals(filePath, filesDeleted.get(0));
        assertFalse(basePath.toFile().exists());

        // delete already deleted folder
        filesDeleted = FileUtils.delete(basePath);
        assertTrue(filesDeleted.isEmpty());
    }

    @Test
    public void deleteFile() throws Exception {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath = Paths.get(basePath.toString() + "/file.txt");
        FileUtils.writeFile(filePath, "test".getBytes(), true, true);

        List<Path> filesDeleted = FileUtils.delete(filePath);
        assertEquals(filePath, filesDeleted.get(0));

        assertTrue(basePath.toFile().exists());
        assertFalse(filePath.toFile().exists());
    }


    @Test
    public void zipDirectory() throws IOException {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath1 = Paths.get(basePath.toString() + "/file1.txt");
        Path filePath2 = Paths.get(basePath.toString() + "/file2.txt");
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);
        FileUtils.writeFile(filePath2, "test".getBytes(), true, true);
        List<Path> filesZipped = FileUtils.zipDirectory(Paths.get(basePath.getParent() + "/a.zip"), basePath);

        assertTrue(Paths.get(basePath.getParent() + "/a.zip").toFile().exists());
        assertEquals(filePath1, filesZipped.get(0));
        assertEquals(filePath2, filesZipped.get(1));
    }

    @After
    public void doAfter() {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        FileUtils.delete(basePath);
    }

    @Test(expected = InstantiationException.class)
    public void utilityClassCheck() throws Throwable {
        try {
            Constructor c = Class.forName(FileUtils.class.getName()).getDeclaredConstructor();
            c.setAccessible(true);
            c.newInstance();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
            // no need to expect reflection errors
            // we are interested in our own exceptions
        }
    }
}
