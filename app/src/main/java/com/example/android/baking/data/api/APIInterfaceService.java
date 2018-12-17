package com.example.android.baking.data.api;

import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.utils.AppHelper;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface APIInterfaceService {


    @GET(AppHelper.END_POINT)
    Observable<List<Recipe>> getRecipes();
}
