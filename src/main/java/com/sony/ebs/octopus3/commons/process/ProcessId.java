package com.sony.ebs.octopus3.commons.process;

/**
 * Every process should have a unique process id. This information will mainly used for logging and tracing the flow
 * via checking unique correlation id.
 *
 * @author trerginl
 * @since 02.07.2014
 */
public interface ProcessId {

    /**
     * The id has to be unique among processes.
     *
     * @return String representation of the id
     */
    public String getId();

}
