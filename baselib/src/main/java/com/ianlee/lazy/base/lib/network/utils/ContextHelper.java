package com.ianlee.lazy.base.lib.network.utils;

import android.app.Application;

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function :
 */
public class ContextHelper {
    private static Application application;

    public static Application getApplication() {

        if (application == null) {
            try {
                application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return application;
    }

    public static void setApplication(Application app) {
        application = app;
    }

}
