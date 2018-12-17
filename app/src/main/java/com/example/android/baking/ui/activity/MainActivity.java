package com.example.android.baking.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.baking.R;
import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.ui.listener.OnRecipeClickListener;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener {
    private  Recipe mRecipe;

    public Recipe getmRecipe() {
        return mRecipe;
    }

    public void setmRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onRecipeSelected(Recipe selectedRecipe) {
        setmRecipe(selectedRecipe);
        startRecipeDetailActivity();
    }

    public void startRecipeDetailActivity(){
        if(mRecipe != null){
            Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.RECIPE_ID, mRecipe);
            startActivity(intent);
        }
    }

}
