package zero.management;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.media.MediaMetadataCompat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by Aiy on 2017/5/26.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ClientTest{
    @Rule
    public ActivityTestRule<Client> rule=new ActivityTestRule<>(Client.class);
    @Test
    public void testError(){
        onView(withId(R.id.Main_Edit_ID)).perform(typeText("1111111111"), closeSoftKeyboard());
        onView(withId(R.id.Main_Edit_Password)).perform(typeText("12345678"),closeSoftKeyboard());
        onView(withId(R.id.Main_Button)).perform(click());
    }
    @Test
    public void testError1(){
        onView(withId(R.id.Main_Edit_ID)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.Main_Edit_Password)).perform(typeText("12334"),closeSoftKeyboard());
        onView(withId(R.id.Main_Button)).perform(click());
    }
    @Test
    public void testTrue(){
        onView(withId(R.id.Main_Edit_ID)).perform(typeText("88888888"), closeSoftKeyboard());
        onView(withId(R.id.Main_Edit_Password)).perform(typeText("password"),closeSoftKeyboard());
        onView(withId(R.id.Main_Button)).perform(click());
    }


}