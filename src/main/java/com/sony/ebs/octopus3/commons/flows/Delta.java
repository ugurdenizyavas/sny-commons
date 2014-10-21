package com.sony.ebs.octopus3.commons.flows;

import com.sony.ebs.octopus3.commons.process.ProcessId;

/**
 * author: TRYavasU
 * date: 20/10/2014
 */
public class Delta {

    ProcessId processId;
    FlowTypeEnum flow;
    ServiceTypeEnum service;
    String publication;
    String locale;
    Object status;

    public ProcessId getProcessId() {
        return processId;
    }

    public void setProcessId(ProcessId processId) {
        this.processId = processId;
    }

    public FlowTypeEnum getFlow() {
        return flow;
    }

    public void setFlow(FlowTypeEnum flow) {
        this.flow = flow;
    }

    public ServiceTypeEnum getService() {
        return service;
    }

    public void setService(ServiceTypeEnum service) {
        this.service = service;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Delta delta = (Delta) o;

        if (!processId.equals(delta.processId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return processId.hashCode();
    }

    @Override
    public String toString() {
        return "Delta{" +
                "processId=" + processId +
                ", flow=" + flow +
                ", service=" + service +
                ", publication='" + publication + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}