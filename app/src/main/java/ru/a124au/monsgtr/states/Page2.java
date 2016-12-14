package ru.a124au.monsgtr.states;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dima on 11/12/16.
 */

public class Page2 extends Fragment {


    View rootView;

    // коллекция для групп
    ArrayList<Map<String, String>> groupData;

    // коллекция для элементов одной группы
    ArrayList<Map<String, String>> childDataItem;

    // общая коллекция для коллекций элементов
    ArrayList<ArrayList<Map<String, String>>> childData;
    // в итоге получится childData = ArrayList<childDataItem>

    // список атрибутов группы или элемента
    Map<String, String> m;

    ExpandableListView elvMain;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_page_2, container, false);
        dbHelper = new DBHelper(getActivity());
        LoadList();

        // список атрибутов групп для чтения
        String groupFrom[] = new String[] {"groupName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int groupTo[] = new int[] {android.R.id.text1};
        // список атрибутов элементов для чтения
        String childFrom[] = new String[] {"childName"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int childTo[] = new int[] {android.R.id.text1};
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getContext(),
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo);

        elvMain = (ExpandableListView) rootView.findViewById(R.id.elvMain);
        elvMain.setAdapter(adapter);

        return rootView;
    }

    void LoadList() {
        // создаем коллекцию для коллекций элементов
        childData = new ArrayList<ArrayList<Map<String, String>>>();
        // заполняем коллекцию групп из массива с названиями групп
        groupData = new ArrayList<Map<String, String>>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuery = "select distinct strftime('%d-%m-%Y',date) as date "
                + "from state_time "
                + "ORDER BY date DESC";

        Cursor c = db.rawQuery(sqlQuery, null);

        if (c.moveToFirst()) {
            int date = c.getColumnIndex("date");
            do {
                // заполняем список атрибутов для каждой группы
                m = new HashMap<String, String>();
                m.put("groupName", c.getString(date)); // имя компании
                groupData.add(m);


                // создаем коллекцию элементов
                childDataItem = new ArrayList<Map<String, String>>();
                sqlQuery = "select s.name as name, st.date as date "
                        + "from state_time as st "
                        + "inner join states as s "
                        + "on st.state_id = s.id "
                        + "where strftime('%d-%m-%Y',st.date) = ? "
                        + "GROUP BY date ORDER BY date DESC";

                Cursor el = db.rawQuery(sqlQuery, new String[]{c.getString(date)});
                if (el.moveToFirst()) {
                    int dateColIndex = el.getColumnIndex("date");
                    int state_idColIndex = el.getColumnIndex("name");
                    do {
                        m = new HashMap<String, String>();
                        m.put("childName", el.getString(state_idColIndex)+" - "+el.getString(dateColIndex));
                        childDataItem.add(m);
                    } while (el.moveToNext());
                    childData.add(childDataItem);
                }
                el.close();
            } while (c.moveToNext());
        }
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
    }
}
