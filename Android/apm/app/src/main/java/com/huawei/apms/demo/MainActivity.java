package com.huawei.apms.demo;

import android.os.Bundle;

import com.huawei.agconnect.apms.APMS;
import com.huawei.agconnect.apms.androiddemo.R;
import com.huawei.agconnect.apms.custom.CustomTrace;
import com.huawei.agconnect.apms.instrument.AddCustomTrace;
import com.huawei.util.HttpUtil;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private boolean anrTestEnable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize button event
        initNetworkEventButton();
        initApmsSwitchButton();
        initApmsAnrSwitchButton();
        initCustomEventButton();
        initAnrTestButton();
    }

    // define custom event by annotation
    @AddCustomTrace(name = "CustomEvent2")
    public void customEventHandle() {
    }

    // define custom event by code
    public void sendCustomEvent() {
        CustomTrace customTrace = APMS.getInstance().createCustomTrace("CustomEvent1");
        customTrace.start();
        // code you want trace
        businessLogicStart(customTrace);
        businessLogicEnd(customTrace);
        customTrace.stop();
    }

    private void sendCustomEventByAnnotation() {
        customEventHandle();
    }

    public void businessLogicStart(CustomTrace customTrace) {
        customTrace.putMeasure("ProcessingTimes", 0);
        for (int i = 0; i < 5; i++) {
            customTrace.incrementMeasure("ProcessingTimes", 1);
        }
    }

    public void businessLogicEnd(CustomTrace customTrace) {
        customTrace.putProperty("ProcessingResult", "Success");
        customTrace.putProperty("Status", "Normal");
    }

    public void initApmsAnrSwitchButton() {
        findViewById(R.id.enable_apms_anr_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "disable apms.");
                APMS.getInstance().enableAnrMonitor(false);
            }
        });
        findViewById(R.id.enable_apms_anr_on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "enable apms.");
                APMS.getInstance().enableAnrMonitor(true);
            }
        });
    }

    public void initApmsSwitchButton() {
        findViewById(R.id.enable_apms_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "disable apms.");
                APMS.getInstance().enableCollection(false);
            }
        });
        findViewById(R.id.enable_apms_on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "enable apms.");
                APMS.getInstance().enableCollection(true);
            }
        });
    }

    public void initNetworkEventButton() {
        Log.d("apmsAndroidDemo", "apms demo start.");
        Button sendNetworkRequestBtn = findViewById(R.id.btn_network);
        sendNetworkRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("apmsAndroidDemo", "send network request.");
                HttpUtil.oneRequest();
            }
        });
    }

    public void initAnrTestButton() {
        findViewById(R.id.anr_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "target anr");
                anrTestEnable = true;
            }
        });
    }

    public void initCustomEventButton() {
        findViewById(R.id.custom_normal_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "send a custom event");
                sendCustomEvent();
            }
        });
        findViewById(R.id.custom_normal_event_by_annotation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "send a custom event by annotation");
                sendCustomEventByAnnotation();
            }
        });
        findViewById(R.id.custom_network_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "send a custom network event");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.customNetworkEvent();
                    }
                }).start();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
        @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (anrTestEnable) {
            try {
               Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
           }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (anrTestEnable) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
