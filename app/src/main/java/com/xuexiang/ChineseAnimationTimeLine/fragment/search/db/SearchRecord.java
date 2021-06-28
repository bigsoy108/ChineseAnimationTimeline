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

package com.xuexiang.ChineseAnimationTimeLine.fragment.search.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class SearchRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("content")
    private String content;

    /**
     * 搜索时间
     */
    @SerializedName("time")
    private long time;

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String  getContent(){return content;}

    public void setContent(String  content){this.content = content;}

    public long getTime(){return time;}

    public void setTime(long time){this.time = time;}

    public SearchRecord(int id,String content,long time) {
        this.id = id;
        this.content = content;
        this.time = time;
    }


    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "Id=" + id +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
