package com.lucabl.activitynavigator;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created by Luca on 27/10/2017.
 */

public abstract class StructureFragmentFactory {
    public abstract StructureFragment getFragment(Bundle arguments);

    public StructureFragment getFragment(Parcelable... args) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < args.length; i++) {
            bundle.putParcelable(""+i, args[i]);
        }
        return getFragment(bundle);
    }
}
