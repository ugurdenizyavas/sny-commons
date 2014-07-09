package com.sony.ebs.octopus3.commons.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author trerginl
 * @since 04.07.2014
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() throws InstantiationException {
        throw new InstantiationException("Utility classes should not be instantiated");
    }

    /**
     * Deletes the given file or folder. If any file or folder cannot be deleted due to any issues,
     * deletion process continues with the other ones.
     *
     * @param dir as the path of file or folder to delete
     * @return List of deleted paths
     */
    public static List<Path> delete(Path dir) {

        TrackingFileVisitor<Path> visitor = new TrackingFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    Files.deleteIfExists(file);
                    getFilesTracked().add(file);
                } catch (Exception e) {
                    logger.debug("Unable to delete file [" + file + "] due to errors", e);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    try {
                        Files.deleteIfExists(dir);
                    } catch (Exception e) {
                        logger.debug("Unable to delete directory [" + dir + "] due to errors", e);
                    }
                } else {
                    logger.debug("Unable to list directory [" + dir + "] due to errors", exc);
                }
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(dir, visitor);
        } catch (Exception e) {
            logger.debug("Unable to walk in directory [" + dir + "] due to errors", e);
        }
        return visitor.getFilesTracked();
    }

    public static boolean writeFile(Path path, byte[] content, boolean override, boolean createMissingFolders) {
        try {
            if (!override && Files.exists(path)) {
                logger.debug("File already exists in path [" + path + "] and override is not allowed");
                return false;
            }
            if (createMissingFolders) Files.createDirectories(path.getParent());
            Files.write(path, content);
            logger.debug("File is written in path [" + path + "]");
        } catch (IOException e) {
            logger.debug("Content cannot be written to path [" + path + "]", e);
            return false;
        }
        return true;
    }

    public static List<Path> zip(Path zipFilePath, Path fileOrFolderToZip) {
        try {
            ZipFileVisitor visitor = new ZipFileVisitor(zipFilePath);
            Files.walkFileTree(fileOrFolderToZip, visitor);
            visitor.fileSystem.close();
            return visitor.getFilesTracked();
        } catch (Exception e) {
            logger.debug("Unable to zip directory [" + fileOrFolderToZip + "] to path [" + zipFilePath + "] due to errors", e);
            return new ArrayList<Path>();
        }
    }
}
