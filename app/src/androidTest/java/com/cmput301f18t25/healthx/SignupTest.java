package com.cmput301f18t25.healthx;


import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import android.widget.EditText;
import android.widget.RadioButton;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignupTest extends ActivityTestRule<Signup>{

    public String test_name = "name";
    public String test_username = "usrname";
    public String test_phone_number = "1234567890";
    public String test_email = "user@email.com";

    private Solo solo;


    public SignupTest() {
        super(Signup.class);
    }

    @Rule
    public ActivityTestRule<Signup> activityTestRule =
            new ActivityTestRule<>(Signup.class);


    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                activityTestRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @Test
    public void testSignupPatient() throws Exception {

        solo.assertCurrentActivity("wrong activity", Signup.class);


        EditText id = (EditText) solo.getView(R.id.input_id);
        EditText name = (EditText) solo.getView(R.id.input_name);
        EditText email = (EditText) solo.getView(R.id.input_email);
        EditText phone = (EditText) solo.getView(R.id.input_phone);

        RadioButton patient_btn = (RadioButton) solo.getView(R.id.radio_patient);
        RadioButton doctor_btn = (RadioButton) solo.getView(R.id.radio_provider);

        solo.enterText(id,test_username);
        solo.enterText(name,test_name);
        solo.enterText(email,test_email);
        solo.enterText(phone,test_phone_number);
        solo.clickOnView(patient_btn);

        solo.clickOnView(solo.getView(R.id.btn_signup));


        boolean next_view = solo.waitForActivity(Login.class, 3000);
        assertTrue(next_view);

        solo.goBack();

        solo.clickOnView(doctor_btn);

        solo.clickOnView(solo.getView(R.id.btn_signup));


        boolean next_view2 = solo.waitForActivity(Login.class, 3000);
        assertTrue("did not go to login page",next_view2);

        // test successful signup by logging in

        EditText log_id = (EditText) solo.getView(R.id.loginUserID);

        solo.enterText(log_id,test_username);
        solo.clickOnView(solo.getView(R.id.btn_login));

        boolean next_view3 = solo.waitForActivity(ViewProblemList.class, 3000);
        assertTrue("did not log in",next_view3);
        assertTrue("toast not shown",solo.waitForText(test_name,1,3000));



    }

}