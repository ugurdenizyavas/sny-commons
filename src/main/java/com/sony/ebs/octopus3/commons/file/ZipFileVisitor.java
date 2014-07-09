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

    ZipFileVisitor(Path zipFilePath, FileOperationResult result) throws IOException {
        this.result = result;
        Map<String, String> env = new HashMap<String, String>();
        // check if file exists
        env.put("create", String.valueOf(!zipFilePath.toFile().exists()));

        // use a Zip filesystem URI
        URI fileUri = zipFilePath.toUri();
        fileSystem = FileSystems.newFileSystem(URI.create("jar:" + fileUri.getScheme() + ":" + fileUri.getPath()), env);
    }

    @Override
    public FileVisitResult visitFile(Path fileToZip, BasicFileAttributes attrs) throws IOException {
        //Create folder structure in zip file system
        Path fileZipped = fileSystem.getPath(fileToZip.getFileName().toString());
        //Create parent folder
        Path parent = fileZipped.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        // copy fileToZip to its location in zip
        Files.copy(fileToZip, fileZipped, StandardCopyOption.REPLACE_EXISTING);

        result.addTracked(fileToZip);
        return FileVisitResult.CONTINUE;
    }

}
