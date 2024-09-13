package com.example.vocabulary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.vocabulary.adapter.ListItem;
import com.example.vocabulary.adapter.MainAdapter;
import com.example.vocabulary.db.MyConstants;
import com.example.vocabulary.db.MyDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class VocabularyActivity extends AppCompatActivity {
    private RecyclerView rcView;
    private MainAdapter mainAdapter;
    private MyDbManager myDbManager;
    private RadioButton rbEnRu, rbRuEn;
    private ArrayList<ListItem> currentList;
    private String currentNewText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        SearchView sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentNewText = WordAdd.getNormalString(newText);
//                currentNewText = newText;
                newUpdateAdapter();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        rcView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(mainAdapter);
        myDbManager = new MyDbManager(this);
        rbEnRu = findViewById(R.id.rbEnRu);
        rbRuEn = findViewById(R.id.rbRuEn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
        if (MyConstants.vocabulary.equals(MyConstants.EN_RU)) {
            rbEnRu.setChecked(true);
            setEnRu();
        } else {
            rbRuEn.setChecked(true);
            setRuEn();
        }
    }

    private void newUpdateAdapter() {
        ArrayList<ListItem> tmpArr = new ArrayList<>();
        for (ListItem itemFromList : currentList) {
            String tmpStr = WordAdd.getNormalString(itemFromList.getMainString());
            if (tmpStr.contains(currentNewText)) {
                tmpArr.add(itemFromList);
            }
        }
        mainAdapter.updateAdapter(tmpArr);
    }

    public void onClickRbEnRu(View view) {
        MyConstants.vocabulary = MyConstants.EN_RU;
        setEnRu();
    }

    public void onClickRbRuEn(View view) {
        MyConstants.vocabulary = MyConstants.RU_EN;
        setRuEn();
    }

    public void setEnRu() {
        HashMap<String, ArrayList<String>> dict = myDbManager.getMapEnRu();
        ArrayList<ListItem> arr = myDbManager.getListItemFromMap(dict);
        currentList = new ArrayList<>(arr);
//        mainAdapter.updateAdapter(arr);
        newUpdateAdapter();
    }

    public void setRuEn() {
        HashMap<String, ArrayList<String>> dict = myDbManager.getMapRuEn();
        ArrayList<ListItem> arr = myDbManager.getListItemFromMap(dict);
        currentList = new ArrayList<>(arr);
//        mainAdapter.updateAdapter(arr);
        newUpdateAdapter();
    }

    public void onClickButtonClearAll(View view) {
        Activity activity = this;
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        a_builder.setMessage(getString(R.string.YouAreSure))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDbManager.allDestroy();
                        if (rbEnRu.isChecked()) {
                            setEnRu();
                        } else {
                            setRuEn();
                        }
                        dialog.cancel();
                        Toast toast;
                        toast = Toast.makeText(activity, getString(R.string.dictDeleteOkay), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }
}