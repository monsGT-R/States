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

/**
 * Created by dima on 11/12/16.
 */

public class Page2 extends Fragment {


    View rootView;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView mylist;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page_2, container, false);
        dbHelper = new DBHelper(getActivity());
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, listItems);
        mylist=(ListView) rootView.findViewById(R.id.lv_page_2);
        mylist.setAdapter(adapter);
        LoadList();
        return rootView;
    }

    void LoadList() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuery = "select s.name as name, st.date as date "
                + "from state_time as st "
                + "inner join states as s "
                + "on st.state_id = s.id ";
        Cursor c = db.rawQuery(sqlQuery, null);

        if (c.moveToFirst()) {
            int state_idColIndex = c.getColumnIndex("name");
            int dateColIndex = c.getColumnIndex("date");
            do {
                listItems.add(c.getString(state_idColIndex)+" - "+c.getString(dateColIndex));
            } while (c.moveToNext());
        }
        adapter.notifyDataSetChanged();
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
    }
}
