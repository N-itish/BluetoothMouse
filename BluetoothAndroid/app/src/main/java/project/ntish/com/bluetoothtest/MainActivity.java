package project.ntish.com.bluetoothtest;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import broadCastRecievers.BluetoothBroadCastReciever;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_ENABLE_BT = 1;
    static final int REQUEST_ENABLE_LOCATION  = 2;
    static final int REQUEST_COARSE_LOCATION_SERVICE = 3;
    static final int REQUEST_FINE_LOCATION_SERVICE = 4;
    static final String DEVICE_STATUS = "deviceStatus";

    private ArrayAdapter<String> arrayAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothBroadCastReciever bluBroadCastReciever;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requesting the permissions for loaction
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_COARSE_LOCATION_SERVICE);
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION_SERVICE);

        ListView deviceList = (ListView) findViewById(R.id.deviceList);
        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        deviceList.setAdapter(arrayAdapter);
        Button findBluetoothDevices = findViewById(R.id.startBluetooth);

        pref = getSharedPreferences(DEVICE_STATUS,MODE_PRIVATE);
        bluBroadCastReciever = new BluetoothBroadCastReciever(arrayAdapter);


        //on selecting the device move to connection page
        deviceList.setOnItemClickListener((parent, view, position, id) -> {
            //getting the bluetoothdevice for the selected device
            String deviceName = (String) deviceList.getItemAtPosition(position);
            BluetoothDevice selectedDevice = bluBroadCastReciever.getDevices(deviceName);
            if(selectedDevice != null) {
                Intent intent = new Intent(getApplicationContext(), TouchDetectActivity.class);
                intent.putExtra("bluetoothDevice", selectedDevice);
                startActivity(intent);
            }
        });

        //start the discovery process
        findBluetoothDevices.setOnClickListener(v -> {
            LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!bluetoothAdapter.isEnabled()) {
                Intent turnOnBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOnBluetooth, REQUEST_ENABLE_BT);
            }
            if(!isGPSEnabled){
                Intent turnOnLocation = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(turnOnLocation,REQUEST_ENABLE_LOCATION);
            }
            startBluetooth();

        });

    }




    public void startBluetooth() {

        //if bluetooth adadpter is not found then bluetooth device is not found in the android phone
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Bluetooth Not supported!!!", Toast.LENGTH_LONG).show();
        } else {

            //if bluetooth is enabled then start the service
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(bluBroadCastReciever, filter);
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    protected void onDestroy(){
        if(bluBroadCastReciever != null) {
            unregisterReceiver(bluBroadCastReciever);
        }
        super.onDestroy();

    }
}
