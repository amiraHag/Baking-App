package com.example.android.baking.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.model.Ingredient;
import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.data.model.Step;
import com.example.android.baking.ui.listener.OnItemClickListener;
import com.example.android.baking.utils.AppHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;


    private Recipe mRecipe;
    private OnItemClickListener mOnItemClickListener;

    public RecipeAdapter(Context mContext, Recipe mRecipe, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.mRecipe = mRecipe;
        this.mOnItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layouInflator = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case 0:

                View viewIngredient = layouInflator.inflate(R.layout.item_recipe_detail, viewGroup, false);
                RecipeAdapter.IngredientViewHolder ingredientViewHolder = new RecipeAdapter.IngredientViewHolder(viewIngredient);
                return ingredientViewHolder;

            default:
                View viewStep = layouInflator.inflate(R.layout.item_recipe_step, viewGroup, false);
                RecipeAdapter.StepViewHolder stepViewHolder = new RecipeAdapter.StepViewHolder(viewStep);
                return stepViewHolder;

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        switch (position) {
            case 0:
                IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) viewHolder;
            ingredientViewHolder.onBind(mRecipe);
            break;
            default:
            StepViewHolder stepViewHolder = (StepViewHolder) viewHolder;
            stepViewHolder.onBind(mRecipe, position);
        }
    }
    public void setmRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
        notifyDataSetChanged();
    }

   @Override
    public int getItemViewType(int position) {
       return position;
    }

    @Override
    public int getItemCount() {
        if(mRecipe == null || mRecipe.getSteps() == null) {
            return 0;
        }
        int stepsNumber = mRecipe.getSteps().size();
        int totalItemsNumber = stepsNumber + 1;
        return totalItemsNumber;
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredients_content)
        public TextView mTextViewIngredients;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
        public void onBind(Recipe selectedRecipe) {
            List<Ingredient> mIngredientsList = selectedRecipe.getIngredients();
            String  mRecipieIngredients = AppHelper.convertIngredientToString(mIngredientsList);
            mTextViewIngredients.setText(mRecipieIngredients);
        }

        }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step_position)
        public TextView mTextViewStepOrder;

        @BindView(R.id.tv_step_name)
        public TextView mTextViewStepName;

        public StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }


        public void onBind(Recipe selectedRecipe, int position) {
            List<Step> mRecipeStepList = selectedRecipe.getSteps();
            int mStepPosition = position - 1;
            Step mCurrentStep = mRecipeStepList.get(mStepPosition);

            String mStepOrder = mStepPosition + " ) ";
            String mStepName  = mCurrentStep.getShortDescription();


           mTextViewStepOrder.setText(mStepOrder);
           mTextViewStepName.setText(mStepName);

           itemView.setOnClickListener(v -> {
                if (mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClick(mStepPosition);
            });
        }

    }

}