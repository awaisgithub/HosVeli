package com.od.mma.Membership;

import com.od.mma.BOs.User;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by awais on 04/01/2017.
 */

public class Membership extends RealmObject {
    @PrimaryKey
    private String id;

    private String applicationStatus = "";
    private boolean loadFromServer = false;

    private String main_category = "";
    private boolean validation = false;
    private int validation_pos = -1;

    //2nd
    private int category = -1;
    private String membershipCategory = "";
    private int year = -1;
    private String yearOfService = "";
    private boolean medical_off_mem = false;
    private String houseDoctorIsLifeTime = "";
    //3rd
    private int title = -1;
    private String titleSelectionBox = "";
    private byte[] personal_pic = null;
    private String applicantPicture = "";
    private String applicantFirstName = "";
    private String applicantLastName = "";
    private String applicantDateOfBirth = "";
    private int gender = -1;
    private String applicantGender = "";
    private int marital_stat = -1;
    private String applicantMaritalStatus = "";
    //4th
    private int id_type = -1;
    private String identificationType = "";
    private String nricNew = "";
    private byte[] nric_pic = null;
    private String applicantNRICPic = "";
    private int nationality = -1;
    private String applicantNationality = "";
    private int race = -1;
    private String applicantRace = "";
    private int religion = -1;
    private String applicantReligion = "";
    //5th
    private int country_pos = -1;
    private String country = "";
    private int state_pos = -1;
    private String state = "";
    private int reg_branch_pos = -1;
    private String registrationState = "";
    private String city = "";
    private String postCode = "";
    private String address = "";
    private String telephoneNo = "";
    //6th
    private int r_country = -1;
    private String countryResidential = "";
    private int r_state = -1;
    private String stateResidential = "";
    private String city_1 = "";
    private String postCodeResidential = "";
    private String addressResidential = "";
    private String telephoneNoResidential = "";
    private int correspondence_address = -1;
    private String correspondenceSelection = "";
    //7th
    private int joint_account = -1;
    private String isJointAccount = "";
    private int spouse_id_type = -1;
    private String spouseIdentificationType = "";
    private String spouseNRICNew = "";
    private String applicantSpouseFirstName = "";
    private String applicantSpouseUsername = "";
    //8th
    private int bachelor_degree = -1;
    private String degree_bachelor = "";
    private String bachelor_uni = "";
    private int bachelor_uni_malay = -1;
    private int bachelor_country = -1;
    private String degree_bachelor_country = "";
    private String bachelor_qualification_date = "";
    //9th
    private int pos_count = 0;

    private String pos_degree = "";
    private String pos_uni = "";
    private String pos_country = "";
    private String pos_qof_date = "";

    private String pos_degree1 = "";
    private String pos_uni1 = "";
    private String pos_country1 = "";
    private String pos_qof_date1 = "";

    private String pos_degree2 = "";
    private String pos_uni2 = "";
    private String pos_country2 = "";
    private String pos_qof_date2 = "";

    //10th
    private String applicantMMCRegistrationNo = "";
    private String tpcRegistrationNo = "";
    private String dateOfRegistrationMMC = "";
    private byte[] mmc_certificate = null;
    private String applicantMMCRegistrationCopy = "";
    //11th
    private int uni_no = -1;
    private String studentMemberUniversity = "";
    private String studentMemberStudyCompletionYear = "";
    private byte[] uni_student_card = null;
    private String studentMemberUniversityLetter = "";
    //12th
    private int emp_status = -1;
    private String employmentStatusSelectBox = "";
    private int emp_prac = -1;
    private String practiceNature = "";
    private int emp_prac_sub = -1;
    private String practiceNatureSubCategory = "";
    //13th
    private int payment_method = -1;
    private int payment_sub_year = -1;
    //13th(a)
    private String bank = "";
    private String cheque_ref_no = "";
    private String debit_ref_no = "";
    private String cash_ref_no = "";
    private String credit_ref_no = "";
    private String bank_payment_date = "";
    private String bank_expiry_date = "";

    private boolean isSyncedLocal = false;

    public String getHouseDoctorIsLifeTime() {
        return houseDoctorIsLifeTime;
    }

    public void setHouseDoctorIsLifeTime(String houseDoctorIsLifeTime) {
        this.houseDoctorIsLifeTime = houseDoctorIsLifeTime;
    }

    public String getApplicantMMCRegistrationCopy() {
        return applicantMMCRegistrationCopy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public boolean isLoadFromServer() {
        return loadFromServer;
    }

    public void setLoadFromServer(boolean loadFromServer) {
        this.loadFromServer = loadFromServer;
    }

    public void setApplicantMMCRegistrationCopy(String applicantMMCRegistrationCopy) {
        this.applicantMMCRegistrationCopy = applicantMMCRegistrationCopy;
    }

    public String getApplicantPicture() {
        return applicantPicture;
    }

    public String getApplicantNRICPic() {
        return applicantNRICPic;
    }

    public void setApplicantNRICPic(String applicantNRICPic) {
        this.applicantNRICPic = applicantNRICPic;
    }

    public void setApplicantPicture(String applicantPicture) {
        this.applicantPicture = applicantPicture;
    }

    public String getEmploymentStatusSelectBox() {
        return employmentStatusSelectBox;
    }

    public void setEmploymentStatusSelectBox(String employmentStatusSelectBox) {
        this.employmentStatusSelectBox = employmentStatusSelectBox;
    }

    public String getPracticeNature() {
        return practiceNature;
    }

    public void setPracticeNature(String practiceNature) {
        this.practiceNature = practiceNature;
    }

    public String getStudentMemberUniversityLetter() {
        return studentMemberUniversityLetter;
    }

    public void setStudentMemberUniversityLetter(String studentMemberUniversityLetter) {
        this.studentMemberUniversityLetter = studentMemberUniversityLetter;
    }

    public String getPracticeNatureSubCategory() {
        return practiceNatureSubCategory;
    }

    public void setPracticeNatureSubCategory(String practiceNatureSubCategory) {
        this.practiceNatureSubCategory = practiceNatureSubCategory;
    }

    public String getStudentMemberUniversity() {
        return studentMemberUniversity;
    }

    public void setStudentMemberUniversity(String studentMemberUniversity) {
        this.studentMemberUniversity = studentMemberUniversity;
    }

    public String getDegree_bachelor() {
        return degree_bachelor;
    }

    public void setDegree_bachelor(String degree_bachelor) {
        this.degree_bachelor = degree_bachelor;
    }

    public String getDegree_bachelor_country() {
        return degree_bachelor_country;
    }

    public void setDegree_bachelor_country(String degree_bachelor_country) {
        this.degree_bachelor_country = degree_bachelor_country;
    }

    public String getSpouseIdentificationType() {
        return spouseIdentificationType;
    }

    public void setSpouseIdentificationType(String spouseIdentificationType) {
        this.spouseIdentificationType = spouseIdentificationType;
    }

    public String getIsJointAccount() {
        return isJointAccount;
    }

    public void setIsJointAccount(String isJointAccount) {
        this.isJointAccount = isJointAccount;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryResidential() {
        return countryResidential;
    }

    public void setCountryResidential(String countryResidential) {
        this.countryResidential = countryResidential;
    }

    public String getStateResidential() {
        return stateResidential;
    }

    public void setStateResidential(String stateResidential) {
        this.stateResidential = stateResidential;
    }

    public String getCorrespondenceSelection() {
        return correspondenceSelection;
    }

    public void setCorrespondenceSelection(String correspondenceSelection) {
        this.correspondenceSelection = correspondenceSelection;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(String registrationState) {
        this.registrationState = registrationState;
    }

    public String getApplicantGender() {
        return applicantGender;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getApplicantNationality() {
        return applicantNationality;
    }

    public void setApplicantNationality(String applicantNationality) {
        this.applicantNationality = applicantNationality;
    }

    public void setApplicantGender(String applicantGender) {
        this.applicantGender = applicantGender;
    }

    public String getApplicantMaritalStatus() {
        return applicantMaritalStatus;
    }

    public void setApplicantMaritalStatus(String applicantMaritalStatus) {
        this.applicantMaritalStatus = applicantMaritalStatus;
    }

    public String getTitleSelectionBox() {
        return titleSelectionBox;
    }

    public void setTitleSelectionBox(String titleSelectionBox) {
        this.titleSelectionBox = titleSelectionBox;
    }

    public String getTpcRegistrationNo() {
        return tpcRegistrationNo;
    }

    public void setTpcRegistrationNo(String tpcRegistrationNo) {
        this.tpcRegistrationNo = tpcRegistrationNo;
    }

    public int getBachelor_uni_malay() {
        return bachelor_uni_malay;
    }

    public void setBachelor_uni_malay(int bachelor_uni_malay) {
        this.bachelor_uni_malay = bachelor_uni_malay;
    }

    public String getYearOfService() {
        return yearOfService;
    }

    public void setYearOfService(String yearOfService) {
        this.yearOfService = yearOfService;
    }

    public int getPos_count() {
        return pos_count;
    }

    public void setPos_count(int pos_count) {
        this.pos_count = pos_count;
    }

    public String getPos_degree() {
        return pos_degree;
    }

    public void setPos_degree(String pos_degree) {
        this.pos_degree = pos_degree;
    }

    public String getPos_uni() {
        return pos_uni;
    }

    public void setPos_uni(String pos_uni) {
        this.pos_uni = pos_uni;
    }

    public String getPos_country() {
        return pos_country;
    }

    public void setPos_country(String pos_country) {
        this.pos_country = pos_country;
    }

    public String getPos_qof_date() {
        return pos_qof_date;
    }

    public void setPos_qof_date(String pos_qof_date) {
        this.pos_qof_date = pos_qof_date;
    }

    public String getPos_degree1() {
        return pos_degree1;
    }

    public void setPos_degree1(String pos_degree1) {
        this.pos_degree1 = pos_degree1;
    }

    public String getPos_uni1() {
        return pos_uni1;
    }

    public void setPos_uni1(String pos_uni1) {
        this.pos_uni1 = pos_uni1;
    }

    public String getPos_country1() {
        return pos_country1;
    }

    public void setPos_country1(String pos_country1) {
        this.pos_country1 = pos_country1;
    }

    public String getPos_qof_date1() {
        return pos_qof_date1;
    }

    public void setPos_qof_date1(String pos_qof_date1) {
        this.pos_qof_date1 = pos_qof_date1;
    }

    public String getPos_degree2() {
        return pos_degree2;
    }

    public void setPos_degree2(String pos_degree2) {
        this.pos_degree2 = pos_degree2;
    }

    public String getPos_uni2() {
        return pos_uni2;
    }

    public void setPos_uni2(String pos_uni2) {
        this.pos_uni2 = pos_uni2;
    }

    public String getPos_country2() {
        return pos_country2;
    }

    public void setPos_country2(String pos_country2) {
        this.pos_country2 = pos_country2;
    }

    public String getPos_qof_date2() {
        return pos_qof_date2;
    }

    public void setPos_qof_date2(String pos_qof_date2) {
        this.pos_qof_date2 = pos_qof_date2;
    }

    public int getReg_branch_pos() {
        return reg_branch_pos;
    }

    public void setReg_branch_pos(int reg_branch_pos) {
        this.reg_branch_pos = reg_branch_pos;
    }

    public boolean isMedical_off_mem() {
        return medical_off_mem;
    }

    public void setMedical_off_mem(boolean medical_off_mem) {
        this.medical_off_mem = medical_off_mem;
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public int getValidation_pos() {
        return validation_pos;
    }

    public void setValidation_pos(int validation_pos) {
        this.validation_pos = validation_pos;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCheque_ref_no() {
        return cheque_ref_no;
    }

    public void setCheque_ref_no(String cheque_ref_no) {
        this.cheque_ref_no = cheque_ref_no;
    }

    public String getDebit_ref_no() {
        return debit_ref_no;
    }

    public void setDebit_ref_no(String debit_ref_no) {
        this.debit_ref_no = debit_ref_no;
    }

    public String getCash_ref_no() {
        return cash_ref_no;
    }

    public void setCash_ref_no(String cash_ref_no) {
        this.cash_ref_no = cash_ref_no;
    }

    public String getCredit_ref_no() {
        return credit_ref_no;
    }

    public void setCredit_ref_no(String credit_ref_no) {
        this.credit_ref_no = credit_ref_no;
    }

    public String getBank_payment_date() {
        return bank_payment_date;
    }

    public void setBank_payment_date(String bank_payment_date) {
        this.bank_payment_date = bank_payment_date;
    }

    public String getBank_expiry_date() {
        return bank_expiry_date;
    }

    public void setBank_expiry_date(String bank_expiry_date) {
        this.bank_expiry_date = bank_expiry_date;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public int getPayment_sub_year() {
        return payment_sub_year;
    }

    public void setPayment_sub_year(int payment_sub_year) {
        this.payment_sub_year = payment_sub_year;
    }

    public int getEmp_status() {
        return emp_status;
    }

    public void setEmp_status(int emp_status) {
        this.emp_status = emp_status;
    }

    public int getEmp_prac() {
        return emp_prac;
    }

    public void setEmp_prac(int emp_prac) {
        this.emp_prac = emp_prac;
    }

    public int getEmp_prac_sub() {
        return emp_prac_sub;
    }

    public void setEmp_prac_sub(int emp_prac_sub) {
        this.emp_prac_sub = emp_prac_sub;
    }

    public int getUni_no() {
        return uni_no;
    }

    public void setUni_no(int uni_no) {
        this.uni_no = uni_no;
    }

    public String getStudentMemberStudyCompletionYear() {
        return studentMemberStudyCompletionYear;
    }

    public void setStudentMemberStudyCompletionYear(String studentMemberStudyCompletionYear) {
        this.studentMemberStudyCompletionYear = studentMemberStudyCompletionYear;
    }

    public byte[] getUni_student_card() {
        return uni_student_card;
    }

    public void setUni_student_card(byte[] uni_student_card) {
        this.uni_student_card = uni_student_card;
    }

    public String getApplicantMMCRegistrationNo() {
        return applicantMMCRegistrationNo;
    }

    public void setApplicantMMCRegistrationNo(String applicantMMCRegistrationNo) {
        this.applicantMMCRegistrationNo = applicantMMCRegistrationNo;
    }

    public String getDateOfRegistrationMMC() {
        return dateOfRegistrationMMC;
    }

    public void setDateOfRegistrationMMC(String dateOfRegistrationMMC) {
        this.dateOfRegistrationMMC = dateOfRegistrationMMC;
    }

    public byte[] getMmc_certificate() {
        return mmc_certificate;
    }

    public void setMmc_certificate(byte[] mmc_certificate) {
        this.mmc_certificate = mmc_certificate;
    }

    public int getBachelor_degree() {
        return bachelor_degree;
    }

    public void setBachelor_degree(int bachelor_degree) {
        this.bachelor_degree = bachelor_degree;
    }

    public String getBachelor_uni() {
        return bachelor_uni;
    }

    public void setBachelor_uni(String bachelor_uni) {
        this.bachelor_uni = bachelor_uni;
    }

    public int getBachelor_country() {
        return bachelor_country;
    }

    public void setBachelor_country(int bachelor_country) {
        this.bachelor_country = bachelor_country;
    }

    public String getBachelor_qualification_date() {
        return bachelor_qualification_date;
    }

    public void setBachelor_qualification_date(String bachelor_qualification_date) {
        this.bachelor_qualification_date = bachelor_qualification_date;
    }

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public String getMembershipCategory() {
        return membershipCategory;
    }

    public void setMembershipCategory(String membershipCategory) {
        this.membershipCategory = membershipCategory;
    }

    public int getJoint_account() {
        return joint_account;
    }

    public void setJoint_account(int joint_account) {
        this.joint_account = joint_account;
    }

    public int getSpouse_id_type() {
        return spouse_id_type;
    }

    public void setSpouse_id_type(int spouse_id_type) {
        this.spouse_id_type = spouse_id_type;
    }

    public String getSpouseNRICNew() {
        return spouseNRICNew;
    }

    public void setSpouseNRICNew(String spouseNRICNew) {
        this.spouseNRICNew = spouseNRICNew;
    }

    public String getApplicantSpouseFirstName() {
        return applicantSpouseFirstName;
    }

    public void setApplicantSpouseFirstName(String applicantSpouseFirstName) {
        this.applicantSpouseFirstName = applicantSpouseFirstName;
    }

    public String getApplicantSpouseUsername() {
        return applicantSpouseUsername;
    }

    public void setApplicantSpouseUsername(String applicantSpouseUsername) {
        this.applicantSpouseUsername = applicantSpouseUsername;
    }

    public int getCorrespondence_address() {
        return correspondence_address;
    }

    public void setCorrespondence_address(int correspondence_address) {
        this.correspondence_address = correspondence_address;
    }

    public int getR_country() {
        return r_country;
    }

    public void setR_country(int r_country) {
        this.r_country = r_country;
    }

    public int getR_state() {
        return r_state;
    }

    public void setR_state(int r_state) {
        this.r_state = r_state;
    }

    public String getCity_1() {
        return city_1;
    }

    public void setCity_1(String city_1) {
        this.city_1 = city_1;
    }

    public String getPostCodeResidential() {
        return postCodeResidential;
    }

    public void setPostCodeResidential(String postCodeResidential) {
        this.postCodeResidential = postCodeResidential;
    }

    public String getAddressResidential() {
        return addressResidential;
    }

    public void setAddressResidential(String addressResidential) {
        this.addressResidential = addressResidential;
    }

    public String getTelephoneNoResidential() {
        return telephoneNoResidential;
    }

    public void setTelephoneNoResidential(String telephoneNoResidential) {
        this.telephoneNoResidential = telephoneNoResidential;
    }

    public int getState_pos() {
        return state_pos;
    }

    public void setState_pos(int state_pos) {
        this.state_pos = state_pos;
    }

    public int getCountry_pos() {
        return country_pos;
    }

    public void setCountry_pos(int country_pos) {
        this.country_pos = country_pos;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getApplicantRace() {
        return applicantRace;
    }

    public void setApplicantRace(String applicantRace) {
        this.applicantRace = applicantRace;
    }

    public String getApplicantReligion() {
        return applicantReligion;
    }

    public void setApplicantReligion(String applicantReligion) {
        this.applicantReligion = applicantReligion;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public String getNricNew() {
        return nricNew;
    }

    public void setNricNew(String nricNew) {
        this.nricNew = nricNew;
    }

    public byte[] getNric_pic() {
        return nric_pic;
    }

    public void setNric_pic(byte[] nric_pic) {
        this.nric_pic = nric_pic;
    }

    public int getNationality() {
        return nationality;
    }

    public void setNationality(int nationality) {
        this.nationality = nationality;
    }

    public int getRace() {
        return race;
    }

    public void setRace(int race) {
        this.race = race;
    }

    public int getReligion() {
        return religion;
    }

    public void setReligion(int religion) {
        this.religion = religion;
    }

    public int getMarital_stat() {
        return marital_stat;
    }

    public void setMarital_stat(int marital_stat) {
        this.marital_stat = marital_stat;
    }

    public byte[] getPersonal_pic() {
        return personal_pic;
    }

    public void setPersonal_pic(byte[] personal_pic) {
        this.personal_pic = personal_pic;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getApplicantFirstName() {
        return applicantFirstName;
    }

    public void setApplicantFirstName(String applicantFirstName) {
        this.applicantFirstName = applicantFirstName;
    }

    public String getApplicantLastName() {
        return applicantLastName;
    }

    public void setApplicantLastName(String applicantLastName) {
        this.applicantLastName = applicantLastName;
    }

    public String getApplicantDateOfBirth() {
        return applicantDateOfBirth;
    }

    public void setApplicantDateOfBirth(String applicantDateOfBirth) {
        this.applicantDateOfBirth = applicantDateOfBirth;
    }

    public Membership() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isSyncedLocal() {
        return isSyncedLocal;
    }

    public void setSyncedLocal(boolean syncedLocal) {
        isSyncedLocal = syncedLocal;
    }

    public static Membership getCurrentRegistration(Realm realm, String id) {
        return realm.where(Membership.class).equalTo("id", id).equalTo("isSyncedLocal", true)
                .findFirst();
    }

}
