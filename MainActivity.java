package nuzar.smartplugsapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothAdapter;
import android.widget.Toast;


public class MainActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter = null;
    private DeviceControl mDeviceControl = null;
    public static final int MESSAGE_TOAST = 1;
    public static final String TOAST = "toast";





    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_LONG).show();
            }

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            Toast.makeText(this,R.string.BluetoothNotAvailable,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        String deviceName = "PLUGS";
        mDeviceControl = new DeviceControl(deviceName,mHandler);
        if(mDeviceControl.isEnable()){
        mDeviceControl.connect();}
        else {
            Toast.makeText(this,R.string.DeviceNotFound,Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        mDeviceControl.stop();
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


}
