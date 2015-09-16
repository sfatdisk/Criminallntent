package com.bawanj.criminalIntent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bawanj.criminalIntent.CrimeApplication;
import com.bawanj.criminalIntent.database.CrimeBaseHelper;
import com.bawanj.criminalIntent.database.CrimeCursorWrapper;
import com.bawanj.criminalIntent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CrimeLab {  // create mCrimeList
    //TODO- can setup a hashMap for lookup the crime object

    private static CrimeLab sCrimeLab;

    // database
    private Context mContext;
    private SQLiteDatabase mDatabase;


    private CrimeLab(Context context){
        mContext= context;
        mDatabase= new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public static synchronized CrimeLab getInstance(){
        if(sCrimeLab == null){
            sCrimeLab= new CrimeLab(CrimeApplication.getGlobalContext());
        }
        return sCrimeLab;
    }

    public void addCrime(Crime c){ // inserting a crime
        ContentValues values= getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null , values);
    }

    public List<Crime> getCrimeList() {

        List<Crime> crimeList= new ArrayList<>();
        CrimeCursorWrapper cursorWrapper= queryCrimes(null,null); // compare to getCrime

        try{
            cursorWrapper.moveToFirst();
            while( !cursorWrapper.isAfterLast() ){
                crimeList.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return crimeList;
    }

    public Crime getCrime( UUID id ){ // use hashMap

        CrimeCursorWrapper cursorWrapper
                = queryCrimes( CrimeTable.Cols.UUID+"=?", new String[]{ id.toString() } );
        try{
            if( cursorWrapper.getCount() ==0 ){
                return null;
            }
            cursorWrapper.moveToFirst(); // ??
            return cursorWrapper.getCrime();

        }finally {
            // cursorWrapper.moveToFirst(); how about it?
            cursorWrapper.close();
        }
    }

    public void updateCrime( Crime crime ){ // updating a crime
        String uuidStr= crime.getId().toString();
        ContentValues updateValues= getContentValues(crime);
        mDatabase.update( CrimeTable.NAME,
                updateValues,
                CrimeTable.Cols.UUID+"=?",
                new String[]{ uuidStr } );
    }

    // Creating a ContentValues
    private static ContentValues getContentValues( Crime crime ){
        ContentValues values= new ContentValues();
        values.put(CrimeTable.Cols.UUID,   crime.getId().toString() ); // String= UUID.toString() <-> UUID= UUID.fromString(String)
        values.put(CrimeTable.Cols.TITLE,  crime.getTitle() );
        values.put(CrimeTable.Cols.DATE,   crime.getDate().getTime() ); // long
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0 );  // int
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect() );
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs ){
        Cursor cursor= mDatabase.query(
                        CrimeTable.NAME,
                        null, // select all columns
                        whereClause,
                        whereArgs,
                        null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

}
