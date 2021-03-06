package com.yyxnb.module_widget.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyxnb.common_base.core.BaseFragment;
import com.yyxnb.lib_adapter.BaseViewHolder;
import com.yyxnb.lib_adapter.ItemDecoration;
import com.yyxnb.lib_adapter.SimpleOnItemClickListener;
import com.yyxnb.module_widget.R;
import com.yyxnb.module_widget.adapter.MainListAdapter;
import com.yyxnb.module_widget.config.DataConfig;
import com.yyxnb.module_widget.databinding.IncludeWidgetSrlRvLayoutBinding;
import com.yyxnb.module_widget.ui.function.WidgetQueueFragment;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2020/12/18
 * 描    述：功能
 * ================================================
 */
public class WidgetFunctionFragment extends BaseFragment {

    private MainListAdapter mAdapter = new MainListAdapter();
    private IncludeWidgetSrlRvLayoutBinding binding;
    private RecyclerView mRecyclerView;

    @Override
    public int initLayoutResId() {
        return R.layout.include_widget_srl_rv_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = getBinding();
        mRecyclerView = binding.rvContent;

        mAdapter.setOnItemClickListener(new SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                setMenu(mAdapter.getData().get(position).key);
            }
        });

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mAdapter.setSpanSizeLookup((gridLayoutManager, position) -> {
            if (mAdapter.getData().get(position).type == 1) {
                return 2;
            }
            return 1;
        });

        mRecyclerView.setLayoutManager(manager);
        ItemDecoration decoration = new ItemDecoration(getContext());
        decoration.setDividerWidth(5);
        decoration.setDividerHeight(5);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initViewData() {
        mAdapter.setDataItems(DataConfig.getFunctionBeans());
    }

    private void setMenu(String key) {
        switch (key) {
            case "queue":
                initArguments().putInt("type",2);
                startFragment(new WidgetQueueFragment());
                break;
            case "idle_queue":
                initArguments().putInt("type",0);
                startFragment(new WidgetQueueFragment());
                break;
            case "priority_queue":
                initArguments().putInt("type",1);
                startFragment(new WidgetQueueFragment());
                break;
            default:
                break;
        }
    }
}