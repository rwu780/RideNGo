package assignment1.ridengo.UITesting.ProfileTest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RoleSelectActivity;
import assignment1.ridengo.User;
import assignment1.ridengo.UserInfoActivity;

/**
 * Test for MainActivity
 * Created by Rui on 2016-11-11.
 */
public class testEditProfile extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public testEditProfile(){
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }


    public void testEditProfile() throws Exception {
        /**
         * Name: editProfile
         * Use Case: US 03.02.01
         */

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamRich");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        solo.clickOnView(solo.getView(R.id.button_EditInfo));
        solo.clearEditText((EditText) solo.getView(R.id.editText_EnterEmail));
        solo.clearEditText((EditText) solo.getView(R.id.editText_EnterPhoneNum));

        solo.enterText((EditText) solo.getView(R.id.editText_EnterEmail), "new@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editText_EnterPhoneNum), "18001234567");
        solo.clickOnView(solo.getView(R.id.button_SignUpMain));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));
    }

    public void testEditVehicle() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamRich");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));
        //Add vehicle Info
        solo.clickOnView(solo.getView(R.id.button_EditInfo));
        TextView textView = (TextView)solo.getView(R.id.vehicleInfoTextView);
        if(textView.getText().equals("No vehicle information\nPlease add vehicle if you want to be a driver")){
            solo.clickOnButton("Add Vehicle");

            solo.clickOnView(solo.getView(R.id.YearSpinner));
            solo.clickOnText("2017");

            solo.enterText((EditText) solo.getView(R.id.MakeEditText), "Rolls Royce");
            solo.enterText((EditText) solo.getView(R.id.ModelEditText), "Ghost");
            solo.clickOnView(solo.getView(R.id.ColorSpinner));
            solo.clickOnText("Black");

            solo.enterText((EditText) solo.getView(R.id.PNumEditText), "001");
            solo.clickOnView(solo.getView(R.id.SaveButton));
            solo.clickOnButton("Confirm");
            assertTrue(solo.waitForActivity(RoleSelectActivity.class));
            solo.clickOnView(solo.getView(R.id.button_EditInfo));
        }

        solo.clickOnButton("Edit Vehicle");

        solo.clickOnView(solo.getView(R.id.YearSpinner));
        solo.scrollUp();
        solo.clickOnText("2017");

        solo.clearEditText((EditText) solo.getView(R.id.MakeEditText));
        solo.enterText((EditText) solo.getView(R.id.MakeEditText), "Mercedes Benz");

        solo.clearEditText((EditText) solo.getView(R.id.ModelEditText));
        solo.enterText((EditText) solo.getView(R.id.ModelEditText), "AMG GT");
        solo.clickOnView(solo.getView(R.id.ColorSpinner));
        solo.scrollUp();
        solo.clickOnText("Yellow");
        solo.clearEditText((EditText) solo.getView(R.id.PNumEditText));
        solo.enterText((EditText) solo.getView(R.id.PNumEditText), "008");
        solo.clickOnView(solo.getView(R.id.SaveButton));

        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        //Delete Vehicle
        solo.clickOnView(solo.getView(R.id.button_EditInfo));
        assertTrue(solo.waitForActivity(UserInfoActivity.class));
        solo.clickOnButton("Edit Vehicle");

        solo.clickOnView(solo.getView(R.id.DeleteButton));
        assertTrue(solo.searchText("Do you want to delete this vehicle?"));
        solo.clickOnButton("Yes");

        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
