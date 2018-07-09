package com.example.yumor.uitest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import jp.co.gitaku.ptp.ObjectInfo;
import jp.co.gitaku.ptp.Response;
import jp.co.gitaku.ptpip.PTPIPInitiator;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.path;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String path;
//    private HashMap<String, String> options;
    private VideoView mVideoView;
    private MediaController controller;
    private DrawerLayout mDrawerLayout;
    private PTPIPInitiator piInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;

        setContentView(R.layout.activity_main);
//        Vitamio.initialize(this);
        initPTP();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        ImageButton edit_addr = (ImageButton) findViewById(R.id.edit);
        final EditText edit = new EditText(MainActivity.this);
        edit_addr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("Please input the address of video streaming:");
                dialog.setView(edit);
                dialog.setCancelable(true);
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取相应地址的视频流（改变path）
                    }
                });
                dialog.show();
            }
        });

        ImageButton photo = (ImageButton)findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
               startActivity(intent);
              
            }
        });

        Button shootBtn = (Button)findViewById(R.id.btn_take_photo);
        shootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this," Send PtP Take Photo Command", Toast.LENGTH_SHORT).show();
                Response res = sendShootCmd("192.168.1.104");
                savePic(res.getData());
            }
        });

        //用Vitamio库获取视频流
        mVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
        path = "rtmp://192.168.1.104:1936/live/test";
        mVideoView.setVideoPath(path);
//        /*options = new HashMap<>();
//        options.put("rtmp_playpath", "");
//        options.put("rtmp_swfurl", "");
//        options.put("rtmp_live", "1");
//        options.put("rtmp_pageurl", "");*/

////        mVideoView.setVideoURI(Uri.parse(path), options);
//        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FIT_PARENT, 0);
//        mVideoView.setMediaController(new MediaController(this));
//        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                /*
                 * add media controller
                 */
                        controller = new MediaController(MainActivity.this);
                        mVideoView.setMediaController(controller);
                /*
                 * and set its position on screen
                 */
                        controller.setAnchorView(mVideoView);
                    }
                });

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private class PTPClientListener implements ServiceListener {

        @Override
        public void serviceAdded(ServiceEvent event) {
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            ServiceInfo info = event.getInfo();

            int[] guid = { 0xe0, 0xcf, 0x20, 0x3a, 0xc3, 0x2a, 0x1b, 0x41, 0x98, 0x10, 0x0a, 0x93, 0x88, 0xec, 0x53, 0x6c };
            String ip = "192.168.1.121";
            // /192.168.1.131
//            ip = info.getInetAddresses()[0].toString().split("/")[1];
            System.out.println(ip);
            // 203e4f68-2423-4b46-9868-157593b63ef3
            // int[] guid = {0x20, 0x3e, 0x4f, 0x68, 0x24, 0x23, 0x4b, 0x46, 0x98,
            // 0x68, 0x15, 0x75, 0x93, 0xb6, 0x3e, 0xf3};
            // String ip = "192.168.1.88";
            //	e0:cf:20:3a:c3:2a:1b:41:98:10:0a:93:88:ec:53:6c:58:00:50:00:2d:00:50:00:43
            try {
                PTPIPInitiator piInit = new PTPIPInitiator(guid, "aaa", ip);
                piInit.addListeners(new testListener());
                System.out.println("connect success");
                piInit.openSession();
                // DeviceInfo devInfo = piInit.getDeviceInfo();
                // devInfo.print();

                // piInit.closeSession();
                // piInit.getStorageIDs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    void initPTP() {
        JmDNS jmdns = null;
        try {
            jmdns = JmDNS.create();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        jmdns.addServiceListener("_ptp._tcp.local.", new PTPClientListener());
    }

    Response sendShootCmd(String IP) {
        int[] guid = { 0xe0, 0xcf, 0x20, 0x3a, 0xc3, 0x2a, 0x1b, 0x41, 0x98, 0x10, 0x0a, 0x93, 0x88, 0xec, 0x53, 0x6c };
        String ip = "127.0.0.1";
        ip = IP;
        try {
            piInit = new PTPIPInitiator(guid, "aaa", ip);
            piInit.getDeviceInfo();
            Response res = piInit.takePhoto();
            int[] handles = piInit.getObjectHandles(-1, ObjectInfo.EXIF_JPEG, 0);
            for (int handle : handles) {
                System.out.println(handle);
                ObjectInfo objInfo = piInit.getObjectInfo(handle);
                objInfo.print();
            }

            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    void savePic(byte[] data){
        if(data.length<3) return;
        FileOutputStream out = null;
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

        // 首先保存图片
       
        // 其次把文件插入到系统图库
       
        // 最后通知图库更新
      }

}
