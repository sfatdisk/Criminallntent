package com.bawanj.criminalIntent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bawanj.criminalIntent.database.CrimeDbSchema.CrimeTable;
import com.bawanj.criminalIntent.model.Crime;

import java.util.Date;
import java.util.UUID;


public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        // parse cursor
        String uuidString= getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title= getString(getColumnIndex(CrimeTable.Cols.TITLE));
        Long date= getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved= getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String  suspect= getString(getColumnIndex(CrimeTable.Cols.SUSPECT) );

        // make crime, Parses a UUID string with the format defined by {@link #toString()}.
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved( isSolved !=0 ); // args is boolean, isSolved is int
        crime.setSuspect(suspect);
        return crime;
    }
}
