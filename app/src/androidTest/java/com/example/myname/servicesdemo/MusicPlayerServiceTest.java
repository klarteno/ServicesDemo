package com.example.myname.servicesdemo;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

public class MusicPlayerServiceTest extends ServiceTestCase<MusicPlayer> {
    public MusicPlayerServiceTest() {
        super(MusicPlayer.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        setupService();
    }

    public void testBinder() throws Exception {
        Intent serviceIntent = new Intent(getContext(), MusicPlayer.class);
        IBinder binder = bindService(serviceIntent);

        assertTrue(binder instanceof MusicPlayer.MusicPlayerBinder);

        MusicPlayer myService = ((MusicPlayer.MusicPlayerBinder) binder).getService();
        assertSame(myService, getService());
    }

    @Override
    public void tearDown() throws Exception {
        shutdownService();
        super.tearDown();
    }
}
