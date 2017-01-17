package com.od.mma.Membership;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by awais on 04/01/2017.
 */

public class Membership extends RealmObject {
    private String main_category = "";
    private boolean validation = false;
    private int validation_pos = -1;

    //2nd
    private int category = -1;
    private String category_name = "";
    private int year = -1;
    private boolean mom = false;
    //3rd
    private int title = -1;
    private byte[] personal_pic = null;
    private String fName = "";
    private String lName = "";
    private String dob = "";
    private int gender = -1;
    private int marital_stat = -1;
    //4th
    private int id_type = -1;
    private String id_no = "";
    private byte[] nric_pic = null;
    private int nationality = -1;
    private int race = -1;
    private String other_race = "";
    private int religion = -1;
    private String other_religion = "";
    //5th
    private int country = -1;
    private int state = -1;
    private int reg_state = -1;
    private String city = "";
    private String postal_code = "";
    private String address = "";
    private String tel_no = "";
    //6th
    private int r_country = -1;
    private int r_state = -1;
    private String r_city = "";
    private String r_postal_code = "";
    private String r_address = "";
    private String r_tel_no = "";
    private int correspondence_address = -1;
    //7th
    private int joint_account = -1;
    private int spouse_id_type = -1;
    private String nric_no = "";
    private String spouse_fName = "";
    private String spouse_email = "";
    //8th
    private int bachelor_degree = -1;
    private String bachelor_uni = "";
    private int bachelor_uni_malay = -1;
    private int bachelor_country = -1;
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
    private String mmc_reg_no = "";
    private String mmc_dob = "";
    private byte[] mmc_certificate = null;
    //11th
    private int uni_no = -1;
    private String uni_year_complete = "";
    private byte[] uni_student_card = null;
    //12th
    private int emp_status = -1;
    private int emp_prac = -1;
    private int emp_prac_sub = -1;
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

    public int getBachelor_uni_malay() {
        return bachelor_uni_malay;
    }

    public void setBachelor_uni_malay(int bachelor_uni_malay) {
        this.bachelor_uni_malay = bachelor_uni_malay;
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

    public int getReg_state() {
        return reg_state;
    }

    public void setReg_state(int reg_state) {
        this.reg_state = reg_state;
    }

    public boolean isMom() {
        return mom;
    }

    public void setMom(boolean mom) {
        this.mom = mom;
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

    public String getUni_year_complete() {
        return uni_year_complete;
    }

    public void setUni_year_complete(String uni_year_complete) {
        this.uni_year_complete = uni_year_complete;
    }

    public byte[] getUni_student_card() {
        return uni_student_card;
    }

    public void setUni_student_card(byte[] uni_student_card) {
        this.uni_student_card = uni_student_card;
    }

    public String getMmc_reg_no() {
        return mmc_reg_no;
    }

    public void setMmc_reg_no(String mmc_reg_no) {
        this.mmc_reg_no = mmc_reg_no;
    }

    public String getMmc_dob() {
        return mmc_dob;
    }

    public void setMmc_dob(String mmc_dob) {
        this.mmc_dob = mmc_dob;
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

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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

    public String getNric_no() {
        return nric_no;
    }

    public void setNric_no(String nric_no) {
        this.nric_no = nric_no;
    }

    public String getSpouse_fName() {
        return spouse_fName;
    }

    public void setSpouse_fName(String spouse_fName) {
        this.spouse_fName = spouse_fName;
    }

    public String getSpouse_email() {
        return spouse_email;
    }

    public void setSpouse_email(String spouse_email) {
        this.spouse_email = spouse_email;
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

    public String getR_city() {
        return r_city;
    }

    public void setR_city(String r_city) {
        this.r_city = r_city;
    }

    public String getR_postal_code() {
        return r_postal_code;
    }

    public void setR_postal_code(String r_postal_code) {
        this.r_postal_code = r_postal_code;
    }

    public String getR_address() {
        return r_address;
    }

    public void setR_address(String r_address) {
        this.r_address = r_address;
    }

    public String getR_tel_no() {
        return r_tel_no;
    }

    public void setR_tel_no(String r_tel_no) {
        this.r_tel_no = r_tel_no;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getOther_race() {
        return other_race;
    }

    public void setOther_race(String other_race) {
        this.other_race = other_race;
    }

    public String getOther_religion() {
        return other_religion;
    }

    public void setOther_religion(String other_religion) {
        this.other_religion = other_religion;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
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

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public static Membership getCurrentRegistration(Realm realm) {
        return realm.where(Membership.class).equalTo("isSyncedLocal", true)
                .findFirst();
    }
}
