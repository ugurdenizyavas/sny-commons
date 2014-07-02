package com.sony.ebs.octopus3.commons.process;

import java.util.UUID;

/**
 * @author trerginl
 * @since 02.07.2014
 */
public class ProcessIdImpl implements ProcessId {

    private String processId;

    public ProcessIdImpl() {
        this.processId = UUID.randomUUID().toString();
    }

    public ProcessIdImpl(String processId) {
        this.processId = processId;
    }

    @Override
    public String getId() {
        return processId;
    }
}
