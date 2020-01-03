package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView versionNameTextView = findViewById(R.id.versionNameTextView);
        final TextView deviceIDTextView = findViewById(R.id.deviceIDTextView);

        setVersionNameTextView(versionNameTextView);
        setDeviceIDTextView(deviceIDTextView);
    }

    private void setVersionNameTextView(@NonNull TextView versionNameTextView) {
        final String versionName = BuildConfig.VERSION_NAME;
        versionNameTextView.setText(versionName);
    }

    private void setDeviceIDTextView(@NonNull TextView deviceIDTextView) {
        if (Build.VERSION.SDK_INT > 28) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Impossible to get device id (API > 28)",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        final int permissionStatus = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            final boolean shouldShowRequestPermissionRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_PHONE_STATE);
            if (shouldShowRequestPermissionRationale){
                requestPermissionWithRationale();
            } else {
                requestPermission();
            }
        } else {
            final TelephonyManager telephonyManager =
                    (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            final String deviceID = telephonyManager.getDeviceId();
            deviceIDTextView.setText(deviceID);
        }
    }

    private void requestPermissionWithRationale() {
        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed for a lab")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.READ_PHONE_STATE},
                REQUEST_CODE_READ_PHONE_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ_PHONE_STATE) {
            final TextView deviceIDTextView = findViewById(R.id.deviceIDTextView);
            setDeviceIDTextView(deviceIDTextView);
        }
    }
}
