package com.bawanj.criminalIntent.activity;

import android.support.v4.app.Fragment;

import com.bawanj.criminalIntent.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
