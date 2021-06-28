/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.ChineseAnimationTimeLine.fragment.trending;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.ChineseAnimationTimeLine.R;
import com.xuexiang.ChineseAnimationTimeLine.TimeLine.TLItemDecoration;
import com.xuexiang.ChineseAnimationTimeLine.TimeLine.db.timeline.TLItem;
import com.xuexiang.ChineseAnimationTimeLine.TimeLine.db.timeline.TimeLineEngine;
import com.xuexiang.ChineseAnimationTimeLine.activity.DetailActivity;
import com.xuexiang.ChineseAnimationTimeLine.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.ChineseAnimationTimeLine.core.BaseFragment;
import com.xuexiang.ChineseAnimationTimeLine.utils.MMKVUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xutil.app.ActivityUtils;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author xuexiang
 * @since 2019-10-30 00:19
 * 趋势页面
 */
@Page(anim = CoreAnim.none)
public class TrendingFragment extends BaseFragment {

    @BindView(R.id.recyclerview_timeline)
    RecyclerView mRecyclerView;
    @BindView(R.id.trend_refreshLayout)
    SmartRefreshLayout refreshLayout;

    private SimpleDelegateAdapter<TLItem> mAdapter;
    List<TLItem> tlItemList;

    String[] color_list;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timeline;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        color_list =  getResources().getStringArray(R.array.timeline_color_list);
        check_update();
        TimeLineEngine timeLineEngine = new TimeLineEngine(getContext());
        try {
            tlItemList = timeLineEngine.getAllItems();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new TLItemDecoration(getContext(),10));
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
            }
        });

        mAdapter = new SimpleDelegateAdapter<TLItem>(R.layout.timeline_item, new LinearLayoutHelper(), tlItemList) {
            @Override
            protected void bindData(@NonNull @NotNull RecyclerViewHolder holder, int position, TLItem item) {
                if (item != null) {
                    ImageLoader.get().loadImage(holder.getImageView(R.id.tl_item_expand_button), item.getTitle_pic());
                    holder.text(R.id.tl_item_title,item.getTitle());
                    holder.text(R.id.time_year,item.getTime());
                    holder.text(R.id.tl_item_content,item.getContent());
                    holder.text(R.id.item_num,item.getItem_id());

                    int item_id = Integer.parseInt(item.getItem_id())+1;
                    holder.itemView.setRotationY(item_id%2*180);
                    holder.getView(R.id.tl_item_expand_button).setRotationY(item_id%2*180);
                    holder.getView(R.id.tl_item_title).setRotationY(item_id%2*180);
                    holder.getView(R.id.tl_item_content_layout).setRotationY(item_id%2*180);
                    holder.getView(R.id.tl_item_title).setRotationY(item_id%2*180);
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.timeline_title);
                    drawable.setColorFilter(Color.parseColor(color_list[position%3]), PorterDuff.Mode.SRC_ATOP);
                    holder.getView(R.id.tl_item_expandable_title).setBackground(drawable);
//                    holder.getView(R.id.tl_item_title).setPadding(item_id%2*30,0,(item_id%2+1)*30,0);
                    //展开和收起条目
                    holder.click(R.id.tl_item_expand_button, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RadiusImageView radiusImageView = (RadiusImageView)view;
                            radiusImageView.setCircle(!radiusImageView.isCircle());
                            ExpandableLayout expandableLayout1 = holder.findViewById(R.id.tl_item_expandable_title);
                            ExpandableLayout expandableLayout2 = holder.findViewById(R.id.tl_item_expandable_content);
                            ExpandableLayout expandableblank = holder.findViewById(R.id.tl_item_expandable_blank);
                            expandableblank.toggle();
                            expandableLayout1.toggle();
                            expandableLayout2.toggle();
                        }
                    });
                    //进入详情页面
                    holder.click(R.id.tl_item_content_layout, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle params = new Bundle();
                            params.putString("anime_id",item.getDetail_id());
                            ActivityUtils.startActivityWithBundle(DetailActivity.class,"item",params);
                        }
                    });
                }
            }
        };


        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
                dataRefresh();
                mAdapter.refresh(tlItemList);
            refreshLayout.getLayout().postDelayed(() -> {
                refreshLayout.finishRefresh();
            }, 2000);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
//                mNewsAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
                refreshLayout.finishLoadMore();
            }, 1000);
        });

    }

    private void dataRefresh() {
        TimeLineEngine timeLineEngine = new TimeLineEngine(getContext());
        try {
            tlItemList.clear();
            tlItemList.addAll(timeLineEngine.getAllItems());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void check_update(){
        Observable.just(getResources().getString(R.string.base_ip)+"/test/v1.0/update_flag/find")
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NotNull String s) throws Exception {
                        try {
                            OkHttpClient client = new OkHttpClient(); //创建http客户端
                            RequestBody requestBody = RequestBody.create(
                                    "{\"query\":{\"update_name\":\"时间轴更新请求\"}}",
                                    MediaType.parse("application/json"));
                            Request request = new Request.Builder()
                                    .url(s) //后端请求接口的地址
                                    .post(requestBody).build(); //创建http请求
                            Response response = client.newCall(request).execute(); //执行发送指令
                            String responseData = response.body().string(); //获取后端接口返回过来的JSON格式的结果
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray data = jsonObject.getJSONArray("data");
                            JSONObject check_tl = (JSONObject) data.get(0);
                            response.close();
                            if(!MMKVUtils.getString("timeline_version","").equals(check_tl.get("version").toString()) ){
                                MMKVUtils.put("timeline_version",check_tl.get("version").toString());
                                MMKVUtils.put("timeline_update",true);
                            }
                            return true;
                        }catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<Boolean, Boolean>(){
                    @Override
                    public Boolean apply(@NotNull Boolean aBoolean) throws Exception {
                        try {
                           return update();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        return false;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull Boolean aBoolean) {
                        if(aBoolean) {
                            refreshLayout.autoRefresh();
                        }
                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    private Boolean update() throws Exception{
        Boolean isupdate =  MMKVUtils.getBoolean("timeline_update",false);
        if(isupdate){
            OkHttpClient client = new OkHttpClient(); //创建http客户端
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.base_ip)+"/test/v1.0/time-line/?limit=100") //后端请求接口的地址
                    .get().build(); //创建http请求
            Response response = client.newCall(request).execute(); //执行发送指令
            String responseData = response.body().string(); //获取后端接口返回过来的JSON格式的结果
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray data = jsonObject.getJSONArray("data");
            response.close();
            Type type = new TypeToken<List<TLItem>>(){}.getType();
            List<TLItem> list = gson.fromJson(data.toString(),type);
            TLItem[] items = new TLItem[list.size()];
            list.toArray(items);
            TimeLineEngine dbengine = new TimeLineEngine(XUI.getContext());
            dbengine.DeleteAllItems();
            dbengine.InsertItems(items);
            MMKVUtils.put("timeline_update",false);
        }
        return isupdate;
    }
}
