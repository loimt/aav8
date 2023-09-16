package com.bkav.aiotcloud.screen.home.device.genneralsetting;


import android.view.View;

import com.bkav.aiotcloud.screen.home.device.genneralsetting.trview.FifthLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.home.device.genneralsetting.trview.FirstLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.home.device.genneralsetting.trview.ForthLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.home.device.genneralsetting.trview.SecondLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.home.device.genneralsetting.trview.SixthLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.home.device.genneralsetting.trview.ThirdLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.notify.NotifyAdapter;
import com.bkav.aiotcloud.R;

public class MyNodeViewFactory extends BaseNodeViewFactory {

    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {
        switch (level) {
            case 0:
                return new FirstLevelNodeViewBinder(view);
            case 1:
                return new SecondLevelNodeViewBinder(view);
            case 2:
                return new ThirdLevelNodeViewBinder(view);
            case 3:
                return new ForthLevelNodeViewBinder(view);
            case 4:
                return new FifthLevelNodeViewBinder(view);
            case 5:
                return new SixthLevelNodeViewBinder(view);
            default:
                return null;
        }
    }



}