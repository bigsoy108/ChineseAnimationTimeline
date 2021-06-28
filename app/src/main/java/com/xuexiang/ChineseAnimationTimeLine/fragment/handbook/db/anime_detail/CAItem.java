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

package com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class CAItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("detail_id")
    String detail_id;

    @SerializedName("display_pic")
    String display_pic;

    @SerializedName("title")
    String title;

    @SerializedName("creater")
    String creater;

    @SerializedName("year")
    String year;

    @SerializedName("content")
    String content;

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getDetail_id(){return detail_id;}

    public void setDetail_id(String detail_id){this.detail_id = detail_id;}

    public String getDisplay_pic() {
        return display_pic;
    }

    public void setDisplay_pic(String display_pic) {
        this.display_pic = display_pic;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CAItem(int id,String detail_id,String display_pic, String title, String creater,String year, String content) {
        this.id = id;
        this.detail_id = detail_id;
        this.display_pic = display_pic;
        this.title = title;
        this.creater = creater;
        this.year = year;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + detail_id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
