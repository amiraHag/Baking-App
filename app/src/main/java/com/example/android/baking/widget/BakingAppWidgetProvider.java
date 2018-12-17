package com.example.android.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.baking.R;
import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.ui.activity.RecipeDetailActivity;

import io.paperdb.Paper;

public class BakingAppWidgetProvider extends AppWidgetProvider {


    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        Recipe mRecipe = Paper.book().read("recipesa");
        String mRecipieName;
        String mRecipieIngredients;


        //Intialize the widget content variables
        if (mRecipe!=null) {
            mRecipieName = mRecipe.getName();
            BakingAppWidgetService.setmRecipieIngredients(mRecipe);
            mRecipieIngredients = BakingAppWidgetService.getmRecipieIngredients();

        } else {
            mRecipieName = "No Recipie";
            mRecipieIngredients = "Wait for fantastic new Recipe";
        }
        // Create an Intent to launch the recipie Activity
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.RECIPE_ID, mRecipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        // Get the layout for the App Widget and attach an on-click listener to the views
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.tv_app_widget_title, mRecipieName);
        views.setTextViewText(R.id.tv_app_widget_ingredient, mRecipieIngredients);
        // views.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);
        views.setOnClickPendingIntent(R.id.tv_app_widget_ingredient, pendingIntent);

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}
