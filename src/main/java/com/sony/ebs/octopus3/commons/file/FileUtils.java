package com.sony.ebs.octopus3.commons.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

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
    public static FileOperationResult delete(Path dir) {
        final FileOperationResult result = new FileOperationResult();

        SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    Files.deleteIfExists(file);
                    result.addTracked(file);
                } catch (Exception e) {
                    result.addFailed(file);
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
            logger.debug("File/folder in path [" + dir + "] is deleted");
        } catch (Exception e) {
            logger.debug("Unable to walk in directory [" + dir + "] due to errors", e);
        }
        return result;
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

    public static FileOperationResult zip(Path zipFilePath, Path fileOrFolderToZip) {
        FileOperationResult result = new FileOperationResult();
        try {
            ZipFileVisitor visitor = new ZipFileVisitor(zipFilePath, result, fileOrFolderToZip);
            Files.walkFileTree(fileOrFolderToZip, visitor);
            visitor.fileSystem.close();
            logger.debug("File/folder in path [" + fileOrFolderToZip + "] is zipped into [" + zipFilePath + "]");
        } catch (Exception e) {
            logger.debug("Unable to zip directory [" + fileOrFolderToZip + "] to path [" + zipFilePath + "] due to errors", e);
        }
        return result;
    }

    public static void copy(Path sourcePath, Path targetPath) throws IOException {
        Files.walkFileTree(sourcePath, new CopyFileVisitor(targetPath));
    }
}
