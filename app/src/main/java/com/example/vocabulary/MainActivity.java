package com.example.vocabulary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vocabulary.db.MyConstants;
import com.example.vocabulary.db.MyDbHelper;
import com.example.vocabulary.db.MyDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText word_1, word_2, word_Del;
    private TextView textViewColor;
    public String word1, word2, wordDel;
    private MyDbManager myDbManager;

    public static HashMap<String, ArrayList<String>> mapEnRu, mapRuEn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        word_1 = findViewById(R.id.word1);
        word_2 = findViewById(R.id.word2);
        myDbManager = new MyDbManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
    }

    public void onClickButtonDelWord(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogalldell_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        word_Del = dialog.findViewById(R.id.wordDel);
        textViewColor = dialog.findViewById(R.id.textViewColor);
        textViewColor.setBackgroundResource(R.drawable.layout_bg3);
        Activity activity = this;

        Button buttonBack = (Button) dialog.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordAdd.closeKeyboard(activity);
                dialog.dismiss();
                WordAdd.closeKeyboard(activity);
            }
        });

        Button buttonDel = (Button) dialog.findViewById(R.id.buttonDel);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordDel = WordAdd.getNormalString(word_Del.getText().toString());
                int code = WordDel.deleteWord(wordDel, myDbManager);
                Toast toast;
                switch (code) {
                    case 0:
                        word_Del.setText(null);
                        toast = Toast.makeText(activity, getString(R.string.delete_okay), Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case 1:
                        toast = Toast.makeText(activity, getString(R.string.incorrect_text), Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case 2:
                        toast = Toast.makeText(activity, getString(R.string.isNotWord), Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case 3:
                        toast = Toast.makeText(activity, getString(R.string.edTextIsNull2), Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    public void onClickButtonAdd(View view) {
        word1 = WordAdd.getNormalString(word_1.getText().toString());
        word2 = WordAdd.getNormalString(word_2.getText().toString());
        int code = WordAdd.addWord(word1, word2, myDbManager);
        Toast toast;
        switch (code) {
            case 0:
                WordAdd.closeKeyboard(this);
                word_1.setText(null);
                word_2.setText(null);
                toast = Toast.makeText(this, getString(R.string.add_okay), Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 1:
                WordAdd.closeKeyboard(this);
                word_1.setText(null);
                word_2.setText(null);
                toast = Toast.makeText(this, getString(R.string.was_words), Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 2:
                toast = Toast.makeText(this, getString(R.string.incorrect_text), Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 3:
                toast = Toast.makeText(this, getString(R.string.edTextIsNull), Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

    public void onClickButtonOpenDict(View view) {
        Intent i = new Intent(MainActivity.this, VocabularyActivity.class);
        startActivity(i);
    }

    public void onClickButtonGoGame(View view) {
        mapEnRu = myDbManager.getMapEnRu();
        mapRuEn = myDbManager.getMapRuEn();
        int cnt_keys_en_ru = mapEnRu.keySet().size();
        int cnt_keys_ru_en = mapRuEn.keySet().size();
        if (cnt_keys_en_ru >= 4 && cnt_keys_ru_en >= 4) {
            Intent i = new Intent(MainActivity.this, TrainingActivity.class);
            startActivity(i);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.smallWords)
                    .setCancelable(false)
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }
}