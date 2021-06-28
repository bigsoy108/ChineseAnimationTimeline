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

package com.xuexiang.ChineseAnimationTimeLine.fragment.handbook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.ChineseAnimationTimeLine.R;
import com.xuexiang.ChineseAnimationTimeLine.activity.DetailActivity;
import com.xuexiang.ChineseAnimationTimeLine.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.ChineseAnimationTimeLine.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.ChineseAnimationTimeLine.core.BaseFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.TimeItem;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItem;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.creater.CreaterItem;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.resource.ResourceUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import me.samlss.broccoli.Broccoli;

@Page
public class HandbookListFragment extends BaseFragment {

    @BindView(R.id.handbook_list_recyclerView)
    RecyclerView recyclerView;

    SmartRefreshLayout refreshLayout;

    List<String> idlist = new ArrayList<>();

    List<CAItem> itemlist = new ArrayList<>();

    CAEngine caEngine = new CAEngine(XUI.getContext());

    int item_count = 0;



    String[] color_list;

    private SimpleDelegateAdapter<CAItem> mAdapter;

    public static HandbookListFragment newInstance(TimeItem list) {
        HandbookListFragment fragment = new HandbookListFragment();
        fragment.idlist.addAll(list.getList());
        return fragment;
    }
    public static HandbookListFragment newInstance(CreaterItem list) {
        HandbookListFragment fragment = new HandbookListFragment();
        fragment.idlist.addAll(list.getList());
        return fragment;
    }
    public static HandbookListFragment newInstance(List<String> list) {
        HandbookListFragment fragment = new HandbookListFragment();
        fragment.idlist.addAll(list);
        return fragment;
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.handbook_list;
    }

    @Override
    protected void initViews() {
        color_list =  getResources().getStringArray(R.array.handbook_color_list);
        refreshLayout = findViewById(R.id.handbook_list_trend_refreshLayout);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
            }
        });
        initView();
    }
    private void fill_list(){
        itemlist.clear();
        try {
            int count=0;
            for(String id:idlist){
                CAItem item = caEngine.getOneItem(id);
                count++;
                if(item==null){
                    continue;
                }
                itemlist.add(item);
                if(count==10)
                    break;
            }
            item_count = count;
        }catch (Exception e){
            e.printStackTrace();
        }
        mAdapter.refresh(itemlist);
    }


    private void initView() {
        List<CAItem> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        mAdapter = new BroccoliSimpleDelegateAdapter<CAItem>(R.layout.handbook_list_item, new LinearLayoutHelper(), list) {

            @Override
            protected void onBindData(RecyclerViewHolder holder, CAItem model, int position) {
                if (model != null) {
                    holder.text(R.id.item_title, model.getTitle());
                    ImageLoader.get().loadImage(holder.getImageView(R.id.item_image),model.getDisplay_pic());
                    holder.getView(R.id.item_title).setBackgroundColor(Color.parseColor(color_list[position%3]));
                }
                holder.click(R.id.card_view, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle params = new Bundle();
                        params.putString("anime_id",model.getDetail_id());
                        ActivityUtils.startActivityWithBundle(DetailActivity.class,"item",params);
                    }
                }); 
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
                broccoli.addPlaceholders(
                        holder.findView(R.id.item_image),
                        holder.findView(R.id.item_title)
                );
            }

        };
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void initListeners() {
//        下拉刷新
//        refreshLayout.setOnRefreshListener(refreshLayout -> {
//            try {
//                fill_list();
//            } catch (Exception e) {
//                refreshLayout.getLayout().postDelayed(() -> {
//                    refreshLayout.finishRefresh();
//                }, 1000);
//            }
//        });
        refreshLayout.setOnRefreshListener(refreshLayout ->{
            try {
                fill_list();
            } catch (Exception e) {
                e.printStackTrace();
            }
            refreshLayout.finishRefresh();
        } );
        refreshLayout.setOnLoadMoreListener(refreshLayout ->{
            load_list();
            refreshLayout.finishLoadMore();
        } );
    }

    private void load_list() {
        Observable.just("")
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NotNull String s) throws Exception {
                        try {
                            int count=0;
                            for(String id:idlist){
                                count++;
                                if(count<=item_count)
                                    continue;
                                CAItem item = caEngine.getOneItem(id);
                                if(item==null){
                                    continue;
                                }
                                itemlist.add(item);
                                if(count-item_count==10)
                                    break;
                            }
                            item_count = count;
                            return true;
                        }catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                    }
                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull Boolean aBoolean) {
                        mAdapter.refresh(itemlist);
                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(item_count==0)
            fill_list();
        else
            mAdapter.refresh(itemlist);
    }
}
