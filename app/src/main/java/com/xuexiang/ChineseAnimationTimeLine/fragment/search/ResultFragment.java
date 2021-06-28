/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.ChineseAnimationTimeLine.fragment.search;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;


import com.xuexiang.ChineseAnimationTimeLine.R;
import com.xuexiang.ChineseAnimationTimeLine.activity.DetailActivity;
import com.xuexiang.ChineseAnimationTimeLine.core.BaseFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.FragmentStateViewPager2Adapter;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.HandbookListFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.search.db.SearchRecord;
import com.xuexiang.ChineseAnimationTimeLine.fragment.search.db.SearchRecordEngine;
import com.xuexiang.ChineseAnimationTimeLine.utils.MMKVUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.searchview.DefaultSearchFilter;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.data.DateUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;

@Page(name = "搜索结果")
public class ResultFragment extends BaseFragment {
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    private FragmentStateViewPager2Adapter mAdapter;
    private CAEngine caEngine;
    private SearchRecordEngine searchRecordEngine;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_result;
    }


    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(Color.parseColor("#f3718d"));
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_baseline_search_24) {
            @Override
            @SingleClick
            public void performAction(View view) {
                mSearchView.showSearch();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        initSearch();
        mAdapter = new FragmentStateViewPager2Adapter(this);
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(mAdapter);
        // 设置缓存的数量
        viewPager.setOffscreenPageLimit(1);

        Bundle arguments = getArguments();
        String target = arguments.getString("target");
        try {
            initList(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSearch() {
        caEngine = new CAEngine(XUI.getContext());
        searchRecordEngine = new SearchRecordEngine(getContext());
        mSearchView.setVoiceSearch(false);
        mSearchView.setEllipsize(true);
        try {
            mSearchView.setSuggestions(caEngine.getAllTitles());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                onQueryResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        mSearchView.setSearchFilter(new DefaultSearchFilter() {
            @Override
            protected boolean filter(String suggestion, String input) {
                return suggestion.toLowerCase().contains(input.toLowerCase());
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        mSearchView.setSubmitOnClick(true);
    }

    private void initList(String target) throws Exception {
        if (viewPager == null) {
            return;
        }
        try {mAdapter.clear();
            mAdapter.addFragment(HandbookListFragment.newInstance(caEngine.searchItems("%"+target+"%")),"");
            mAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(0, false);
        }catch (Exception e){
        }
        finally {
        }
    }

    @Override
    protected void initListeners() {
        super.initListeners();
    }

    private void onQueryResult(String query) {
        Bundle params = new Bundle();
        try {
            String str = caEngine.getOneDetailID(query);
            if(str!=null)
            {
                params.putString("anime_id",str);
                ActivityUtils.startActivityWithBundle(DetailActivity.class,"item",params);
            }else{
                params.putString("target",query);
                openPage(ResultFragment.class,params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            SnackbarUtils.Long(mSearchView, "ERRO: " + query).show();
            return;
        }
        try {
            SearchRecord record = searchRecordEngine.getOneItem(query);
            if (record == null) {
                record = new SearchRecord(0,query,DateUtils.getNowMills());
                searchRecordEngine.InsertItems(record);
            } else {
                record.setTime(DateUtils.getNowMills());
                searchRecordEngine.UpdateItems(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}