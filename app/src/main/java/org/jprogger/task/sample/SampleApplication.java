package org.jprogger.task.sample;

import android.app.Application;

import com.google.inject.util.Modules;

import roboguice.RoboGuice;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(this)).with(new AppModule()));
    }
}
