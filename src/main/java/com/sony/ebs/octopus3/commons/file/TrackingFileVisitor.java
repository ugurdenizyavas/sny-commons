package com.sony.ebs.octopus3.commons.file;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * author: TRYavasU
 * date: 07/07/2014
 */
public class TrackingFileVisitor<T> extends SimpleFileVisitor<T> {

    private List<Path> filesTracked = new ArrayList<Path>();

    public List<Path> getFilesTracked() {
        return filesTracked;
    }
}
