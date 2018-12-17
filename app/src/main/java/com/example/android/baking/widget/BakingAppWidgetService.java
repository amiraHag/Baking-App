package com.example.android.baking.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.baking.data.model.Ingredient;
import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.utils.AppHelper;

import java.util.List;

import io.paperdb.Paper;

public class BakingAppWidgetService extends IntentService {

    private static String mRecipieIngredients;

    public BakingAppWidgetService(String name) {
        super(name);
    }

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void initWidgetContent(Context context, Recipe recipe) {
        Paper.book().write("recipeupdated", recipe);

        initAppWidget(context);
    }

    public static void initAppWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidgetProvider.class));
        BakingAppWidgetProvider.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    public static String getmRecipieIngredients() {
        return mRecipieIngredients;
    }

    public static void setmRecipieIngredients(Recipe recipe) {


        List<Ingredient> mIngredientsList = recipe.getIngredients();
        mRecipieIngredients = AppHelper.convertIngredientToString(mIngredientsList);

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null) {
            handleActionOpenRecipeActivity();
        }

    }

    private void handleActionOpenRecipeActivity() {
        Recipe recipe = Paper.book().read("recipeupdated");
        setmRecipieIngredients(recipe);
        initAppWidget(this);
    }
}