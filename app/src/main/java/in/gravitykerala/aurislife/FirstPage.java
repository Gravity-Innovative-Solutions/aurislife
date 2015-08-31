/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.gravitykerala.aurislife;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;


public class FirstPage extends AppCompatActivity {

    //    String uid;
//    String token;
    // public static MobileServiceClient mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_local_phone_white_48dp);
//        uid=getIntent().getExtras().getString("Uid");
//        token=getIntent().getExtras().getString("Tok");

        SplashPage.initializeMclient(this);
        SplashPage.Storetok(this);
        //mClient = SplashPage.mClient;
            String userId = PrefUtils.getFromPrefs(FirstPage.this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
            String tok = PrefUtils.getFromPrefs(FirstPage.this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
            if (userId.equals("default") && tok.equals("default")) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.profile));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.upld_presc));
        // tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                return true;

            default:
                return false;
        }
    }

    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class ProfileFragment extends Fragment {
//        MobileServiceClient mClient;

        // MobileServiceClient mClient;
        MobileServiceTable<MobileProfile> mToDoTable;
        int currnt_blnce = 0;
        Toast toast = null;
        View fab;
        View menuLayout;
        ArcLayout arcLayout;
        private TextView tv;
        private TextView tv1;
        private TextView tv2;
        private ProgressBar mProgressBar;
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_profile, container, false);

            fab = rootView.findViewById(R.id.fab);
            menuLayout = rootView.findViewById(R.id.menu_layout);
            arcLayout = (ArcLayout) rootView.findViewById(R.id.arc_layout);

//            for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
//                arcLayout.getChildAt(i).setOnClickListener(this);
//            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        hideMenu();
                    } else {
                        showMenu();
                    }
                }
            });


            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_CourseSelection);
            mProgressBar.setVisibility(ProgressBar.GONE);
//            mProgressBar.setBackgroundColor(Color.RED);
            mToDoTable = SplashPage.mClient.getTable(MobileProfile.class);
            tv = (TextView) rootView.findViewById(R.id.textView_points);
            tv1 = (TextView) rootView.findViewById(R.id.textView_email);
            tv2 = (TextView) rootView.findViewById(R.id.textView_phn);
            isOnline();
            refreshItemsFromTable();
            Button chngepswd = (Button) rootView.findViewById(R.id.btn_change_pswd);
            chngepswd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), OTPverification
                            .class);
                    startActivity(i);
                }
            });
            final String bal = tv.getText().toString();
            Button Recahrge = (Button) rootView.findViewById(R.id.button2);
            Recahrge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (currnt_blnce == 0) {
//                        Toast.makeText(getActivity(), "Current balance is zero", Toast.LENGTH_SHORT).show();
//                    } else
                    {


                        Intent i = new Intent(getActivity(), Transaction.class);
                        i.putExtra("bal", currnt_blnce);
                        startActivity(i);
                    }
                }
            });
            Button recnttrans = (Button) rootView.findViewById(R.id.button3);
            recnttrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), RecentTransactions.class);
                    startActivity(i);
                }
            });
            Button refresh = (Button) rootView.findViewById(R.id.button5);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOnline() == true) {
                        refreshItemsFromTable();
                    } else {

                    }
                }
            });
            Button logout = (Button) rootView.findViewById(R.id.button6);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
                    PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }
            });


            return rootView;
        }

        private void hideMenu() {

            List<Animator> animList = new ArrayList<>();

            for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
                animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
            }

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(400);
            animSet.setInterpolator(new AnticipateInterpolator());
            animSet.playTogether(animList);
            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    menuLayout.setVisibility(View.INVISIBLE);
                }
            });
            animSet.start();

        }

        private void showMenu() {
            menuLayout.setVisibility(View.VISIBLE);

            List<Animator> animList = new ArrayList<>();

            for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
                animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
            }

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(400);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(animList);
            animSet.start();
        }

        private Animator createShowItemAnimator(View item) {

            float dx = fab.getX() - item.getX();
            float dy = fab.getY() - item.getY();

            item.setRotation(0f);
            item.setTranslationX(dx);
            item.setTranslationY(dy);

            Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                    item,
                    AnimatorUtils.rotation(0f, 720f),
                    AnimatorUtils.translationX(dx, 0f),
                    AnimatorUtils.translationY(dy, 0f)
            );

            return anim;
        }

        private Animator createHideItemAnimator(final View item) {
            float dx = fab.getX() - item.getX();
            float dy = fab.getY() - item.getY();

            Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                    item,
                    AnimatorUtils.rotation(720f, 0f),
                    AnimatorUtils.translationX(0f, dx),
                    AnimatorUtils.translationY(0f, dy)
            );

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    item.setTranslationX(0f);
                    item.setTranslationY(0f);
                }
            });

            return anim;
        }

        public void refreshItemsFromTable() {

            // Get the items that weren't marked as completed and add them in the
            // adapter

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        final List<MobileProfile> results =
                                mToDoTable.where().execute().get();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                for (MobileProfile item : results) {
                                    tv.setText("" + item.bal);
                                    tv1.setText(item.email);
                                    tv2.setText(item.phn);
                                    currnt_blnce = item.bal;

                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();

                        createAndShowDialog(e, "Error");

                    }

                    return null;
                }

                protected void onPostExecute(Void results) {
                    super.onPostExecute(results);
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }.execute();
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
//        public boolean isOnline() {
//            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo netInfo = cm.getActiveNetworkInfo();
//            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//                return true;
//            } else {
//               // mSwipeLayout.setRefreshing(false);
//                Toast.makeText(getActivity(), "YOU ARE NOT CONNECTED TO AN NETWORK", Toast.LENGTH_LONG).show();
//            }
//            return false;
//        }

        private void createAndShowDialog(Exception exception, String title) {
            Throwable ex = exception;
            if (exception.getCause() != null) {
                ex = exception.getCause();
            }
            // createAndShowDialog(ex.getMessage(), title);
        }

        public boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    //
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
//
//            // Demonstration of a collection-browsing activity.
//            rootView.findViewById(R.id.demo_collection_button)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//            // Demonstration of navigating to external activities.
//            rootView.findViewById(R.id.demo_external_activity)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Create an intent that asks the user to pick a photo, but using
//                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
//                            // the application from the device home screen does not return
//                            // to the external activity.
//                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
//                            externalActivityIntent.setType("image/*");
//                            externalActivityIntent.addFlags(
//                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                            startActivity(externalActivityIntent);
//                        }
//                    });
//
//            return rootView;
//        }
//    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
//    public static class DummySectionFragment extends Fragment {
//
//        public static final String ARG_SECTION_NUMBER = "section_number";
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
//            Bundle args = getArguments();
//            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }
}
