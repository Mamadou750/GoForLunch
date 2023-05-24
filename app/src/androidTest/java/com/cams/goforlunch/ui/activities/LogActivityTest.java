package com.cams.goforlunch.ui.activities;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.cams.goforlunch.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LogActivityTest {

    @Rule
    public ActivityScenarioRule<LogActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LogActivity.class);

    @Test
    public void logActivityTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.email_button), withText("SIGN IN WITH EMAIL"),
                        childAtPosition(
                                allOf(withId(R.id.main_constraint_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.appcompat.widget.ContentFrameLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(com.firebase.ui.auth.R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.firebase.ui.auth.R.id.email_layout),
                                        0),
                                0)));
        textInputEditText.perform(scrollTo(), replaceText("caliou750@gmail.com"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(com.firebase.ui.auth.R.id.button_next), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(com.firebase.ui.auth.R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.firebase.ui.auth.R.id.password_layout),
                                        0),
                                0)));
        textInputEditText2.perform(scrollTo(), replaceText("goforlunch"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(com.firebase.ui.auth.R.id.button_done), withText("Sign in"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        materialButton3.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
