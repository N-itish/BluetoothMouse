package project.ntish.com.bluetoothtest;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import broadCastRecievers.BluetoothBroadCastReciever;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_ENABLE_BT = 1;
    static final int REQUEST_COARSE_LOCATION_SERVICE = 2;
    static final int REQUEST_FINE_LOCATION_SERVICE = 3;

    private ArrayAdapter<String> arrayAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothBroadCastReciever bluBroadCastReciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requesting the permissions for loaction
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_COARSE_LOCATION_SERVICE);
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION_SERVICE);

        ListView deviceList = (ListView) findViewById(R.id.deviceList);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 );
        deviceList.setAdapter(arrayAdapter);
        Button findBluetoothDevices = findViewById(R.id.startBluetooth);
        bluBroadCastReciever = new BluetoothBroadCastReciever(arrayAdapter);
        //on selecting the device move to connection page
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getting the bluetoothdevice for the selected device
                String deviceName = (String) deviceList.getItemAtPosition(position);
                BluetoothDevice selectedDevice = bluBroadCastReciever.getDevices(deviceName);
                if(selectedDevice != null) {
                    Intent intent = new Intent(getApplicationContext(), TouchDetectActivity.class);
                    intent.putExtra("bluetoothDevice", selectedDevice);
                    startActivity(intent);
                }
            }
        });

        //start the discovery process
        findBluetoothDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) {
                    Intent turnOnBluetooth = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOnBluetooth, REQUEST_ENABLE_BT);

                }
                startBluetooth();

            }
        });
    }




    public void startBluetooth() {

        //getting the bluetooth adapter
        //if bluetooth is found then turn on the bluetooth
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
