package com.example.vocabulary.db;

public class MyConstants {
    public static final String EN_RU = "En-Ru";
    public static final String RU_EN = "Ru-En";
    public static boolean isFlagOnSwitch = false;
    public static String vocabulary = EN_RU;
    public static String training = EN_RU;
    public static final String MAP_EN_RU = "map_en_ru";
    public static final String MAP_RU_EN = "map_ru_en";
    public static final String LIST_ITEM_INTENT = "list_item_intent";
    public static final String TABLE_NAME_EN = "table_en";
    public static final String TABLE_NAME_RU = "table_ru";
    public static final String TABLE_NAME_EDGE = "table_edge";
    public static final String _ID = "_id";
    public static final String TITLE_ID_EN = "id_en";
    public static final String TITLE_ID_RU = "id_ru";
    public static final String TITLE_WORD = "word";
    public static final String DB_NAME = "dictionary.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_STRUCT_EN = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_EN + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE_WORD + " Text)";
    public static final String DROP_TABLE_EN = "DROP TABLE IF EXISTS " + TABLE_NAME_EN;

    public static final String TABLE_STRUCT_RU = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_RU + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE_WORD + " Text)";
    public static final String DROP_TABLE_RU = "DROP TABLE IF EXISTS " + TABLE_NAME_RU;

    public static final String TABLE_STRUCT_EDGE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_EDGE + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE_ID_EN +
            " INTEGER," + TITLE_ID_RU + " INTEGER)";
    public static final String DROP_TABLE_EDGE = "DROP TABLE IF EXISTS " + TABLE_NAME_EDGE;
}
