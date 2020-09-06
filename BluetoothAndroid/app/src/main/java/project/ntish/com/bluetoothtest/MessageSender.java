package project.ntish.com.bluetoothtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bluetooth.BluetoothClient;
import bluetooth.connectedThread;

public class MessageSender extends AppCompatActivity {

    private BluetoothClient connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sender);
        final Button send = findViewById(R.id.send);
        final EditText message = findViewById(R.id.message);
        final TextView connectionStatus = findViewById(R.id.connectionStatus);

        BluetoothDevice device = getIntent().getExtras().getParcelable("bluetoothDevice");
        final BluetoothClient connect = new BluetoothClient(device);

        connectionStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connect.getSocket() != null) {
                    connect.start();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Bluetooth paired device"+ device.getName()+"is not responding", Toast.LENGTH_LONG).show();
                }
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Connection to the server...",Toast.LENGTH_LONG).show();
                BluetoothSocket socket = connect.getSocket();
                if (socket != null) {
                    connectionStatus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                    connectedThread sender = new connectedThread(socket, message.getText().toString());
                    sender.start();
                }
                else {
                    Log.v("MessageSendertest","socket is empty!!");
                }
                Toast.makeText(getApplicationContext(),"Connected to the server",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
