package nuzar.smartplugsapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothAdapter;
import android.widget.Toast;


public class MainActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter = null;
    private PlugsControl mPlugsControl = null;



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
        mPlugsControl = new PlugsControl(mHandler);
        if(mPlugsControl.isEnable()){
        mPlugsControl.connect();}
        else {
            Toast.makeText(this,R.string.DeviceNotFound,Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        mPlugsControl.stop();
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

    private final Handler mHandler = new Handler();
}
