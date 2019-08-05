package com.hiya.sample.base;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BaseFragment extends Fragment {

    protected void setToolbarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }


    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

}
