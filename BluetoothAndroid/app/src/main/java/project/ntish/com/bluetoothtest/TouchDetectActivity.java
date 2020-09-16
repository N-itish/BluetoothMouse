package project.ntish.com.bluetoothtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import bluetooth.ConnectionStatus;
import bluetooth.BluetoothClient;
import bluetooth.ConnectedThread;

public class TouchDetectActivity extends AppCompatActivity {
    private BluetoothSocket socket;
    private ConnectionStatus connectionStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_detect);

        Button left = findViewById(R.id.left);
        Button right= findViewById(R.id.right);
        Button connect = findViewById(R.id.startConnection);
        ConstraintLayout layout = findViewById(R.id.mousePad);

        BluetoothDevice device = getIntent().getExtras().getParcelable("bluetoothDevice");

        connectionStatus = new ConnectionStatus();
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionStatus.getConnectionStatus()) {
                    ConnectedThread messageSender = new ConnectedThread(socket, "left",connectionStatus);
                    messageSender.start();

                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionStatus.getConnectionStatus()) {
                    ConnectedThread messageSender = new ConnectedThread(socket, "right",connectionStatus);
                    messageSender.start();
                }

            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!connectionStatus.getConnectionStatus()){
                  BluetoothClient connectDevices = new BluetoothClient(device);
                  connectDevices.start();
                  socket = connectDevices.getSocket();
                  connectionStatus.setConnectionStatus(true);
              }
                else
                {
                    Toast.makeText(getApplicationContext(),"Already connected to server!!!",Toast.LENGTH_LONG).show();
                }
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(connectionStatus.getConnectionStatus()) {
                    ConnectedThread messageSender = new ConnectedThread(socket, event.getX() + ":" + event.getY(),connectionStatus);
                    messageSender.start();
                }
                return true;
            }
        });
    }
}