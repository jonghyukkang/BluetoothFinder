/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kang.bluetoothfinder;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 1;

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BarClass barClass;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private BluetoothDevice mDeviceInfo;
    private ListView mList;
    private FirstBarView mFirstbarView, mFirstbarView1, mFirstbarView2; // 첫번째 Device Rssi값에 따른 막대 그래프
    private RelativeLayout mRelativeLayout;
    private ArrayList<Integer> L_value1, L_value2, L_value3;
    private int mSum1, mSum2, mSum3;    // 각 자리 별 Rssi값의 합
    private int mAverage1, mAverage2, mAverage3;    // 각 자리 별 Rssi값의 평균값

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        L_value1 = new ArrayList<Integer>();
        L_value2 = new ArrayList<Integer>();
        L_value3 = new ArrayList<Integer>();

        getActionBar().setTitle(R.string.title_devices);    //Title name
        mRelativeLayout = (RelativeLayout) findViewById(R.id.llayout);

        mList = (ListView) findViewById(R.id.mList);

        //BluetoothLE 지원 가능한 device인지 체크, 아닐경우 Toast 띄우기
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        scanLeDevice(true);
        barClass = new BarClass();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {   // mScanning이 false이면 scan메뉴 띄우기
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {            //mScanning이 true이면 stop메뉴 띄우기
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan: //scan메뉴를 눌렀을 때
                mLeDeviceListAdapter.clear(); // mLeDeviceListAdapter 초기화
                InitiateValue();    // 각 멤버변수들 초기화
                scanLeDevice(true); // 스캔 시작
                break;
            case R.id.menu_stop: //stop메뉴를 눌렀을 때
                scanLeDevice(false); //스캔 정지
                break;
        }
        return true;
    }

    public void InitiateValue() {  //Rssi값 평균치 구하기 관련 변수들 초기화 함수
        L_value1.clear();
        L_value2.clear();
        L_value3.clear();
        mSum1 = 0;
        mSum2 = 0;
        mSum3 = 0;
        mAverage1 = 0;
        mAverage2 = 0;
        mAverage3 = 0;
        mRelativeLayout.removeView(mFirstbarView);
        mRelativeLayout.removeView(mFirstbarView1);
        mRelativeLayout.removeView(mFirstbarView2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //bluetooth가 비활성화 상태시 인텐트를 보내 활성화 여부 묻기
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        mList.setAdapter(mLeDeviceListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // bluetooth 활성화 여부에서 Negative 시 Activirt finish하고 리턴
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false); //Pause()상태일 때 스캔 정지
        mLeDeviceListAdapter.clear();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }


    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;
        //Key값은 BluetoothDevice, Value값은 Integer(Rssi)값으로
        private HashMap<BluetoothDevice, Integer> mRssiMap = new HashMap<BluetoothDevice, Integer>();

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {
            if (!mLeDevices.contains(device)) { // device를 포함하지 않을 시 추가
                mLeDevices.add(device);
            }
            mRssiMap.put(device, rssi); // hashmap에 device와 rssi값 put
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
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) convertView.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
                viewHolder.deviceRssi = (TextView) convertView.findViewById(R.id.device_rssi);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final BluetoothDevice device = mLeDevices.get(i);

            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(" " + device.getAddress());
            viewHolder.deviceRssi.setText("" + mRssiMap.get(device) + "dBm");

            if (device.getAddress() == mLeDevices.get(0).getAddress()) {
                viewHolder.deviceAddress.setTextColor(Const.COLOR_ONE);

            } else {
                if (device.getAddress() == mLeDevices.get(1).getAddress()) {
                    viewHolder.deviceAddress.setTextColor(Const.COLOR_TWO);
                } else {
                    if (device.getAddress() == mLeDevices.get(2).getAddress()) {
                        viewHolder.deviceAddress.setTextColor(Const.COLOR_THREE);
                    }
                }
            }
            return convertView;
        }
    }


    //BLE Scan CallbackMethod
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDeviceInfo = device;
                    mLeDeviceListAdapter.addDevice(device, rssi);
                    barClass.addDevices(device, rssi);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRssi;
    }

    private class BarClass {
        private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<>();
        private HashMap<BluetoothDevice, Integer> mRssiMap = new HashMap<>();

        public void addDevices(BluetoothDevice device, int rssi) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
            mRssiMap.put(device, rssi);
            bar();
        }

        public void bar() {
            BluetoothDevice device = mLeDevices.get(0);
            BarView(1, mRssiMap.get(device));

            if(mLeDevices.size() > 1){
                device = mLeDevices.get(1);
                BarView(2, mRssiMap.get(device));

                if(mLeDevices.size() > 2){
                    device = mLeDevices.get(2);
                    BarView(3, mRssiMap.get(device));
                }
            }
        }
    }

    public void BarView(int id, int rssi) {
        switch (id) {
            case 1:
                L_value1.add(rssi);
                if (L_value1.size() == 30) {
                    for (int j = 0; j < L_value1.size(); j++) {
                        mSum1 += L_value1.get(j);
                        mAverage1 = mSum1 / L_value1.size();
                    }
                    mRelativeLayout.removeView(mFirstbarView);
                    mFirstbarView = new FirstBarView(getApplicationContext(), Math.abs(mAverage1) * 10 - 122, id);
                    mRelativeLayout.addView(mFirstbarView);
                    mSum1 = 0;
                    mAverage1 = 0;
                    L_value1.clear();
                }
                break;

            case 2:
                L_value2.add(rssi);
                if (L_value2.size() == 30) {
                    for (int j = 0; j < L_value2.size(); j++) {
                        mSum2 += L_value2.get(j);
                        mAverage2 = mSum2 / L_value2.size();
                    }
                    mRelativeLayout.removeView(mFirstbarView1);
                    mFirstbarView1 = new FirstBarView(getApplicationContext(), Math.abs(mAverage2) * 10 - 122, id);
                    mRelativeLayout.addView(mFirstbarView1);
                    mSum2 = 0;
                    mAverage2 = 0;
                    L_value2.clear();
                }
                break;

            case 3:
                L_value3.add(rssi);
                if (L_value3.size() == 30) {
                    for (int j = 0; j < L_value3.size(); j++) {
                        mSum3 += L_value3.get(j);
                        mAverage3 = mSum3 / L_value3.size();
                    }
                    mRelativeLayout.removeView(mFirstbarView2);
                    mFirstbarView2 = new FirstBarView(getApplicationContext(), Math.abs(mAverage3) * 10 - 122, id);
                    mRelativeLayout.addView(mFirstbarView2);
                    mSum3 = 0;
                    mAverage3 = 0;
                    L_value3.clear();
                }
                break;
        }
    }
}



