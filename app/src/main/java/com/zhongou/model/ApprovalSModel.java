package com.zhongou.model;

import java.io.Serializable;

/**
 * 我的审批-抄送-提交对象
 *
 * Created by sjy on 2017/2/9.
 */

public class ApprovalSModel implements Serializable {
   private String sApprovalid;
   private String sComment;
   private String sApplicationid;
   private String sApplicationtype;
   private String sEmployeeid;
   private String sStoreid;
   private String sApplicationtitle;
   private String sApprovalemployeeinfos;

    public String getsApprovalid() {
        return sApprovalid;
    }

    public void setsApprovalid(String sApprovalid) {
        this.sApprovalid = sApprovalid;
    }

    public String getsComment() {
        return sComment;
    }

    public void setsComment(String sComment) {
        this.sComment = sComment;
    }

    public String getsApplicationid() {
        return sApplicationid;
    }

    public void setsApplicationid(String sApplicationid) {
        this.sApplicationid = sApplicationid;
    }

    public String getsApplicationtype() {
        return sApplicationtype;
    }

    public void setsApplicationtype(String sApplicationtype) {
        this.sApplicationtype = sApplicationtype;
    }

    public String getsEmployeeid() {
        return sEmployeeid;
    }

    public void setsEmployeeid(String sEmployeeid) {
        this.sEmployeeid = sEmployeeid;
    }

    public String getsStoreid() {
        return sStoreid;
    }

    public void setsStoreid(String sStoreid) {
        this.sStoreid = sStoreid;
    }

    public String getsApplicationtitle() {
        return sApplicationtitle;
    }

    public void setsApplicationtitle(String sApplicationtitle) {
        this.sApplicationtitle = sApplicationtitle;
    }

    public String getsApprovalemployeeinfos() {
        return sApprovalemployeeinfos;
    }

    public void setsApprovalemployeeinfos(String sApprovalemployeeinfos) {
        this.sApprovalemployeeinfos = sApprovalemployeeinfos;
    }
}
