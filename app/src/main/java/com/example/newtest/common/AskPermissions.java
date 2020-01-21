package com.example.newtest.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.newtest.R;

@SuppressLint("Registered")
public class AskPermissions extends AppCompatActivity {

    public final String[] PERMISSIONS_LOCATION = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    public final String[] PERMISSIONS_CAMERA = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public final String[] PERMISSIONS_ALBUM = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public final int PERMISSIONS_RESULT_LOCATION = 100;
    public final int PERMISSIONS_RESULT_CAMERA = 200;
    public final int PERMISSIONS_RESULT_ALBUM = 300;
    private final int ACTIVITY_RESULT_SETTINGS_LOCATION = 400; // from '설정'
    private final int ACTIVITY_RESULT_SETTINGS_CAMERA = 500;
    private final int ACTIVITY_RESULT_SETTINGS_ALBUM = 600;

    protected void allowedPermissionLocation() {

    }

    protected void allowedPermissionCamera() {

    }

    protected void allowedPermissionAlbum() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean checkResult = true;
        for (int result : grantResults) { // 모든 퍼미션이 허용됐는지 체크
            if (result != PackageManager.PERMISSION_GRANTED) {
                checkResult = false;
                break;
            }
        }

        if (requestCode == PERMISSIONS_RESULT_LOCATION) {
            if (checkResult) {
                allowedPermissionLocation();
            } else {
                // 일반 거부: true & 처음, 다시 묻지 않음 거부: false
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])) {
                    showDialogLocationPermission();
                } else {
                    showDialogLocationPermissionBySelectNoShow();
                }
            }
        }

        if (requestCode == PERMISSIONS_RESULT_CAMERA) {
            if (checkResult) {
                allowedPermissionCamera();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[2])) {
                    showDialogCameraPermission();
                } else {
                    showDialogCameraPermissionBySelectNoShow();
                }
            }
        }

        if (requestCode == PERMISSIONS_RESULT_ALBUM) {
            if (checkResult) {
                allowedPermissionAlbum();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])) {
                    showDialogAlbumPermission();
                } else {
                    showDialogAlbumPermissionBySelectNoShow();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_RESULT_SETTINGS_LOCATION)
            requestPermissions(PERMISSIONS_LOCATION, PERMISSIONS_RESULT_LOCATION);

        if (requestCode == ACTIVITY_RESULT_SETTINGS_CAMERA)
            requestPermissions(PERMISSIONS_CAMERA, PERMISSIONS_RESULT_CAMERA);

        if (requestCode == ACTIVITY_RESULT_SETTINGS_ALBUM)
            requestPermissions(PERMISSIONS_ALBUM, PERMISSIONS_RESULT_ALBUM);
    }

    private void showDialogLocationPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_place_black_24dp)
                .setTitle("위치 사용 권한")
                .setMessage("현재 위치를 표시하기 위해 '위치' 사용 권한이 필요합니다.")
                .setPositiveButton("재시도", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(PERMISSIONS_LOCATION, PERMISSIONS_RESULT_LOCATION);
                    }
                })
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showDialogLocationPermissionBySelectNoShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_place_black_24dp)
                .setTitle("위치 사용 권한")
                .setMessage("현재 위치를 표시하기 위해 '위치' 사용 권한이 필요합니다. [설정] → [권한] 에서 권한을 허용해주세요.")
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(intent, ACTIVITY_RESULT_SETTINGS_LOCATION);
                    }
                })
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showDialogCameraPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("필수 사용 권한")
                .setMessage(getEmojiByUnicode(0x1F4F7) + " 카메라\n"
                        + "사진 촬영을 위해 '카메라' 사용 권한이 필요합니다.\n\n"
                        + getEmojiByUnicode(0x1F4BE) + " 저장공간\n"
                        + "사진을 저장하고 불러오기 위해 '저장공간' 사용 권한이 필요합니다.")
                .setPositiveButton("재시도", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(PERMISSIONS_CAMERA, PERMISSIONS_RESULT_CAMERA);
                    }
                })
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showDialogCameraPermissionBySelectNoShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("필수 사용 권한")
                .setMessage(getEmojiByUnicode(0x1F4F7) + " 카메라\n"
                        + "사진 촬영을 위해 '카메라' 사용 권한이 필요합니다.\n\n"
                        + getEmojiByUnicode(0x1F4BE) + " 저장공간\n"
                        + "사진을 저장하고 불러오기 위해 '저장공간' 사용 권한이 필요합니다.\n\n"
                        + "[설정] → [권한] 에서 권한을 허용해주세요.")
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(intent, ACTIVITY_RESULT_SETTINGS_CAMERA);
                    }
                })
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showDialogAlbumPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("필수 사용 권한")
                .setMessage(getEmojiByUnicode(0x1F4BE) + " 저장공간\n"
                        + "사진을 저장하고 불러오기 위해 '저장공간' 사용 권한이 필요합니다.")
                .setPositiveButton("재시도", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(PERMISSIONS_ALBUM, PERMISSIONS_RESULT_ALBUM);
                    }
                })
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showDialogAlbumPermissionBySelectNoShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("필수 사용 권한")
                .setMessage(getEmojiByUnicode(0x1F4BE) + " 저장공간\n"
                        + "사진을 저장하고 불러오기 위해 '저장공간' 사용 권한이 필요합니다.\n\n"
                        + "[설정] → [권한] 에서 권한을 허용해주세요.")
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(intent, ACTIVITY_RESULT_SETTINGS_ALBUM);
                    }
                })
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public boolean checkSelfPermissionLocation() {
        return ContextCompat.checkSelfPermission(this, PERMISSIONS_LOCATION[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, PERMISSIONS_LOCATION[1]) == PackageManager.PERMISSION_GRANTED;
    }
}
