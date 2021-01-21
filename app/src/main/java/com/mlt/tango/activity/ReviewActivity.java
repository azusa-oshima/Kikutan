package com.mlt.tango.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.mlt.tango.R;
import com.mlt.tango.entity.WordInfoDto;
import com.mlt.tango.flagment.ReviewFlagment;
import com.mlt.tango.library.MusicLibrary;
import com.mlt.tango.model.AppConstants;
import com.mlt.tango.service.MusicService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewActivity extends AppCompatActivity {

    MediaBrowserCompat mBrowser;
    MediaControllerCompat mController;


    TextView textView_title;
    TextView textView_position;
    TextView textView_duration;
    ImageButton button_prev;
    ImageButton button_next;
    FloatingActionButton button_play;
    ImageView imageView;
    SeekBar seekBar;


    ArrayList<WordInfoDto> testWordsList = new ArrayList<WordInfoDto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /// ★追加★
        // Fragmentを作成します
        ReviewFlagment fragment = new ReviewFlagment();
        Bundle bundle = new Bundle();
        bundle.putString("test1", "これはテストです");
// フラグメントに渡す値をセット
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment1, fragment);
        transaction.commit();
        setContentView(R.layout.activity_review);

        // サマリ画面から復習する単語を受け取る
        // ★変更点追加
        // メイン画面からのワード情報を受け取る
        // 最終的にアクティビティから直接来る可能性もあるので、DBから直接取った方がいい。
        // DBでは、0テスト中でない → 1復習中 → 2テスト終了
        Intent intent = getIntent();
        testWordsList = (ArrayList<WordInfoDto>) intent.getSerializableExtra("TEST_WORDS");

        // 音楽ファイルの名前を取得する
        List<String> musicFileNameList = testWordsList
                .stream()
                .map(s -> AppConstants.MB_MUSIC_NAME + s.number + AppConstants.MUSIC_FILE)
                .collect(Collectors.toList());
        // 初期化する
        MusicLibrary.init(AppConstants.CATEGORY, musicFileNameList);

        //UI系のセットアップ
//        textView_title = findViewById(R.id.textView_title);
        textView_position = findViewById(R.id.textView_position);
        textView_duration = findViewById(R.id.textView_duration);
        button_next = findViewById(R.id.button_next);
        button_prev = findViewById(R.id.button_prev);
        button_play = findViewById(R.id.button_play);
//        imageView = findViewById(R.id.imageView);
        seekBar = findViewById(R.id.seekBar);

        button_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.getTransportControls().skipToPrevious();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.getTransportControls().skipToNext();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //シークする
                mController.getTransportControls().seekTo(seekBar.getProgress());
            }
        });

        //MediaBrowserを初期化
        mBrowser = new MediaBrowserCompat(this, new ComponentName(this, MusicService.class), connectionCallback, null);
        //接続(サービスをバインド)
        mBrowser.connect();




        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
//        transaction = getSupportFragmentManager().beginTransaction();
//        // 新しく追加を行うのでaddを使用します
//        // 他にも、よく使う操作で、replace removeといったメソッドがあります
//        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
////        transaction.replace(R.id.fragment1, fragment);
//        // 最後にcommitを使用することで変更を反映します
//        transaction.commit();

        //  onDestroy()
    }

    //接続時に呼び出されるコールバックTEST_WORDS
    private MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            System.out.println("コネクト1");

            try {
                //接続が完了するとSessionTokenが取得できるので
                //それを利用してMediaControllerを作成
                mController = new MediaControllerCompat(ReviewActivity.this, mBrowser.getSessionToken());
                //サービスから送られてくるプレイヤーの状態や曲の情報が変更された際のコールバックを設定
                mController.registerCallback(controllerCallback);

                //既に再生中だった場合コールバックを自ら呼び出してUIを更新
                if (mController.getPlaybackState() != null && mController.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    controllerCallback.onMetadataChanged(mController.getMetadata());
                    controllerCallback.onPlaybackStateChanged(mController.getPlaybackState());
                }

            } catch (RemoteException ex) {
                ex.printStackTrace();
                Toast.makeText(ReviewActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            //サービスから再生可能な曲のリストを取得
            mBrowser.subscribe(mBrowser.getRoot(), subscriptionCallback);
        }


    };

    //Subscribeした際に呼び出されるコールバック
    private MediaBrowserCompat.SubscriptionCallback subscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            //既に再生中でなければ初めの曲を再生をリクエスト
            if (mController.getPlaybackState() == null)
                Play(children.get(0).getMediaId());
        }
    };

    //MediaControllerのコールバック
    private MediaControllerCompat.Callback controllerCallback = new MediaControllerCompat.Callback() {
        //再生中の曲の情報が変更された際に呼び出される
        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            // MusicLIbraryを更新する
            // 音楽が止まった　→　とまったことをactivityのcallbackでキャッチする
            // もう一回再生する（ライブラリの番号を指定する）

//            textView_title.setText(metadata.getDescription().getTitle());
//            imageView.setImageBitmap(metadata.getDescription().getIconBitmap());
            textView_duration.setText(Long2TimeString(metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)));
            seekBar.setMax((int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));

            // ここに画面を作る
            System.out.println("タイトルなどを変更する");
            String test = metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment1, ReviewFlagment.createInstance(Integer.parseInt(test) , Color.RED));
        }

        //プレイヤーの状態が変更された時に呼び出される
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {

            //プレイヤーの状態によってボタンの挙動とアイコンを変更する
            if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                button_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mController.getTransportControls().pause();
                    }
                });
                button_play.setImageResource(R.drawable.ic_round);
            } else {
                button_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mController.getTransportControls().play();
                    }
                });
                button_play.setImageResource(R.drawable.ic_sansyo_study);
            }

            textView_position.setText(Long2TimeString(state.getPosition()));
            seekBar.setProgress((int) state.getPosition());

        }
    };

    private void Play(String id) {
        //MediaControllerからサービスへ操作を要求するためのTransportControlを取得する
        //playFromMediaIdを呼び出すと、サービス側のMediaSessionのコールバック内のonPlayFromMediaIdが呼ばれる
        mController.getTransportControls().playFromMediaId(id, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBrowser.disconnect();

        if (mController.getPlaybackState().getState() != PlaybackStateCompat.STATE_PLAYING) {
            stopService(new Intent(this, MusicService.class));
        }
    }


    //Long値をm:ssの形式の文字列にする
    private String Long2TimeString(long src) {
        String mm = String.valueOf(src / 1000 / 60);
        String ss = String.valueOf((src / 1000) % 60);

        //秒は常に二桁じゃないと変
        if (ss.length() == 1) ss = "0" + ss;

        return mm + ":" + ss;
    }

    // テスト実行するボタン
    public void onClickToTest(View v) {
        mBrowser.disconnect();
        // 次の画面に受け渡す値をセット
        //インテントの作成
        Intent intent = new Intent(this, TestWordActivity.class);
        intent.putExtra("TEST_WORDS", testWordsList);
        //遷移先の画面を起動
        startActivity(intent);

    }

    // 戻るボタン押下
    @Override
    public void onBackPressed() {
        // 何もしない
    }
}
