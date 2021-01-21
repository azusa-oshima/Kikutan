package com.mlt.tango.helper;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class CreateGraphHelper {

    // 円グラフのデータ設定
    public PieData createPieChartData(int allCount, int finishCount) {

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
}