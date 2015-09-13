package com.bawanj.criminalIntent.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bawanj.criminalIntent.R;
import com.bawanj.criminalIntent.model.Crime;
import com.bawanj.criminalIntent.utils.TimeUtils;


public class CrimeFragment extends Fragment {

    private Crime mCrime;

    @Override
    public void onCreate(Bundle  savedInstanceState ){
        super.onCreate(savedInstanceState);
        mCrime= new Crime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState ){

        View rootView = inflater.inflate( R.layout.fragment_crime , container, false );
        // If 3rd param is false, container is only used to create the correct subclass
        // of LayoutParams for the root view in the XML.
        // 3rd param: whether to add the inflated view to the view's parent,
        // false: attached the view to activity
        final CheckBox mSolvedCheckBox = (CheckBox) rootView.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isChecked) {
                        mCrime.setSolved(isChecked);
                    }
        });
        final Button mDateButton = (Button)rootView.findViewById(R.id.crime_date);
        String srcDateTime= mCrime.getDate().toString();
        mDateButton.setText(TimeUtils.getCurDateYear(srcDateTime));
        mDateButton.setEnabled(false);

        final EditText mTitleFiled = (EditText) rootView.findViewById(R.id.crime_title);
        mTitleFiled.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle( charSequence.toString() );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootView;
    }


}
