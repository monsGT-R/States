package ru.a124au.monsgtr.states;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Page3 extends Fragment {

    View rootView;
    ArrayList<String> listItems;
    //ArrayAdapter<String> adapter;
    SimpleAdapter adapter;
    ListView mylist;
    DBHelper dbHelper;

    Page4 page4;


    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_RB = "rb";

    ArrayList<Map<String, Object>> data;

    public Page3(Page4 page4) {
        this.page4 = page4;
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page_3, container, false);
        dbHelper = new DBHelper(getActivity());
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext(),
                R.layout.questions, R.id.text_question, listItems);
        mylist=(ListView) rootView.findViewById(R.id.lv_page_3);
        mylist.setAdapter(adapter);
        LoadList();
        return rootView;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page_3, container, false);
        dbHelper = new DBHelper(getActivity());

        // упаковываем данные в понятную для адаптера структуру
        data = new ArrayList<Map<String, Object>>();

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_RB };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.text_question, R.id.radioButtons };

        // создаем адаптер
        adapter = new SimpleAdapter(getContext(), data, R.layout.questions,
                from, to);

        // Указываем адаптеру свой биндер
        adapter.setViewBinder(new MyViewBinder());

        // определяем список и присваиваем ему адаптер
        mylist=(ListView) rootView.findViewById(R.id.lv_page_3);
        mylist.setAdapter(adapter);
        LoadList();
        return rootView;
    }

    void LoadList() {
        /* String sqlQuery = "select q.name as name, at.answer as answer "
                + "from questions as q "
                + "left join answer_time as at "
                + "on at.question_id = q.id " +
                "where strftime('%d-%m-%Y',at.date) = ? "
                + "GROUP BY at.date ORDER BY at.id";*/
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuery = "select name "
                + "from questions";
        Cursor c = db.rawQuery(sqlQuery, null);
        Random rnd = new Random();
        Map<String, Object> m;
        if (c.moveToFirst()) {
            int state_idColIndex = c.getColumnIndex("name");
            do {

                m = new HashMap<String, Object>();
                m.put(ATTRIBUTE_NAME_TEXT, "Оцените "+c.getString(state_idColIndex));
                m.put(ATTRIBUTE_NAME_RB, rnd.nextInt(5)+1);
                data.add(m);

            } while (c.moveToNext());
        }
        adapter.notifyDataSetChanged();
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
    }

    void editState(String stateText, long id, String time, int answer) {
        String RowID;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (id != -1) {
            stateText = data.get((int)id).get(ATTRIBUTE_NAME_TEXT).toString();
        }
        stateText = stateText.replaceAll("Оцените ", "");
        Cursor c = db.query("questions", null, "upper(name) = upper(?)", new String[] { stateText }, null, null, null);
        ContentValues cv = new ContentValues();
        if (c.moveToFirst()) {
            RowID = c.getString(c.getColumnIndex("id"));
        } else {
            cv.put("name", stateText);
            id = db.insert("questions", null, cv); // на будущее возможность добавлять вопросы динамически
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
        }
        cv.put("date", time);
        cv.put("question_id", RowID);
        cv.put("answer", answer);
        id = db.insert("answer_time", null, cv);
        RowID = String.valueOf(id);
        cv.clear();

        /**
         * Изменение старого состояния state_time, добавление конечной метки времени
         */
       /* cv.put("end_time", time);
        db.update("state_time",cv,"id = ?",new String[] { currId });
        cv.clear();*/

        /**
         * Обновление 2 экрана
         */
        page4.ReloadList();
    }

    public void InsertDB() {
        Random rnd = new Random();
        for (int i = 0; i < 9; i++) {
            editState("", 0, "2017-06-0"+(i+1)+" 07:33:2"+i, rnd.nextInt(5)+1);
            editState("", 1, "2017-06-0"+(i+1)+" 08:02:1"+i, rnd.nextInt(5)+1);
            editState("", 2, "2017-06-0"+(i+1)+" 09:00:1"+i, rnd.nextInt(5)+1);
            editState("", 3, "2017-06-0"+(i+1)+" 13:30:1"+i, rnd.nextInt(5)+1);
            editState("", 4, "2017-06-0"+(i+1)+" 14:20:1"+i, rnd.nextInt(5)+1);
            editState("", 5, "2017-06-0"+(i+1)+" 18:10:1"+i, rnd.nextInt(5)+1);
        }
    }
    class MyViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            switch (view.getId()) {
                case R.id.radioButtons:
                    RadioButton rb = (RadioButton) view.findViewWithTag("rb"+data);
                    rb.setChecked(true);
                    return true;
            }
            return false;
        }
    }
}
