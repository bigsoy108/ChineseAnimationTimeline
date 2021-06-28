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

package com.xuexiang.ChineseAnimationTimeLine.TimeLine.db.timeline;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class TLItem  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("detail_id")
    String detail_id;

    @SerializedName("title_pic")
    String title_pic;

    @SerializedName("title")
    String title;

    @SerializedName("time")
    String time;

    @SerializedName("content")
    String content;

    @SerializedName("item_id")
    String item_id;

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getDetail_id(){return detail_id;}

    public void setDetail_id(String detail_id){this.detail_id = detail_id;}

    public String getItem_id(){return item_id;}

    public void setItem_id(String item_id){this.item_id = item_id;}

    public String getTitle_pic() {
        return title_pic;
    }

    public void setTitle_pic(String title_pic) {
        this.title_pic = title_pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TLItem(int id,String title_pic, String title, String content,String detail_id) {
        this.id = id;
        this.title_pic = title_pic;
        this.title = title;
        this.content = content;
        this.detail_id = detail_id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title_pic='" + title_pic + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", detail_id='" + detail_id + '\'' +
                '}';
    }

}