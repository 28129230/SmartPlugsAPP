package nuzar.smartplugsapp;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;



public class PlugsControl {

    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectThread mConnectThread;


    public PlugsControl(Handler handler ){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;

    }

    public synchronized boolean isEnable(){
        return true;}



    public synchronized void connect(){
        mConnectThread = new ConnectThread();
        mConnectThread.start();
    }

    public synchronized void stop(){
        if(mConnectThread != null){mConnectThread.cancel();mConnectThread = null;}
    }



    private class ConnectThread extends Thread{
        private  BluetoothDevice mmDevice;
        private  BluetoothSocket mmSocket;
        private  BluetoothDevice GetDevice;


        public void run(){

            Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
            if (pairedDevices.size() > 0)
            {
                for (BluetoothDevice tmp : pairedDevices) {
                    if (tmp.getName().equals("PLUGS")) {
                        GetDevice = tmp;
                    }
                }
            }

            String address = GetDevice.getAddress();

            mmDevice = mAdapter.getRemoteDevice(address);

            BluetoothSocket tmp = null;
            try{
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket =tmp;

            mAdapter.cancelDiscovery();

            try{
                mmSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                try{
                    mmSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        public void cancel(){
            try{
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
