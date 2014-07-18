package com.sony.ebs.octopus3.commons.file;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * author: TRYavasU
 * date: 08/07/2014
 */
public class ZipFileVisitor extends SimpleFileVisitor<Path> {

    FileSystem fileSystem;
    FileOperationResult result;
    Path fileOrFolderToZip;

    ZipFileVisitor(Path zipFilePath, FileOperationResult result, Path fileOrFolderToZip) throws IOException {
        this.result = result;
        this.fileOrFolderToZip = fileOrFolderToZip;

        Map<String, String> env = new HashMap<String, String>();
        // check if file exists
        env.put("create", String.valueOf(!zipFilePath.toFile().exists()));

        // use a Zip filesystem URI
        URI fileUri = zipFilePath.toUri();
        fileSystem = FileSystems.newFileSystem(URI.create("jar:" + fileUri.getScheme() + ":" + fileUri.getPath()), env);
    }

    @Override
    public FileVisitResult visitFile(Path fileToZip, BasicFileAttributes attrs) throws IOException {
        //Default file behavior is to include file in zip; default folder behavior is not to include folder in zip
        Path root = Files.isRegularFile(fileOrFolderToZip) ? fileOrFolderToZip.getParent() : fileOrFolderToZip;

        Path locationInZip = fileSystem.getPath(root.relativize(fileToZip).toString());

        //Create folder structure in zip file system
        Path parent = locationInZip.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        // copy fileToZip to its location in zip
        Files.copy(fileToZip, locationInZip, StandardCopyOption.REPLACE_EXISTING);

        result.addTracked(fileToZip);
        return FileVisitResult.CONTINUE;
    }

}
