package com.cams.goforlunch.ui.activities;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import com.cams.goforlunch.R;
import com.cams.goforlunch.data.DetailsRepository;
import com.cams.goforlunch.model.Restaurant;
import com.cams.goforlunch.model.User;

import java.util.List;


public class MainActivityTest {
    private final int POSITION_ITEM = 0;



    @Rule
    public final ActivityTestRule<MainActivity>activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickOnListView_opensListFragment() {

        onView(withId(R.id.bottom_list_view)).perform(click());
        onView(withId(R.id.list_restaurants)).check(matches(isDisplayed()));
    }

    @Test
    public void myRestaurantsList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_restaurants))
                .check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void myRestaurantsList_onClickItem_shouldOpenDetailRestaurant() {
        //Result : Launch page profile
        //Item click
        onView(withId(R.id.list_restaurants))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        //After : Verification of the display of the restaurant name.
        onView(withId(R.id.restaurant_name)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnWorkmatesView_opensWorkmatesFragment() {

        onView(withId(R.id.bottom_workmates)).perform(click());
        onView(withId(R.id.list_tasks)).check(matches(isDisplayed()));
    }

    @Test
    public void myWorkmatesList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_tasks))
                .check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void clickOnMapsView_opensMapsFragment() {

        onView(withId(R.id.bottom_map)).perform(click());
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
    }

    @Test
    public void UserGoingRestaurantList_IsDisplayed() {

        onView(withId(R.id.list_restaurants))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        onView(ViewMatchers.withId(R.id.restaurant_details_recycler_view)).check(matches(isDisplayed()));

    }

}
