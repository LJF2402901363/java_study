package com.example.boot.entity;

import java.io.Serializable;
import java.util.Date;

public class FileInformation implements Serializable {
    private Integer id;

    private String title;

    private Date createtimeat;

    private Date updatetimeat;

    private String filename;

    private String filepath;

    private Boolean isdeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getCreatetimeat() {
        return createtimeat;
    }

    public void setCreatetimeat(Date createtimeat) {
        this.createtimeat = createtimeat;
    }

    public Date getUpdatetimeat() {
        return updatetimeat;
    }

    public void setUpdatetimeat(Date updatetimeat) {
        this.updatetimeat = updatetimeat;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }
}