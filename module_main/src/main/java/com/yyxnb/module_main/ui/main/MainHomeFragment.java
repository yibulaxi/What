package com.yyxnb.module_main.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.yyxnb.common_res.arouter.ARouterUtils;
import com.yyxnb.common_base.core.BaseFragment;
import com.yyxnb.common_res.weight.NoScrollViewPager;
import com.yyxnb.common_res.weight.ScaleTransitionPagerTitleView;
import com.yyxnb.lib_adapter.BaseFragmentPagerAdapter;
import com.yyxnb.lib_arch.annotations.BindRes;
import com.yyxnb.lib_arch.annotations.BindViewModel;
import com.yyxnb.util_core.DpUtils;
import com.yyxnb.module_main.R;
import com.yyxnb.module_main.bean.MainHomeBean;
import com.yyxnb.module_main.config.DataConfig;
import com.yyxnb.module_main.databinding.FragmentMainHomeBinding;
import com.yyxnb.module_main.ui.MainTestFragment;
import com.yyxnb.module_main.viewmodel.MainViewModel;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import static com.yyxnb.common_res.arouter.ARouterConstant.JOKE_HOME_PROVIDE_FRAGMENT;
import static com.yyxnb.common_res.arouter.ARouterConstant.LIVE_HOME_PROVIDE_FRAGMENT;
import static com.yyxnb.common_res.arouter.ARouterConstant.MALL_HOME_PROVIDE_FRAGMENT;
import static com.yyxnb.common_res.arouter.ARouterConstant.MUSIC_HOME_PROVIDE_FRAGMENT;
import static com.yyxnb.common_res.arouter.ARouterConstant.NEWS_HOME_PROVIDE_FRAGMENT;
import static com.yyxnb.common_res.arouter.ARouterConstant.NOVEL_HOME_PROVIDE_FRAGMENT;
import static com.yyxnb.common_res.arouter.ARouterConstant.VIDEO_HOME_PROVIDE_FRAGMENT;
import static com.yyxnb.common_res.arouter.ARouterConstant.WAN_HOME_PROVIDE_FRAGMENT;

/**
 * ================================================
 * 作    者：yyx
 * 版    本：1.0
 * 日    期：11/26/20
 * 历    史：
 * 描    述：主界面 首页 - vp 列表
 * ================================================
 */
@BindRes(subPage = true)
public class MainHomeFragment extends BaseFragment {

    @BindViewModel(isActivity = true)
    MainViewModel mViewModel;

    private FragmentMainHomeBinding binding;

    private MagicIndicator mIndicator;
    private NoScrollViewPager mViewPager;

    private List<String> titles;
    private List<Fragment> fragments;

    private int currentIndex;

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_main_home;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding = getBinding();
        mIndicator = binding.vIndicator;
        mViewPager = binding.vpContent;
    }

    @Override
    public void initViewData() {
        super.initViewData();

        if (null == fragments) {

            titles = new ArrayList<>();
            fragments = new ArrayList<>();
            for (MainHomeBean bean : DataConfig.getHomeListBeans()) {
                titles.add(bean.title);
                fragments.add(getFragment(bean.title));
            }

        }

        initIndicator();
        mIndicator.onPageSelected(1);
        mViewPager.setCurrentItem(1);
    }

    private Fragment getFragment(String title) {
        switch (title) {
            case "关注":
                return new MainFollowFragment();
            case "推荐":
                return (Fragment) ARouterUtils.navFragment(JOKE_HOME_PROVIDE_FRAGMENT);
            case "视频":
            case "短视频":
                return (Fragment) ARouterUtils.navFragment(VIDEO_HOME_PROVIDE_FRAGMENT);
            case "玩安卓":
                return (Fragment) ARouterUtils.navFragment(WAN_HOME_PROVIDE_FRAGMENT);
            case "音乐":
                return (Fragment) ARouterUtils.navFragment(MUSIC_HOME_PROVIDE_FRAGMENT);
            case "小说":
                return (Fragment) ARouterUtils.navFragment(NOVEL_HOME_PROVIDE_FRAGMENT);
            case "头条":
                return (Fragment) ARouterUtils.navFragment(NEWS_HOME_PROVIDE_FRAGMENT);
            case "商城":
                return (Fragment) ARouterUtils.navFragment(MALL_HOME_PROVIDE_FRAGMENT);
            case "直播":
                return (Fragment) ARouterUtils.navFragment(LIVE_HOME_PROVIDE_FRAGMENT);
            default:
                break;
        }
        return new MainTestFragment();
    }

    @Override
    public void initObservable() {

    }

    @Override
    public void onVisible() {
        super.onVisible();
        getBaseDelegate().setNeedsStatusBarAppearanceUpdate();
        log("---onVisible---");
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        //ture 即标题平分屏幕宽度的模式
        commonNavigator.setAdjustMode(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ScaleTransitionPagerTitleView colorTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context, 0.7f);

                colorTransitionPagerTitleView.setNormalColor(Color.WHITE);
                colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
                colorTransitionPagerTitleView.setTextSize(18f);
                colorTransitionPagerTitleView.setText(titles.get(index));
                colorTransitionPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));

//                if ("视频".equals(titles.get(index))){
//                    colorTransitionPagerTitleView.setNormalColor(Color.WHITE);
//                    colorTransitionPagerTitleView.setSelectedColor(Color.RED);
//                }
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //设置宽度
                indicator.setLineWidth(DpUtils.dp2px(getContext(), 20));
                //设置高度
                indicator.setLineHeight(DpUtils.dp2px(getContext(), 2));
                //设置颜色
                indicator.setColors(Color.WHITE);
                //设置圆角
                indicator.setRoundRadius(5);
                //设置模式
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return null;
            }
        });
        mIndicator.setNavigator(commonNavigator);

        mViewPager.setOffscreenPageLimit(titles.size());
        mViewPager.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments, titles));
        //与ViewPagger联动
        ViewPagerHelper.bind(mIndicator, mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            boolean isSlide = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d("onPageScrolled", "position：" + position + " ，positionOffset ：" + positionOffset
//                        + "，positionOffsetPixels：" + positionOffsetPixels);


                if (positionOffset > 0.2f && positionOffset < 0.8f && currentIndex + 1 < titles.size() && currentIndex - 1 > 0) {
                    if (("视频".equals(titles.get(currentIndex - 1))) && positionOffset > 0.7f) {
                        if (!isSlide && !"视频".equals(titles.get(currentIndex))) {
                            isSlide = true;
                            Log.e("MainHomeFragment", " 2 - 8 : " + positionOffset);
                            mViewModel.isHideBottomBar.setValue(true);
                        }
                    } else if (("视频".equals(titles.get(currentIndex + 1))) && positionOffset < 0.3f) {
                        if (!isSlide && !"视频".equals(titles.get(currentIndex))) {
                            isSlide = true;
                            Log.e("MainHomeFragment", " 2 - 8 : " + positionOffset);
                            mViewModel.isHideBottomBar.setValue(true);
                        }
                    }
                } else {
                    isSlide = false;
                    if (!"视频".equals(titles.get(currentIndex))) {
                        mViewModel.isHideBottomBar.setValue(false);
                    }
                }

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected", "position：" + position);
                currentIndex = position;
                if ("视频".equals(titles.get(currentIndex))) {
                    mViewModel.isHideBottomBar.setValue(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("onPageScrollState", "state：" + state);
            }
        });
    }

}
