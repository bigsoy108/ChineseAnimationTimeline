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

package com.xuexiang.ChineseAnimationTimeLine.TimeLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.xuexiang.ChineseAnimationTimeLine.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xutil.resource.ResourceUtils;

public class TLItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private int distance;

    Drawable verticalLine;
    Drawable horizontalLine;
    TextDrawable text;
    int point_size = 90;
    int line_width = 6;


    public TLItemDecoration(Context context, int distance) {
        mContext = context;
        this.distance = distance;
        verticalLine = ContextCompat.getDrawable(mContext, R.drawable.gray_line);
//        drawable = ContextCompat.getDrawable(mContext, R.drawable.time);
        horizontalLine = ContextCompat.getDrawable(mContext, R.drawable.horizontal_line);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = distance+80;
        outRect.right = distance;
        outRect.bottom = 0;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = 4*distance;
        }

        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = distance;
            outRect.right = distance+80;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int parentWidth = parent.getMeasuredWidth();


        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final LinearLayout linearLayout = child.findViewById(R.id.timeline_item_title);
            RadiusImageView rd = child.findViewById(R.id.tl_item_expand_button);
            TextView title = child.findViewById(R.id.tl_item_title);
            String year = ((TextView)child.findViewById(R.id.time_year)).getText().toString();
            int num = Integer.parseInt(((TextView)child.findViewById(R.id.item_num)).getText().toString());
            int vertop = child.getTop();
            int verbottom = child.getBottom();





            int horizontalLineLeft = rd.getRight();
            int horizontalLineRight = parentWidth/2+title.getMeasuredWidth()*3/4;

            int verLeft = horizontalLineRight-line_width/2;
            int verRight = horizontalLineRight+line_width/2;


            final int horizontalLineCenter = child.getTop()+ linearLayout.getTop() + (linearLayout.getBottom() - linearLayout.getTop()) / 2;
            int drawble_horizon = horizontalLineRight;
            if (num%2==0) {
                horizontalLineLeft = parentWidth - parentWidth/2-title.getMeasuredWidth()*2/3;
                horizontalLineRight = parentWidth - rd.getRight();
                verLeft = horizontalLineLeft-line_width/2;
                verRight = horizontalLineLeft+line_width/2;
                drawble_horizon = horizontalLineLeft;
            }

            horizontalLine.setBounds(horizontalLineLeft, horizontalLineCenter- line_width/2,horizontalLineRight,horizontalLineCenter + line_width/2);
            horizontalLine.draw(c);
            verticalLine.setBounds(verLeft, vertop, verRight,verbottom);
            verticalLine.draw(c);

            text = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(dip2px(XUI.getContext(),point_size)/10) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(year, mContext.getColor(R.color.app_pink));

            text.setBounds(drawble_horizon-point_size/2, horizontalLineCenter-point_size / 2, drawble_horizon+point_size/2, horizontalLineCenter+point_size / 2);
            text.draw(c);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
