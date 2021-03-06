package me.abidi.tangoapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableRow;

import com.home_connect.sdk.internal.log.HCLog;
import com.home_connect.sdk.model.HomeApplianceModel;
import com.home_connect.sdk.property.RxBinder;
import com.home_connect.sdk.services.ApplianceService;
import com.home_connect.sdk.services.LoginService;


import java.util.List;

import me.abidi.tangoapp.tango.EventProcessor;
import me.abidi.tangoapp.tango.FridgeMonitor;
import me.abidi.tangoapp.tango.OvenMonitor;
import me.abidi.tangoapp.tango.TextProcessor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String URL = "android.resource://me.abidi.Tangoapp/" + R.raw.bg;
    //public static final String URL = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_50mb.mp4";
    private  OvenMonitor ovenMonitor;
    private FridgeMonitor fridgeMonitor;
    private EventProcessor eventProcessor = new EventProcessor();
    TableRow ovenRow;
    TableRow fridgeRow;
    TableRow coffeeMachineRow;
    TableRow lightRow;
    TableRow echoRow;
    private LoginService loginService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onRefreshApplianceList();


        /* ********************* Background Video *************************** */

        MediaPlayer bgVidepPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.bg_video);
        bgVidepPlayer.start();

//        VideoView bgVideo = (VideoView) findViewById(R.id.videoView);
//
//        Uri uri = Uri.parse(URL);
//
//        bgVideo.setMediaController(new MediaController(this));
//        bgVideo.setVideoURI(uri);
//        bgVideo.requestFocus();
//        bgVideo.start();

        /* **************************************************************** */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //        // test monitor
            }
        });

        ovenRow = (TableRow) findViewById(R.id.bosh_oven);
        lightRow = (TableRow) findViewById(R.id.philipps_hue);

        ovenRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Log.e("Click ", "Bosch Oven Row");
                Intent rowIntent = new Intent(MainActivity.this, CardViewActivity.class);
                if (fridgeMonitor == null){
                    fridgeMonitor = new FridgeMonitor(eventProcessor, getContentResolver());

                }
                fridgeMonitor.startMonitoring();
                if (ovenMonitor == null){
                    ovenMonitor = new OvenMonitor(eventProcessor);

                }
                ovenMonitor.startMonitoring();
                rowIntent.putExtra("DEVICE_NAME", "Bosch HNG6764S6");
                startActivity(rowIntent);
            }
        });

        lightRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Log.e("Click ", "Philipps Hue Row");
                Intent rowIntent = new Intent(MainActivity.this, CardViewActivity.class);
                Log.e("Click ", "Row 1");
                if (fridgeMonitor == null){
                    fridgeMonitor = new FridgeMonitor(eventProcessor, getContentResolver());

                }
                fridgeMonitor.startMonitoring();
                if (ovenMonitor == null){
                    ovenMonitor = new OvenMonitor(eventProcessor);

                }
                ovenMonitor.startMonitoring();
                rowIntent.putExtra("DEVICE_NAME", "Philipps Hue");
                startActivity(rowIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (fridgeMonitor != null){
            fridgeMonitor.startMonitoring();
        }
        if (ovenMonitor != null){
            ovenMonitor.startMonitoring();
        }
    }


    /**
     * Necessary call to unbind the {@link RxBinder} to avoid memory leaks
     */
    @Override
    public void onPause() {
        super.onPause();
        RxBinder.unbind(this);
    }

    public void onRefreshApplianceList() {
        ApplianceService applianceService = ApplianceService.create();

        RxBinder.bind(this,
                applianceService.updateAppliances(),
                new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // do nothing here
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        HCLog.d(throwable);
                    }
                });

        RxBinder.bind(this,
                applianceService.getAppliances().observe().observeOn(AndroidSchedulers.mainThread()),
                new Action1<List<HomeApplianceModel>>() {
                    @Override
                    public void call(List<HomeApplianceModel> homeApplianceModels) {
                        //presenter.updateApplianceList(homeApplianceModels);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        HCLog.d(throwable);
                    }
                });
    }
}
