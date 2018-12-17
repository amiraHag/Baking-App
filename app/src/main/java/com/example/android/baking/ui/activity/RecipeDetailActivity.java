package com.example.android.baking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.baking.R;
import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.ui.adapter.RecipeAdapter;
import com.example.android.baking.ui.listener.OnItemClickListener;
import com.example.android.baking.utils.AppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "recipe_id";

    @BindView(R.id.rv_recipe_steps_list)
    RecyclerView mRecyclerViewRecipeDetail;
    @BindView(android.R.id.content)
    View mParentLayout;
    @BindView(R.id.tb_recipe_detail)
    Toolbar mToolbar;

    private boolean isTablet;
    private Recipe mRecipe;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        checkIntentEXtra(bundle);

        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        displayToolBar(mToolbar);
        checkTabletDisplay(savedInstanceState);
        createRecyclerView();
    }

    public void checkIntentEXtra(Bundle bundle) {
        if (bundle!=null && bundle.containsKey(RECIPE_ID)) {
            mRecipe = bundle.getParcelable(RECIPE_ID);
        } else {
            AppHelper.showSnackBar(this, mParentLayout, getString(R.string.failed_to_load_recipe));
            finish();
        }
    }


    public void displayToolBar(Toolbar toolBar) {
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void checkTabletDisplay(Bundle savedInstanceState) {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            if (savedInstanceState==null && !mRecipe.getSteps().isEmpty()) {
                displayStepDetail(0);
            }
        }
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

    private void createRecyclerView() {
        OnItemClickListener mItemClickListener = position -> displayStepDetail(position);
        mRecipeAdapter = new RecipeAdapter(getApplicationContext(), mRecipe, mItemClickListener);
        mRecipeAdapter.notifyDataSetChanged();
        mRecyclerViewRecipeDetail.setAdapter(mRecipeAdapter);
        mRecyclerViewRecipeDetail.setItemAnimator(new DefaultItemAnimator());
    }

    private void displayStepDetail(int position) {
        if (isTablet) {
            displayTabletLayout(position);

        } else {
            startStepDetailActivity(position);
        }
    }

    public void displayTabletLayout(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepFragment.STEP_ID, mRecipe.getSteps().get(position));
        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_recipe_step_detail, fragment)
                .commit();

    }

    public void startStepDetailActivity(int position) {
        Intent intent = new Intent(this, RecipeStepActivity.class);
        intent.putExtra(RecipeStepActivity.RECIPE_ID, mRecipe);
        intent.putExtra(RecipeStepActivity.STEP_SELECTED_ID, position);
        startActivity(intent);
    }
}