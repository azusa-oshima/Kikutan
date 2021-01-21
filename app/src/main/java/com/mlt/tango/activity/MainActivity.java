package com.mlt.tango.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mlt.tango.AppDatabase;
import com.mlt.tango.AppDatabaseSingleton;
import com.mlt.tango.R;
import com.mlt.tango.dao.UserInfoDao;
import com.mlt.tango.dao.WordInfoDao;
import com.mlt.tango.dao.WordTestDao;
import com.mlt.tango.entity.UserInfoEntity;
import com.mlt.tango.entity.WordInfoDto;
import com.mlt.tango.entity.WordTestEntity;
import com.mlt.tango.model.AppConstants;
import com.mlt.tango.model.TestResult;
import com.mlt.tango.model.TestStatus;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    AppDatabase db = AppDatabaseSingleton.getInstance();
    UserInfoDao userInfoDao = db.userInfoDao();
    WordInfoDao wordInfoDao = db.wordInfoDao();
    WordTestDao wordTestDao = db.wordTestDao();

    /* 初期画面表示 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DBからユーザ情報取得
        UserInfoEntity userInfoEntity = userInfoDao.getByNumber(AppConstants.USER_NUM);

        // DBから単語情報を取得して、グラフを表示する。
        List<WordInfoDto> wordInfoList = wordInfoDao.getAll();
        int round = userInfoEntity.now_round;
        int nowCategory = userInfoEntity.now_category;
        if (nowCategory == 0) {
            // 初期状態の時に、全単語情報のカテゴリを決定して更新する。
            setDisplayResetCategory(wordInfoList, round);
        } else {
            // 初期状態以外の時は、「学習スタート」ボタンを表示する。
            setDisplay(wordInfoList, round, nowCategory);
        }
    }

    /* 何周目かのテキストを表示する */
    private void displayText(int round) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(String.valueOf(round));
        int start = sb.length();
        sb.append(AppConstants.TEXT_ROUND);
        sb.setSpan(new RelativeSizeSpan(0.5f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView = findViewById(R.id.wordNumberText);
        textView.setText(sb);
    }

    /* 初期化時に全単語の「単語テスト情報：word_test」のレコードを追加する */
    private void setDisplayResetCategory(List<WordInfoDto> wordInfoList, int round) {
        List<Integer> numberList = wordInfoList
                .stream()
                .map(s -> s.number)
                .collect(Collectors.toList());

        // 単語テスト情報に全レコードを追加する
        List<List<Integer>> splitListList = ListUtils.partition(numberList, AppConstants.TEST_WORDS_GROUP);
        int category = 1;
        for (List<Integer> splitList : splitListList) {
            for (Integer split : splitList) {
                WordTestEntity entity = new WordTestEntity();
                entity.number = split;
                entity.category = category;
                entity.test_status = TestStatus.ONGOING.getCode();
                entity.test_result = TestResult.NO_DATA.getCode();
                entity.pre_test_result = TestStatus.ONGOING.getCode();
                entity.wrong_count = 0;
                wordTestDao.insert(entity);
            }
            category++;
        }

        // ユーザ情報のカテゴリを最新に更新する
        userInfoDao.updateCategory(AppConstants.USER_NUM, AppConstants.INITIAL_CATEGORY);

        // 周回の表示
        displayText(round);

        // グラフの表示
        int allCount = numberList.size();
        int cateAllCount = AppConstants.TEST_WORDS_GROUP;
        int finishCount = 0;
        createPieChart(allCount, finishCount, cateAllCount, finishCount, 1) ;
    }

    /* カテゴリが一致していて、ステータスが進行中のものをピックアップする。 */
    private void setDisplay(List<WordInfoDto> wordInfoList, int round, int nowCategory) {
        // カテゴリがそれ以外の時は、そのカテゴリのレコードをallCountたちにセットする。
        int cateAllCount = (int) wordInfoList
                .stream().filter(s -> s.category == nowCategory).count();
        int allCount = wordInfoList.size();
        int cateFinishCount = (int) wordInfoList
                .stream().filter(s -> s.category == nowCategory
                        && TestStatus.FINISH.equalsByCode(s.test_status)).count();
        int allFinishCount = (int) wordInfoList
                .stream().filter(s -> TestStatus.FINISH.equalsByCode(s.test_status)).count();

        // 周回表示を更新する
        displayText(round);

        // 円グラフを作成する
        createPieChart(allCount, allFinishCount, cateAllCount, cateFinishCount, nowCategory);
    }

    public void updateAllRecordCategory(List<WordInfoDto>  allWordInfoList) {
        List<Integer> numberList = allWordInfoList
                .stream()
                .map(s -> s.number)
                .collect(Collectors.toList());

        // 単語テスト情報に全レコードを追加する
        int category = 1;
        List<List<Integer>> splitListList = ListUtils.partition(numberList, AppConstants.TEST_WORDS_GROUP);
        for(List<Integer> splitList :splitListList) {
            wordTestDao.updateForNewRound(
                    category
                    , TestStatus.ONGOING.getCode()
                    , TestResult.NO_DATA.getCode()
                    , splitList);
            category++;
        }
    }

    /* 「このアプリについて」ボタンをクリックした時の動作 */
    public void goAboutApp(View v) {
        Intent intent = new Intent(this, AboutAppActivity.class);
        startActivity(intent);
    }

    /* 「学習をはじめる」ボタンをクリックした時の動作 */
    public void onClickStart(View v) {
        // DBからユーザ情報取得
        UserInfoEntity userInfoEntity = userInfoDao.getByNumber(AppConstants.USER_NUM);
        int nowCategory = userInfoEntity.now_category;

        // DBからテスト単語情報取得
        List<WordInfoDto> allWordInfoList = wordInfoDao.getAll();
        List<WordInfoDto> nowWordInfoList = allWordInfoList
                .stream().filter(s -> s.category == nowCategory && TestStatus.ONGOING.equalsByCode(s.test_status))
                .collect(Collectors.toList());
        List<WordInfoDto> nextWordInfoList = allWordInfoList
                .stream().filter(s -> s.category == nowCategory + 1).collect(Collectors.toList());

        // 現在のカテゴリーで未消化のテストがある場合
        if (CollectionUtils.isNotEmpty(nowWordInfoList)) {
            // 未消化の単語について、単語テスト画面に遷移する
            Intent intent = new Intent(this, TestWordActivity.class);
            intent.putExtra("TEST_WORDS", (ArrayList<WordInfoDto>) nowWordInfoList);
            startActivity(intent);
            return;
        }

        int testRound = 0;
        int testCategory = 0;
        List<WordInfoDto> testAllWordInfoList = new ArrayList<>();
        // 次のカテゴリーのテストがある場合
        if (CollectionUtils.isNotEmpty(nextWordInfoList)) {
            testAllWordInfoList =  wordInfoDao.getAll();
            testRound = userInfoEntity.now_round;
            testCategory = nowCategory + 1;
        } else {
            // 次のカテゴリーがないときは、周回数を更新する。
            // 全単語のカテゴリ情報を更新する
            updateAllRecordCategory(allWordInfoList);

            // カテゴリ情報更新後に再度全データを取得しておく
            testAllWordInfoList =  wordInfoDao.getAll();
            testRound = userInfoEntity.now_round + 1;
            testCategory = AppConstants.INITIAL_CATEGORY;
        }

        // 次のカテゴリーのテストがある場合
        // 「ユーザ情報」のカテゴリ、周回を更新する。
        userInfoDao.updateRound(AppConstants.USER_NUM, testRound, testCategory);
        setDisplay(testAllWordInfoList, testRound, testCategory);
    }

    /*
    * テスト単語が存在しない場合、ポップアップを表示する。
    **/
    public void onTapEvent() {
        Toast myToast = Toast.makeText(
                getApplicationContext(),
                "テストする単語が存在しません。",
                Toast.LENGTH_SHORT
        );
        myToast.show();
    }

    /*
    *  円グラフを作成する
    *
    * */
    private void createPieChart(int allCount, int finishCount, int cateAllCount, int cateFinishCount, int nowCategory) {

        // パイチャート自体の大きさはViewで管理
        PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.setDrawHoleEnabled(true); // 真ん中に穴を空けるかどうか
        pieChart.setHoleRadius(80f);       // 真ん中の穴の大きさ(%指定)
        pieChart.setRotationAngle(270);          // 開始位置の調整
        pieChart.setRotationEnabled(true);       // 回転可能かどうか
        pieChart.getLegend().setEnabled(false);   //
        Description des = new Description();
        des.setEnabled(false);
        pieChart.setDescription(des);

        // 円グラフ中央の文字を設定する（例：1語/300語）
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(finishCount+"");
        int start2 = sb.length();
        sb.append("語\n/\n" +  allCount +"語");
        sb.setSpan(new RelativeSizeSpan(0.4f), start2, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart.setCenterTextSize(64);
        pieChart.setCenterText(sb);

        // 円グラフのデータを設定する
        pieChart.setData(createPieChartData(allCount, finishCount));

        // 円グラフのデータを作成   TODO
        // 円グラフの下の残りの全て単語数を設定する
//        TextView textView = findViewById(R.id.all_tango_number);
//        int readyCount = allCount - finishCount;
//        textView.setText(String.valueOf(readyCount));
        // カテゴリ数を表示
//        SpannableStringBuilder sb2 = new SpannableStringBuilder();
//        sb2.append(nowCategory+"");
//        int start3 = sb2.length();
//        sb2.append("/" +  AppConstants.GROUP_NUMBER);
//        sb2.setSpan(new RelativeSizeSpan(0.7f), start3, sb2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        TextView textView = findViewById(R.id.all_tango_number);
//        textView.setTextSize(36);
//        textView.setText(sb2);

        // 「カテゴリ」を画面に挿入
        TextView textView = findViewById(R.id.cate_main);
        textView.setText("カテゴリ" + nowCategory + "/"+ AppConstants.GROUP_NUMBER);

        // 円グラフの次のテスト単語数を設定する
        TextView textView2 = findViewById(R.id.tango);
        int readyCount2 = cateAllCount - cateFinishCount;
        textView2.setText(String.valueOf(readyCount2));

        // 更新
        pieChart.invalidate();
        // アニメーション
        pieChart.animateXY(2000, 2000); // 表示アニメーション
    }

    // 円グラフのデータ設定
    private PieData createPieChartData(int allCount, int finishCount) {

        float finishRate = ((float) finishCount / allCount) * 100;
        float readyRate = 100 - finishRate;

        ArrayList<String> xVals = new ArrayList<>();
        List<PieEntry> yVals = new ArrayList<>();

        xVals.add("学習済");
        xVals.add("未学習");

        yVals.add(new PieEntry(finishRate, 0));
        yVals.add(new PieEntry(readyRate, 1));

        PieDataSet dataSet = new PieDataSet(yVals, "Data");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(1f);

        // 色の設定
        // ★設定方法：色コードの16進数の先頭に0xFFをつける
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(0xFF95CD82);
        colors.add(0xFFE5E5E5);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLACK);
        return data;
    }

    // 戻るボタン押下
    @Override
    public void onBackPressed() {
        // 何もしない
    }
}
