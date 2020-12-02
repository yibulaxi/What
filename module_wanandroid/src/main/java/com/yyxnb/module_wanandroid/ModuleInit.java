package com.yyxnb.module_wanandroid;

import android.app.Application;

import com.yyxnb.common_res.module.IModuleInit;
import com.yyxnb.lib_network.interceptor.weaknetwork.WeakNetworkManager;

public class ModuleInit implements IModuleInit {

    @Override
    public void onCreate(Application application) {
        WeakNetworkManager.get().setActive(true);
        WeakNetworkManager.get().setParameter(5000, 5, 5);
        WeakNetworkManager.get().setType(WeakNetworkManager.TYPE_SPEED_LIMIT);
    }
}
