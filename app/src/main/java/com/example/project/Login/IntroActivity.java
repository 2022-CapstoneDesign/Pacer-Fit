package com.example.project.Login;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;

public class IntroActivity extends AppCompatActivity {
    private final int ACCESS_FINE_LOCATION = 1000;
    private Button loginBtn;
    private Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_intro_activity);

        loginBtn = findViewById(R.id.introLoginBtn);
        joinBtn = findViewById(R.id.introJoinBtn);

        if (checkLocationService()) {
            permissionCheck();
        } else {
            Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
        });

        joinBtn.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, RegisterActivity.class));
            finish();
        });
    }

    // GPS 허용 여부 권환 확인

    private void permissionCheck() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        SharedPreferences preference = getPreferences(MODE_PRIVATE);
        boolean isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절시 다시 한번 물어봄
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(IntroActivity.this, permissions, ACCESS_FINE_LOCATION);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply();
                    ActivityCompat.requestPermissions(this, permissions, ACCESS_FINE_LOCATION);
                } else {
                    // 다시 묻지 않음 클릭 (앱 정보 화면으로 이동)
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요");
                    builder.setPositiveButton("설정으로 이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"));
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            }
        } else {
            // 권한이 있는 상태
        }
    }
    private boolean checkLocationService() {
        LocationManager locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}