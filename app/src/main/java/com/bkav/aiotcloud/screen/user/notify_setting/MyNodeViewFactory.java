package com.bkav.aiotcloud.screen.user.notify_setting;

import android.view.View;

import com.bkav.aiotcloud.screen.notify.NotifyAdapter;
import com.bkav.aiotcloud.screen.user.notify_setting.levelNodeBinder.FifthLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.user.notify_setting.levelNodeBinder.FirstLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.user.notify_setting.levelNodeBinder.ForthLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.user.notify_setting.levelNodeBinder.SecondLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.user.notify_setting.levelNodeBinder.SixthLevelNodeViewBinder;
import com.bkav.aiotcloud.screen.user.notify_setting.levelNodeBinder.ThirdLevelNodeViewBinder;

import me.texy.treeview.base.BaseNodeViewBinder;
import me.texy.treeview.base.BaseNodeViewFactory;
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
