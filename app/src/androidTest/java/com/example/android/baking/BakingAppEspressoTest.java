package com.example.android.baking;

import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.baking.idlingresource.MyCustomApplication;
import com.example.android.baking.ui.activity.MainActivity;
import com.example.android.baking.ui.activity.RecipeDetailActivity;
import com.example.android.baking.ui.activity.RecipeStepActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BakingAppEspressoTest {

    protected MyCustomApplication myCustomApplication;
    protected IdlingResource mIdlingResource;
    protected boolean isTablet ;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {

        Context activityContext = activityTestRule.getActivity().getApplicationContext();
        myCustomApplication = (MyCustomApplication) activityContext;

        mIdlingResource = myCustomApplication.getmIdlingResource();
        // Register Idling Resources
        IdlingRegistry.getInstance().register(mIdlingResource);
        isTablet = myCustomApplication.getResources().getBoolean(R.bool.isTablet);
    }



    //start main activity test

    @Test
    public void recipesMainActivityTest() {
        onView(ViewMatchers.withId(R.id.rv_recipes)).check(matches(isDisplayed()));
    }


    @Test
    public void recipesMainActivityClickTest() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    }

    @Test
    public void recipesMainActivityIntentTest() {
        Intents.init();

        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        intended(hasExtraWithKey(RecipeDetailActivity.RECIPE_ID));

        Intents.release();
    }

    //End main activity test

    //start recipe details activity test

    @Test
    public void recipeDetailActivityTest() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(ViewMatchers.withId(R.id.tv_ingredients_content)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.rv_recipe_steps_list)).check(matches(isDisplayed()));
    }
    @Test
    public void recipeDetailActivityClickTest() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(ViewMatchers.withId(R.id.rv_recipe_steps_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
    }



    @Test
    public void recipeDetailActivityIntentTest() {
            onView(ViewMatchers.withId(R.id.rv_recipes))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        if (!isTablet) {
            // Checks if the keys are present and the intent launched is RecipeStepActivity
            Intents.init();
            onView(ViewMatchers.withId(R.id.rv_recipe_steps_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

            intended(hasComponent(RecipeStepActivity.class.getName()));
            intended(hasExtraWithKey(RecipeStepActivity.RECIPE_ID));
            intended(hasExtraWithKey(RecipeStepActivity.STEP_SELECTED_ID));
            Intents.release();
        }
    }
    //End recipe details activity test


    //start recipe step detail activity test
   @Test
    public void recipeStepActivityDisplayTest() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        if (!isTablet) {
            // Checks if the keys are present and the intent launched is RecipeStepActivity
            Intents.init();
            onView(ViewMatchers.withId(R.id.rv_recipe_steps_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

            intended(hasComponent(RecipeStepActivity.class.getName()));
            intended(hasExtraWithKey(RecipeStepActivity.RECIPE_ID));
            intended(hasExtraWithKey(RecipeStepActivity.STEP_SELECTED_ID));
            Intents.release();
            onView(ViewMatchers.withId(R.id.tb_recipe_step))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void recipeStepActivityFullyDisplayedTest() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        if (!isTablet) {
            Intents.init();
            onView(ViewMatchers.withId(R.id.rv_recipe_steps_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

            intended(hasComponent(RecipeStepActivity.class.getName()));
            intended(hasExtraWithKey(RecipeStepActivity.RECIPE_ID));
            intended(hasExtraWithKey(RecipeStepActivity.STEP_SELECTED_ID));
            Intents.release();
            onView(ViewMatchers.withId(R.id.tb_recipe_step))
                    .check(matches(isDisplayed()));
            onView(ViewMatchers.withId(R.id.vp_recipe_step))
                    .check(matches(isDisplayed()));
        }
    }

    //End recipe step detail activity test




    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}