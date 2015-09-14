package com.bawanj.criminalIntent.model;

import android.content.Context;

import com.bawanj.criminalIntent.CrimeApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jeffreychou on 9/12/15.
 *
 */
public class CrimeLab {  // create mCrimeList

    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimeList;

    private CrimeLab(Context context){
        mCrimeList= new ArrayList<>();

        for(int i=0; i<100; ++i){
            Crime crime= new Crime();
            crime.setTitle("Crime #"+i );
            crime.setSolved(i % 2 == 0 );
            mCrimeList.add(crime);
        }
    }

    public static synchronized CrimeLab getInstance(){
        if(sCrimeLab == null){
            sCrimeLab= new CrimeLab(CrimeApplication.getGlobalContext());
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimeList() {
        return mCrimeList;
    }

    public Crime getCrime( UUID id ){

        for(Crime crime : mCrimeList){
            if( crime.getId().equals(id) ){
                return crime;
            }
        }
        return null;
    }

}
