/*
 * Class Name: LoginTest
 *
 * Version: Version 1.0
 *
 * Date : December 3, 2018
 *
 * Copyright (c) Team 25, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */
package com.cmput301f18t25.healthx;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import android.widget.EditText;
import android.widget.RadioButton;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginTest extends ActivityTestRule<Login>{

    public String test_name = "name"+RandomStringUtils.randomAlphabetic(3);

    // make a dif username each time we test it, so we're not mixing up users
    public String test_username = "usrname"+RandomStringUtils.randomAlphanumeric(4);
    public String test_phone_number = "1234567890";
    public String test_email = test_username+"@email.com";
    private Solo solo;


    public LoginTest() {
        super(Login.class);
    }

    @Rule
    public ActivityTestRule<Login> activityTestRule =
            new ActivityTestRule<>(Login.class);


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
    public void testLogin() throws Exception {

        // make a new account

        solo.assertCurrentActivity("wrong activity",Login.class);
        solo.clickOnView(solo.getView(R.id.link_signup));

        solo.assertCurrentActivity("wrong activity", Signup.class);


        EditText id = (EditText) solo.getView(R.id.input_id);
        EditText name = (EditText) solo.getView(R.id.input_name);
        EditText email = (EditText) solo.getView(R.id.input_email);
        EditText phone = (EditText) solo.getView(R.id.input_phone);

        RadioButton patient_btn = (RadioButton) solo.getView(R.id.radio_patient);

        solo.enterText(id,test_username);
        solo.enterText(name,test_name);
        solo.enterText(email,test_email);
        solo.enterText(phone,test_phone_number);
        solo.clickOnView(patient_btn);

        solo.clickOnView(solo.getView(R.id.btn_signup));
        assertTrue("not logged in",solo.waitForActivity(ViewProblemList.class, 3000));

        // log out and back in
        solo.clickOnActionBarHomeButton();
        solo.clickOnView(solo.getView(R.id.nav_logout));


        assertTrue(solo.waitForActivity(Login.class));


        EditText user_id = (EditText) solo.getView(R.id.loginUserID);

        solo.clickOnView(solo.getView(R.id.btn_login));
        assertTrue("toast not shown",solo.waitForText("Invalid Credentials!",1,3000));

        solo.enterText(user_id,test_username);
        solo.clickOnView(solo.getView(R.id.btn_login));

        boolean next_view = solo.waitForActivity(ViewProblemList.class, 3000);
        assertTrue("not logged in",next_view);

    }

}