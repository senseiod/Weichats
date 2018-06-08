package top.iouyi.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final static int WRITE_EXTERNAL_STORAGE_CODE = 102;

    private PermissionModel[] models = new PermissionModel[]{
            new PermissionModel(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "我们需要您允许我们读写你的存储卡，以方便我们临时保存一些数据",
                    WRITE_EXTERNAL_STORAGE_CODE),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openDemo();
        } else {
           // 未获得所有权限所以退出
            finish();
            System.exit(0);
        }

    }

    private void openDemo() {
        startActivity(new Intent(this, SynthActivity.class));
        this.finish();
    }

    private void checkPermissions() {
        for (PermissionModel model : models) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, model.permission)) {
                ActivityCompat.requestPermissions(this, new String[]{model.permission}, model.requestCode);
                return;
            }
            openDemo();
        }
    }

    private static class PermissionModel {
        String permission;
        String explain;
        int requestCode;
        PermissionModel(String permission, String explain, int requestCode) {
            this.permission = permission;
            this.explain = explain;
            this.requestCode = requestCode;
        }
    }

}
