package com.sony.ebs.octopus3.commons.file;

import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Test
    public void writeFile() throws Exception {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath = Paths.get(basePath.toString() + "/file.txt");
        assertTrue(FileUtils.writeFile(filePath, "test".getBytes(), true, true));
    }

    @Test
    public void writeFiles_deleteFolder() throws Exception {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
        Path filePath1 = Paths.get(basePath.toString() + "/file1.txt");
        Path filePath2 = Paths.get(basePath.toString() + "/file2.txt");
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);
        FileUtils.writeFile(filePath2, "test".getBytes(), true, true);
        List<Path> filesDeleted = FileUtils.deleteDirectory(basePath);

        assertEquals(Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c/file1.txt"), filesDeleted.get(0));
        assertEquals(Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c/file2.txt"), filesDeleted.get(1));
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

    @After
    public void doAfter() {
        Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest");
        FileUtils.deleteDirectory(basePath);
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
