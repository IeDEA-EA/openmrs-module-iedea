package org.openmrs.module.iedea;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.BaseOpenmrsObject;

public class ImportLogItem extends BaseOpenmrsObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String uuid;
    private Date started;
    private Date finished;
    private Date lastAttempt;
    private String status;
    private String message;
    private String importProfile;
    private String patientId;
    private Boolean createdNewPatient;

    public ImportLogItem() {}

    public ImportLogItem(String uuid, String status, String message) {
        this.uuid = uuid;
        this.status = status;
        this.message = message;
        this.started = new Date();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
    
    public Date getStarted() {
        return started;
    }
    
    public void setStarted(Date started) {
        this.started = started;
    }
    
    public Date getFinished() {
        return finished;
    }
    
    public void setFinished(Date finished) {
        this.finished = finished;
    }
    
    public Date getLastAttempt() {
        return lastAttempt;
    }
    
    public void setLastAttempt(Date lastAttempt) {
        this.lastAttempt = lastAttempt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getImportProfile() {
        return importProfile;
    }
    
    public void setImportProfile(String importProfile) {
        this.importProfile = importProfile;
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public Boolean getCreatedNewPatient() {
        return createdNewPatient;
    }
    
    public void setCreatedNewPatient(Boolean createdNewPatient) {
        this.createdNewPatient = createdNewPatient;
    }
}
