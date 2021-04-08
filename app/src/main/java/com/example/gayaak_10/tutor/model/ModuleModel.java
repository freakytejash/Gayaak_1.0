package com.example.gayaak_10.tutor.model;

public class ModuleModel {
    public Integer moduleId;
    public Integer statusId;

    public ModuleModel(Integer moduleId, Integer statusId) {
        this.moduleId = moduleId;
        this.statusId = statusId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}
