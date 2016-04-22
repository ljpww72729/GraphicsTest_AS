package com.kk.lp.viewdrag;

/**
 * Created by ljpww72729 on 16/3/30.
 *
 * Interface used to communicate from the v21-specific code for configuring a DrawerLayout
 * to the DrawerLayout itself.
 */
public interface MyDrawerLayoutImpl {
    void setChildInsets(Object insets, boolean drawStatusBar);
}
