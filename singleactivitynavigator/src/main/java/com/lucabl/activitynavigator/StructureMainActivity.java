package com.lucabl.activitynavigator;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class StructureMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int HOME_FRAGMENT_NAVID = -100;
    private SparseArray<StructureFragmentFactory> navFactories;
    private SparseIntArray navParents;
    private Toolbar toolbar;
    private DrawerLayout drawer, drawerFake;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private boolean isHomeAsUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // we set a fake drawer to avoid unwanted icon changes - because we're not using normal fragment backstack for navigation
        drawerFake = new DrawerLayout(this);
        toggle = new ActionBarDrawerToggle(
                this, drawerFake, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerFake.setDrawerListener(toggle);
        toggle.syncState();

        // overrides normal behaviour to support the true drawer
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHomeAsUp) onBackPressed();
                else {
                    int drawerLockMode = drawer.getDrawerLockMode(GravityCompat.START);
                    if (drawer.isDrawerVisible(GravityCompat.START)
                            && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            }
        });

        navFactories = new SparseArray<>();
        navParents = new SparseIntArray();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void addNavigationRule(int navId, Integer parentNavId, StructureFragmentFactory factory) {
        navFactories.put(navId, factory);
        navParents.put(navId, parentNavId==null ? HOME_FRAGMENT_NAVID : parentNavId);
    }

    protected void setNavigationMenu(int headerId, int menuId) {
        navigationView.inflateHeaderView(headerId);
        navigationView.inflateMenu(menuId);
    }

    protected void setHomeFragment(StructureFragmentFactory factory) {
        navFactories.put(HOME_FRAGMENT_NAVID, factory);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, factory.getFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            StructureFragment frag = (StructureFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(!frag.onBackPressed()) {

                Integer parentNavId = frag.navId==null ? null : navParents.get(frag.navId);
                if (parentNavId!=null) {
                    MenuItem item = navigationView.getMenu().findItem(frag.navId);
                    if(item!=null) item.setChecked(false);

                    changePage(parentNavId, frag.backArguments);
                }
                else super.onBackPressed();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.drawer, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        changePage(item.getItemId());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    StructureFragment.NotifyTitlesUpdateCallback titlesUpdateCallback = new StructureFragment.NotifyTitlesUpdateCallback() {
        @Override
        public void notifyTitlesUpdate() {
            updateTitles();
        }
    };

    public void changePage(int id, Parcelable... args) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < args.length; i++) {
            bundle.putParcelable(""+i, args[i]);
        }
        changePage(id, bundle);
    }

    public void changePage(int id, Bundle bundle) {

        if(id == HOME_FRAGMENT_NAVID) {
            StructureFragment frag = navFactories.get(HOME_FRAGMENT_NAVID).getFragment(bundle);
            frag.notifyTitlesUpdateCallback = titlesUpdateCallback;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, frag).commit();

            toolbar.setTitle(frag.getTitle(this));
            toolbar.setSubtitle(frag.getSubtitle(this));
            //show hamburger
            setHomeAsUp(false);
        }
        else {

            boolean found = false;
            int i = 0, size = navFactories.size(), navId;

            while (!found && i < size) {
                navId = navFactories.keyAt(i);
                if (id == navId) {
                    found = true;
                    StructureFragment frag = navFactories.valueAt(i).getFragment(bundle);
                    frag.navId = navId;
                    frag.notifyTitlesUpdateCallback = titlesUpdateCallback;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, frag).commit();

                    MenuItem item = navigationView.getMenu().findItem(frag.navId);
                    if(item!=null && item.isCheckable() && !item.isChecked()) item.setChecked(true);

                    toolbar.setTitle(frag.getTitle(this));
                    toolbar.setSubtitle(frag.getSubtitle(this));
                    setHomeAsUp(true);
                }
                i++;
            }
        }
    }

    private void updateTitles() {
        StructureFragment frag = (StructureFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        toolbar.setTitle(frag.getTitle(this));
        toolbar.setSubtitle(frag.getSubtitle(this));
    }

    // call this method for animation between hamburger and arrow
    protected void setHomeAsUp(boolean isHomeAsUp){
        if (this.isHomeAsUp != isHomeAsUp) {
            this.isHomeAsUp = isHomeAsUp;

            ValueAnimator anim = isHomeAsUp ? ValueAnimator.ofFloat(0, 1) : ValueAnimator.ofFloat(1, 0);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = (Float) valueAnimator.getAnimatedValue();
                    toggle.onDrawerSlide(drawerFake, slideOffset);
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            // You can change this duration to more closely match that of the default animation.
            anim.setDuration(500);
            anim.start();
        }
        else if(isHomeAsUp) {

            ValueAnimator anim = ValueAnimator.ofFloat(1, 0.5f);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = (Float) valueAnimator.getAnimatedValue();
                    toggle.onDrawerSlide(drawerFake, slideOffset);
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            // You can change this duration to more closely match that of the default animation.
            anim.setDuration(250);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if(animation.getAnimatedFraction() == 1) {
                        ValueAnimator anim = ValueAnimator.ofFloat(0.5f, 1);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                                toggle.onDrawerSlide(drawerFake, slideOffset);
                            }
                        });
                        anim.setInterpolator(new DecelerateInterpolator());
                        // You can change this duration to more closely match that of the default animation.
                        anim.setDuration(250);
                        anim.start();
                    }
                }
            });
            anim.start();
        }
    }
}
