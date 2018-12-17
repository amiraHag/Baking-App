package com.example.android.baking.ui.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.baking.data.model.Step;
import com.example.android.baking.ui.activity.RecipeStepFragment;

import java.util.List;

public class StepsPagerAdapter extends FragmentPagerAdapter {

    private List<Step> mStepList;

    public StepsPagerAdapter(FragmentManager fm, List<Step> steps) {
        super(fm);
        this.mStepList = steps;
    }

    @Override
    public Fragment getItem(int position) {
        return getRecipeStepFragment(position);
    }

    public Fragment getRecipeStepFragment(int position){
        Bundle mFragmentArguments = new Bundle();
        Step currentStep = mStepList.get(position);
        mFragmentArguments.putParcelable(RecipeStepFragment.STEP_ID, currentStep);
        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(mFragmentArguments);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String mPositionText = " " + position + " ";
        return mPositionText;
    }

    @Override
    public int getCount() {
        return mStepList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}