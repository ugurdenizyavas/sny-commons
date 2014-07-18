package com.sony.ebs.octopus3.commons.process;

import java.util.UUID;

/**
 * Default implementation of the ProcessId. It uses UUID to fulfil the uniqueness. We might think about having
 * other implementations to provide more readable unique ids.
 *
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

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName()).append("(id=").append(processId != null ? processId : "").append(")").toString();
    }

}
