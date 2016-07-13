package kang.bluetoothfinder;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kangjonghyuk on 2016. 7. 12..
 */
public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mLeDevices;
    private LayoutInflater mInflator;
    private HashMap<BluetoothDevice, Integer> mRssiMap = new HashMap<BluetoothDevice, Integer>();
    private Context mContext;

    public LeDeviceListAdapter(Context context) {
        super();
        mContext = context;
        mLeDevices = new ArrayList<BluetoothDevice>();
        mInflator = ((Activity)mContext).getLayoutInflater();
    }

    public void addDevice(BluetoothDevice device, int rssi) {
        if (!mLeDevices.contains(device)) { // device를 포함하지 않을 시 추가
            mLeDevices.add(device);
        }
        mRssiMap.put(device, rssi);
        bar();
    }

    public void bar() {
        BluetoothDevice device = mLeDevices.get(0);
        DeviceScanActivity.L_value1.add(mRssiMap.get(device));

        if (mLeDevices.size() > 1) {
            device = mLeDevices.get(1);
            DeviceScanActivity.L_value2.add(mRssiMap.get(device));

            if (mLeDevices.size() > 2) {
                device = mLeDevices.get(2);
                DeviceScanActivity.L_value3.add(mRssiMap.get(device));
            }
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TextView deviceAddress, deviceName, deviceRssi;
        Log.d("getView", "getView");
        convertView = mInflator.inflate(R.layout.listitem_device, null);
        deviceAddress = (TextView) convertView.findViewById(R.id.device_address);
        deviceName = (TextView) convertView.findViewById(R.id.device_name);
        deviceRssi = (TextView) convertView.findViewById(R.id.device_rssi);

        BluetoothDevice device = mLeDevices.get(i);

        String dName = device.getName();

        if (dName != null && dName.length() > 0)
            deviceName.setText(dName);
        else
            deviceName.setText(R.string.unknown_device);
        deviceAddress.setText(" " + device.getAddress());

        deviceRssi.setText("" + mRssiMap.get(device) + "dBm");

        if (device.getAddress() == mLeDevices.get(0).getAddress()) {
            deviceAddress.setTextColor(Const.COLOR_DARKBLUE);
        } else {
            if (device.getAddress() == mLeDevices.get(1).getAddress()) {
                deviceAddress.setTextColor(Const.COLOR_LIGHTBLUE);
            } else {
                if (device.getAddress() == mLeDevices.get(2).getAddress()) {
                    deviceAddress.setTextColor(Const.COLOR_ORANGEWHITE);
                }
            }
        }
        return convertView;
    }
}

