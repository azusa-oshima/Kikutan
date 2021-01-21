package com.mlt.tango.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mlt.tango.AppDatabase;
import com.mlt.tango.AppDatabaseSingleton;
import com.mlt.tango.R;
import com.mlt.tango.dao.UserInfoDao;
import com.mlt.tango.dao.WordInfoDao;
import com.mlt.tango.dao.WordTestDao;
import com.mlt.tango.entity.WordInfoDto;
import com.mlt.tango.model.AppConstants;
import com.mlt.tango.model.TestStatus;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestSummaryActivity extends AppCompatActivity {

    AppDatabase db = AppDatabaseSingleton.getInstance();
    UserInfoDao userInfoDao = db.userInfoDao();
    WordInfoDao wordInfoDao = db.wordInfoDao();
    WordTestDao wordTestDao = db.wordTestDao();

    List<WordInfoDto> knownList = new ArrayList<WordInfoDto>();
    List<WordInfoDto> unknownList = new ArrayList<WordInfoDto>();
    List<WordInfoDto> nextWordsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_summary);

        // メイン画面からの単語情報を受け取る
        Intent intent = getIntent();
        knownList = (ArrayList<WordInfoDto>) intent.getSerializableExtra("KNOWN_DATA");
        unknownList = (ArrayList<WordInfoDto>) intent.getSerializableExtra("UNKNOWN_DATA");

        // 「分かる」「分からない」の単語数を画面に挿入
        int knownNumber = knownList.size();
        int unknownNumber = unknownList.size();
        int sumCount = knownNumber + unknownNumber;
        displayText(knownNumber, sumCount);

        // 「カテゴリ」を画面に挿入
        int nowCategory = (int) intent.getSerializableExtra("NOW_CATEGORY");
        SpannableStringBuilder sb = new SpannableStringBuilder();
        TextView textView = findViewById(R.id.summary_cate);
        textView.setText("カテゴリ" + nowCategory);

        // 正解したもののステータスを「test_status=1」(FINISH)に更新する TODO
        List<String> knownNumberList = knownList
                .stream()
                .map(s -> String.valueOf(s.number))
                .collect(Collectors.toList());
        wordTestDao.updateTestStatus(TestStatus.FINISH.getCode(), knownNumberList);

        // 全問正解の場合、次のカテゴリの問題があるかを確認しておく。
        // カテゴリがなければ、一旦ホームに戻る。（2周目するときなど）
//        int nowCategory = userInfoDao.getByNumber(AppConstants.USER_NUM).now_category;

        int nextCategory = nowCategory + 1;
        TextView nextButton = findViewById(R.id.next);
//        TextView reviewButton = findViewById(R.id.review);
        // 全問正解の場合
        if (unknownNumber == 0) {
            nextWordsList = wordInfoDao.getTestWord(nextCategory, TestStatus.ONGOING.getCode());
            // 次のカテゴリがなければ、「次の問題へ」のボタンを表示しない
            if (CollectionUtils.isEmpty(nextWordsList)) {
//                nextButton.setVisibility(View.INVISIBLE);
//                reviewButton.setVisibility(View.INVISIBLE);
            } else {
                // カテゴリがあれば、現在のカテゴリを更新する
                userInfoDao.updateCategory(AppConstants.USER_NUM, nextCategory);
                // ボタンに「次の問題へ」を表示する
//                nextButton.setVisibility(View.VISIBLE);
//                reviewButton.setVisibility(View.INVISIBLE);
            }
        } else {
            // 全問正解じゃない場合はホームボタンを表示する
            // 「復習する」のボタンを表示するか
            // ボタンに「次の問題へ」を表示する
//            nextButton.setVisibility(View.INVISIBLE);
//            reviewButton.setVisibility(View.VISIBLE);
        }
    }

    /* 正解数のテキストを表示する */
    private void displayText(int knownNumber, int sumCount) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(String.valueOf(knownNumber));
        int start = sb.length();
        sb.append("/" + sumCount + "問正解");
        sb.setSpan(new RelativeSizeSpan(0.5f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView = findViewById(R.id.test_score);
        textView.setText(sb);
    }

    /*
     * 次の問題に行くとき
     **/
    public void nextQs(View text1){
        // 復習ボタンが押された時、復習画面に遷移する
        Intent intent = new Intent(this, TestWordActivity.class);
        intent.putExtra("TEST_WORDS", (ArrayList<WordInfoDto>) unknownList);
        startActivity(intent);
    }

    /*
    * 復習するとき
    * 次の問題に行くとき
    **/
    public void retry(View text1){
        // 復習ボタンが押された時、復習画面に遷移する
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("TEST_WORDS", (ArrayList<WordInfoDto>) unknownList);
        startActivity(intent);
    }

    // ホームボタンが押された時
    public void goHome(View text1){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // 戻るボタン押下
    @Override
    public void onBackPressed() {
        // 何もしない
    }
}
