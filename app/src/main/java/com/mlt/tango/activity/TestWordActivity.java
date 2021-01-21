package com.mlt.tango.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mlt.tango.R;
import com.mlt.tango.entity.WordInfoDto;

import java.util.ArrayList;

public class TestWordActivity extends AppCompatActivity {

    ArrayList<WordInfoDto> testWordsList = new ArrayList<WordInfoDto>();
    ArrayList<WordInfoDto> knownList = new ArrayList<WordInfoDto>();
    ArrayList<WordInfoDto> unknownList = new ArrayList<WordInfoDto>();

    // 連打防止用にカテゴリを引き渡す
    int nowCategory = 0;

    // テスト番号
    private int nowTestCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_word);

        // メイン画面からの単語情報を受け取る
        Intent intent = getIntent();
        testWordsList = (ArrayList<WordInfoDto>) intent.getSerializableExtra("TEST_WORDS");
        WordInfoDto testWords = testWordsList.get(nowTestCount);
        nowCategory = testWords.getCategory();

        // 単語番号の格納
        displayText(nowTestCount + 1, testWordsList.size());

        // ミャンマー文字にDBから取得した値を格納
        TextView myanmarText = findViewById(R.id.myanmarText);
        myanmarText.setText(testWords.burmese);

        // 日本語にDBから取得した値を格納
        TextView japaneseText = findViewById(R.id.japaneseText);
        japaneseText.setText(testWords.japanese);

        // 「カテゴリ」を画面に挿入
        TextView textView = findViewById(R.id.cate_test);
        textView.setText("カテゴリ" + nowCategory);
    }

    /* 何周目かのテキストを表示する */
    private void displayText(int nowTestNumber, int allTestNumber) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(String.valueOf(nowTestNumber));
        int start = sb.length();
        sb.append("/" + allTestNumber);
        sb.setSpan(new RelativeSizeSpan(0.5f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView = findViewById(R.id.wordNumberText);
        textView.setText(sb);
    }

    // 意味確認時の画面メッセージ、ボタン表示を変更する。
    public void pressCheckMeaning(View text1){
        // 日本語訳を表示
        TextView japaneseText = findViewById(R.id.japaneseText);
        japaneseText.setVisibility(View.VISIBLE);

        //「 意味確認」ボタンを「分からない」ボタンに変更
        TextView unknownText = findViewById(R.id.unknownText);
        unknownText.setText("分からない");
        ImageButton checkMeaningButton = findViewById(R.id.unknownButton);
        checkMeaningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pressKnownUnknownButton(v);
            }
        });
    }

    // 「分かる」「分からない」ボタンが押された時の動作
    public void pressKnownUnknownButton(View view) {

        // ワードリストが終わっている場合は終了画面に遷移
        // もし連打されてリスト番号がリストより大きくなってしまったら、この処理をよける。
        if (nowTestCount < testWordsList.size()) {
            // 「分かる」「分からない」のどちらのボタンが押されたかによって追加するリストを分ける。
            switch (view.getId()) {
                case R.id.knownButton:
                    knownList.add(testWordsList.get(nowTestCount));
                    break;
                case R.id.unknownButton:
                    unknownList.add(testWordsList.get(nowTestCount));
                    break;
                default:
            }
            nowTestCount++;
        }

        if (nowTestCount < testWordsList.size()) {
            // ワードリストがまだある場合は次の試験
            goNextWord();
        } else {
            goSummary();
        }
    }

    // サマリ画面に行く
    public void goSummary(){
        Intent intent = new Intent(this, TestSummaryActivity.class);
        intent.putExtra("KNOWN_DATA", knownList);
        intent.putExtra("UNKNOWN_DATA", unknownList);
        intent.putExtra("NOW_CATEGORY", nowCategory);
        startActivity(intent);
    }

    // 次の単語テストに進む
    public void goNextWord(){
        // テスト画面を次に進める
        setContentView(R.layout.activity_test_word);
        WordInfoDto testWords = testWordsList.get(nowTestCount);

        // ミャンマー文字にDBから取得した値を格納
        TextView myanmarText = findViewById(R.id.myanmarText);
        myanmarText.setText(testWords.burmese);

        // 日本語にDBから取得した値を格納
        TextView japaneseText = findViewById(R.id.japaneseText);
        japaneseText.setText(testWords.japanese);

        // 単語番号の格納
        displayText(nowTestCount + 1, testWordsList.size());

        // 「カテゴリ」を画面に挿入
        TextView textView = findViewById(R.id.cate_test);
        textView.setText("カテゴリ" + nowCategory);
    }

    // 戻るボタン押下
    @Override
    public void onBackPressed() {
        // 何もしない
    }

    // ホームボタンが押された時
    public void home(View text1){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
