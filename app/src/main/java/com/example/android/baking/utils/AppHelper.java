package com.example.android.baking.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.api.APIInterfaceService;
import com.example.android.baking.data.api.APIClient;
import com.example.android.baking.data.model.Ingredient;

import java.util.List;

public class AppHelper {

    public final static String END_POINT = "baking.json";
    public final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    public  static APIInterfaceService mClientApi = APIClient.getAPIInterfaceService();
    public static final int  WIDGET_DESIRED_RECIPE_ID = 0;
    public static final int  TABLET_ITEMS_PER_ROW = 3;
    public static final int  PHONE_ITEM_PER_ROW = 1;
    public static int [] recipie_icons = {
            R.drawable.ic_nuttilapie,
            R.drawable.ic_brownies,
            R.drawable.ic_yellowcake,
            R.drawable.ic_cheesecake
    };

    public static String convertIngredientToString(List<Ingredient> ingredientList){
        StringBuilder stringBuilder = new StringBuilder();

        for (Ingredient mIngredient : ingredientList) {

            String quantity = mIngredient.getQuantity();
            stringBuilder.append(quantity + " * ");

            String measure = mIngredient.getMeasure();
            stringBuilder.append(measure + " of ");

            String ingredientName = mIngredient.getIngredient();
            stringBuilder.append(ingredientName + ".");
            stringBuilder.append("\n");


        }
        String  mRecipieIngredients = stringBuilder.toString();
        return mRecipieIngredients;
    }

    public static void showSnackBar(Context context, View view, String text) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreyLight));
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);

        textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        snackbar.show();
    }

    public static boolean isNetworkAvailable(Context c) {
        NetworkInfo netInfo = null;
        try {
            ConnectivityManager cm = (ConnectivityManager) c
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



}