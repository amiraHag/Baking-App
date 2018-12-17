package com.example.android.baking.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.baking.R;
import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.data.model.Step;
import com.example.android.baking.ui.adapter.StepsPagerAdapter;
import com.example.android.baking.utils.AppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeStepActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP_SELECTED_ID = "step_id";

    @BindView(R.id.tb_recipe_step)
    TabLayout mTabLayoutRecipeStep;
    @BindView(R.id.vp_recipe_step)
    ViewPager mViewPagerRecipeStep;
    @BindView(android.R.id.content)
    View mParentLayout;
    @BindView(R.id.detail_toolbar)
    Toolbar mtoolbarStepActivity;

    ActionBar mSupportActionBar;

    private Recipe mRecipe;
    private int mStepSelectedPosition;
    StepsPagerAdapter mStepPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        checkStepActivityIntentEXtra(bundle);
        displayRecipeStepToolBar(mtoolbarStepActivity);

        mStepPagerAdapter = new StepsPagerAdapter(getSupportFragmentManager(), mRecipe.getSteps());
        initStepPager(mStepPagerAdapter);
    }

    public void checkStepActivityIntentEXtra(Bundle bundle) {
        if (bundle!=null && bundle.containsKey(RECIPE_ID) && bundle.containsKey(STEP_SELECTED_ID)) {
            mRecipe = bundle.getParcelable(RECIPE_ID);
            mStepSelectedPosition = bundle.getInt(STEP_SELECTED_ID);
        } else {
            AppHelper.showSnackBar(this, mParentLayout, getString(R.string.failed_to_load_recipe));
            finish();
        }
    }

    public void displayRecipeStepToolBar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
        mSupportActionBar = getSupportActionBar();
        if (mSupportActionBar!=null) {
            mSupportActionBar.setTitle(mRecipe.getName());
            mSupportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void initStepPager(StepsPagerAdapter mStepPagerAdapter) {

        mViewPagerRecipeStep.setAdapter(mStepPagerAdapter);
        mTabLayoutRecipeStep.setupWithViewPager(mViewPagerRecipeStep);
        mViewPagerRecipeStep.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mSupportActionBar!=null) {
                    Step currentStep = mRecipe.getSteps().get(position);
                    String stepShortDescription = currentStep.getShortDescription();
                    mSupportActionBar.setTitle(stepShortDescription);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerRecipeStep.setCurrentItem(mStepSelectedPosition);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
    }
}