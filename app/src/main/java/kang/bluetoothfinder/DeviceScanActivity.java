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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 1;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private BluetoothDevice mDeviceInfo;
    private ListView mList;
    private FirstBarView mFirstbarView, mFirstbarView1, mFirstbarView2;
    private RelativeLayout mRelativeLayout;
    private ArrayList<Integer> L_value1 = new ArrayList<>();
    private ArrayList<Integer> L_value2 = new ArrayList<>();
    private ArrayList<Integer> L_value3 = new ArrayList<>();
    private Timer mTimer;
    private TimerTask mTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        getActionBar().setTitle(R.string.title_devices);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.llayout);
        mList = (ListView) findViewById(R.id.mList);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
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
        mTimer.cancel();
        mLeDeviceListAdapter.clear();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mTimer = new Timer();
            mTask = new TimerTask() {
                @Override
                public void run() {
                    (DeviceScanActivity.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (L_value1 != null)
                                sendView(L_value1, 0);
                            if (L_value2 != null)
                                sendView(L_value2, 1);
                            if (L_value3 != null)
                                sendView(L_value3, 2);
                        }
                    });
                }
            };
            mTimer.schedule(mTask, 1000, Const.ONE_SEC);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mTimer.cancel();
        }
        invalidateOptionsMenu();
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;
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
            mRssiMap.put(device,rssi);
            bar();
        }

        public void bar() {
            BluetoothDevice device = mLeDevices.get(0);
            L_value1.add(mRssiMap.get(device));

            if (mLeDevices.size() > 1) {
                device = mLeDevices.get(1);
                L_value2.add(mRssiMap.get(device));

                if (mLeDevices.size() > 2) {
                    device = mLeDevices.get(2);
                    L_value3.add(mRssiMap.get(device));
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
                deviceAddress.setTextColor(Const.COLOR_RED);
            } else {
                if (device.getAddress() == mLeDevices.get(1).getAddress()) {
                    deviceAddress.setTextColor(Const.COLOR_GREEN);
                } else {
                    if (device.getAddress() == mLeDevices.get(2).getAddress()) {
                        deviceAddress.setTextColor(Const.COLOR_BLUE);
                    }
                }
            }
            return convertView;
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDeviceInfo = device;
                    mLeDeviceListAdapter.addDevice(device, rssi);
                }
            });
        }
    };

    public void sendView(ArrayList<Integer> value, int id) {
        int sum = 0, average;
        try {
            for (int i = 0; i < value.size(); i++) {
                sum += value.get(i);
            }
            average = sum / value.size();
            Log.d("tag", "size : " + value.size() + "  sum : " + sum + "  average : " + average);

            if (id == 0) {
                if (mFirstbarView != null)
                    mRelativeLayout.removeView(mFirstbarView);
                mFirstbarView = new FirstBarView(getApplicationContext(), Math.abs(average) * 20 - 240, id);
                mRelativeLayout.addView(mFirstbarView);
                value.clear();
            }

            if (id == 1) {
                if (mFirstbarView1 != null)
                    mRelativeLayout.removeView(mFirstbarView1);
                mFirstbarView1 = new FirstBarView(getApplicationContext(), Math.abs(average) * 20 - 240, id);
                mRelativeLayout.addView(mFirstbarView1);
                value.clear();
            }

            if (id == 2) {
                if (mFirstbarView2 != null)
                    mRelativeLayout.removeView(mFirstbarView2);
                mFirstbarView2 = new FirstBarView(getApplicationContext(), Math.abs(average) * 20 - 240, id);
                mRelativeLayout.addView(mFirstbarView2);
                value.clear();
            }
            mLeDeviceListAdapter.notifyDataSetChanged();
        } catch (ArithmeticException e) {
            Log.d("value", "" + value.size());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}


