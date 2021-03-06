package ru.a124au.monsgtr.states;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dima on 11/12/16.
 */

public class Page1 extends Fragment {

    final String LOG_TAG = "myLogs";

    EditText inputField;
    TextView titleText;
    DBHelper dbHelper;
    View rootView;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView mylist;
    String currId = "0";
    Page2 page2;

    public Page1(Page2 page2) {
        this.page2 = page2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page_1, container, false);
        inputField = (EditText) rootView.findViewById(R.id.editText);
        titleText = (TextView) rootView.findViewById(R.id.textView);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(getActivity());

        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, listItems);
        mylist=(ListView) rootView.findViewById(R.id.lv_page_1);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                editState(null, id, null);
            }
        });

        inputField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String stateText = inputField.getText().toString();
                    if (stateText.length() > 0) {
                        editState(stateText, -1, null);
                        v.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
        LoadLastState();
        AddStates();
        return rootView;
    }

    void LoadLastState() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuery = "select s.name as name, st.id as id "
                + "from current_state as cs "
                + "inner join state_time as st "
                + "on cs.state_id = st.id "
                + "inner join states as s "
                + "on st.state_id = s.id";
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            titleText.setText("Ваш статус - "+c.getString(nameColIndex));
            currId = c.getString(c.getColumnIndex("id"));
        }
        Log.d(LOG_TAG, currId);
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
    }

    void AddStates() {
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // делаем запрос всех данных из таблицы states, получаем Cursor
        Cursor c = db.query("states", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            do {
                listItems.add(c.getString(nameColIndex));
            } while (c.moveToNext());
        }
        adapter.notifyDataSetChanged();
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
    }

    void AddStateButton(String stateText) {
        listItems.add(0,stateText);
        adapter.notifyDataSetChanged();
    }

    void editState(String stateText, long id, String time) {
        String RowID;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (id != -1) {
            stateText = listItems.get((int) id);
        }
        Cursor c = db.query("states", null, "upper(name) = upper(?)", new String[] { stateText }, null, null, null);
        titleText.setText("Вы изменили свое состояние на \"" + stateText + "\"");
        ContentValues cv = new ContentValues();
        if (c.moveToFirst()) {
            RowID = c.getString(c.getColumnIndex("id"));
        } else {
            AddStateButton(stateText);
            cv.put("name", stateText);
            id = db.insert("states", null, cv);
            RowID = String.valueOf(id);
        }
        cv.clear();


        /**
         * Добавление нового состояния state_time
         */
        if (time == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            time = dateFormat.format(date);
            Log.d(LOG_TAG, time);
        }
        cv.put("date", time);
        cv.put("state_id", RowID);
        id = db.insert("state_time", null, cv);
        RowID = String.valueOf(id);
        cv.clear();

        /**
         * Изменение старого состояния state_time, добавление конечной метки времени
         */
        cv.put("end_time", time);
        db.update("state_time",cv,"id = ?",new String[] { currId });
        cv.clear();
        currId = RowID;

        /**
         * Добавление current_state
         */
        cv.put("state_id",RowID);
        db.update("current_state",cv,null,null);

        /**
         * Обновление 2 экрана
         */
        page2.ReloadList();
    }

    public void ClearDB() {
        titleText.setText("Вы отчистили базу данных");
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // удаляем все записи
        db.delete("states", null, null);
        db.delete("state_time", null, null);
        db.delete("answer_time", null, null);
        // закрываем подключение к БД
        dbHelper.close();
    }

    public void InsertDB() {
        for (int i = 0; i < 9; i++) {
            editState("Проснулся", -1, "2017-06-0"+(i+1)+" 07:33:2"+i);
            editState("Завтракаю", -1, "2017-06-0"+(i+1)+" 08:02:1"+i);
            editState("Работаю", -1, "2017-06-0"+(i+1)+" 09:00:1"+i);
            editState("Обедаю", -1, "2017-06-0"+(i+1)+" 13:30:1"+i);
            editState("Продолжаю работать", -1, "2017-06-0"+(i+1)+" 14:20:1"+i);
            editState("Возвращаюсь домой", -1, "2017-06-0"+(i+1)+" 18:10:1"+i);
            editState("Ужинаю", -1, "2017-06-0"+(i+1)+" 18:40:1"+i);
            editState("Гуляю", -1, "2017-06-0"+(i+1)+" 20:03:1"+i);
            editState("Ложусь спать", -1, "2017-06-0"+(i+1)+" 23:00:1"+i);
        }
    }

    // вывод в лог данных из курсора
    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }

}


    /*
    1 таблица справочник состояний

    Создать 3 таблицы,
    заполнение таблицы состояний
    Вывод на 2 окно таблицы состояний
    Сделать до след встречи

    Этап таблица на второй странице сгрупировать по дням
    Ввести настоящие данные на 5 дней.
    Проснулся завтрак зарядка учеба обед дорога спорт хобби и сон (примерно 10 состояний)
    Визуализация событий на день (Диаграмма, шкала времени, кликательный элемент для открывания списка)

    след. этап
    Справочник вопросов и анкетирования
    Вопросы:
    Оцените усталость. Варианты ответа 1 2 3 4 5 (очень уставший)
    Настроение.
    Голод.
    Сонливость.



    Глобальный список:
    Предуприждение об анкетировании.


     */
