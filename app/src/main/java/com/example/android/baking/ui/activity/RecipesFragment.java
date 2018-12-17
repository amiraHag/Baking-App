package com.example.android.baking.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baking.idlingresource.MyCustomApplication;
import com.example.android.baking.R;
import com.example.android.baking.data.model.Recipe;
import com.example.android.baking.ui.listener.OnItemClickListener;
import com.example.android.baking.ui.listener.OnRecipeClickListener;
import com.example.android.baking.ui.adapter.RecipesAdapter;
import com.example.android.baking.utils.AppHelper;
import com.example.android.baking.widget.BakingAppWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class RecipesFragment extends Fragment {

    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerViewRecipes;
    @BindView(R.id.srf_container_recipes)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private BroadcastReceiver mRecipeBroadcastReceiver;
    private OnRecipeClickListener mRecipeClickListener;
    private Unbinder layoutBinder;
    private List<Recipe> mRecipesList;
    private MyCustomApplication myCustomApplication;
    private CompositeDisposable mCompositeDisposable;
    private RecipesAdapter mRecipesAdapter;

    public RecipesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_recipes, container, false);
        layoutBinder = ButterKnife.bind(this, viewRoot);
        mSwipeRefreshLayout.setOnRefreshListener(() -> initRecipesList());

        myCustomApplication = (MyCustomApplication) getActivity().getApplicationContext();
        myCustomApplication.setIdleState(false);

        createRecyclerView();
        setupRecipeBroadcastReciever();
        checkSavedState(savedInstanceState);

        return viewRoot;
    }

    public void checkSavedState(Bundle savedState) {
        if (savedState!=null && savedState.containsKey(getResources().getString(R.string.RECIPES_LIST))) {
            mRecipesList = savedState.getParcelableArrayList(getResources().getString(R.string.RECIPES_LIST));

            mRecyclerViewRecipes.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(),
                    mRecipesList, position -> mRecipeClickListener.onRecipeSelected(mRecipesList.get(position))));
            showRecipes();
        }
        }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeClickListener) {
            mRecipeClickListener = (OnRecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRecipeClickListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        layoutBinder.unbind();
        Timber.d("onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();

            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            getActivity().registerReceiver(mRecipeBroadcastReceiver, intentFilter);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mRecipeBroadcastReceiver);

    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (mRecipesList!= null && !mRecipesList.isEmpty()) {
            savedInstanceState.putParcelableArrayList(getResources().getString(R.string.RECIPES_LIST), (ArrayList<? extends Parcelable>) mRecipesList);
            savedInstanceState.putParcelable(getResources().getString(R.string.RV_MAIN_LAYOUT), mRecyclerViewRecipes.getLayoutManager().onSaveInstanceState());
        }
    }

    private void createRecyclerView() {

        mRecyclerViewRecipes.setVisibility(View.GONE);

        boolean isTabletDisplay = getResources().getBoolean(R.bool.isTablet);
        if (isTabletDisplay) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), AppHelper.TABLET_ITEMS_PER_ROW);
            mRecyclerViewRecipes.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerViewRecipes.setLayoutManager(layoutManager);
        }


        OnItemClickListener adapterClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mRecipeClickListener.onRecipeSelected(mRecipesList.get(position));
            }
        };
        mRecipesAdapter = new RecipesAdapter(getActivity().getApplicationContext(),
                mRecipesList, adapterClickListener);
        mRecyclerViewRecipes.setAdapter(mRecipesAdapter);
        mRecipesAdapter.notifyDataSetChanged();
        mRecyclerViewRecipes.setItemAnimator(new DefaultItemAnimator());
         mRecyclerViewRecipes.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

    private void initRecipesList() {


        if (AppHelper.isNetworkAvailable(getContext())) {

            mSwipeRefreshLayout.setRefreshing(true);

            mCompositeDisposable.add(AppHelper.mClientApi.getRecipes()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (result != null) {
                            mRecipesList = result;
                            mRecipesAdapter.setmMovieList(mRecipesList);
                            BakingAppWidgetService.initWidgetContent(getActivity(), mRecipesList.get(AppHelper.WIDGET_DESIRED_RECIPE_ID));
                        }
                        showRecipes();
                    }, throwable -> {
                        showRecipes();
                        throwable.printStackTrace();
                        AppHelper.showSnackBar(getActivity(), getView(), getString(R.string.data_not_loaded));
                    }));
        } else {
            AppHelper.showSnackBar(getActivity(), getView(), getString(R.string.no_internet_access));
        }
    }
    private void initWidgitRecipes() {
          if(mRecipesList == null) {
              initRecipesList();
          }else {
              BakingAppWidgetService.initWidgetContent(getActivity(), mRecipesList.get(AppHelper.WIDGET_DESIRED_RECIPE_ID));
          }

    }


    private void showRecipes() {
        if(mRecipesList!= null && mRecipesList.size()>0){
            mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerViewRecipes.setVisibility(View.VISIBLE);
            myCustomApplication.setIdleState(true);
        }else{
            mSwipeRefreshLayout.setRefreshing(true);
            mRecyclerViewRecipes.setVisibility(View.GONE);
            myCustomApplication.setIdleState(false);
        }


    }

public void setupRecipeBroadcastReciever(){
    mRecipeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mRecipesList== null) {
                initRecipesList();
            }
        }
    };

}
}