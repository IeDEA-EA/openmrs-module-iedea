package org.openmrs.module.iedea;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsObject;

public class ImportLogItem extends BaseOpenmrsObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String uuid;
    private String status;
    private String message;

    public ImportLogItem() {}

    public ImportLogItem(String uuid, String status, String message) {
        this.uuid = uuid;
        this.status = status;
        this.message = message;
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

}
