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

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xuexiang.ChineseAnimationTimeLine.R;
import com.xuexiang.ChineseAnimationTimeLine.core.BaseFragment;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.TimeItem;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.TimeItemEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.creater.CreaterEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.creater.CreaterItem;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.List;

import butterknife.BindView;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

@Page(name = "手册")
public class HandbookFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;

    String flag;


    private FragmentStateViewPager2Adapter mAdapter;

    public static HandbookFragment newInstance(String type) {
        HandbookFragment fragment = new HandbookFragment();
        fragment.flag = type;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_handbook;
    }


    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        initTabLayout();
    }

    private void initTabLayout() {
        mAdapter = new FragmentStateViewPager2Adapter(this);
        tabLayout.setTabMode(MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(this);

//        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(XUI.getContext(),
//                R.drawable.gray_line));
//        linearLayout.setDividerPadding(16);

        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(mAdapter);
        // 设置缓存的数量
        viewPager.setOffscreenPageLimit(5);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(mAdapter.getPageTitle(position))).attach();
        refreshAdapter();
    }

    private void refreshAdapter() {
        if (viewPager == null) {
            return;
        }
        if (flag.equals("time")) {
            try {
                TimeItemEngine dbengine = new TimeItemEngine(XUI.getContext());
                List<TimeItem> list = dbengine.getAllItems();
                for(TimeItem item:list){
                    mAdapter.addFragment(HandbookListFragment.newInstance(item),item.getTime());
                }
                mAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0, false);
                WidgetUtils.setTabLayoutTextFont(tabLayout);
            }catch (Exception e){

            }
        } else {
            try {
                CreaterEngine dbengine = new CreaterEngine(XUI.getContext());
                List<CreaterItem> list = dbengine.getAllItems();
                for (CreaterItem item : list) {
                    mAdapter.addFragment(HandbookListFragment.newInstance(item), item.getCreater());
                }
                mAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0, false);
                WidgetUtils.setTabLayoutTextFont(tabLayout);
            }catch (Exception e){

            }
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//        XToastUtils.toast("选中了:" + tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    protected void initListeners() {
        super.initListeners();
    }

}
