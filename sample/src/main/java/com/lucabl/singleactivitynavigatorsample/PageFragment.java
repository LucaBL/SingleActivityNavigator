package com.lucabl.singleactivitynavigatorsample;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lucabl.singleactivitynavigator.StructureFragment;

/**
 * Created by Luca on 19/10/2017.
 */

public class PageFragment extends StructureFragment {

    private MainActivity activity;
    private RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) super.getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_page, container, false);

        RelativeLayout layoutcontainer = layout.findViewById(R.id.container);
        TextView text = layout.findViewById(R.id.text);
        Button button = layout.findViewById(R.id.button);

        layoutcontainer.setBackgroundColor((Integer) getArgument("color"));
        text.setText((String) getArgument("name"));
        Object arg = getArgument("buttonDestination");
        if(arg!=null) {
            button.setText("Change page");
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePage(10, new Bundle());
                }
            });
        }

        return layout;
    }

    @Override
    public String getTitle(Context context) {
        return (String) getArgument("name");
    }

    @Override
    public String getSubtitle(Context context) {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
