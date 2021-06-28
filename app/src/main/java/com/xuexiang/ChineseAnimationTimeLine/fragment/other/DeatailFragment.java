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

package com.xuexiang.ChineseAnimationTimeLine.fragment.other;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.ChineseAnimationTimeLine.R;
import com.xuexiang.ChineseAnimationTimeLine.core.BaseFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItem;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import butterknife.BindView;


@Page(name = "详情")
public class DeatailFragment extends BaseFragment {

    @BindView(R.id.animation_detail_imageView)
    ImageView imageView;

    @BindView(R.id.detail_title)
    TextView title;
    @BindView(R.id.detail_content)
    TextView content;
    @BindView(R.id.detail_year)
    TextView year;
    @BindView(R.id.detail_creater)
    TextView creater;

    @BindView(R.id.detail_refreshLayout)
    SmartRefreshLayout refreshLayout;

    String id;


    @Override
    protected int getLayoutId() {
        return R.layout.animation_detail;
    }

    @Override
    protected void initViews() {
        Bundle arguments = getArguments();
        id = arguments.getString("anime_id");
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(Color.parseColor("#f3718d"));
        return titleBar;
    }

    private void pulldata(String id){
        CAEngine caEngine = new CAEngine(XUI.getContext());
        try {
            CAItem result = caEngine.getOneItem(id);
            if(result!=null){
                setData(result);
            }
            refreshLayout.finishRefresh();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setData(CAItem item) {
        if(item.getDisplay_pic()!=null){
            ImageLoader.get().loadImage(imageView,item.getDisplay_pic());
        }

        title.setText(item.getTitle());
        content.setText(item.getContent());
        year.setText(item.getYear());
        creater.setText(item.getCreater());
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            pulldata(id);
        });
        refreshLayout.autoRefresh();
    }


}
