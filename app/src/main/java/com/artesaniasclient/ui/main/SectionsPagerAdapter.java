package com.artesaniasclient.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.artesaniasclient.R;
import com.artesaniasclient.fragments.fragment_my_crafts;
import com.artesaniasclient.fragments.fragment_tab_my_crafts;
import com.artesaniasclient.fragments.fragment_tab_registrer_crafts;
import com.artesaniasclient.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    Bundle bundle;
    fragment_my_crafts fragment_my_crafts;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Bundle distbundle, fragment_my_crafts fragment_my_crafts) {
        super(fm);
        bundle = distbundle;
        this.fragment_my_crafts = fragment_my_crafts;
        this.addFragment(new fragment_tab_my_crafts(fragment_my_crafts), "Mis artesanias");
        this.addFragment(new fragment_tab_registrer_crafts(fragment_my_crafts), "Registrar artesanias");
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position);
        fragment.setArguments(Util.getBundleFusion(fragment_my_crafts.getArguments(), bundle));
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}