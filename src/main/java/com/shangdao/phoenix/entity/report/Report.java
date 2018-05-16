package com.shangdao.phoenix.entity.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.customer.Customer;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.productionManager.ProductionManager;
import com.shangdao.phoenix.entity.report.module.*;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.enums.ReportStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * @author huanghengkun
 * @date 2018/04/02
 */
@Entity
@Table(name = "report")
public class Report implements IBaseEntity {
    /**
     *
     */
    @Transient
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "entity_manager_id")
    private EntityManager entityManager;

    @Column(name = "deleted_at")
    @JsonIgnore
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    //////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "created_by_company_id")
    private Company createdByCompany;

    @Column(name = "finished_at")
    private Date finishedAt;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private ReportStatus status;

    @ManyToOne
    @JoinColumn(name = "production_manager_id")
    private ProductionManager productionManager;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_basic_info_id")
    private BasicInfoModule basicInfoModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_fraud_id")
    private FraudModule fraudModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_multiple_head_lend_id")
    private MultipleHeadLendModule multipleHeadLendModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_overdue_credit_id")
    private OverdueCreditModule overdueCreditModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_dishonest_black_id")
    private DishonestBlackModule dishonestBlackModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_violation_id")
    private ViolationModule violationModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_criminal_id")
    private CriminalModule criminalModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_education_id")
    private EducationModule educationModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_zhima_score_id")
    private ZhimaScoreModule zhimaScoreModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_id_check_id")
    private IdCheckModule idCheckModule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_court_id")
    private CourtRuledMoudle courtRuledMoudle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_court_suspected_id")
    private CourtRuledMoudle courtRuledSuspectedMoudle;


    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "customer_mobile")
    private String customerMobile;
    @Column(name = "customer_idcard")
    private String customerIdCard;
    @Column(name = "customer_bankcard")
    private String customerBankCard;
    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "plate_number")
    private String plateNumber;
    @Column(name = "vin")
    private String vin;
    @Column(name = "engine_no")
    private String engineNo;
    @Column(name = "car_type")
    private String carType;

    public Company getCreatedByCompany() {
        return createdByCompany;
    }

    public void setCreatedByCompany(Company createdByCompany) {
        this.createdByCompany = createdByCompany;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerIdCard() {
        return customerIdCard;
    }

    public void setCustomerIdCard(String customerIdCard) {
        this.customerIdCard = customerIdCard;
    }

    public String getCustomerBankCard() {
        return customerBankCard;
    }

    public void setCustomerBankCard(String customerBankCard) {
        this.customerBankCard = customerBankCard;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public BasicInfoModule getBasicInfoModule() {
        return basicInfoModule;
    }

    public void setBasicInfoModule(BasicInfoModule basicInfoModule) {
        this.basicInfoModule = basicInfoModule;
    }

    public FraudModule getFraudModule() {
        return fraudModule;
    }

    public void setFraudModule(FraudModule fraudModule) {
        this.fraudModule = fraudModule;
    }

    public CourtRuledMoudle getCourtRuledSuspectedMoudle() {
        return courtRuledSuspectedMoudle;
    }

    public void setCourtRuledSuspectedMoudle(CourtRuledMoudle courtRuledSuspectedMoudle) {
        this.courtRuledSuspectedMoudle = courtRuledSuspectedMoudle;
    }

    public CourtRuledMoudle getCourtRuledMoudle() {
        return courtRuledMoudle;
    }

    public void setCourtRuledMoudle(CourtRuledMoudle courtRuledMoudle) {
        this.courtRuledMoudle = courtRuledMoudle;
    }

    public MultipleHeadLendModule getMultipleHeadLendModule() {
        return multipleHeadLendModule;
    }

    public void setMultipleHeadLendModule(MultipleHeadLendModule multipleHeadLendModule) {
        this.multipleHeadLendModule = multipleHeadLendModule;
    }

    public OverdueCreditModule getOverdueCreditModule() {
        return overdueCreditModule;
    }

    public void setOverdueCreditModule(OverdueCreditModule overdueCreditModule) {
        this.overdueCreditModule = overdueCreditModule;
    }

    public DishonestBlackModule getDishonestBlackModule() {
        return dishonestBlackModule;
    }

    public void setDishonestBlackModule(DishonestBlackModule dishonestBlackModule) {
        this.dishonestBlackModule = dishonestBlackModule;
    }

    public ViolationModule getViolationModule() {
        return violationModule;
    }

    public void setViolationModule(ViolationModule violationModule) {
        this.violationModule = violationModule;
    }

    public CriminalModule getCriminalModule() {
        return criminalModule;
    }

    public void setCriminalModule(CriminalModule criminalModule) {
        this.criminalModule = criminalModule;
    }

    public EducationModule getEducationModule() {
        return educationModule;
    }

    public void setEducationModule(EducationModule educationModule) {
        this.educationModule = educationModule;
    }

    public ZhimaScoreModule getZhimaScoreModule() {
        return zhimaScoreModule;
    }

    public void setZhimaScoreModule(ZhimaScoreModule zhimaScoreModule) {
        this.zhimaScoreModule = zhimaScoreModule;
    }

    public IdCheckModule getIdCheckModule() {
        return idCheckModule;
    }

    public void setIdCheckModule(IdCheckModule idCheckModule) {
        this.idCheckModule = idCheckModule;
    }

    public ProductionManager getProductionManager() {
        return productionManager;
    }

    public void setProductionManager(ProductionManager productionManager) {
        this.productionManager = productionManager;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Date getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }
}
