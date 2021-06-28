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

package com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.creater;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.StringTypeConverter;

import java.util.List;

@Entity
public class CreaterItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("creater")
    String creater;

    @SerializedName("id_list")
    @TypeConverters(StringTypeConverter.class)
    List<String> list;

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", creater='" + creater + '\'' +
                ", list='" + list.toString() + '\'' +
                '}';
    }
}
