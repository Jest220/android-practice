package com.lr3_428_09;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.lr3_428_09.database.DbHelper;
import com.lr3_428_09.database.SimpleDao;
import com.lr3_428_09.database.TableName;
import com.lr3_428_09.model.SimpleModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdministrationActivity extends AppCompatActivity {
    private Button btnCancel;
    private Button btnAdd;
    private Spinner spinnerTable;
    private ListView listView;
    private SQLiteDatabase db;
    private SimpleDao simpleDao;
    TableName[] tableNames;
    List<SimpleModel> records;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_simple);

        initializeViews();
        initializeDatabase();
        initializeSpinnerTable();
        setupButtons();
    }

    private void initializeDatabase() {
        DbHelper dbHelper = new DbHelper(this);
        try {
            dbHelper.createDatabase();
            db = dbHelper.openDatabase();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при создании базы данных", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        btnCancel = findViewById(R.id.btnCancel);
        btnAdd = findViewById(R.id.btnAdd);
        spinnerTable = findViewById(R.id.spinnerTable);
        listView = findViewById(R.id.listView);
    }

    private void setupButtons() {
        btnCancel.setOnClickListener(v -> finish());
        btnAdd.setOnClickListener(v -> {
            PopupDialogFragment dialog = new PopupDialogFragment(simpleDao, false);
            dialog.setListener(this::updateListView);
            dialog.show(getSupportFragmentManager(), "popup_dialog");
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            SimpleModel selected = (SimpleModel)listView.getItemAtPosition(position);
            PopupDialogFragment dialog = new PopupDialogFragment(simpleDao, selected);
            dialog.setListener(this::updateListView);
            dialog.show(getSupportFragmentManager(), "popup_dialog");
        });
    }

    private void updateListView() {
        if (simpleDao != null) {
            records = simpleDao.getAll();
            ArrayAdapter<SimpleModel> listViewAdapter = new ArrayAdapter<>(AdministrationActivity.this, R.layout.list_item_normal, records);
            listView.setAdapter(listViewAdapter);
        }
    }

    private void initializeSpinnerTable() {
        // инициализируем список таблиц
        tableNames = TableName.values();
        List<String> tableNameList = new ArrayList<>();
        for (TableName tableName : tableNames) {
            tableNameList.add(tableName.getReadableName());
        }
        // заполняем спиннер
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(AdministrationActivity.this, R.layout.spinner_item_bold, tableNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTable.setAdapter(spinnerAdapter);

        // при изменении выбранного элемента
        spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tableName = tableNames[spinnerTable.getSelectedItemPosition()].getName();
                simpleDao = new SimpleDao(tableName, db);
                records = simpleDao.getAll();
                ArrayAdapter<SimpleModel> listViewAdapter = new ArrayAdapter<>(AdministrationActivity.this, R.layout.list_item_normal, records);
                listView.setAdapter(listViewAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AdministrationActivity.this, "Ничего не выбрано", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}