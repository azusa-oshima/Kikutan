package com.mlt.tango.flagment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;

import com.mlt.tango.R;

import java.util.Arrays;
import java.util.List;

public class ReviewFlagment extends Fragment {
    private static TextView mTextView;
    private static TextView jTextView;
    private static List<String> wordMyanmar = Arrays.asList("apple", "banana","orange");
    private static List<String> wordJapanese = Arrays.asList("りんご", "バナナ","オレンジ");

    // Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flagment_review, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Activity側から渡された値を取得する。
//        String strValue01 = getArguments().getString("test1");
//        System.out.println("activityからの値" + strValue01);

    }


    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Activity側から渡された値を取得する。
        if (getArguments() != null) {
            String strValue01 = getArguments().getString("test1");
        }
        System.out.println("activityからの値" + getArguments() );

        super.onViewCreated(view, savedInstanceState);



        // TextViewをひも付けます
        mTextView = (TextView) view.findViewById(R.id.textView_myanmar);
        jTextView = (TextView) view.findViewById(R.id.textView_japanese);

        // Buttonのクリックした時の処理を書きます
//        view.findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClickStart(View v) {
//                mTextView.setText(mTextView.getText() + "!");
//            }
//        });

        view.setBackgroundColor(mBackgroundColor);
        // onCreateで受け取った値をセットする
        mTextView.setText(mName);

        System.out.println("てすと２" );
    }

    // 値をonCreateで受け取るため、新規で変数を作成する
    // 値がセットされなかった時のために初期値をセットする
    private String mName = "";
    private @ColorInt int mBackgroundColor = Color.TRANSPARENT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("てすと" );
        super.onCreate(savedInstanceState);
        // Bundleの値を受け取る際はonCreateメソッド内で行う
        Bundle args = getArguments();
        // Bundleがセットされていなかった時はNullなのでNullチェックをする
        if (args != null) {
            // String型でNameの値を受け取る
            mName = args.getString(KEY_NAME);
            // int型で背景色を受け取る
            mBackgroundColor = args.getInt(KEY_BACKGROUND, Color.TRANSPARENT);
        }
    }
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        /** 省略 **/
//
//        // ラストに追加
//        // 背景色をセットする
//        view.setBackgroundColor(mBackgroundColor);
//        // onCreateで受け取った値をセットする
//        mTextView.setText(mName);
//    }

    // このクラス内でだけ参照する値のため、BundleのKEYの値をprivateにする
    private final static String KEY_NAME = "key_name";
    private final static String KEY_BACKGROUND = "key_background_color";

    // このメソッドからFragmentを作成することを強制する
    // Fragmentに値を引き渡す。
    @CheckResult
    public static ReviewFlagment createInstance(int number, @ColorInt int color) {
        // Fragmentを作成して返すメソッド
        // createInstanceメソッドを使用することで、そのクラスを作成する際にどのような値が必要になるか制約を設けることができる
        ReviewFlagment fragment = new ReviewFlagment();
        // Fragmentに渡す値はBundleという型でやり取りする
        Bundle args = new Bundle();
        // Key/Pairの形で値をセットする
//        args.putString(KEY_NAME, name);
        args.putInt(KEY_BACKGROUND, color);
        // Fragmentに値をセットする
        fragment.setArguments(args);
//        System.out.println("受け渡し成功" + name);


        // 番号
        mTextView.setText(wordMyanmar.get(number));
        jTextView.setText(wordMyanmar.get(number));

        return fragment;
    }
}