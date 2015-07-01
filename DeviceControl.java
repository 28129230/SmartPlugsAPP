package nuzar.smartplugsapp;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class DeviceControl {

    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectThread mConnectThread;
    private String mDeviceName;
    public boolean STATE_GETADDRESS;
    public boolean STATE_CONNECT;

    public DeviceControl(String DeviceName, Handler handler ){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
        mConnectThread = new ConnectThread();
        mDeviceName = DeviceName;
    }

    public synchronized boolean isEnable(){
        mConnectThread.getAddress();
        return STATE_GETADDRESS;
    }

    public synchronized void connect(){
        mConnectThread.start();
    }

    public synchronized void stop(){
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
    }

    private class ConnectThread extends Thread{
        private BluetoothDevice mmDevice;
        private BluetoothSocket mmSocket;
        private  BluetoothDevice GetDevice;
        private  String address = null;

        public void getAddress(){
            Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
            if (pairedDevices.size() > 0)
            {
                for (BluetoothDevice tmp : pairedDevices) {
                    if (tmp.getName().equals(mDeviceName)) {
                        GetDevice = tmp;
                    }
                }
                address = GetDevice.getAddress();
            }
            STATE_GETADDRESS = (address != null);
        }

        public void run(){

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
                STATE_CONNECT = true;
            } catch (IOException e) {
                e.printStackTrace();
                Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.TOAST, "无法连接设备，请检查设备是否打开！");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
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
