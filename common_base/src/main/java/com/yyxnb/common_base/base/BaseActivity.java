package com.yyxnb.common_base.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.github.anzewei.parallaxbacklayout.ParallaxBack;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout;
import com.yyxnb.arch.annotations.SwipeStyle;
import com.yyxnb.arch.base.IActivity;
import com.yyxnb.arch.base.IFragment;
import com.yyxnb.arch.base.Java8Observer;
import com.yyxnb.arch.common.ArchConfig;
import com.yyxnb.arch.delegate.ActivityDelegate;
import com.yyxnb.common.KeyboardUtils;
import com.yyxnb.skinloader.SkinInflaterFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import me.jessyan.autosize.AutoSizeCompat;

/**
 * 建议 {@link ContainerActivity#initBaseFragment()}  }
 */
@ParallaxBack(edgeMode = ParallaxBack.EdgeMode.EDGE)
public abstract class BaseActivity extends AppCompatActivity implements IActivity {

    protected final String TAG = getClass().getCanonicalName();
    protected WeakReference<Context> mContext;

    private Java8Observer java8Observer;
    protected ActivityDelegate mActivityDelegate = getBaseDelegate();

    public Context getContext() {
        return mContext.get();
    }

    public BaseActivity() {
        java8Observer = new Java8Observer(TAG);
        getLifecycle().addObserver(java8Observer);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SkinInflaterFactory.setFactory(this);
        getWindow().setBackgroundDrawable(null);
        mContext = new WeakReference<>(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setSwipeBack(int mSwipeBack) {
        final ParallaxBackLayout layout = ParallaxHelper.getParallaxBackLayout(this, true);
        switch (mSwipeBack) {
            case SwipeStyle.Full:
                ParallaxHelper.enableParallaxBack(this);
                //全屏滑动
                layout.setEdgeMode(ParallaxBackLayout.EDGE_MODE_FULL);
                break;
            case SwipeStyle.Edge:
                ParallaxHelper.enableParallaxBack(this);
                //边缘滑动
                layout.setEdgeMode(ParallaxBackLayout.EDGE_MODE_DEFAULT);
                break;
            case SwipeStyle.None:
                ParallaxHelper.disableParallaxBack(this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(java8Observer);
        mContext.clear();
        mContext = null;
        mActivityDelegate = null;
    }

    @Override
    public Resources getResources() {
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
        //如果没有自定义需求用这个方法
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
        //如果有自定义需求就用这个方法
//        AutoSizeCompat.autoConvertDensity(super.getResources(), 667f, false);
        return super.getResources();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.isEmpty()) {
            super.onBackPressed();
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把操作放在用户点击的时候
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            View v = getCurrentFocus();
            //判断用户点击的是否是输入框以外的区域
            if (mActivityDelegate.isShouldHideKeyboard(v, event)) {
                //收起键盘
                if (v != null) {
                    KeyboardUtils.hideSoftInput(v);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public <T extends IFragment> void startFragment(T targetFragment) {
        startFragment(targetFragment, 0);
    }

    public <T extends IFragment> void startFragment(T targetFragment, int requestCode) {
        try {
            Intent intent = new Intent(this, ContainerActivity.class);
            Bundle bundle = targetFragment.initArguments();
            bundle.putInt(ArchConfig.REQUEST_CODE, requestCode);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ArchConfig.FRAGMENT, targetFragment.getClass().getCanonicalName());
            intent.putExtra(ArchConfig.BUNDLE, bundle);
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T extends IFragment> void setRootFragment(T fragment, int containerId) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, (Fragment) fragment, fragment.sceneId());
            transaction.addToBackStack(fragment.sceneId());
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}