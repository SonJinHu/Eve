package com.example.newtest.common;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
public class PickImage extends AskPermissions {

    private final String TAG = getClass().getName();
    private final String DIRECTORY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EVe";
    private final int REQUEST_CODE_PICK_FROM_CAMERA = 101;
    private final int REQUEST_CODE_PICK_FROM_ALBUM = 102;

    private Uri tmpFileUri;
    private CroppedListener listener;

    public interface CroppedListener {
        void onAfterCrop(File file, Bitmap bitmap);
    }

    public void showDialogHowToPickImage(CroppedListener listener) {
        this.listener = listener;
        List<String> ListItems = new ArrayList<>();
        ListItems.add("카메라");
        ListItems.add("갤러리");
        CharSequence[] items = ListItems.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("사진 추가");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                switch (pos) {
                    case 0: // 카메라
                        requestPermissions(PERMISSIONS_CAMERA, PERMISSIONS_RESULT_CAMERA);
                        break;
                    case 1: // 갤러리
                        requestPermissions(PERMISSIONS_ALBUM, PERMISSIONS_RESULT_ALBUM);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void allowedPermissionCamera() {
        super.allowedPermissionCamera();
        takeCameraAction();
    }

    @Override
    protected void allowedPermissionAlbum() {
        super.allowedPermissionAlbum();
        takeAlbumAction();
    }

    private void takeCameraAction() {
        String name = "tmp_" + System.currentTimeMillis() + ".png";
        File file = new File(getDirectoryPath(), name);
        try {
            if (file.createNewFile()) {
                Log.e(TAG, "(임시) 이미지 파일 생성: " + file.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
                sendBroadcast(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            tmpFileUri = FileProvider.getUriForFile(this,
                    "com.example.newtest.fileprovider", file);
        else
            tmpFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tmpFileUri);
        startActivityForResult(intent, REQUEST_CODE_PICK_FROM_CAMERA);
    }

    private void takeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE_PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int REQUEST_CODE_CROP_IMAGE = 103;

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_PICK_FROM_ALBUM:
                // content://media/external/images/media/12410
                if (data != null)
                    tmpFileUri = data.getData();
            case REQUEST_CODE_PICK_FROM_CAMERA:
                // content://com.example.newtest.fileprovider/files/tmp_1568160912116.jpg
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(tmpFileUri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //intent.putExtra("outputX", 800);
                //intent.putExtra("outputY", 600);
                intent.putExtra("aspectX", 4);
                intent.putExtra("aspectY", 3);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
                break;
            case REQUEST_CODE_CROP_IMAGE:
                if (data != null && data.getExtras() != null) {
                    Bitmap bitmap = data.getExtras().getParcelable("data");
                    File file = saveCropImage(bitmap);
                    listener.onAfterCrop(file, bitmap);
                }
                break;
        }
    }

    private File saveCropImage(Bitmap bitmap) {
        String name = System.currentTimeMillis() + ".png";
        File file = new File(getDirectoryPath(), name);
        try {
            if (file.createNewFile()) {
                Log.e(TAG, "이미지 파일 생성: " + file.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
                sendBroadcast(intent);
            }
            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private String getDirectoryPath() {
        File dir = new File(DIRECTORY_PATH);
        if (!dir.exists() && dir.mkdir())
            Log.e(TAG, "EVe 폴더 생성: " + dir.getAbsolutePath());
        return DIRECTORY_PATH;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTmpFile();
    }

    private void deleteTmpFile() {
        File dir = new File(DIRECTORY_PATH);
        if (!dir.isDirectory())
            return;

        File[] files = dir.listFiles();
        if (files == null || files.length == 0)
            return;

        for (File file : files) {
            if (file.getName().startsWith("tmp_") && file.delete())
                Log.e(TAG, "임시 파일 삭제: " + file.getAbsolutePath());
        }
    }
}
