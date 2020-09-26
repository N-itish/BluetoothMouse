package project.ntish.com.bluetoothtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import bluetooth.BluetoothClient;
import bluetooth.ConnectedThread;
import bluetooth.ConnectionStatus;

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
                    ConnectedThread messageSender = new ConnectedThread(socket, "left");
                    messageSender.start();

                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionStatus.getConnectionStatus()) {
                    ConnectedThread messageSender = new ConnectedThread(socket, "right");
                    messageSender.start();
                }

            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!connectionStatus.getConnectionStatus() ){
                  BluetoothClient connectDevices = new BluetoothClient(device,connectionStatus);
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
            //detecting the double tap and single tap
            private GestureDetector gestureDetector = new GestureDetector(TouchDetectActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent event) {
                    Log.v("messageSender","dTap");
                    if(connectionStatus.getConnectionStatus()) {
                        ConnectedThread messageSender = new ConnectedThread(socket, "right");
                        messageSender.start();
                    }
                    return super.onDoubleTap(event);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent event) {
                    if(connectionStatus.getConnectionStatus()){
                        Log.v("messageSender","single tap");
                        ConnectedThread messageSender = new ConnectedThread(socket,"left");
                        messageSender.start();
                    }
                    return super.onSingleTapUp(event);
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    if(connectionStatus.getConnectionStatus()){
                        ConnectedThread messageSender = new ConnectedThread(socket,"drag");
                        messageSender.start();
                    }
                }
            });


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                if(connectionStatus.getConnectionStatus()) {
                    ConnectedThread messageSender = new ConnectedThread(socket, event.getX() + ":" + event.getY());
                    messageSender.start();
                }
                return true;
            }
        });
    }

}