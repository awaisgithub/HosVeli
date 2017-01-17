package com.od.mma.Membership;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.od.mma.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by awais on 11/01/2017.
 */

public class PostgradDegreeDialogFrag extends DialogFragment {
    View rootView;
    Calendar myCalendar;
    EditText uni_name;
    EditText date_pik;
    Spinner degree;
    Spinner country;
    Spinner uni;
    Button add;
    FragPostGradInterface pos_grad_interface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_membership_qualification_postgraduate_dialog, container, false);

        initView();

        return rootView;
    }


    private void initView() {
        getDialog().setTitle("Post Graduate");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);


        add = (Button) rootView.findViewById(R.id.add_button);
        uni_name = (EditText) rootView.findViewById(R.id.name);
        date_pik = (EditText) rootView.findViewById(R.id.date_qualify);
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date_pik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        degree = (Spinner) rootView.findViewById(R.id.id_bach);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.id_bachelor, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        degree.setPrompt("Select Employment Status");
        degree.setAdapter(
                new NoneSelectSpinnerAdapter(adapter, R.layout.spinner_hint_frag8_1,
                        getActivity()));

        country = (Spinner) rootView.findViewById(R.id.id_uni);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.id_nationality, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        country.setPrompt("Select Country");
        country.setAdapter(
                new NoneSelectSpinnerAdapter(adapter1, R.layout.spinner_hint_frag8_2,// R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));
        country.setSelection(1);

        uni = (Spinner) rootView.findViewById(R.id.id_uni_malaysia);                                  //change array here
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.id_bachelor, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        uni.setPrompt("Select Employment Status");
        uni.setAdapter(
                new NoneSelectSpinnerAdapter(adapter2, R.layout.spinner_hint_frag8_3,
                        getActivity()));

        degree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            switch (PagerViewPager.membership.getPos_count()) {
                                case 0:
                                    PagerViewPager.membership.setPos_degree(degree.getSelectedItem().toString());
                                    break;
                                case 1:
                                    PagerViewPager.membership.setPos_degree1(degree.getSelectedItem().toString());
                                    break;
                                case 2:
                                    PagerViewPager.membership.setPos_degree2(degree.getSelectedItem().toString());
                                    break;
                                default:
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (position == 1 || country.getSelectedItem().toString().equals("MALAYSIA")) {
                        uni.setVisibility(View.VISIBLE);
                        uni_name.setVisibility(View.GONE);
                    } else {
                        uni_name.setVisibility(View.VISIBLE);
                        uni.setVisibility(View.GONE);
                    }
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            switch (PagerViewPager.membership.getPos_count()) {
                                case 0:
                                    PagerViewPager.membership.setPos_country(country.getSelectedItem().toString());
                                    break;
                                case 1:
                                    PagerViewPager.membership.setPos_country1(country.getSelectedItem().toString());
                                    break;
                                case 2:
                                    PagerViewPager.membership.setPos_country2(country.getSelectedItem().toString());
                                    break;
                                default:
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            switch (PagerViewPager.membership.getPos_count()) {
                                case 0:
                                    PagerViewPager.membership.setPos_uni(uni.getSelectedItem().toString());
                                    break;
                                case 1:
                                    PagerViewPager.membership.setPos_uni1(uni.getSelectedItem().toString());
                                    break;
                                case 2:
                                    PagerViewPager.membership.setPos_uni2(uni.getSelectedItem().toString());
                                    break;
                                default:
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uni_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (!(s.equals("") || s.toString().isEmpty())) {

                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            switch (PagerViewPager.membership.getPos_count()) {
                                case 0:
                                    PagerViewPager.membership.setPos_uni(s.toString());
                                    break;
                                case 1:
                                    PagerViewPager.membership.setPos_uni1(s.toString());
                                    break;
                                case 2:
                                    PagerViewPager.membership.setPos_uni2(s.toString());
                                    break;
                                default:
                            }
                        }
                    });
                } else {
                    uni_name.setError("Enter University Name");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        date_pik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (!(s.equals("") || s.toString().isEmpty())) {
                    PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            switch (PagerViewPager.membership.getPos_count()) {
                                case 0:
                                    PagerViewPager.membership.setPos_qof_date(s.toString());
                                    break;
                                case 1:
                                    PagerViewPager.membership.setPos_qof_date1(s.toString());
                                    break;
                                case 2:
                                    PagerViewPager.membership.setPos_qof_date2(s.toString());
                                    break;
                                default:
                            }
                        }
                    });
                } else {
                    date_pik.setError("Enter Date");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagerViewPager.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if (PagerViewPager.membership.getPos_count() < 3) {
                            //check if all values are selected
                            if (AllValues()) {
                                PagerViewPager.membership.setPos_count(PagerViewPager.membership.getPos_count() + 1);
                                pos_grad_interface.Added();
                                dismiss();
                            } else {
                                showActionSnackBarMessage("Please, Fill all values.");
                                Toast.makeText(getActivity(), "Please, Fill all values.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }
        });

    }

    private boolean AllValues() {
        if (country.getSelectedItemPosition() == 0) {
            return false;
        } else if (country.getSelectedItem().toString().equals("MALAYSIA") || country.getSelectedItemPosition() == 1) {
            if (uni.getSelectedItemPosition() == 0) {
                return false;
            }
        } else {
            if (uni_name.getText().toString().equals("") || uni_name.getText().toString().isEmpty()) {
                return false;
            }
        }


        if (degree.getSelectedItemPosition() == 0) {
            return false;
        }
        if (date_pik.getText().toString().equals("") || date_pik.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    public void context_interface(PostgradDegreeFrag membershipPostgraduateDegreeFragment) {
        pos_grad_interface = (PostgradDegreeFrag) membershipPostgraduateDegreeFragment;
    }

    private void showActionSnackBarMessage(String message) {
        View view = rootView.findViewById(R.id.parent_layout_pos);
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setMaxLines(2);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_pik.setText(sdf.format(myCalendar.getTime()));
    }

    public static PostgradDegreeDialogFrag newInstance() {
        PostgradDegreeDialogFrag f = new PostgradDegreeDialogFrag();
        return f;
    }

}


