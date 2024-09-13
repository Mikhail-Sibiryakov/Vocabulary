package com.example.vocabulary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vocabulary.adapter.ListItem;
import com.example.vocabulary.adapter.ListItem2;
import com.example.vocabulary.adapter.TranslateAdapter;
import com.example.vocabulary.db.MyConstants;
import com.example.vocabulary.db.MyDbManager;

import java.util.ArrayList;

public class TranslateActivity extends AppCompatActivity {
    private RecyclerView rcView;
    private TranslateAdapter translateAdapter;
    private ArrayList<String> arr;
    private MyDbManager myDbManager;
    private Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        init();
        getMyIntents();
    }

    private void init() {
        myDbManager = new MyDbManager(this);
        rcView = findViewById(R.id.rcView);
        translateAdapter = new TranslateAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelper().attachToRecyclerView(rcView);
        rcView.setAdapter(translateAdapter);
    }

    private void getMyIntents() {
        Intent i = getIntent();
        if (i != null) {
            ListItem item = (ListItem)i.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);
            arr = item.getArrayList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        myDbManager.openDb();
        ArrayList<ListItem2> arrList = myDbManager.getArrayListItem2FromArray(arr);
        translateAdapter.updateAdapter(arrList);
    }

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private RecyclerView recyclerView;
            private RecyclerView.ViewHolder viewHolder;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                this.recyclerView = recyclerView;
                this.viewHolder = viewHolder;
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ArrayList<String> tempList = translateAdapter.getKeyAnaValue(viewHolder.getAdapterPosition());
                String key = tempList.get(0);
                String value = tempList.get(1);

                AlertDialog.Builder a_builder = new AlertDialog.Builder(TranslateActivity.this);
                a_builder.setMessage(getString(R.string.textForQuestion1) + value +
                        getString(R.string.textForQuestion2) + key + getString(R.string.textForQuestion3))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                translateAdapter.removeItem(viewHolder.getAdapterPosition(), myDbManager);
                                dialog.cancel();
                                Toast toast;
                                toast = Toast.makeText(activity, getString(R.string.delete_okay), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        })
                        .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<ListItem2> arrList = myDbManager.getArrayListItem2FromArray(arr);
                                translateAdapter.updateAdapter(arrList);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.show();
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder holder) {
                int position = holder.getAdapterPosition();
                return createSwipeFlags(position);
            }

            private int createSwipeFlags(int position) {
                return position == 0 ? 0 : super.getSwipeDirs(recyclerView, viewHolder);
            }
        });
    }
}
