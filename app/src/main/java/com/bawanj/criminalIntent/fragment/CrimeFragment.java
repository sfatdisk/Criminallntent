package com.bawanj.criminalIntent.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.bawanj.criminalIntent.model.CrimeLab;
import com.bawanj.criminalIntent.utils.TimeUtils;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID= "crime_id" ;
    private static final String DIALOG_DATE = "DialogDate" ;

    private static final int REQUEST_DATE= 0; // setTargetFragment(), requestCode

    private Crime mCrime;

    private Button mDateButton;

    public static CrimeFragment newInstance( UUID crimeId ){

        Bundle args = new Bundle();
        args.putSerializable( ARG_CRIME_ID , crimeId );

        CrimeFragment crimeFragment= new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle  savedInstanceState ){
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getInstance().getCrime( crimeId );
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
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isChecked) {
                        mCrime.setSolved(isChecked);
                    }
                });

        mDateButton = (Button)rootView.findViewById(R.id.crime_date);
        updateButton();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();

                DatePickerFragment dialog=
                        DatePickerFragment.newInstance( mCrime.getDate() );
                dialog.setTargetFragment( CrimeFragment.this, REQUEST_DATE );
                // a connection with CrimeFragment and DatePickerFragment
                dialog.show(fm , DIALOG_DATE);
            }
        });

        final EditText mTitleFiled = (EditText) rootView.findViewById(R.id.crime_title);
        mTitleFiled.setText(mCrime.getTitle());
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

    @Override // from DatePickerFragment.sendResult()
    public void onActivityResult( int requestCode, int resultCode, Intent data ){
        if(resultCode != Activity.RESULT_OK){
            return ;
        }
        if(requestCode==REQUEST_DATE){
            Date date= (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateButton();
        }
    }

    private void updateButton(){
        String srcTime= mCrime.getDate().toString();
        mDateButton.setText( TimeUtils.getCurDateYear(srcTime) );
    }


}
