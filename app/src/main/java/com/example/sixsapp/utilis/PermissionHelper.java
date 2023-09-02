package com.example.sixsapp.utilis;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    private static final int REQUEST_CODE_PERMISSIONS = 100;

    private String[] requiredPermissions;

    private Activity activity;
    private PermissionListener permissionListener;

    public PermissionHelper(Activity activity, PermissionListener permissionListener) {
        this.activity = activity;
        this.permissionListener = permissionListener;

        // Define your required permissions here
        requiredPermissions = new String[]{
                Manifest.permission.CAMERA
                // Add more permissions if needed
        };
    }

    public void checkAndRequestPermissions() {
        List<String> missingPermissions = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (missingPermissions.isEmpty()) {
            permissionListener.onPermissionsGranted();
        } else {
            showRationaleDialog(missingPermissions.toArray(new String[0]));
        }
    }

    private void showRationaleDialog(final String[] permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Permission Required")
            .setMessage("We need camera and storage access to capture and save images.")
            .setPositiveButton("Grant", (dialog, which) -> {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSIONS);
                dialog.dismiss();
            })
            .setNegativeButton("Cancel", (dialog, which) -> {
                permissionListener.onPermissionsDenied();
                dialog.dismiss();
            })
            .show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                permissionListener.onPermissionsGranted();
            } else {
                permissionListener.onPermissionsDenied();
            }
        }
    }

    public interface PermissionListener {
        void onPermissionsGranted();
        void onPermissionsDenied();
    }
}
