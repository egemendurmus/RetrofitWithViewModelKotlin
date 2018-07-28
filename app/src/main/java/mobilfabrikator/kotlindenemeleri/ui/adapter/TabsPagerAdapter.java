package mobilfabrikator.kotlindenemeleri.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mobilfabrikator.kotlindenemeleri.ui.fragments.DailyProgramListFragments;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private String tabTitles[] = new String[]{"PAZARTESİ", "SALI", "ÇARŞAMBA", "PERŞEMBE", "CUMA", "CUMARTESİ", "PAZAR"};

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        return new DailyProgramListFragments();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 7;
    }

}