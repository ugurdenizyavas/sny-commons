package com.sony.ebs.octopus3.commons.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
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

    public static List<Path> deleteDirectory(Path dir) {
        try {
            TrackingFileVisitor<Path> visitor = new TrackingFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    getFilesTracked().add(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw exc;
                    }
                }
            };

            Files.walkFileTree(dir, visitor);
            return visitor.getFilesTracked();
        } catch (IOException e) {
            logger.debug("Unable to delete directory [" + dir + "] due to errors", e);
            return null;
        }
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
}
