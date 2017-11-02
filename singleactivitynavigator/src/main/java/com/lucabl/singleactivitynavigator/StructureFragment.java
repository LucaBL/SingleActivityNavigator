package com.lucabl.singleactivitynavigator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Luca on 26/10/2017.
 */

public abstract class StructureFragment extends Fragment {

    Integer navId = null;
    Bundle backArguments = null;
    MainActivityCallback mainActivityCallback = null;

    public abstract String getTitle(Context context);

    public abstract String getSubtitle(Context context);

    protected void notifyTitlesUpdate() {
        mainActivityCallback.notifyTitlesUpdate();
    }

    protected void changePage(int id, Bundle bundle) {
        mainActivityCallback.requestPageChange(id, bundle);
    }

    /**
     * Can be overridden by subclasses to handle the pression of the back button
     *
     * @return true if the press has been handled and should not be handled by parent activity
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * Can be called by subclasses to get (and then set more inside) arguments to pass back to parent when user goes back
     *
     * @return bundle of arguments
     */
    protected Bundle getBackArguments() {
        if(backArguments==null) backArguments = new Bundle();
        return backArguments;
    }

    /**
     * Can be called by subclasses to get an argument passed to this fragment
     *
     * @param key key of the wanted argument
     * @return the wanted argument
     */
    protected Object getArgument(String key) {
        Object obj = null;
        Bundle bundle = getArguments();
        if(bundle!=null) {
            obj = bundle.get(key);
        }
        return obj;
    }

    public static abstract class MainActivityCallback {

        public abstract void notifyTitlesUpdate();

        public abstract void requestPageChange(int id, Bundle bundle);
    }
}
