package com.lucabl.activitynavigator;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * Created by Luca on 26/10/2017.
 */

public abstract class StructureFragment extends Fragment {

    Integer navId = null;
    Bundle backArguments = null;
    NotifyTitlesUpdateCallback notifyTitlesUpdateCallback = null;

    public abstract String getTitle(Context context);

    public abstract String getSubtitle(Context context);

    protected void notifyTitlesUpdate() {
        notifyTitlesUpdateCallback.notifyTitlesUpdate();
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
     * Can be called by subclasses to set arguments to pass back to parent when user goes back
     *
     * @param args arguments to set
     */
    protected void setBackArguments(Parcelable... args) {
        backArguments = new Bundle();
        for (int i = 0; i < args.length; i++) {
            backArguments.putParcelable(""+i, args[i]);
        }
    }

    /**
     * Can be called by subclasses to get an argument passed to this fragment
     *
     * @param index index of the wanted argument
     * @return the wanted argument
     */
    protected Parcelable getArgument(int index) {
        Parcelable parc = null;
        Bundle bundle = getArguments();
        if(bundle!=null) {
            parc = bundle.getParcelable(""+index);
        }
        return parc;
    }

    public static abstract class NotifyTitlesUpdateCallback {
        public abstract void notifyTitlesUpdate();
    }
}
