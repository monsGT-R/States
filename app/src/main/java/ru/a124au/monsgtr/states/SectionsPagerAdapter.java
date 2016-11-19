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

    public SectionsPagerAdapter(FragmentManager fm, Page1 mpage1, Page2 mpage2, Page3 mpage3) {
        super(fm);
        page1 = mpage1;
        page2 = mpage2;
        page3 = mpage3;
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
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
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
        }
        return null;
    }
}

