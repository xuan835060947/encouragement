package com.person.xuan.encouragement.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

/**
 * Created by chenxiaoxuan1 on 15/12/17.
 */
public class BaseFragment extends Fragment {
    /**
     * search view
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int id) {
        Activity activity = getActivity();
        if (activity != null) {
            return (T)getActivity().findViewById(id);
        }
        return null;
    }
}
