package com.sony.ebs.octopus3.commons.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    Path basePath = Paths.get(System.getProperty("java.io.tmpdir") + "/fileTest/a/b/c");
    Path filePath1 = Paths.get(basePath.toString() + "/file1.txt");
    Path filePath2 = Paths.get(basePath.toString() + "/file2.txt");
    Path filePath3 = Paths.get(basePath.toString() + "/file3.txt");

    @Before
    public void doBefore() {
        resetFilePermissions();
    }

    @After
    public void doAfter() {
        resetFilePermissions();
        FileUtils.delete(basePath);
    }

    @Test
    public void writeFile() throws Exception {
        assertTrue(FileUtils.writeFile(filePath1, "test".getBytes(), true, true));
    }

    @Test
    public void writeFile_unableToWrite_alreadyExists() {
        assertTrue(FileUtils.writeFile(filePath1, "test".getBytes(), true, true));
        assertFalse(FileUtils.writeFile(filePath1, "test".getBytes(), false, true));
    }

    @Test
    public void writeFile_unableToWrite_missingFolders() throws Exception {
        assertFalse(FileUtils.writeFile(filePath1, "test".getBytes(), true, false));
    }

    @Test
    public void writeFile_unableToWrite_writeToDirectory() throws Exception {
        assertTrue(FileUtils.writeFile(filePath1, "test".getBytes(), true, true));

        // basePath is a directory now
        assertFalse(FileUtils.writeFile(basePath, "test".getBytes(), true, true));
    }

    @Test
    public void deleteDirectory() throws Exception {
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);
        FileUtils.writeFile(filePath2, "test".getBytes(), true, true);
        List<Path> filesDeleted = FileUtils.delete(basePath);

        assertFalse(basePath.toFile().exists());
        assertEquals(filePath1, filesDeleted.get(0));
        assertEquals(filePath2, filesDeleted.get(1));
    }

    @Test
    public void deleteFile() throws Exception {
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);

        List<Path> filesDeleted = FileUtils.delete(filePath1);
        assertEquals(filePath1, filesDeleted.get(0));

        assertTrue(basePath.toFile().exists());
        assertFalse(filePath1.toFile().exists());
    }

    @Test
    public void deleteDirectory_unableToDelete_missingFolder() throws Exception {
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);

        List<Path> filesDeleted = FileUtils.delete(basePath);
        assertEquals(filePath1, filesDeleted.get(0));
        assertFalse(basePath.toFile().exists());

        // delete already deleted folder
        filesDeleted = FileUtils.delete(basePath);
        assertTrue(filesDeleted.isEmpty());
    }

    @Test
    public void deleteFile_unableToDelete_securityIssuesOnOneFile() {
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);
        FileUtils.writeFile(filePath2, "test".getBytes(), true, true);
        FileUtils.writeFile(filePath3, "test".getBytes(), true, true);

        // disable delete permissions for filePath2
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkDelete(String file) {
                if (file.equals(filePath2.toString()))
                    throw new SecurityException("Unable to delete " + file);
            }

            @Override
            public void checkPermission(Permission perm) {
            }
        });
        List<Path> filesDeleted = FileUtils.delete(basePath);

        assertTrue(basePath.toFile().exists());
        assertFalse(filePath1.toFile().exists());
        assertTrue(filePath2.toFile().exists());
        assertFalse(filePath3.toFile().exists());

        assertEquals(2, filesDeleted.size());
        assertEquals(filePath1, filesDeleted.get(0));
        assertEquals(filePath3, filesDeleted.get(1));
    }

    @Test
    public void deleteFolder_unableToDelete_securityIssuesOnOneFolder() {
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);
        FileUtils.writeFile(filePath2, "test".getBytes(), true, true);

        // disable delete permissions for basePath
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkDelete(String file) {
                if (file.equals(basePath.toString()))
                    throw new SecurityException("Unable to delete " + file);
            }

            @Override
            public void checkPermission(Permission perm) {
            }
        });
        List<Path> filesDeleted = FileUtils.delete(basePath);

        assertTrue(basePath.toFile().exists());
        assertFalse(filePath1.toFile().exists());
        assertFalse(filePath2.toFile().exists());

        assertEquals(2, filesDeleted.size());
        assertEquals(filePath1, filesDeleted.get(0));
        assertEquals(filePath2, filesDeleted.get(1));
    }

    @Test
    public void zipDirectory() throws IOException {
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);
        FileUtils.writeFile(filePath2, "test".getBytes(), true, true);
        Path zipPath = Paths.get(basePath.getParent() + "/a.zip");
        List<Path> filesZipped = FileUtils.zip(zipPath, basePath);

        assertTrue(Paths.get(basePath.getParent() + "/a.zip").toFile().exists());
        assertEquals(filePath1, filesZipped.get(0));
        assertEquals(filePath2, filesZipped.get(1));

        validateZip(zipPath, Arrays.asList("file1.txt", "file2.txt"));
    }

    @Test
    public void zipFile() throws IOException {
        FileUtils.writeFile(filePath1, "test".getBytes(), true, true);
        Path zipPath = Paths.get(filePath1.getParent() + "/a.zip");
        List<Path> filesZipped = FileUtils.zip(zipPath, filePath1);

        assertTrue(zipPath.toFile().exists());
        assertEquals(filePath1, filesZipped.get(0));

        validateZip(zipPath, Arrays.asList("file1.txt"));
    }

    @Test
    public void zipFile_noFilesToZip() throws IOException {
        List<Path> filesZipped = FileUtils.zip(Paths.get(filePath1.getParent() + "/a.zip"), filePath1);

        assertFalse(Paths.get(filePath1.getParent() + "/a.zip").toFile().exists());
        assertEquals(0, filesZipped.size());
    }

    @Test
    public void zipFile_missingData() throws IOException {
        List<Path> filesZipped = FileUtils.zip(null, null);
        assertEquals(0, filesZipped.size());
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

    private static void resetFilePermissions() {
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkDelete(String file) {
            }

            @Override
            public void checkPermission(Permission perm) {
            }
        });
    }

    private void validateZip(Path zipPath, List<String> content) throws IOException {
        byte[] zipFileBytes = Files.readAllBytes(zipPath);
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipFileBytes));

        ZipEntry entry;
        List<String> zipContent = new ArrayList<String>();
        while((entry = zis.getNextEntry()) != null) {
            zipContent.add(entry.getName());
        }
        assertArrayEquals(content.toArray(), zipContent.toArray());
    }

}
