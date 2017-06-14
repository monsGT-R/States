package ru.a124au.monsgtr.states;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Page3 extends Fragment {

    View rootView;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView mylist;
    DBHelper dbHelper;

    @Override
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
    }

    void LoadList() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuery = "select name "
                + "from questions";
        Cursor c = db.rawQuery(sqlQuery, null);

        if (c.moveToFirst()) {
            int state_idColIndex = c.getColumnIndex("name");
            do {
                listItems.add("Оцените "+c.getString(state_idColIndex));
            } while (c.moveToNext());
        }
        adapter.notifyDataSetChanged();
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
    }

}
