package net.pupil.newlife.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.pupil.newlife.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Angry on 2017/11/23.
 *
 */

public class BluetoothActivity extends Activity {

    private TextView mTextView;
    private Activity mActivity = BluetoothActivity.this;

    private BluetoothAdapter mBluetoothAdapter;
    private final String TAG = this.getClass().getSimpleName();
    private BluetoothDevice mDevice;
    private BluetoothSocket mClientSocket;
    private BluetoothSocket mServerSocket;

    private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "ACTION_FOUND name="+mDevice.getName()+" address="+mDevice.getAddress());
                mTextView.setText("ACTION_FOUND name="+mDevice.getName()+" address="+mDevice.getAddress());
                mBluetoothAdapter.cancelDiscovery();

//                BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
//                    @Override
//                    public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
//                        super.onConnectionStateChange(gatt, status, newState);
//                        Log.d(TAG, "onConnectionStateChange status:" + status + " newState:" + newState);
//
//                        if (status == 133) {
//                            gatt.disconnect();
//                            gatt.close();
//                            BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(scanDevice.getAddress());
//                            bluetoothDevice.connectGatt(mActivity, false, this);
//                        } else {
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (newState == BluetoothProfile.STATE_CONNECTED) {
//                                        mTextView.setText("connected!!");
//                                    } else {
//                                        mTextView.setText("connect error!!");
//                                    }
//                                }
//                            });
//                            gatt.discoverServices();
//
//                        }
//                    }
//
//                    @Override
//                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//                        super.onServicesDiscovered(gatt, status);
//                        Log.d(TAG, "onServicesDiscovered status:" + status);
//                        List<BluetoothGattService> services = gatt.getServices();
//                        for (BluetoothGattService service : services) {
//                            Log.d(TAG, "service uuid:" + service.getUuid().toString());
//                        }
//                        BluetoothGattService bluetoothGattService = gatt.getService(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"));
//                        List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
//                        for (BluetoothGattCharacteristic characteristic : characteristics) {
//                            Log.d(TAG, "characteristic uuid:" + characteristic.getUuid().toString());
//
//                        }
//                        UUID characteristicUuid = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
//                        BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(characteristicUuid);
//                        bluetoothGattCharacteristic.setValue("haha");
//                        gatt.writeCharacteristic(bluetoothGattCharacteristic);
//
//                    }
//
//                    @Override
//                    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//                        super.onCharacteristicWrite(gatt, characteristic, status);
//                        Log.d(TAG, "onCharacteristicWrite value:" + characteristic.getValue().toString());
//
//                    }
//                };
//
//                mDevice.connectGatt(mActivity, false, bluetoothGattCallback);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mTextView = (TextView) findViewById(R.id.textView);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                    startActivity(discoverableIntent);
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        //发现设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //设备连接状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //蓝牙设备状态改变
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothReceiver, filter);

        //6.0以后的如果需要利用本机查找周围的wifi和蓝牙设备，需要在配置文件中申请位置权限
        //<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
        //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (Build.VERSION.SDK_INT >= 6.0 && permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        } else {
            bindStartDiscoveryBtn();
        }
    }

    private void bindStartDiscoveryBtn() {
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText("...");
                mBluetoothAdapter.startDiscovery();
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mClientSocket = mDevice.createRfcommSocketToServiceRecord(SERVICE_UUID);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTextView.setText("正在连接服务器...");
                                    }
                                });
                                mClientSocket.connect();
                                if (mClientSocket.isConnected()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mActivity, "socket.connect() connected", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final BluetoothServerSocket serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("service_name", SERVICE_UUID);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTextView.setText("等待客户端连接...");
                                    }
                                });
                                mServerSocket = serverSocket.accept();
                                if (mServerSocket.isConnected()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mActivity, "serverSocket.accept() connected", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputStream outputStream = null;
                try {
                    outputStream = mClientSocket.getOutputStream();
                    String message = "qnmlgbd";
                    outputStream.write(message.getBytes("UTF-8"));
                    Log.d(TAG, "发送成功");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
//                    try {
//                        if (outputStream != null) {
//                            outputStream.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });

        findViewById(R.id.receiver_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReceiveThread().start();
            }
        });
    }

    class ReceiveThread extends Thread {

        InputStream mInputStream;

        public ReceiveThread () {
            try {
                mInputStream = mServerSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            byte[] buff = new byte[1024];
            while (true) {
                try {
                    final int bytes = mInputStream.read(buff);
                    final String str = new String(buff, "UTF-8");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(str.substring(0, bytes));
                        }
                    });
                    Log.d(TAG, "接收消息中...");
                    Thread.sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //获取本机蓝牙名称
            String name = mBluetoothAdapter.getName();
            //获取本机蓝牙地址
            String address = mBluetoothAdapter.getAddress();
            Log.d(TAG, "bluetooth name =" + name + " address =" + address);
            //获取已配对蓝牙设备
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
            Log.d(TAG, "bonded device size =" + devices.size());
            for (BluetoothDevice bonddevice : devices) {
                Log.d(TAG, "bonded device name =" + bonddevice.getName() + " address" + bonddevice.getAddress());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted!
                    bindStartDiscoveryBtn();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBluetoothReceiver);
    }
}
