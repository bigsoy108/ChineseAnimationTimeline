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

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.ChineseAnimationTimeLine.R;
import com.xuexiang.ChineseAnimationTimeLine.activity.DetailActivity;
import com.xuexiang.ChineseAnimationTimeLine.core.BaseFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.HandbookListFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.other.DeatailFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.search.db.SearchRecord;
import com.xuexiang.ChineseAnimationTimeLine.fragment.search.db.SearchRecordEngine;
import com.xuexiang.ChineseAnimationTimeLine.utils.MMKVUtils;
import com.xuexiang.ChineseAnimationTimeLine.utils.Utils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.searchview.DefaultSearchFilter;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.data.DateUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "搜索")
public class SearchFragment extends BaseFragment implements RecyclerViewHolder.OnItemClickListener<SearchRecord>{
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private SearchRecordEngine searchRecordEngine;
    private SearchRecordTagAdapter mAdapter;


    CAEngine caEngine;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
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
        caEngine = new CAEngine(XUI.getContext());
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
        mSearchView.setSubmitOnClick(true);

        recyclerView.setLayoutManager(Utils.getFlexboxLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new SearchRecordTagAdapter());
        searchRecordEngine = new SearchRecordEngine(getContext());
        refreshRecord();
    }

    private void refreshRecord() {
        try {
            mAdapter.refresh(searchRecordEngine.getAllItems());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            refreshRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SingleClick
    @OnClick(R.id.iv_delete)
    public void onViewClicked(View view) {
        try {
            searchRecordEngine.DeleteAllItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter.clear();
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener(this);
    }

    @SingleClick(500)
    @Override
    public void onItemClick(View itemView, SearchRecord item, int position) {
        if (item != null) {
            onQueryResult(item.getContent());
            try {
                item.setTime(DateUtils.getNowMills());
                searchRecordEngine.UpdateItems(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecord();
    }
}
