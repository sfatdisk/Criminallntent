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
    //TODO- can setup a hashMap for lookup the crime object

    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimeList;

    private CrimeLab(Context context){
        mCrimeList= new ArrayList<>();
    }

    public static synchronized CrimeLab getInstance(){
        if(sCrimeLab == null){
            sCrimeLab= new CrimeLab(CrimeApplication.getGlobalContext());
        }
        return sCrimeLab;
    }

    public void addCrime(Crime c){
        mCrimeList.add(c);
    }

    public List<Crime> getCrimeList() {
        return mCrimeList;
    }

    public Crime getCrime( UUID id ){ // use hashMap

        for(Crime crime : mCrimeList){
            if( crime.getId().equals(id) ){
                return crime;
            }
        }
        return null;
    }

}
