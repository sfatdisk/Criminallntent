package com.bawanj.criminalIntent.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bawanj.criminalIntent.R;
import com.bawanj.criminalIntent.model.Crime;
import com.bawanj.criminalIntent.model.CrimeLab;
import com.bawanj.criminalIntent.utils.TimeUtils;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID= "crime_id" ;
    private static final String DIALOG_DATE = "DialogDate" ;

    private static final int REQUEST_DATE= 0;    // setTargetFragment(), requestCode
    private static final int REQUEST_CONTACT= 1; // intent shoot contact app

    private Crime mCrime;

    private Button mDateButton;
    private Button mReportButton;
    private Button mSuspectButton;

    private ImageView mPhotoView;
    private ImageButton mPhotoButton;

    public static CrimeFragment newInstance( UUID crimeId ){

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

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
    public void onPause(){
        super.onPause();
        CrimeLab.getInstance().updateCrime(mCrime);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
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
        // ---
        mDateButton = (Button)rootView.findViewById(R.id.crime_date);
        updateButton();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // TODO -- challenge: make a TimePicker button
                FragmentManager fm = getFragmentManager();

                DatePickerFragment dialog =
                        DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                // a connection with CrimeFragment and DatePickerFragment
                dialog.show(fm, DIALOG_DATE);
            }
        });
        // ---
        mReportButton = (Button)rootView.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // TODO -- Challenge: use ShareCompat to build intent
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent= Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });

        final Intent pickContact  // TODO -- challenge: Another Implicit Intent
                = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton= (Button)rootView.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if( mCrime.getSuspect() != null ){// ?? go to onActivityResult first
            mSuspectButton.setText(mCrime.getSuspect());
        }
        // if user does not have a contact app
        PackageManager packageManager= getActivity().getPackageManager();
        if( packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) ==null ){
            mSuspectButton.setEnabled(false);
        }
        // --
        mPhotoButton = (ImageButton)rootView.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView) rootView.findViewById(R.id.crime_photo);

        // ---
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
        if(requestCode == REQUEST_DATE ){
            Date date= (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateButton();
        }else if(requestCode == REQUEST_CONTACT && data != null ){
            Uri contactUri= data.getData(); // content://com.android.contacts/contacts/lookup/1490i237e619c8ae849e6.1111i4492/896
            // make a query to access the app's content provider
            String[] queryFields= new String[]{ContactsContract.Contacts.DISPLAY_NAME}; // projection "display_name"
            Cursor cursor= getActivity()
                    .getContentResolver()
                    .query(contactUri,queryFields, null, null, null);
            try{
                if(cursor.getCount()==0){
                    return;
                }
                cursor.moveToFirst();
                String suspect= cursor.getString(0);  // "suspect's name"
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }finally {
                cursor.close();
            }
        }
    }

    private void updateButton(){
        String srcTime= mCrime.getDate().toString();
        mDateButton.setText( TimeUtils.getCurDateYear(srcTime) );
    }

    private String getCrimeReport(){ // mReportButton

        String solvedString= null;

        if(mCrime.isSolved()){
            solvedString= getString(R.string.crime_report_no_suspect);
        }else{
            solvedString= getString(R.string.crime_report_suspect);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if( suspect == null ){
            suspect= getString(R.string.crime_report_no_suspect);
        }else{
            suspect= getString(R.string.crime_report_suspect);
        }

        String report= getString(R.string.crime_report,
                mCrime.getTitle(),
                dateString,
                solvedString,
                suspect);

        return report;
    }
}
