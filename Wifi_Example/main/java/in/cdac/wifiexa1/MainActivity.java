package in.cdac.wifiexa1;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
WifiManager wifiManager;
TextView tvStatus;
ListView myWifiListView;
IntentFilter intentFilter;
List<WifiConfiguration> list;
ArrayAdapter<String>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter(WifiManager.
                SCAN_RESULTS_AVAILABLE_ACTION);
        tvStatus=(TextView)findViewById(R.id.textStatus);
        myWifiListView=(ListView)findViewById(R.id.wifiListView);
        adapter=new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1
        );
        myWifiListView.setAdapter(adapter);





        wifiManager=(WifiManager)getApplicationContext().
                getSystemService(WIFI_SERVICE);


        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String [] {
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                101
        );

    }

    public void stopScanWiFi(View view) {
        unregisterReceiver(receiver);
    }

    public void startScanWiFi(View view) {
        adapter.clear();
        registerReceiver(receiver,intentFilter);
    }

    public void listWiFi(View view) {
        list=wifiManager.getConfiguredNetworks();
        for(WifiConfiguration item: list){
            String ssid=item.SSID;
            int pri=item.priority;
            String info="SSID:"+ssid+"\n"+"Priority:"+pri;
            adapter.add(info);
        }
    }

    public void offWiFi(View view) {
        if(wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
            tvStatus.setText("Now WiFi is OFF");
        } else {
            tvStatus.setText("WiFi is already OFF");
        }
    }

    public void onWiFi(View view) {
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
            tvStatus.setText("Now WiFi is ON");
        } else {
            tvStatus.setText("WiFi is already ON");
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context,
                              Intent intent) {
            List<ScanResult>list=wifiManager.getScanResults();
            for(ScanResult item: list){
                String ssid=item.SSID;
                String cap=item.capabilities;
                int level=item.level;
                String info="ssid:"+ssid+"\n"+"capability:"+cap+
                        "\n"+"level:"+level;
                adapter.add(info);
            }
       }
    };

}
