package com.example.myname.servicesdemo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {


    private MainActivity mTestActivity;
    private TextView mFirstTestText;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mTestActivity = getActivity();
    }


    /**
     * Test if your test fixture has been set up correctly. You should always implement a test that
     * checks the correct setup of your test fixture. If this tests fails all other tests are
     * likely to fail as well.
     */
    public void testPreconditions() {
        assertNotNull("mFirstTestActivity is null", mTestActivity);
    }

}

