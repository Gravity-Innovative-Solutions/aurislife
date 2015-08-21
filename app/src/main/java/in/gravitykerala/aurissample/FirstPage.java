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

package in.gravitykerala.aurissample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.ogaclejapan.arclayout.ArcLayout;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FirstPage extends AppCompatActivity {

    //    String uid;
//    String token;
    public static MobileServiceClient mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        uid=getIntent().getExtras().getString("Uid");
//        token=getIntent().getExtras().getString("Tok");
        try {
            mClient = new MobileServiceClient("https://gravityaurislife.azure-mobile.net",
                    "eaQlkccAUXuRPnafjDXCNaDjxrrDTG68",
                    this);
            String userId = PrefUtils.getFromPrefs(FirstPage.this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "default");
            String tok = PrefUtils.getFromPrefs(FirstPage.this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "default");
            MobileServiceUser user = new MobileServiceUser(userId);
            user.setAuthenticationToken(tok);
            mClient.setCurrentUser(user);
            if (userId.equals("default") && tok.equals("default")) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Upload Priscription"));
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class ProfileFragment extends Fragment {

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
            mProgressBar.setBackgroundColor(Color.RED);
            mToDoTable = mClient.getTable(MobileProfile.class);
            tv = (TextView) rootView.findViewById(R.id.textView_points);
            tv1 = (TextView) rootView.findViewById(R.id.textView_email);
            tv2 = (TextView) rootView.findViewById(R.id.textView_phn);
            isOnline();
            refreshItemsFromTable();
            Button chngepswd = (Button) rootView.findViewById(R.id.btn_change_pswd);
            chngepswd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
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
//            Button logout = (Button) rootView.findViewById(R.id.button6);
//            logout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                  Intent i=new Intent(getActivity(),LoginActivity.class );
//                    startActivity(i);
//                }
//            });


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

    public static class UploadFragment extends Fragment {
        public static final int MEDIA_TYPE_IMAGE = 1;
        private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
        private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
        //    public static final int MEDIA_TYPE_VIDEO = 2;
        // directory name to store captured images and videos
        private static final String IMAGE_DIRECTORY_NAME = "Auris Life";

        private Uri fileUri; // file url to store image/video

        private ImageView imgPreview;
        private VideoView videoPreview;
        private Button btnCapturePicture, btnRecordVideo;

        /*
         * returning image / video
         */
        private static File getOutputMediaFile(int type) {

            // External sdcard location
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                            + IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
//        } else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_upload, container, false);
            imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);
//            videoPreview = (VideoView) rootView.findViewById(R.id.videoPreview);
            btnCapturePicture = (Button) rootView.findViewById(R.id.btnCapturePicture);
            btnCapturePicture.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // capture picture
                    captureImage();
                }
            });
            if (!isDeviceSupportCamera()) {
                Toast.makeText(getActivity(),
                        R.string.camera_not_support,
                        Toast.LENGTH_LONG).show();
                // will close the app if the device does't have camera
                // finish();
            }
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_tab1);
            // Button apply = (Button) rootView.findViewById(R.id.button_apply);
//            apply.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i= new Intent(getActivity(),Admission_Act.class);
//                    startActivity(i);
//                }
//            });

//            ListView listView = (ListView) rootView.findViewById(R.id.listView2);
//            String[] value = new String[]{
//                    " COMPUTER SCIENCE", " APPLIED ELECTRONICS & COMMUNICATION", " THERMAL ENGINEERING", " ENVIRONMENTAL ENGINEERING"
//            };
//            ArrayAdapter<String> Listviewadapter = new ArrayAdapter<String>(getActivity(),
//                    R.layout.list_item, R.id.text, value);
//            listView.setAdapter(Listviewadapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView parent, View v,
//                                        int position, long id) {
//
//
//                }
//            });
            Button mobpricss = (Button) rootView.findViewById(R.id.button4);
            mobpricss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), PriscriptionDetails.class);
                    startActivity(i);
                }
            });
            return rootView;
        }

        private boolean isDeviceSupportCamera() {
            if (getActivity().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)) {
                // this device has a camera
                return true;
            } else {
                // no camera on this device
                return false;
            }
        }

        private void captureImage() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }

        // @Override
//        protected void onRestoreInstanceState(Bundle savedInstanceState) {
//            super.onRestoreInstanceState(savedInstanceState);
//
//            // get the file url
//            fileUri = savedInstanceState.getParcelable("file_uri");
//        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            // save file url in bundle as it will be null on scren orientation
            // changes
            outState.putParcelable("file_uri", fileUri);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // if the result is capturing Image
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    // successfully captured the image
                    // display it in image view
                    previewCapturedImage();
                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getActivity(),
                            R.string.user_cancelled, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity(),
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        private void previewCapturedImage() {
            try {
                // hide video preview
//            videoPreview.setVisibility(View.GONE);

                imgPreview.setVisibility(View.VISIBLE);

                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                        options);

                imgPreview.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        public Uri getOutputMediaFileUri(int type) {
            Log.d("URI type", "" + type);
            File file = getOutputMediaFile(type);
            return Uri.fromFile(file);
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
