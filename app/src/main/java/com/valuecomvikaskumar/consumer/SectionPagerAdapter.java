package com.valuecomvikaskumar.consumer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by vikas on 16/12/17.
 */

class SectionPagerAdapter extends FragmentPagerAdapter {
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                ProductFragment pf=new ProductFragment();
                return pf;

            case 1:
                OrderFragment of=new OrderFragment();
                return of;

            default:return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "Products";
            case 1:
                return "Orders";
            default:
                return null;

        }
    }
}
