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

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItemDao;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItemDataBase;


@Database(entities = {SearchRecord.class},version = 1,exportSchema = false)
public abstract class SearchDatabase extends RoomDatabase {
    public abstract SearchDao getItemDao();
    private static SearchDatabase INSTANCE;
    public static synchronized SearchDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SearchDatabase.class,"SearchRecord").build();
        }
        return INSTANCE;
    }
}
