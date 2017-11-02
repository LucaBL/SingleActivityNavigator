package com.lucabl.singleactivitynavigatorsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.lucabl.activitynavigator.StructureFragment;
import com.lucabl.activitynavigator.StructureFragmentFactory;
import com.lucabl.activitynavigator.StructureMainActivity;

public class MainActivity extends StructureMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNavigationMenu(R.layout.nav_header_drawer, R.menu.activity_drawer_drawer);

        setHomeFragment(new StructureFragmentFactory() {
            @Override
            public StructureFragment getFragment(Bundle arguments) {
                PageFragment home = new PageFragment();
                arguments.putInt("color", Color.GREEN);
                arguments.putString("name", "Home");
                home.setArguments(arguments);
                return home;
            }
        });

        addNavigationRule(R.id.nav_page1, null, new StructureFragmentFactory() {
            @Override
            public StructureFragment getFragment(Bundle arguments) {
                PageFragment frag = new PageFragment();
                arguments.putInt("color", Color.BLUE);
                arguments.putString("name", "Page 1");
                frag.setArguments(arguments);
                return frag;
            }
        });

        addNavigationRule(R.id.nav_page2, null, new StructureFragmentFactory() {
            @Override
            public StructureFragment getFragment(Bundle arguments) {
                PageFragment frag = new PageFragment();
                arguments.putInt("color", Color.YELLOW);
                arguments.putString("name", "Page 2");
                frag.setArguments(arguments);
                return frag;
            }
        });

        addNavigationRule(R.id.nav_page3, null, new StructureFragmentFactory() {
            @Override
            public StructureFragment getFragment(Bundle arguments) {
                PageFragment frag = new PageFragment();
                arguments.putInt("color", Color.MAGENTA);
                arguments.putString("name", "Page 3");
                arguments.putInt("buttonDestination", 10);
                frag.setArguments(arguments);
                return frag;
            }
        });

        addNavigationRule(10, R.id.nav_page3, new StructureFragmentFactory() {
            @Override
            public StructureFragment getFragment(Bundle arguments) {
                PageFragment frag = new PageFragment();
                arguments.putInt("color", Color.WHITE);
                arguments.putString("name", "Page 3b");
                frag.setArguments(arguments);
                return frag;
            }
        });
    }
}
