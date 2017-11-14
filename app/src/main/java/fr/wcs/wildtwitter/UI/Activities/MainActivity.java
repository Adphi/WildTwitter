package fr.wcs.wildtwitter.UI.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.wcs.wildtwitter.GlideApp;
import fr.wcs.wildtwitter.MessagesFragment;
import fr.wcs.wildtwitter.R;
import fr.wcs.wildtwitter.SearchFragment;
import fr.wcs.wildtwitter.UI.Fragments.TweetsFragment;
import fr.wcs.wildtwitter.Utils.Constants;

public class MainActivity extends AppCompatActivity {

    private final String TAG = Constants.TAG;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * mPagerFragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ArrayList<Fragment> mPagerFragments = new ArrayList<>();

    private int mBackButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton floatingActionButtonWrite = findViewById(R.id.floatingActionButtonWrite);
        floatingActionButtonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WriteTweetActivity.class));
            }
        });

        mPagerFragments.add(Fragment.instantiate(this, TweetsFragment.class.getName()));
        mPagerFragments.add(Fragment.instantiate(this, SearchFragment.class.getName()));
        mPagerFragments.add(Fragment.instantiate(this, MessagesFragment.class.getName()));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mPagerFragments);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        final TabLayout tabLayout = findViewById(R.id.tabs);
        final int unselectedColor = Color.GRAY;
        final PorterDuff.Mode filterMode = PorterDuff.Mode.SRC_IN;
        tabLayout.getTabAt(1).getIcon().setColorFilter(unselectedColor, filterMode);
        tabLayout.getTabAt(2).getIcon().setColorFilter(unselectedColor, filterMode);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                mBackButtonCount = 0;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, false);
            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                mBackButtonCount = 0;
                if (tab != null) {
                    tab.select();
                }
                switch (position) {
                    case 0:
                        tabLayout.getTabAt(0).getIcon().clearColorFilter();
                        tabLayout.getTabAt(1).getIcon().setColorFilter(unselectedColor, filterMode);
                        tabLayout.getTabAt(2).getIcon().setColorFilter(unselectedColor, filterMode);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).getIcon().setColorFilter(unselectedColor, filterMode);
                        tabLayout.getTabAt(1).getIcon().clearColorFilter();
                        tabLayout.getTabAt(2).getIcon().setColorFilter(unselectedColor, filterMode);
                        break;
                    case 2:
                        tabLayout.getTabAt(0).getIcon().setColorFilter(unselectedColor, filterMode);
                        tabLayout.getTabAt(1).getIcon().setColorFilter(unselectedColor, filterMode);
                        tabLayout.getTabAt(2).getIcon().clearColorFilter();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }
        };

        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "onCreate: User Data: " + user.getPhotoUrl() + " " + user);

        CircleImageView avatarViewUser = findViewById(R.id.avatarViewUser);
        GlideApp.with(this)
                .load(user.getPhotoUrl())
                .into(avatarViewUser);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {
            mAuth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mFragments;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mPagerFragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mPagerFragments.size();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBackButtonCount = 0;
    }

    @Override
    public void onBackPressed() {
        if(mBackButtonCount > 0) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(mViewPager.getCurrentItem() == 0){
            Toast.makeText(this, R.string.exit_confirmation, Toast.LENGTH_SHORT).show();
            mBackButtonCount++;
        }
        else {
            mViewPager.setCurrentItem(0);
        }
    }
}
