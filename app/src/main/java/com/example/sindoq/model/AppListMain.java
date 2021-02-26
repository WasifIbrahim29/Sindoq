package com.example.sindoq.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by thetaubuntu5 on 14/12/17.
 */

public class AppListMain implements Serializable {
    Drawable appIcon;
    CharSequence appName;
    CharSequence appPackage;
    boolean Selected;

    public Drawable getAppIcon() {
        return appIcon;
    }
    public boolean getAppSelected() {
        return Selected;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
    public void setAppSelected(boolean Selected) {
        this.Selected = Selected;
    }

    public CharSequence getAppName() {
        return appName;
    }

    public void setAppName(CharSequence appName) {
        this.appName = appName;

    }

    public CharSequence getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(CharSequence appPackage) {
        this.appPackage = appPackage;
    }
}
