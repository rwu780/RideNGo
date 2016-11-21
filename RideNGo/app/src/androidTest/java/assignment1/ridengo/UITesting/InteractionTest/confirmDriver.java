package assignment1.ridengo.UITesting.InteractionTest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RiderMainActivity;
import assignment1.ridengo.RoleSelectActivity;

/**
 * Created by Rui on 2016-11-20.
 */
public class confirmDriver extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public confirmDriver() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testConfirmDriver(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamRider");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        solo.clickOnView(solo.getView(R.id.button_Rider));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));

        solo.clickInList(1);
        
    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

}