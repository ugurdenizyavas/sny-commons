package com.sony.ebs.octopus3.commons.file;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * author: TRYavasU
 * date: 07/07/2014
 */
public class FileOperationResult {

    private String message;

    private List<Path> filesTracked = new ArrayList<Path>();
    private List<Path> filesFailed = new ArrayList<Path>();

    public void addTracked(Path path) {
        filesTracked.add(path);
    }

    public void addFailed(Path path) {
        filesFailed.add(path);
    }

    public List<Path> getTracked() {
        return filesTracked;
    }

    public List<Path> getFailed() {
        return filesFailed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
