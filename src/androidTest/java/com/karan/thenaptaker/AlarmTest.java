package com.karan.thenaptaker;

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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AlarmTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
    @Before
    public void setUp() throws Exception {


        mActivityRule.launchActivity(null);
        DBHelper dbHelper =new DBHelper(InstrumentationRegistry.getTargetContext());
        dbHelper.insertNapDetails("testAdding",0,0,0.1f);
        dbHelper.close();
        onView(withId(R.id.button_start)).perform(ViewActions.click());
        onView(withText("testAdding")).perform(ViewActions.click());


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

    @Test
    public void thanks() throws Exception {

        onView(withId(R.id.textView_thanku)).check(matches(withText(R.string.thanku)));
    }

}