package com.sensorberg.sdk;

import com.sensorberg.SensorbergApplicationBootstrapper;
import com.sensorberg.di.Component;
import com.sensorberg.sdk.di.TestComponent;

import android.app.Application;

import lombok.Getter;
import lombok.Setter;

public class SensorbergTestApplication extends Application {

    @Getter
    @Setter
    private static Component component;

    @Override
    public void onCreate() {
        super.onCreate();
        setComponent(buildComponentAndInject());

        //we need this because this is the entry point for dagger
        SensorbergApplicationBootstrapper bootstrapper = new SensorbergApplicationBootstrapper(this, null);
    }

    public Component buildComponentAndInject() {
        return TestComponent.Initializer.init(this);
    }
}
