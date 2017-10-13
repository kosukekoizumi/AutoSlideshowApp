package jp.techacademy.kousuke.koizumi.autoslideshowapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import java.util.Timer;
import java.util.TimerTask;

import static jp.techacademy.kousuke.koizumi.autoslideshowapp.R.id.start_button;
import static jp.techacademy.kousuke.koizumi.autoslideshowapp.R.id.rewind_button;
import static jp.techacademy.kousuke.koizumi.autoslideshowapp.R.id.forward_button;

public class MainActivity extends AppCompatActivity {

    Timer mTimer;
    TextView mTimerText;
    double mTimerSec = 0.0;

    Handler mHandler = new Handler();

    Button mStartButton;
    Button mRewindButton;
    Button mForwardButton;
    boolean canPush = true;

    private static final int PERMISSIONS_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            getContentsInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );

        if (cursor.moveToNext()) {

        } else {
            cursor.moveToFirst();
        }
        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        Long id = cursor.getLong(fieldIndex);
        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
        imageVIew.setImageURI(imageUri);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setText("  自動再生  ");
        mRewindButton = (Button) findViewById(R.id.rewind_button);
        mRewindButton.setText(" ＜＜ 戻る  ");
        mForwardButton = (Button) findViewById(forward_button);
        mForwardButton.setText(" 進む ＞＞  ");

        mStartButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {   //★自動再生・再生停止ボタンクリック

                if (mTimer == null) {
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mTimerSec += 0.1;

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //mTimerText.setText(String.format("%.1f", mTimerSec));
                                    //★タイマー関連はまだ手付かず！

                                }
                            });
                        }
                    }, 100, 100);
                }

                if ( canPush == true ) {
                    mStartButton.setText("  再生停止  ");
                    mForwardButton.setEnabled(false);
                    mRewindButton.setEnabled(false);
                    canPush = false;
                } else {
                    mStartButton.setText("  自動再生  ");
                    mForwardButton.setEnabled(true);
                    mRewindButton.setEnabled(true);
                    canPush = true;
                }

            }
        });

        mRewindButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {   //★戻るボタンクリック

                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                        null, // 項目(null = 全項目)
                        null, // フィルタ条件(null = フィルタなし)
                        null, // フィルタ用パラメータ
                        null // ソート (null ソートなし)
                );

                if (cursor.moveToPrevious()) {

                } else {
                    cursor.moveToFirst();
                }

                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

            }
        });

        mForwardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {   //★進むボタンクリック

                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                        null, // 項目(null = 全項目)
                        null, // フィルタ条件(null = フィルタなし)
                        null, // フィルタ用パラメータ
                        null // ソート (null ソートなし)
                );

                    if (cursor.moveToNext()) {

                    } else {
                        cursor.moveToFirst();
                    }

                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                    imageVIew.setImageURI(imageUri);

                }
            });

        cursor.close();
    }
}