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

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CAItemDao {
    @Insert
    void InsertItems(CAItem...caItems);

    @Query("DELETE FROM CAItem")
    void DeleteAll();

    @Query("SELECT * FROM CAItem ORDER BY id asc")
    List<CAItem> getAllItems();

    @Query("select * from CAItem where detail_id = (:this_id)")
    CAItem getOneItem(String this_id);

    @Query("select detail_id from CAItem where title = (:this_title)")
    String getOneDetailID(String this_title);

    @Query("select detail_id from CAItem where title like (:search_key)")
    List<String>  searchItems(String search_key);
}
