package com.shangdao.phoenix.entity.report.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Entity
@Table(name = "court_ruled_detail")
public class CourtRuledDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "detail_id")
    private String detailId;              //各种公告Id 见枚举
    
    @Column(name = "summary")
    private String summary;            //案例摘要
    
    @Column(name = "sort_time")
    private String sortTime;            //审结时间
    
    @Column(name = "case_type")
    private String caseType;         //文书类型（案件类型）
    
    @Column(name = "result")
    private String result;         //判决结果类型
    
    @Column(name = "body",columnDefinition = "text")
    private String body;         //内容
     
    @Column(name = "fileurl")
    private String fileurl;              //附件下载地址
    
    @Column(name = "court")
    private String court;         //法院名称
    
    @Column(name = "case_cause")
    private String caseCause;         //案由
    
    @Column(name = "title")
    private String title;          //标题
    
    @Column(name = "judge")
    private String judge;            //审判员
    
    @Column(name = "province") 
    private String province;            //法院所在省或直辖市
     
    @Column(name = "case_no")
    private String caseNo;             //案号
    
    @Column(name = "judge_result")
    private String judgeResult;             //判决结果
    
    @Column(name = "focus")
    private String focus;              //争议焦点 
    
    @Column(name = "opinion")
    private String opinion;             //法院认为
    
    @Column(name = "yiju")
    private String yiju;            //判决依据
    
    @Column(name = "court_rank")
    private String courtRank;            //法院等级
    
    @Column(name = "districk")
    private String district;            //法院所在地区代码
    
    @Column(name = "argue")
    private String argue;             //诉辩称
    
    @Column(name = "trial_procedure")
    private String trialProcedure;          //审理程序
    
    @Column(name = "related_party")
    private String relatedParty;          //相关主体
    
    @Column(name = "anyou")
    private String anyou;           //案由编码
     
    @Column(name = "fact")
    private String fact;                   //确认事实
    
    @Column(name = "anyou_type")
    private String anyouType;           //案件类型
    
    @Column(name = "partys",columnDefinition = "text")
    private String partys;                  //当事人集合
    
    @Column(name = "plaintiff")
    private String plaintiff;              //原告
    
    @Column(name = "organizer")
    private String organizer;             //承办部门
    
    @Column(name = "courtroom")
    private String courtroom;           //法庭名称
    
    @Column(name = "party")
    private String party;            //当事人
    
    @Column(name = "defendant")
    private String defendant;         //被告
    
    @Column(name = "status")
    private String status;          //执行状态
    
    @Column(name = "pname") 
    private String pname;          //被执行人姓名         法院公告为当事人
    
    @Column(name = "proposer")
    private String proposer;       //申请人（预留字段，返回结果为空）
    
    @Column(name = "case_state")
    private String caseState;       //案件状态
    
    @Column(name = "id_card_no")
    private String idcardNo;       //身份证/组织机构代码
    
    @Column(name = "exec_money")
    private String execMoney;     //执行标的
    
    @Column(name = "sex")
    private String sex;           //性别
    
    @Column(name = "lxqk")
    private String lxqk;          //被执行人的履行情况
    
    @Column(name = "yj_code")
    private String yjCode;        //执行依据文号
    
    @Column(name = "yjdw")
    private String yjdw;          //做出执行依据单位
    
    @Column(name = "jtqx")
    private String jtqx;          //失信被执行人行为具体情形
    
    @Column(name = "yiwu",columnDefinition = "text")
    private String yiwu;          //生效法律文书确定的义务
    
    @Column(name = "age")
    private String age;           //年龄
    
    @Column(name = "post_time")
    private String postTime;      //发布时间
    
    @Column(name = "layout")
    private String layout;        //刊登版面
    
    @Column(name = "gg_type")     
    private String ggType;        //公告类型
    
    @Enumerated(EnumType.STRING)
    @Column(name = "court_ruled")
    private CourtRuledEnum courtRuled;
    
    @ManyToOne
    @JoinColumn(name = "court_ruled_moudle")
    private CourtRuledMoudle courtRuledMoudle;
    
    public CourtRuledDetail(){
        
    }
    
    public CourtRuledDetail(Object object,CourtRuledMoudle moudle){
        JSONObject jo = (JSONObject)object;
        if(jo.containsKey("cpwsId")){
            this.detailId = jo.getString("cpwsId");
            this.body = jo.getString("body");
            this.title = jo.getString("title");
            this.caseNo = jo.getString("caseNo");
            this.courtRuled = CourtRuledEnum.CPWS;
        }
        if(jo.containsKey("ktggId")){
            this.detailId = jo.getString("ktggId");
            this.body = jo.getString("body");
            this.title = jo.getString("title");
            this.caseNo = jo.getString("caseNo");
            this.courtroom = jo.getString("courtroom");
            this.caseCause = jo.getString("caseCause");
            this.courtRuled = CourtRuledEnum.KTGG;
        }
        if(jo.containsKey("zxggId")){
            this.detailId = jo.getString("zxggId");
            this.body = jo.getString("body");
            this.title = jo.getString("title");
            this.caseNo = jo.getString("caseNo");
            this.status = jo.getString("status");
            this.pname = jo.getString("pname");
            this.proposer = jo.getString("proposer");
            this.caseState = jo.getString("caseState");
            this.idcardNo = jo.getString("idcardNo");
            this.execMoney = jo.getString("execMoney");
            this.courtRuled = CourtRuledEnum.ZXGG;
        }
        if(jo.containsKey("sxggId")){
            this.detailId = jo.getString("sxggId");
            this.idcardNo = jo.getString("idcardNo");
            this.pname = jo.getString("pname");
            this.caseNo = jo.getString("caseNo");
            this.sex = jo.getString("sex");
            this.lxqk = jo.getString("lxqk");
            this.yjCode = jo.getString("yjCode");
            this.province = jo.getString("province");
            this.postTime = jo.getString("postTime");
            this.age = jo.getString("age");
            this.relatedParty = jo.getString("relatedParty");
            this.yiwu = jo.getString("yiwu");
            this.yjdw = jo.getString("yjdw");
            this.jtqx = jo.getString("jtqx");
            this.courtRuled = CourtRuledEnum.SXGG;
        }
        if(jo.containsKey("fyggId")){
            this.detailId = jo.getString("fyggId");
            this.relatedParty = jo.getString("relatedParty");
            this.pname = jo.getString("pname");
            this.body = jo.getString("body");
            this.ggType = jo.getString("ggType");
            this.layout = jo.getString("layout");
            this.courtRuled = CourtRuledEnum.FYGG;
        }
        this.sortTime = jo.getString("sortTime");
        this.court = jo.getString("court");
        this.courtRuledMoudle = moudle;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSortTime() {
        return sortTime;
    }

    public void setSortTime(String sortTime) {
        this.sortTime = sortTime;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getCaseCause() {
        return caseCause;
    }

    public void setCaseCause(String caseCause) {
        this.caseCause = caseCause;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getJudgeResult() {
        return judgeResult;
    }

    public void setJudgeResult(String judgeResult) {
        this.judgeResult = judgeResult;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getYiju() {
        return yiju;
    }

    public void setYiju(String yiju) {
        this.yiju = yiju;
    }

    public String getCourtRank() {
        return courtRank;
    }

    public void setCourtRank(String courtRank) {
        this.courtRank = courtRank;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getArgue() {
        return argue;
    }

    public void setArgue(String argue) {
        this.argue = argue;
    }

    public String getTrialProcedure() {
        return trialProcedure;
    }

    public void setTrialProcedure(String trialProcedure) {
        this.trialProcedure = trialProcedure;
    }

    public String getRelatedParty() {
        return relatedParty;
    }

    public void setRelatedParty(String relatedParty) {
        this.relatedParty = relatedParty;
    }

    public String getAnyou() {
        return anyou;
    }

    public void setAnyou(String anyou) {
        this.anyou = anyou;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public String getAnyouType() {
        return anyouType;
    }

    public void setAnyouType(String anyouType) {
        this.anyouType = anyouType;
    }

    public String getPartys() {
        return partys;
    }

    public void setPartys(JSONArray partys) {
        this.partys = partys.toJSONString();
    }

    public String getPlaintiff() {
        return plaintiff;
    }

    public void setPlaintiff(String plaintiff) {
        this.plaintiff = plaintiff;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getCourtroom() {
        return courtroom;
    }

    public void setCourtroom(String courtroom) {
        this.courtroom = courtroom;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getDefendant() {
        return defendant;
    }

    public void setDefendant(String defendant) {
        this.defendant = defendant;
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

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getCaseState() {
        return caseState;
    }

    public void setCaseState(String caseState) {
        this.caseState = caseState;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getExecMoney() {
        return execMoney;
    }

    public void setExecMoney(String execMoney) {
        this.execMoney = execMoney;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLxqk() {
        return lxqk;
    }

    public void setLxqk(String lxqk) {
        this.lxqk = lxqk;
    }

    public String getYjCode() {
        return yjCode;
    }

    public void setYjCode(String yjCode) {
        this.yjCode = yjCode;
    }

    public String getYjdw() {
        return yjdw;
    }

    public void setYjdw(String yjdw) {
        this.yjdw = yjdw;
    }

    public String getJtqx() {
        return jtqx;
    }

    public void setJtqx(String jtqx) {
        this.jtqx = jtqx;
    }

    public String getYiwu() {
        return yiwu;
    }

    public void setYiwu(String yiwu) {
        this.yiwu = yiwu;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getGgType() {
        return ggType;
    }

    public void setGgType(String ggType) {
        this.ggType = ggType;
    }

    public CourtRuledEnum getCourtRuled() {
        return courtRuled;
    }

    public void setCourtRuled(CourtRuledEnum courtRuled) {
        this.courtRuled = courtRuled;
    }



    public enum CourtRuledEnum {
        CPWS,      
        KTGG,
        ZXGG,
        SXGG,
        FYGG
    }

}
