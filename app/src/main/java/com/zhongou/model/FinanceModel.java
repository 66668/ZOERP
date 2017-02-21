package com.zhongou.model;

import java.io.Serializable;

/**
 * 应用-财务记录model
 * Created by sjy on 2017/2/15.
 */

public class FinanceModel implements Serializable {

    private String CopyTime;
    private String ApplicationType;
    private String ApplicationID;
    private String ApplicationTitle;
    private String IsBack;//

    public String getCopyTime() {
        return CopyTime;
    }

    public void setCopyTime(String copyTime) {
        CopyTime = copyTime;
    }

    public String getApplicationType() {
        return ApplicationType;
    }

    public void setApplicationType(String applicationType) {
        ApplicationType = applicationType;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        ApplicationID = applicationID;
    }

    public String getApplicationTitle() {
        return ApplicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        ApplicationTitle = applicationTitle;
    }

    public String getIsBack() {
        return IsBack;
    }

    public void setIsBack(String isBack) {
        IsBack = isBack;
    }
}
