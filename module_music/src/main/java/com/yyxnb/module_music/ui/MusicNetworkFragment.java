package com.yyxnb.module_music.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyxnb.lib_adapter.BaseViewHolder;
import com.yyxnb.lib_adapter.SimpleOnItemClickListener;
import com.yyxnb.lib_arch.annotations.BindRes;
import com.yyxnb.lib_arch.annotations.BindViewModel;
import com.yyxnb.common_base.core.BaseFragment;
import com.yyxnb.module_music.R;
import com.yyxnb.module_music.adapter.MusicNetWorkListAdapter;
import com.yyxnb.module_music.databinding.FragmentMusicNetworkBinding;
import com.yyxnb.module_music.db.MusicDatabase;
import com.yyxnb.module_music.viewmodel.MusicViewModel;
import com.yyxnb.lib_music.MusicPlayerManager;

/**
 * 网络音乐.
 */
@BindRes(subPage = true)
public class MusicNetworkFragment extends BaseFragment {

    private FragmentMusicNetworkBinding binding;
    private RecyclerView mRecyclerView;
    private MusicNetWorkListAdapter mAdapter;

    @BindViewModel
    MusicViewModel mViewModel;

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_music_network;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = getBinding();
        mRecyclerView = binding.rvContent;
    }

    @Override
    public void initViewData() {
        mAdapter = new MusicNetWorkListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);

                // 更新数据源
                MusicDatabase.getInstance().musicDao().deleteAll();
                MusicDatabase.getInstance().musicDao().insertItems(mAdapter.getData());
                MusicPlayerManager.getInstance().startPlayMusic(mAdapter.getData(), position);
            }
        });
    }

    @Override
    public void initObservable() {
        mViewModel.reqMusicData();

        mViewModel.getMusicData().observe(this, data -> {
            if (data != null && data.size() > 0) {
                mAdapter.setDataItems(data);
            }
        });
    }
}