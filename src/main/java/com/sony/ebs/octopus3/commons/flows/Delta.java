package com.sony.ebs.octopus3.commons.flows;

import com.sony.ebs.octopus3.commons.process.ProcessId;

import java.util.HashMap;
import java.util.Map;

/**
 * author: TRYavasU
 * date: 20/10/2014
 */
public class Delta {

    ProcessId processId;
    FlowTypeEnum flow;
    ServiceTypeEnum service;
    RepoValue type;
    String publication;
    String locale;
    Object status;
    boolean upload;
    String sdate;
    String edate;

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

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public RepoValue getType() {
        return type;
    }

    public void setType(RepoValue type) {
        this.type = type;
    }

    public Map<String, String> getTicket() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("flow", flow.toString());
        map.put("service", service.toString());
        map.put("publication", publication);
        map.put("locale", locale);
        return map;
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
                ", type=" + type +
                ", sdate=" + sdate +
                ", edate=" + edate +
                ", upload=" + upload +
                ", publication='" + publication + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }

}