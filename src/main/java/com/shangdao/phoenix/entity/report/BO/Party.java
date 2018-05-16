package com.shangdao.phoenix.entity.report.BO;

/**
 * 法院裁决当事人
 * @author Administrator
 *
 */
public class Party {
    
    private String birthday;   //当事人生日
    
    private String title;       //当事人称号
    
    private String partytype;   //当事人类型
    
    private String lawOffice;   //律师事务所
    
    private String status;     //当事人立场
    
    private String pname;      //当事人名称
    
    private String idcardNo;   //当事人身份证号码
    
    private String lawyer;     //委托辩护人

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPartytype() {
        return partytype;
    }

    public void setPartytype(String partytype) {
        this.partytype = partytype;
    }

    public String getLawOffice() {
        return lawOffice;
    }

    public void setLawOffice(String lawOffice) {
        this.lawOffice = lawOffice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getLawyer() {
        return lawyer;
    }

    public void setLawyer(String lawyer) {
        this.lawyer = lawyer;
    }
    

}
