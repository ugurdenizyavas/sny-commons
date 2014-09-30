package com.sony.ebs.octopus3.commons.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * author: TRYavasU
 * date: 30/09/2014
 */
public class CopyFileVisitor extends SimpleFileVisitor<Path> {
    private final Path targetPath;
    private Path sourcePath = null;

    private static final Logger logger = LoggerFactory.getLogger(CopyFileVisitor.class);

    public CopyFileVisitor(Path targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir,
                                             final BasicFileAttributes attrs) throws IOException {
        if (sourcePath == null) {
            sourcePath = dir;
        } else {
            Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file,
                                     final BasicFileAttributes attrs) {
        try {
            if (sourcePath == null) {
                sourcePath = file.getParent();
            }

            Path targetFile = targetPath.resolve(sourcePath.relativize(file));
            Path targetParent = targetFile.getParent();
            if (Files.notExists(targetParent)) {
                logger.debug("Destination path " + targetFile + " does not exist, so creating parent folder");
                Files.createDirectories(targetParent);
            }
            Files.copy(file, targetFile);
            logger.info("File [" + file + "] is copied to target [" + targetFile + "]");
        } catch (IOException e) {
            logger.debug("Unable to copy file [" + file + "] due to errors", e);
        }
        return FileVisitResult.CONTINUE;
    }
}
