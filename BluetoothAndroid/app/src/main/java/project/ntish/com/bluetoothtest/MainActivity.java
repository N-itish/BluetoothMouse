package project.ntish.com.bluetoothtest;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import broadCastRecievers.BluetoothBroadCastReciever;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_ENABLE_BT = 1;
    static final int REQUEST_COARSE_LOCATION_SERVICE = 2;
    static final int REQUEST_FINE_LOCATION_SERVICE = 3;
    private ArrayAdapter<String> bluetoothArrayAdapter;
    private HashMap<String,String> deviceMap = new HashMap<>();
    private BluetoothBroadCastReciever bluBroadCastReciever;
    private BluetoothAdapter bluetoothAdapter;
    private Map<String,BluetoothDevice> pairedDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requesting the permissions for loaction
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_COARSE_LOCATION_SERVICE);
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION_SERVICE);
        ListView deviceList = (ListView) findViewById(R.id.deviceList);
        bluetoothArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 );
        deviceList.setAdapter(bluetoothArrayAdapter);
        //broadcastlistners
        bluBroadCastReciever = new BluetoothBroadCastReciever(bluetoothArrayAdapter);

        Button findBluetoothDevices = (Button) findViewById(R.id.startBluetooth);
        Button findPairedDevice = (Button) findViewById(R.id.pairedDevices);

        findPairedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothArrayAdapter.clear();
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                for(BluetoothDevice pairedDevice: bluetoothAdapter.getBondedDevices()){
                    pairedDevices.put(pairedDevice.getName(),pairedDevice);
                    bluetoothArrayAdapter.add(pairedDevice.getName());
                }
                bluetoothArrayAdapter.notifyDataSetChanged();

            }
        });
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice selectedDevice = bluBroadCastReciever.getDevices().get(position);
                Log.v("bluetoothDevice","Connecting to device: " + selectedDevice.getName());
                Intent intent = new Intent(getApplicationContext(),TouchDetectActivity.class);
                intent.putExtra("bluetoothDevice",selectedDevice);
                startActivity(intent);
            }
        });

        //start the discovery process
        findBluetoothDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                //Clearing the previous data
                if(bluetoothArrayAdapter != null) {
                    bluetoothArrayAdapter.clear();
                }
                //TODO: enable location service as well as the bluetooth
                if (!bluetoothAdapter.isEnabled()) {
                    Log.v("BroadcastReceiver","Turning on Bluetooth");
                    Intent turnOnBluetooth = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOnBluetooth, REQUEST_ENABLE_BT);

                }
                startbluetooth(bluetoothAdapter);
            }
        });
    }


    public void startbluetooth(BluetoothAdapter bluetoothAdapter) {

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
        super.onDestroy();
        if(bluBroadCastReciever != null) {
            unregisterReceiver(bluBroadCastReciever);
        }
    }
}
