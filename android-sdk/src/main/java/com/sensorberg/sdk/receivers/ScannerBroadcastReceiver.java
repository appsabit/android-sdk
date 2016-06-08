package com.sensorberg.sdk.receivers;

import com.sensorberg.sdk.Logger;
import com.sensorberg.sdk.SensorbergService;
import com.sensorberg.sdk.SensorbergServiceMessage;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

public class ScannerBroadcastReceiver extends SensorbergBroadcastReceiver {

    public static void setManifestReceiverEnabled(boolean enabled, Context context) {
        SensorbergBroadcastReceiver.setManifestReceiverEnabled(enabled, context, ScannerBroadcastReceiver.class);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_OFF:
                    this.stopScan(context);
                    break;
                case BluetoothAdapter.STATE_ON:
                    this.startScan(context);
                    break;
            }
            Logger.log.logBluetoothState(state);
        } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
            pingScanner(context);
            Logger.log.userPresent();
        }
    }

    private void pingScanner(Context context) {
        Intent service = new Intent(context, SensorbergService.class);
        service.putExtra(SensorbergServiceMessage.EXTRA_GENERIC_TYPE, SensorbergServiceMessage.MSG_PING);
        context.startService(service);
    }

    private void startScan(Context context) {
        Intent service = new Intent(context, SensorbergService.class);
        service.putExtra(SensorbergServiceMessage.EXTRA_GENERIC_TYPE, SensorbergServiceMessage.MSG_BLUETOOTH);
        service.putExtra(SensorbergServiceMessage.EXTRA_BLUETOOTH_STATE, true);
        context.startService(service);
    }

    private void stopScan(Context context) {
        Intent service = new Intent(context, SensorbergService.class);
        service.putExtra(SensorbergServiceMessage.EXTRA_GENERIC_TYPE, SensorbergServiceMessage.MSG_BLUETOOTH);
        service.putExtra(SensorbergServiceMessage.EXTRA_BLUETOOTH_STATE, false);
        context.startService(service);
    }
}
