package com.bawanj.criminalIntent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bawanj.criminalIntent.R;
import com.bawanj.criminalIntent.activity.CrimePagerActivity;
import com.bawanj.criminalIntent.model.Crime;
import com.bawanj.criminalIntent.model.CrimeLab;
import com.bawanj.criminalIntent.utils.TimeUtils;

import java.util.List;


public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;

    private static final String SAVED_SUBTITLE_VISIBLE= "subtitle";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // -- setup Menu in the Fragment
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView =(RecyclerView) rootView.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null){
            mSubtitleVisible= savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();

        return rootView;
    }

    private void updateUI(){// setup adapter and connect to recyclerView
        CrimeLab crimeLab= CrimeLab.getInstance();
        List<Crime> crimes= crimeLab.getCrimeList();

        if( mAdapter == null ){
            mAdapter= new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
            mCrimeRecyclerView.setHasFixedSize(true);

        }else{
            mAdapter.setCrimes(crimes); // refresh the latest data
            mAdapter.notifyDataSetChanged();
            // mAdapter.notifyItemChanged();
        }
        updateSubtitle();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outSate){
        super.onSaveInstanceState(outSate);
        outSate.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { // fragmentManager calling
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_crime_list, menu);
        MenuItem subtitleItem= menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ){

            case(R.id.menu_item_new_crime):{
                Crime crime= new Crime();
                CrimeLab.getInstance().addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent( getActivity(), crime.getId());
                startActivity(intent);
                return true;
            }

            case(R.id.menu_item_show_subtitle):{
                mSubtitleVisible= !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void updateSubtitle(){
        CrimeLab crimeLab= CrimeLab.getInstance();
        int crimeSize = crimeLab.getCrimeList().size();

        //String subtitle= getString(R.string.subtitle_format, crimeCount);
        String subtitle= getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);

        if(!mSubtitleVisible){
            subtitle= null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(subtitle);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder( View itemView ){
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView= (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView= (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox= (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        public void bindCrime(Crime crime){
            mCrime= crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(TimeUtils.getCurDateYear(mCrime.getDate().toString()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {

            Intent intent= CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);

        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes= crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater
                    .from(getActivity())
                    .inflate(R.layout.list_item_crime, parent, false);

            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime= mCrimes.get(position);
            holder.bindCrime(crime); // nice idea !!
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }

    }

}
