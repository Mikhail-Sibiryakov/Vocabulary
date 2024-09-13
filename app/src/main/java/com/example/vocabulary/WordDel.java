package com.example.vocabulary;

import com.example.vocabulary.db.MyDbManager;

public class WordDel {

    // Удаляет слово и все связанные с ним рёбра
    public static int deleteWord(String word, MyDbManager myDbManager) {
        if (WordAdd.isEn(word)) {
            int id = myDbManager.getIdWordEn(word);
            if (id < 0) {
                return 2; // Такого слова и не было
            }
            myDbManager.deleteFromEdge(word, id);
            myDbManager.deleteFromEn(id);
        } else if (WordAdd.isRu(word)) {
            int id = myDbManager.getIdWordRu(word);
            if (id < 0) {
                return 2; // Такого слова и не было
            }
            myDbManager.deleteFromEdge(word, id);
            myDbManager.deleteFromRu(id);
        } else if (word.equals("")) {
            return 3; // Слово не введено
        } else {
            return 1; // Удаляемый текст некорректный
        }
        return 0; // Слово и все связанные с ним рёбра корректно удалены
    }
}