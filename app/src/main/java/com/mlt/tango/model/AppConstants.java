package com.mlt.tango.model;

public class AppConstants {

    // privateコンストラクタでインスタンス生成を抑止
    private AppConstants(){}

    // ユーザ番号
    public static final int USER_NUM = 1;
    // 何周目の表示
    public static final String TEXT_ROUND = "周目";
    /* 1グループの単語数 */
    public static final int TEST_WORDS_NUMBER = 295;
    /* 1グループの単語数 */
    public static final int TEST_WORDS_GROUP = 10;
    /* グループ数　固定で持つ 295/10 のくりあげ */
    public static final int GROUP_NUMBER = 30;
    /* 1グループの単語数 */
    public static final int INITIAL_CATEGORY = 1;
    /* 1グループの単語数 */
    public static final String CATEGORY = "mb";
    /* 1グループの単語数 */
    public static final String MB_MUSIC_NAME = CATEGORY + "_word_";
    /* 1グループの単語数 */
    public static final String MUSIC_FILE = ".m4a";

}
