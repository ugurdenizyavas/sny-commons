package com.sony.ebs.octopus3.commons.process;

import org.apache.commons.lang.StringUtils;

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
        this();
        if (StringUtils.isNotEmpty(processId)) {
            this.processId = processId;
        }
    }

    @Override
    public String getId() {
        return processId;
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName()).append("(id=").append(processId != null ? processId : "").append(")").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessIdImpl)) return false;

        ProcessIdImpl processId1 = (ProcessIdImpl) o;

        if (processId != null ? !processId.equals(processId1.processId) : processId1.processId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return processId != null ? processId.hashCode() : 0;
    }
}
