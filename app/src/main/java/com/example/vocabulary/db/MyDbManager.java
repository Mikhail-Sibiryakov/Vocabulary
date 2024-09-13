package com.example.vocabulary.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vocabulary.WordAdd;
import com.example.vocabulary.adapter.ListItem;
import com.example.vocabulary.adapter.ListItem2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class MyDbManager {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    public void openDb() {
        db = myDbHelper.getWritableDatabase();
    }

    // Добавление слова в англискую таблицу
    public void insertToEn(String word) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE_WORD, word);
        db.insert(MyConstants.TABLE_NAME_EN, null, cv);
    }

    // Добавление слова в русскую таблицу
    public void insertToRu(String word) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE_WORD, word);
        db.insert(MyConstants.TABLE_NAME_RU, null, cv);
    }

    // Добавление ребра в таблицу: id англ слова, id русск слова
    public void insertToEdge(int id_en, int id_ru) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE_ID_EN, id_en);
        cv.put(MyConstants.TITLE_ID_RU, id_ru);
        db.insert(MyConstants.TABLE_NAME_EDGE, null, cv);
    }

    // Получение англиского слова по его id
    public String getWordFromIdEn(int id) {
        String selection = MyConstants._ID + "=" + id;
        Cursor cursor = db.query(MyConstants.TABLE_NAME_EN, null, selection, null,
                null, null, null);
        String ans = "";
        while (cursor.moveToNext()) {
            int tmp_id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
            if (tmp_id == id) {
                ans = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE_WORD));
                break;
            }
        }
        cursor.close();
        return ans;
    }

    // Получение русского слова по его id
    public String getWordFromIdRu(int id) {
        String selection = MyConstants._ID + "=" + id;
        Cursor cursor = db.query(MyConstants.TABLE_NAME_RU, null, selection, null,
                null, null, null);
        String ans = "";
        while (cursor.moveToNext()) {
            int tmp_id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
            if (tmp_id == id) {
                ans = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE_WORD));
                break;
            }
        }
        cursor.close();
        return ans;
    }

    // Получение id английского слова по самому слову
    public int getIdWordEn(String word) {
        String selection = MyConstants.TITLE_WORD + " like ?";
        Cursor cursor = db.query(MyConstants.TABLE_NAME_EN, null, selection, new String[]{word},
                null, null, null);
        int ans = -1;
        while (cursor.moveToNext()) {
            String tmp_word = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE_WORD));
            if (tmp_word.equals(word)) {
                ans = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
                break;
            }
        }
        cursor.close();
        return ans;
    }

    // Получение id русского слова по самому слову
    public int getIdWordRu(String word) {
        String selection = MyConstants.TITLE_WORD + " like ?";
        Cursor cursor = db.query(MyConstants.TABLE_NAME_RU, null, selection, new String[]{word},
                null, null, null);
        int ans = -1;
        while (cursor.moveToNext()) {
            String tmp_word = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE_WORD));
            if (tmp_word.equals(word)) {
                ans = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
                break;
            }
        }
        cursor.close();
        return ans;
    }

    // Получение id пары по самой паре (сначала id англ слова, потом id русск слова)
    public int getIdPair(int en_id, int ru_id) {
        Cursor cursor = db.query(MyConstants.TABLE_NAME_EDGE, null, null, null,
                null, null, null);
        int ans = -1;
        while (cursor.moveToNext()) {
            int tmp_en_id = cursor.getInt(cursor.getColumnIndex(MyConstants.TITLE_ID_EN));
            int tmp_ru_id = cursor.getInt(cursor.getColumnIndex(MyConstants.TITLE_ID_RU));
            if ((tmp_en_id == en_id) && (tmp_ru_id == ru_id)) {
                ans = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
                break;
            }
        }
        cursor.close();
        return ans;
    }

    // Очищение всей БД
    public void allDestroy() {
        myDbHelper.onUpgrade(db, MyConstants.DB_VERSION, MyConstants.DB_VERSION);
    }

    // По англ id и русск id определяется, есть ли такое ребро
    public boolean isWasThisPair(int en_id, int ru_id) {
        Cursor cursor = db.query(MyConstants.TABLE_NAME_EDGE, null, null, null,
                null, null, null);
        boolean ans = false;
        while (cursor.moveToNext()) {
            int tmp_en_id = cursor.getInt(cursor.getColumnIndex(MyConstants.TITLE_ID_EN));
            int tmp_ru_id = cursor.getInt(cursor.getColumnIndex(MyConstants.TITLE_ID_RU));
            if ((tmp_en_id == en_id) && (tmp_ru_id == ru_id)) {
                ans = true;
                break;
            }
        }
        cursor.close();
        return ans;
    }

    // Удаление английского слова по его id
    public void deleteFromEn(int id) {
        String selection = MyConstants._ID + "=" + id;
        db.delete(MyConstants.TABLE_NAME_EN, selection, null);
    }

    // Удаление русского слова по его id
    public void deleteFromRu(int id) {
        String selection = MyConstants._ID + "=" + id;
        db.delete(MyConstants.TABLE_NAME_RU, selection, null);
    }

    // Удаление ребра по его id
    public void deleteFromEdge(int id) {
        String selection = MyConstants._ID + "=" + id;
        db.delete(MyConstants.TABLE_NAME_EDGE, selection, null);
    }

    public void closeDb() {
        myDbHelper.close();
    }

    // Получение словаря где ключ - англ слово, значение - список русских переводов
    public HashMap<String, ArrayList<String>> getMapEnRu() {
        HashMap<String, ArrayList<String>> dict = new HashMap<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME_EDGE, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            int en_id = cursor.getInt(cursor.getColumnIndex(MyConstants.TITLE_ID_EN));
            int ru_id = cursor.getInt(cursor.getColumnIndex(MyConstants.TITLE_ID_RU));
            String en_word = getWordFromIdEn(en_id);
            String ru_word = getWordFromIdRu(ru_id);
            if (!dict.containsKey(en_word)) {
                dict.put(en_word, new ArrayList<>());
            }
            Objects.requireNonNull(dict.get(en_word)).add(ru_word);
        }
        cursor.close();
        return dict;
    }

    // Получение словаря где ключ - русск слово, значение - список английских переводов
    public HashMap<String, ArrayList<String>> getMapRuEn() {
        HashMap<String, ArrayList<String>> dict = new HashMap<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME_EDGE, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            int en_id = cursor.getInt(cursor.getColumnIndex(MyConstants.TITLE_ID_EN));
            int ru_id = cursor.getInt(cursor.getColumnIndex(MyConstants.TITLE_ID_RU));
            String en_word = getWordFromIdEn(en_id);
            String ru_word = getWordFromIdRu(ru_id);
            if (!dict.containsKey(ru_word)) {
                dict.put(ru_word, new ArrayList<>());
            }
            Objects.requireNonNull(dict.get(ru_word)).add(en_word);
        }
        cursor.close();
        return dict;
    }

    // Получение списка ключей map'ы
    public ArrayList<String> getKeysFromMap(HashMap<String, ArrayList<String>> dict) {
        ArrayList<String> ans = new ArrayList<>(dict.keySet());
        return ans;
    }

    // Удаление всех рёбер, в которых есть слово, по этому слову и его id
    public void deleteFromEdge(String word, int id_word) {
        String language;
        if (word.matches("[a-zA-Z]+")) {
            language = MyConstants.TITLE_ID_EN;
        } else {
            language = MyConstants.TITLE_ID_RU;
        }
        Cursor cursor = db.query(MyConstants.TABLE_NAME_EDGE, null, null, null,
                null, null, null);
        ArrayList<Integer> arr = new ArrayList<>();
        while (cursor.moveToNext()) {
            int tmp_id = cursor.getInt(cursor.getColumnIndex(language));
            if (id_word == tmp_id) {
                arr.add(cursor.getInt(cursor.getColumnIndex(MyConstants._ID)));
            }
        }
        for (int i : arr) {
            deleteFromEdge(i);
        }
        cursor.close();
    }

    // Делает первую букву слова заглавной
    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    // По мапе возвращает ArrayList из ListItem для адаптера
    public ArrayList<ListItem> getListItemFromMap(HashMap<String, ArrayList<String>> dict) {
        ArrayList<ListItem> tmpList = new ArrayList<>();
        ArrayList<String> keys = getKeysFromMap(dict);
        Collections.sort(keys);
        for (String key : keys) {
            ArrayList<String> values = dict.get(key);
            Collections.sort(values);
            StringBuilder tmp_str = new StringBuilder();
            tmp_str.append(firstUpperCase(key)).append(" - ");
            boolean isFirst = true;
            for (String value : values) {
                if (isFirst) {
                    isFirst = false;
                    tmp_str.append(value);
                    continue;
                }
                tmp_str.append(", ").append(value);
            }
            ListItem tmp = new ListItem(tmp_str.toString(), key, values);
            tmpList.add(tmp);
        }
        return tmpList;
    }

    // Удаление ребра двух слов по этим словам
    public void deleteEdgeFromTwoWords(String word1, String word2) {
        String word_en, word_ru;
        if (WordAdd.isEn(word1)) {
            word_en = word1;
            word_ru = word2;
        } else {
            word_en = word2;
            word_ru = word1;
        }
        int id_en = getIdWordEn(word_en);
        int id_ru = getIdWordRu(word_ru);
        int id_pair = getIdPair(id_en, id_ru);
        Log.d(word1, word2);
        Log.d(word_en, word_ru);
        deleteFromEdge(id_pair);
    }

    // Из ArrayList<String> делает ArrayList<ListItem2>, где ключ - 1ое слово, а значение - соотв эл-т
    public ArrayList<ListItem2> getArrayListItem2FromArray(ArrayList<String> arr) {
        ArrayList<ListItem2> ans = new ArrayList<>();
        String key = arr.get(0);
        for (String value : arr) {
            ListItem2 tmp = new ListItem2(key, value);
            ans.add(tmp);
        }
        return ans;
    }
}
