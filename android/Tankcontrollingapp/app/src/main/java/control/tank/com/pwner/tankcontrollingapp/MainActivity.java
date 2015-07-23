package control.tank.com.pwner.tankcontrollingapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

//TODO
//

public class MainActivity extends Activity {
    // Fag shit
    boolean connected = false;
    TextView txtX, txtY;
    JoystickView joystick;
    Button connect;
    Button disconnect;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        joystick = (JoystickView) findViewById(R.id.joystickView);
        connect = (Button) findViewById(R.id.button2);
        disconnect = (Button) findViewById(R.id.button1);
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) throws IOException {
                switch (direction) {
                    case JoystickView.FRONT:
                        sendData("F");
                        break;

                    case JoystickView.FRONT_RIGHT:
                        sendData("F");
                        break;

                    case JoystickView.RIGHT:
                        sendData("L");
                        break;

                    case JoystickView.RIGHT_BOTTOM:
                        sendData("B");
                        break;

                    case JoystickView.BOTTOM:
                        sendData("B");
                        break;

                    case JoystickView.BOTTOM_LEFT:
                        sendData("B");
                        break;

                    case JoystickView.LEFT:
                        sendData("R");
                        break;

                    case JoystickView.LEFT_FRONT:
                        sendData("F");
                        break;

                    default:
                        sendData("S");

                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        //Listeners
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Connect
                try {
                    connect();
                } catch (IOException e) {

                }
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disconnect
                try {
                    disconnect();
                } catch (IOException e) {

                }
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     *  Functions
     *  TODO
     *  1:MD5 hashed cmds
     *  2:speed commands
     */

    void sendData(String msg) throws IOException
    {
        if(connected) {
            msg += "\n";
            mmOutputStream.write(msg.getBytes());
        } else {
            Toast.makeText(getApplicationContext(),
                    "Not connected!", Toast.LENGTH_LONG).show();
        }
    }

    public void disconnect() throws IOException {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        Toast.makeText(getApplicationContext(),
                "Disconnected", Toast.LENGTH_LONG).show();
        connected = false;
    }

    public void connect() throws IOException {
        if(!connected) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("HC-05")) {
                        mmDevice = device;
                        break;
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "No paired devices available", Toast.LENGTH_LONG).show();
            }
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            Toast.makeText(getApplicationContext(),
                    "Connected :D", Toast.LENGTH_LONG).show();
            connected = true;
        } else {
            Toast.makeText(getApplicationContext(),
                    "Already connected!", Toast.LENGTH_LONG).show();
        }
    }
}
