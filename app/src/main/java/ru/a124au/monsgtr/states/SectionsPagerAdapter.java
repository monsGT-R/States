package ru.a124au.monsgtr.states;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by monsgtr on 11/20/16.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Page1 page1;
    private Page2 page2;
    private Page3 page3;
    private Page4 page4;

    public SectionsPagerAdapter(FragmentManager fm, Page1 page1, Page2 page2, Page3 page3, Page4 page4) {
        super(fm);
        this.page1 = page1;
        this.page2 = page2;
        this.page3 = page3;
        this.page4 = page4;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return page1;
            case 1:
                return page2;
            case 2:
                return page3;
            case 3:
                return page4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Выбор состояния";
            case 1:
                return "Прошлые состояния";
            case 2:
                return "Анкетирование";
            case 3:
                return "Журнал анкетирования";
        }
        return null;
    }
}

