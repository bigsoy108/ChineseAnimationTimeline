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

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.ChineseAnimationTimeLine.R;
import com.xuexiang.ChineseAnimationTimeLine.activity.DetailActivity;
import com.xuexiang.ChineseAnimationTimeLine.core.BaseFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.TimeItem;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.TimeItemEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItem;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.creater.CreaterEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.creater.CreaterItem;
import com.xuexiang.ChineseAnimationTimeLine.utils.MMKVUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Page
public class HandbookHeadFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;



    MiniLoadingDialog mMiniLoadingDialog;

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    private FragmentStateViewPager2Adapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.handbook_head;
    }

    @Override
    protected void initViews() {
        mMiniLoadingDialog =  WidgetUtils.getMiniLoadingDialog(getContext());
        if(!MMKVUtils.containsKey("detail_version"))
            mMiniLoadingDialog.updateMessage("首次加载可能需要一点时间，请耐心等待");
        mMiniLoadingDialog.show();
        check_update();
        mAdapter = new FragmentStateViewPager2Adapter(this);
//        tabLayout.setTabMode(MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(mAdapter);
        // 设置缓存的数量
        viewPager.setOffscreenPageLimit(1);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(mAdapter.getPageTitle(position))).attach();


    }
    private void refreshAdapter() {
        if (viewPager == null) {
            return;
        }
        mAdapter.addFragment(HandbookFragment.newInstance("creater"),"主创");
        mAdapter.addFragment(HandbookFragment.newInstance("time"),"时间");
        mAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0, false);
        WidgetUtils.setTabLayoutTextFont(tabLayout);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void check_update(){
        Observable.just(getResources().getString(R.string.base_ip)+"/test/v1.0/update_flag")
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NotNull String s) throws Exception {
                        try {
                            OkHttpClient client = new OkHttpClient(); //创建http客户端
                            Request request = new Request.Builder()
                                    .url(s) //后端请求接口的地址
                                    .get().build(); //创建http请求
                            Response response = client.newCall(request).execute(); //执行发送指令
                            String responseData = response.body().string(); //获取后端接口返回过来的JSON格式的结果
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray data = jsonObject.getJSONArray("data");
                            JSONObject check_time = (JSONObject) data.get(1);
                            JSONObject check_creater = (JSONObject) data.get(2);
                            JSONObject check_detail = (JSONObject)data.get(3);
                            response.close();
                            if(!MMKVUtils.getString("detail_version","").equals(check_detail.get("version").toString()) ){
                                MMKVUtils.put("detail_version",check_detail.get("version").toString());
                                MMKVUtils.put("detail_update",true);
                            }
                            if(!MMKVUtils.getString("handbook_time_version","").equals(check_time.get("version").toString())){
                                MMKVUtils.put("handbook_time_version",check_time.get("version").toString());
                                MMKVUtils.put("handbook_time_update",true);
                            }
                            if(!MMKVUtils.getString("handbook_creater_version","").equals(check_creater.get("version").toString()) ){
                                MMKVUtils.put("handbook_creater_version",check_creater.get("version").toString());
                                MMKVUtils.put("handbook_creater_update",true);
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
                            initHandbook1();
                        }catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<Boolean, Boolean>(){
                    @Override
                    public Boolean apply(@NotNull Boolean aBoolean) throws Exception {
                        try {
                            initHandbook2();
                        }catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<Boolean, Boolean>(){
                    @Override
                    public Boolean apply(@NotNull Boolean aBoolean) throws Exception {
                        try {
                            pulldata();
                        }catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                        return true;
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
                        refreshAdapter();
                        mMiniLoadingDialog.dismiss();
                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void pulldata() throws Exception {
        Boolean isupdate =  MMKVUtils.getBoolean("detail_update",false);
        if(isupdate) {
            CAEngine caEngine = new CAEngine(XUI.getContext());
            OkHttpClient client = new OkHttpClient(); //创建http客户端
            Request request0 = new Request.Builder()
                    .url(getResources().getString(R.string.base_ip) + "/test/v1.0/animation_detail/?limit=1000&skip=0") //后端请求接口的地址
                    .get().build(); //创建http请求
            Response response0 = client.newCall(request0).execute(); //执行发送指令
            String responseData0 = response0.body().string(); //获取后端接口返回过来的JSON格式的结果
            Gson gson = new Gson();
            JSONObject jsonObject0 = new JSONObject(responseData0);
            int count = Integer.parseInt(jsonObject0.get("total").toString());
            JSONArray data0 = jsonObject0.getJSONArray("data");
            response0.close();
            Type type = new TypeToken<List<CAItem>>() {
            }.getType();
            List<CAItem> list0 = gson.fromJson(data0.toString(), type);
            CAItem[] items0 = new CAItem[list0.size()];
            list0.toArray(items0);
            caEngine.DeleteAllItems();
            caEngine.InsertItems(items0);
            for (int i = 1000; i < count; i = i + 1000) {
                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.base_ip) + "/test/v1.0/animation_detail/?limit=1000&skip=" + i) //后端请求接口的地址
                        .get().build(); //创建http请求
                Response response = client.newCall(request).execute(); //执行发送指令
                String responseData = response.body().string(); //获取后端接口返回过来的JSON格式的结果
                JSONObject jsonObject = new JSONObject(responseData);
                JSONArray data = jsonObject.getJSONArray("data");
                response.close();
                List<CAItem> list = gson.fromJson(data.toString(), type);
                CAItem[] items = new CAItem[list.size()];
                list.toArray(items);
                caEngine.InsertItems(items);
            }
            MMKVUtils.put("detail_update",false);
        }
    }

    private void initHandbook1() throws Exception {
        Boolean isupdate =  MMKVUtils.getBoolean("handbook_time_update",false);
        if(isupdate) {
            OkHttpClient client = new OkHttpClient(); //创建http客户端
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.base_ip) + "/test/v1.0/handbook-time") //后端请求接口的地址
                    .get().build(); //创建http请求
            Response response = client.newCall(request).execute(); //执行发送指令
            String responseData = response.body().string(); //获取后端接口返回过来的JSON格式的结果
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray data = jsonObject.getJSONArray("data");
            response.close();
            Type type = new TypeToken<List<TimeItem>>() {
            }.getType();
            List<TimeItem> list = gson.fromJson(data.toString(), type);
            TimeItem[] items = new TimeItem[list.size()];
            list.toArray(items);
            TimeItemEngine dbengine = new TimeItemEngine(XUI.getContext());
            dbengine.DeleteAllItems();
            dbengine.InsertItems(items);
            MMKVUtils.put("handbook_time_update",false);
        }
    }
    private void initHandbook2() throws Exception{
        Boolean isupdate =  MMKVUtils.getBoolean("handbook_creater_update",false);
        if(isupdate) {
            OkHttpClient client = new OkHttpClient(); //创建http客户端
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.base_ip) + "/test/v1.0/handbook-creater") //后端请求接口的地址
                    .get().build(); //创建http请求
            Response response = client.newCall(request).execute(); //执行发送指令
            String responseData = response.body().string(); //获取后端接口返回过来的JSON格式的结果
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray data = jsonObject.getJSONArray("data");
            response.close();
            Type type = new TypeToken<List<CreaterItem>>() {
            }.getType();
            List<CreaterItem> list = gson.fromJson(data.toString(), type);
            CreaterItem[] items = new CreaterItem[list.size()];
            list.toArray(items);
            CreaterEngine dbengine = new CreaterEngine(XUI.getContext());
            dbengine.DeleteAllItems();
            dbengine.InsertItems(items);
            MMKVUtils.put("handbook_creater_update",false);
        }

    }
}
