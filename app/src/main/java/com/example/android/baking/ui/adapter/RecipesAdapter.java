package com.example.android.baking.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.ui.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipeList;
    private OnItemClickListener mOnItemClickListener;

    public RecipesAdapter(Context context, List<Recipe> recipeList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mRecipeList = recipeList;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public  RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layouInflator = LayoutInflater.from(viewGroup.getContext());
        View view = layouInflator.inflate(R.layout.item_recipe, viewGroup, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, final int position) {
        Recipe currentRecipe = mRecipeList.get(position);
        viewHolder.onBind(currentRecipe , position);
    }
    public void setmMovieList(List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipeList==null) {
            return 0;
        }
        return mRecipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recipe)
        public TextView mTextViewRecipeTitle;

        @BindView(R.id.iv_recipe)
        public AppCompatImageView mImageViewRecipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        public void onBind(Recipe selectedRecipe, int recipePosition){

            String mRecipeTitle = selectedRecipe.getName();
            String mRecipeImage = selectedRecipe.getImage();

            mTextViewRecipeTitle.setText(mRecipeTitle);
            setImageResource(recipePosition);

            if (!mRecipeImage.isEmpty()) {
                Picasso.get().load(mRecipeImage)
                        .placeholder(R.drawable.ic_app_background)
                        .into(mImageViewRecipeImage);
            }


           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener!=null)
                        mOnItemClickListener.onItemClick(recipePosition);
                }
            });

        }
        public void setImageResource(int itemPosition){
            switch (itemPosition){
                case 0 : mImageViewRecipeImage.setImageResource(R.drawable.ic_nuttilapie);
                    break;
                case 1 : mImageViewRecipeImage.setImageResource(R.drawable.ic_brownies);
                    break;
                case 2 : mImageViewRecipeImage.setImageResource(R.drawable.ic_yellowcake);
                    break;
                case 3 : mImageViewRecipeImage.setImageResource(R.drawable.ic_cheesecake);
                    break;
            }
        }
    }
}