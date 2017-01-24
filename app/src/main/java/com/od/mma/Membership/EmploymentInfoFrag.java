package com.od.mma.Membership;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.od.mma.BOs.User;
import com.od.mma.R;

import io.realm.Realm;

import static com.od.mma.MMAApplication.realm;

/**
 * Created by awais on 29/12/2016.
 */

public class EmploymentInfoFrag extends Fragment {
    Spinner emp;
    Spinner prac;
    Spinner prac_sub;
    Button next, back;
    boolean error = false;
    FragInterface mem_interface;
    ArrayAdapter<CharSequence> temp = null;
    private View rootView;
    Membership membership;

    public static EmploymentInfoFrag newInstance(String text) {
        EmploymentInfoFrag f = new EmploymentInfoFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_employment, container, false);
        initView();

        return rootView;
    }

    private void initView() {
        membership = Membership.getCurrentRegistration(realm, User.getCurrentUser(realm).getId());
        prac_sub = (Spinner) rootView.findViewById(R.id.id_prac_sub);
        prac_sub.setVisibility(View.GONE);
        prac = (Spinner) rootView.findViewById(R.id.id_prac);
        prac.setVisibility(View.GONE);
        emp = (Spinner) rootView.findViewById(R.id.id_emp);
        emp.setGravity(1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_emp, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        emp.setPrompt("Select Employment Status");
        emp.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag12_1,
                        getActivity()));
        final ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.id_practice, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        prac.setPrompt("Select Practice Nature");
        prac.setAdapter(
                new NoneSelectSpinnerAdapter(adapter1, R.layout.spinner_hint_frag12_2,
                        getActivity()));

        prac_sub.setGravity(1);
        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.id_practice_sub, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        prac_sub.setPrompt("Select Practice Nature Sub Category");
        prac_sub.setAdapter(
                new NoneSelectSpinnerAdapter(adapter2, R.layout.spinner_hint_frag12_3,
                        getActivity()));

        if (!membership.isLoadFromServer()) {
            if (membership.getEmp_status() != -1) {
                emp.setSelection(membership.getEmp_status());
            }
        } else {
            int spinnerPosition;
            spinnerPosition = adapter.getPosition(membership.getEmploymentStatusSelectBox());
            emp.setSelection(spinnerPosition + 1);
            if (membership.getEmploymentStatusSelectBox().contains("Specialist")) {
                prac_sub.setVisibility(View.VISIBLE);
                prac.setVisibility(View.VISIBLE);
            }
            else {
                prac_sub.setVisibility(View.GONE);
                prac.setVisibility(View.GONE);
            }
        }
        if (!membership.isLoadFromServer()) {
            if (membership.getEmp_prac() != -1) {
                prac.setSelection(membership.getEmp_prac());
            }
        } else {
            int spinnerPosition;
            spinnerPosition = adapter.getPosition(membership.getPracticeNature());
            prac.setSelection(spinnerPosition + 1);
        }
        if (!membership.isLoadFromServer()) {
            if (membership.getEmp_prac_sub() != -1) {
                setSpinnerArray(prac.getSelectedItem().toString());
                prac_sub.setSelection(membership.getEmp_prac_sub());
            }
        } else {
            int spinnerPosition;
            spinnerPosition = adapter.getPosition(membership.getPracticeNatureSubCategory());
            prac_sub.setSelection(spinnerPosition + 1);
        }


        if (membership.isValidation()) {
            loadItems();
        }


        listeners();
        navigate();

    }

    private void listeners() {
        emp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setEmp_status(emp.getSelectedItemPosition());
                            membership.setEmploymentStatusSelectBox(emp.getSelectedItem().toString());
                        }
                    });
                    String result = emp.getSelectedItem().toString();
                    if (result.contains("Specialist")) {
                        prac_sub.setVisibility(View.VISIBLE);
                        prac.setVisibility(View.VISIBLE);
                    } else {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                membership.setEmp_prac(-1);
                                membership.setPracticeNature("");
                                membership.setEmp_prac_sub(-1);
                                membership.setPracticeNatureSubCategory("");
                                prac.setSelection(0);
                                prac_sub.setSelection(0);
                            }
                        });
                        prac_sub.setVisibility(View.GONE);
                        prac.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setEmp_prac(prac.getSelectedItemPosition());
                            membership.setPracticeNature(prac.getSelectedItem().toString());
                        }
                    });
                    String result = prac.getSelectedItem().toString();
                    setSpinnerArray(result.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prac_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            membership.setEmp_prac_sub(prac_sub.getSelectedItemPosition());
                            membership.setPracticeNatureSubCategory(prac_sub.getSelectedItem().toString());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void navigate() {
        next = (Button) rootView.findViewById(R.id.next_button1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
                mem_interface.onFragmentNav(FragInterface.MembershipToNav.NEXT);
            }
        });

        back = (Button) rootView.findViewById(R.id.back_button1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mem_interface.onFragmentNav(FragInterface.MembershipToNav.BACK);
            }
        });
    }

    private void setSpinnerArray(String s) {
        ArrayAdapter<CharSequence> temp = null;
        switch (s) {
            case "ANAESTHESIOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_ANAESTHESIOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "EMERGENCY MEDICINE":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_EMERGENCY_MEDICINE, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "FAMILY MEDICINE":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_FAMILY_MEDICINE, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "MEDICINE":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_MEDICINE, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "NUCLEAR MEDICINE":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_NUCLEAR_MEDICINE, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "OBSTETRICS AND GYNAECOLOGY (O & G)":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_OBSTETRICS_AND_GYNAECOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "ONCOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_ONCOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "OPHTHALMOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_OPHTHALMOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "OTORHINOLARYNGOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_OTORHINOLARYNGOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PAEDIATRICS":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PAEDIATRICS, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PATHOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PATHOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PSYCHIATRY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PSYCHIATRY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PUBLIC HEALTH":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PUBLIC_HEALTH, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "RADIOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_RADIOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "REHABILITATION MEDICINE":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_REHABILITATION_MEDICINE, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "SPORTS MEDICINE":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_SPORTS_MEDICINE, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "SURGERY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_SURGERY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "UROLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_UROLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "DENTAL SURGERY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_DENTAL_SURGERY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "OCCUPATIONAL HEALTH":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_OCCUPATIONAL_HEALTH, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "ORTHOPAEDIC SURGERY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_ORTHOPAEDIC_SURGERY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PARASITOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PARASITOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PHARMACOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PHARMACOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PHYSIOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PHYSIOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "MEDICAL ADMINISTRATION":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_MEDICAL_ADMINISTRATION, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PSYCOHOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PSYCOHOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "RADIOTHERAPY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_RADIOTHERAPY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "OTHERS":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_OTHERS, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "AVIATION MEDICINE":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_AVIATION_MEDICINE, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "PRIMARY CARE PHYSICIAN":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_PRIMARY_CARE_PHYSICIAN, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
            case "IMMUNOLOGY":
                temp = ArrayAdapter.createFromResource(getActivity(), R.array.id_prac_sub_IMMUNOLOGY, R.layout.spinner_item);
                temp.setDropDownViewResource(R.layout.spinner_dropdown_item);
                prac_sub.setPrompt("Select Practice Nature Sub Category");
                prac_sub.setAdapter(
                        new NoneSelectSpinnerAdapter(temp, R.layout.spinner_hint_frag12_3,
                                getActivity()));
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mem_interface = (FragInterface) context;
    }

    private void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
    }

    public void validation() {
//        if (emp.getSelectedItemPosition() > 0) {
//            error = true;
//        } else {
//            error = false;
//            if (membership.getValidation_pos() == -1) {
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        membership.setValidation_pos(PagerViewPager.getPos());
//                    }
//                });
//            }
//        }
    }

    private void loadItems() {
//        if (emp.getSelectedItemPosition() > 0) {
//            error = true;
//        } else {
//            error = false;
//            showActionSnackBarMessage(getString(R.string.error_emp_status));
//        }
    }
}
