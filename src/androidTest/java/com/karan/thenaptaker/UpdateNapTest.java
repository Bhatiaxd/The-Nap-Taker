package com.karan.thenaptaker;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.karan.thenaptaker.napdatabase.DBHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UpdateNapTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
    @Before
    public void setUp() throws Exception {

        mActivityRule.launchActivity(null);
        DBHelper dbHelper =new DBHelper(InstrumentationRegistry.getTargetContext());
        dbHelper.insertNapDetails("testAdding",0,0,0.1f);
        dbHelper.close();

    }


    @Test
    public void updateNap() throws Exception {
        onView(withId(R.id.button_setNap)).perform(ViewActions.click());
        onView(withText("testAdding")).perform(ViewActions.click());
        onView(withId(R.id.editText_name)).perform(ViewActions.clearText());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("testUpdate"));
        closeSoftKeyboard();
        onView(withId(R.id.editText_time)).perform(ViewActions.clearText());
        onView(withId(R.id.editText_time)).perform(ViewActions.typeText("0.1"));
        closeSoftKeyboard();
        onView(withId(R.id.button_submit)).perform(ViewActions.click());
        Intent intent = new Intent(InstrumentationRegistry.getTargetContext(),MainActivity.class);
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.button_start)).perform(ViewActions.click());
        onView(withText("testUpdate")).perform(ViewActions.click());
        onView(withId(R.id.textView_thanku)).check(matches(withText(R.string.thanku)));
    }

    @After
    public void tearDown() throws Exception {
        DBHelper dbHelper =new DBHelper(InstrumentationRegistry.getTargetContext());
        for (int i = 4; i <=dbHelper.numberOfRows(); i++) {
            Log.e("no", "" + dbHelper.numberOfRows());
            Log.e("no", "" +  dbHelper.deleteNapDetails(i));
        }
        dbHelper.close();
    }

}