package com.example.vocabulary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.vocabulary.db.MyDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class WordAdd {

    // true - слово из букв, false - в слове не только буквы
    public static boolean isAlpha(String word) {
        return (isEn(word) || isRu(word));
    }

    // true - слово из англ букв, false - в слове не только англ буквы
    public static boolean isEn(String word) {
        return word.matches("[a-zA-Z\\s]+");
    }

    // true - слово из русск букв, false - в слове не только русск буквы
    public static boolean isRu(String word) {
        return word.matches("[а-яА-Я\\s]+");
    }

    // Делает все буквы маленькмими, убирает лишние пробелы
    public static String getNormalString(String word) {
        word = word.toLowerCase();
        int left = 0, right = word.length();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != ' ') {
                left = i;
                break;
            }
        }
        for (int i = word.length() - 1; i >= 0; i--) {
            if (word.charAt(i) != ' ') {
                right = i + 1;
                break;
            }
        }
        word = word.substring(left, right);
        StringBuilder new_word = new StringBuilder();
        int flag = 0;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (ch != ' ') {
                new_word.append(ch);
                flag = 0;
            } else if (flag == 0) {
                new_word.append(ch);
                flag++;
            }
        }
        return new_word.toString();
    }

    // Добавляет два слова в словарь, дабавляет их ребро
    public static int addWord(String word1, String word2, MyDbManager myDbManager) {
        String word_en, word_ru;
        if (isEn(word1) && isRu(word2)) {
            word_en = word1;
            word_ru = word2;
        } else if (isEn(word2) && isRu(word1)) {
            word_en = word2;
            word_ru = word1;
        } else if (word1.equals("") || word2.equals("")){
            return 3; // Хотя бы одно из полей пустое
        } else {
            return 2; // Некорректные данные
        }
        int id_en = myDbManager.getIdWordEn(word_en);
        int id_ru = myDbManager.getIdWordRu(word_ru);
        if (myDbManager.isWasThisPair(id_en, id_ru)) {
            return 1; // Такая пара слов уже есть
        }
        if (id_en < 0) {
            myDbManager.insertToEn(word_en);
        }
        if (id_ru < 0) {
            myDbManager.insertToRu(word_ru);
        }
        myDbManager.insertToEdge(myDbManager.getIdWordEn(word_en), myDbManager.getIdWordRu(word_ru));
        return 0; // Пара слов корректно добавлена
    }

    // Проверяет слово, введённое для проверки на коррекность и правильность ответа
    public static int checkWordRu(String word_user, String word) {
        if (word_user.equals("")) {
            return 2; // Пустое поле
        } else if (!isRu(word_user)) {
            return 1; // Некорретные данные
        }
        HashMap<String, ArrayList<String>> mapEnRu = MainActivity.mapEnRu;
        ArrayList<String> words_true = new ArrayList<>(mapEnRu.get(word));
        boolean result = false;
        for (String var : words_true) {
            if (word_user.equals(var)) {
                result = true;
                break;
            }
        }
        if (result) {
            return 0;
        } else {
            return -1;
        }
    }

    public static int checkWordEn(String word_user, String word) {
        if (word_user.equals("")) {
            return 2; // Пустое поле
        } else if (!isEn(word_user)) {
            return 1; // Некорретные данные
        }
        HashMap<String, ArrayList<String>> mapRuEn = MainActivity.mapRuEn;
        ArrayList<String> words_true = new ArrayList<>(mapRuEn.get(word));
        boolean result = false;
        for (String var : words_true) {
            if (word_user.equals(var)) {
                result = true;
                break;
            }
        }
        if (result) {
            return 0;
        } else {
            return -1;
        }
    }

    // Скрывает клавиатуру (если она открыта), нужно передать Activity (напр. this)
    public static void closeKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    // Делает первую букву слова заглавной
    public static String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
