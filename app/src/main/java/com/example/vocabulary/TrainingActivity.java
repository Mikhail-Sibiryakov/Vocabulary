package com.example.vocabulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vocabulary.adapter.Adapter1;
import com.example.vocabulary.adapter.ListItem;
import com.example.vocabulary.adapter.TranslateAdapter;
import com.example.vocabulary.db.MyConstants;
import com.example.vocabulary.db.MyDbManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class TrainingActivity extends AppCompatActivity {
    private MyDbManager myDbManager;
    private Adapter1 mainAdapter;
    private ArrayList<String> arr;
    private RecyclerView rcView;
    private RadioButton rbEnRu, rbRuEn, rbEnRu2, rbRuEn2;
    private Switch switchButton, switchButton2;
    private TextView wordForTranslate, textResult, textTrueVar, wordForTranslate2;
    private HashMap<String, ArrayList<String>> mapEnRu, mapRuEn;
    private ArrayList<String> mapEnRuKeys, mapRuEnKeys;
    private String word;
    private EditText edTextTranslate;
    private boolean flag = false, mf = true, isWasTap = false;
    private TextView textView1, textView2, textView3, textView4, textViewV;
    private String ans_from_user, ans_true;
    private String last_word = ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        String cnt;
        if (MyConstants.isFlagOnSwitch) cnt = "True";
        else cnt = "False";
        Log.d("MyLog", cnt);

        if (MyConstants.isFlagOnSwitch) {
            setContentView(R.layout.activity_training2);
            mf = false;
            init();
            init2();
            start2();
        } else {
            setContentView(R.layout.activity_training);
            init();
            init1();
            start();
        }
    }

    private void init() {
        myDbManager = new MyDbManager(this);
        mapEnRu = MainActivity.mapEnRu;
        mapRuEn = MainActivity.mapRuEn;
        mapEnRuKeys = myDbManager.getKeysFromMap(mapEnRu);
        mapRuEnKeys = myDbManager.getKeysFromMap(mapRuEn);
    }

    private void init1() {
        switchButton = findViewById(R.id.switchButton1);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("MyLog", "Сработал свитч 1");
                MyConstants.isFlagOnSwitch = !MyConstants.isFlagOnSwitch;
                setContentView(R.layout.activity_training2);
                init2();
                if (MyConstants.training.equals(MyConstants.EN_RU)) {
                    rbEnRu2.setChecked(true);
                } else {
                    rbRuEn2.setChecked(true);
                }
                mf = false;
                switchButton2.setChecked(MyConstants.isFlagOnSwitch);
                start2();
            }
        });
        rbEnRu = findViewById(R.id.rbEnRu1);
        rbRuEn = findViewById(R.id.rbRuEn1);
        wordForTranslate = findViewById(R.id.wordWas1);
        textResult = findViewById(R.id.textResult1);
        textTrueVar = findViewById(R.id.textTrueVar1);
        edTextTranslate = findViewById(R.id.edTextTranslate);
        rcView = findViewById(R.id.rcView1);
        mainAdapter = new Adapter1(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(mainAdapter);
    }

    private void init2() {
        rbEnRu2 = findViewById(R.id.rbEnRu2);
        rbRuEn2 = findViewById(R.id.rbRuEn2);
        switchButton2 = findViewById(R.id.switchButton2);
        switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mf) {
                    Log.d("MyLog", "Сработал свитч 2");
                    MyConstants.isFlagOnSwitch = !MyConstants.isFlagOnSwitch;
                    setContentView(R.layout.activity_training);
                    init1();
                    if (MyConstants.training.equals(MyConstants.EN_RU)) {
                        rbEnRu.setChecked(true);
                    } else {
                        rbRuEn.setChecked(true);
                    }
                    switchButton.setChecked(MyConstants.isFlagOnSwitch);
                    start();
                } else {
                    mf = true;
                }
            }
        });
        wordForTranslate2 = findViewById(R.id.wordWas2);
        textViewV = findViewById(R.id.textView_v);
        textView1 = findViewById(R.id.textView_1);
        textView2 = findViewById(R.id.textView_2);
        textView3 = findViewById(R.id.textView_3);
        textView4 = findViewById(R.id.textView_4);
    }

    private void start() {
        Random random = new Random();
        word = last_word;
        while (word.equals(last_word)) {
            if (MyConstants.training.equals(MyConstants.EN_RU)) {
                word = mapEnRuKeys.get(random.nextInt(mapEnRuKeys.size()));
            } else {
                word = mapRuEnKeys.get(random.nextInt(mapRuEnKeys.size()));
            }
        }
        last_word = word;
        wordForTranslate.setText(WordAdd.firstUpperCase(word));
        wordForTranslate.setBackgroundResource(R.drawable.layout_bg2);
        edTextTranslate.setEnabled(true);
        edTextTranslate.setText("");
        textResult.setBackgroundResource(R.drawable.layout_bg_transparent);
        mainAdapter.updateAdapter(new ArrayList<>());
        textResult.setText("");
        textTrueVar.setText("");
        flag = false;
    }

    public void start2() {
        Random random = new Random();
        word = last_word;
        while (word.equals(last_word)) {
            if (MyConstants.training.equals(MyConstants.EN_RU)) {
                word = mapEnRuKeys.get(random.nextInt(mapEnRuKeys.size()));
            } else {
                word = mapRuEnKeys.get(random.nextInt(mapRuEnKeys.size()));
            }
        }
        last_word = word;
        HashSet<String> vars = new HashSet<>();
        String translate_true;
        if (MyConstants.training.equals(MyConstants.EN_RU)) {
            translate_true = mapEnRu.get(word).get(random.nextInt(mapEnRu.get(word).size()));
        } else {
            translate_true = mapRuEn.get(word).get(random.nextInt(mapRuEn.get(word).size()));
        }
        ans_true = translate_true;
        vars.add(translate_true);
        ArrayList<String> rand_all_keys = new ArrayList<>();
        if (MyConstants.training.equals(MyConstants.EN_RU)) {
            rand_all_keys = mapEnRuKeys;
        } else {
            rand_all_keys = mapRuEnKeys;
        }
        Collections.shuffle(rand_all_keys);
        HashSet<String> tmp_set = new HashSet<>();
        while (vars.size() < 4) {
            for (String tmp_key : rand_all_keys) {
                if (tmp_key.equals(word)) continue;
                int len = vars.size();
                tmp_set = new HashSet<>(vars);
                if (MyConstants.training.equals(MyConstants.EN_RU)) {
                    tmp_set.addAll(mapEnRu.get(tmp_key));
                } else {
                    tmp_set.addAll(mapRuEn.get(tmp_key));
                }
                if (tmp_set.size() == len) continue;
                while (vars.size() == len) {
                    String tmp_var;
                    if (MyConstants.training.equals(MyConstants.EN_RU)) {
                        tmp_var = mapEnRu.get(tmp_key).get(random.nextInt(mapEnRu.get(tmp_key).size()));
                    } else {
                        tmp_var = mapRuEn.get(tmp_key).get(random.nextInt(mapRuEn.get(tmp_key).size()));
                    }
                    vars.add(tmp_var);
                }
                if (vars.size() == 4) break;
            }
        }
        ArrayList<String> ans = new ArrayList<>(vars);
        Collections.shuffle(ans);

        wordForTranslate2.setText(WordAdd.firstUpperCase(word));
        wordForTranslate2.setBackgroundResource(R.drawable.layout_bg2);
        textViewV.setText(getString(R.string.chose_true_var));
        textView1.setText(WordAdd.firstUpperCase(ans.get(0)));
        textView1.setBackgroundResource(R.drawable.layout_bg1);
        textView2.setText(WordAdd.firstUpperCase(ans.get(1)));
        textView2.setBackgroundResource(R.drawable.layout_bg1);
        textView3.setText(WordAdd.firstUpperCase(ans.get(2)));
        textView3.setBackgroundResource(R.drawable.layout_bg1);
        textView4.setText(WordAdd.firstUpperCase(ans.get(3)));
        textView4.setBackgroundResource(R.drawable.layout_bg1);
        isWasTap = false;
    }

    public void onClickText(TextView textTmp) {
        ans_from_user = textTmp.getText().toString().toLowerCase();
        if (ans_true.equals(ans_from_user)) {
            textTmp.setBackgroundResource(R.drawable.layout_bg_green);
        } else {
            textTmp.setBackgroundResource(R.drawable.layout_bg_red);
            if (ans_true.equals(textView1.getText().toString().toLowerCase())) {
                textView1.setBackgroundResource(R.drawable.layout_bg_green);
            }
            if (ans_true.equals(textView2.getText().toString().toLowerCase())) {
                textView2.setBackgroundResource(R.drawable.layout_bg_green);
            }
            if (ans_true.equals(textView3.getText().toString().toLowerCase())) {
                textView3.setBackgroundResource(R.drawable.layout_bg_green);
            }
            if (ans_true.equals(textView4.getText().toString().toLowerCase())) {
                textView4.setBackgroundResource(R.drawable.layout_bg_green);
            }
        }
    }

    public void onClickText1(View view) {
        if (!isWasTap) {
            isWasTap = true;
            onClickText(textView1);
        }
    }

    public void onClickText2(View view) {
        if (!isWasTap) {
            isWasTap = true;
            onClickText(textView2);
        }
    }

    public void onClickText3(View view) {
        if (!isWasTap) {
            isWasTap = true;
            onClickText(textView3);
        }
    }

    public void onClickText4(View view) {
        if (!isWasTap) {
            isWasTap = true;
            onClickText(textView4);
        }
    }

    public void onClickButtonCheck(View view) {
        if (MyConstants.training.equals(MyConstants.EN_RU)) {
            checkEnRu();
        } else {
            checkRuEn();
        }
    }

    public void checkEnRu() {
        String word_user = WordAdd.getNormalString(edTextTranslate.getText().toString());
        ArrayList<String> words_true = mapEnRu.get(word);
        Collections.sort(words_true);
        int code = WordAdd.checkWordRu(word_user, word);
        Toast toast;
        switch (code) {
            case 1:
                toast = Toast.makeText(this, getString(R.string.incorrect_text), Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 2:
                toast = Toast.makeText(this, getString(R.string.edTextIsNull2), Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 0:
                textResult.setText(getString(R.string.itIsTrue));
                textResult.setBackgroundResource(R.drawable.layout_bg_green);
                if (words_true.size() > 1) {
                    textTrueVar.setText(getString(R.string.itIsTrueVars));
                } else {
                    textTrueVar.setText(getString(R.string.itIsTrueVar));
                }
                mainAdapter.updateAdapter(words_true);
                edTextTranslate.setEnabled(false);
                WordAdd.closeKeyboard(this);
                flag = true;
                break;
            case -1:
                textResult.setText(getString(R.string.itIsFalse));
                textResult.setBackgroundResource(R.drawable.layout_bg_red);
                if (words_true.size() > 1) {
                    textTrueVar.setText(getString(R.string.itIsTrueVars));
                } else {
                    textTrueVar.setText(getString(R.string.itIsTrueVar));
                }
                mainAdapter.updateAdapter(words_true);
                edTextTranslate.setEnabled(false);
                WordAdd.closeKeyboard(this);
                flag = true;
                break;
        }
    }

    public void checkRuEn() {
        String word_user = WordAdd.getNormalString(edTextTranslate.getText().toString());
        ArrayList<String> words_true = mapRuEn.get(word);
        Collections.sort(words_true);
        int code = WordAdd.checkWordEn(word_user, word);
        Toast toast;
        switch (code) {
            case 1:
                toast = Toast.makeText(this, getString(R.string.incorrect_text), Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 2:
                toast = Toast.makeText(this, getString(R.string.edTextIsNull2), Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 0:
                textResult.setText(getString(R.string.itIsTrue));
                textResult.setBackgroundResource(R.drawable.layout_bg_green);
                if (words_true.size() > 1) {
                    textTrueVar.setText(getString(R.string.itIsTrueVars));
                } else {
                    textTrueVar.setText(getString(R.string.itIsTrueVar));
                }
                mainAdapter.updateAdapter(words_true);
                edTextTranslate.setEnabled(false);
                WordAdd.closeKeyboard(this);
                flag = true;
                break;
            case -1:
                textResult.setText(getString(R.string.itIsFalse));
                textResult.setBackgroundResource(R.drawable.layout_bg_red);
                if (words_true.size() > 1) {
                    textTrueVar.setText(getString(R.string.itIsTrueVars));
                } else {
                    textTrueVar.setText(getString(R.string.itIsTrueVar));
                }
                mainAdapter.updateAdapter(words_true);
                edTextTranslate.setEnabled(false);
                WordAdd.closeKeyboard(this);
                flag = true;
                break;
        }
    }

    public void onClickButtonNext1(View view) {
        if (flag) {
            start();
        }
    }

    public  void onClickButtonNext2(View view) {
        if (isWasTap) {
            start2();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapEnRu = MainActivity.mapEnRu;
        mapRuEn = MainActivity.mapRuEn;
        mapEnRuKeys = myDbManager.getKeysFromMap(mapEnRu);
        mapRuEnKeys = myDbManager.getKeysFromMap(mapRuEn);
        if (MyConstants.isFlagOnSwitch) {
            if (MyConstants.training.equals(MyConstants.EN_RU)) {
                rbEnRu2.setChecked(true);
            } else {
                rbRuEn2.setChecked(true);
            }
            switchButton2.setChecked(MyConstants.isFlagOnSwitch);
        } else {
            if (MyConstants.training.equals(MyConstants.EN_RU)) {
                rbEnRu.setChecked(true);
            } else {
                rbRuEn.setChecked(true);
            }
            switchButton.setChecked(MyConstants.isFlagOnSwitch);
        }
    }

    public void onClickRbEnRu1(View view) {
        if (!MyConstants.training.equals(MyConstants.EN_RU)) {
            MyConstants.training = MyConstants.EN_RU;
            start();
        }
    }

    public void onClickRbRuEn1(View view) {
        if (!MyConstants.training.equals(MyConstants.RU_EN)) {
            MyConstants.training = MyConstants.RU_EN;
            start();
        }
    }

    public void onClickRbEnRu2(View view) {
        if (!MyConstants.training.equals(MyConstants.EN_RU)) {
            MyConstants.training = MyConstants.EN_RU;
            start2();
        }
    }

    public void onClickRbRuEn2(View view) {
        if (!MyConstants.training.equals(MyConstants.RU_EN)) {
            MyConstants.training = MyConstants.RU_EN;
            start2();
        }
    }
}
