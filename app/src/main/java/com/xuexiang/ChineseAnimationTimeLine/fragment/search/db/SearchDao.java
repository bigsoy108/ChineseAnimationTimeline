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

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItem;

import java.util.List;

@Dao
public interface SearchDao {
    @Insert
    void InsertItems(SearchRecord...searchRecords);

    @Query("DELETE FROM SearchRecord")
    void DeleteAll();

    @Query("SELECT * FROM SearchRecord ORDER BY time desc")
    List<SearchRecord> getAllItems();

    @Query("select * from SearchRecord where content = (:this_content)")
    SearchRecord getOneItem(String this_content);

    @Update
    void UpdateItems(SearchRecord...searchRecords);
}
