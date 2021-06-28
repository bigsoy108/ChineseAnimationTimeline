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
import android.os.AsyncTask;

import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAEngine;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItem;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItemDao;
import com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db.anime_detail.CAItemDataBase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchRecordEngine {
    private SearchDao searchDao;

    public SearchRecordEngine(Context context) {
        SearchDatabase itemDataBase = SearchDatabase.getInstance(context);
        searchDao = itemDataBase.getItemDao();
    }

    public void InsertItems(SearchRecord...searchRecords){
        new SearchRecordEngine.InsertAsyncTask(searchDao).execute(searchRecords);
    }

    public void DeleteAllItems(){
        new SearchRecordEngine.DeleteAllAsyncTask(searchDao).execute();
    }

    public List<SearchRecord> getAllItems() throws ExecutionException, InterruptedException {
        return new SearchRecordEngine.getAllAsyncTask(searchDao).execute().get();
    }
    public void UpdateItems(SearchRecord...searchRecords){
        new SearchRecordEngine.UpdateAsyncTask(searchDao).execute(searchRecords);
    }
    public SearchRecord getOneItem(String... strings) throws  Exception{
        return new SearchRecordEngine.getOneAsyncTask(searchDao).execute(strings).get();
    }


    static class InsertAsyncTask extends AsyncTask<SearchRecord,Void,Void> {
        private  SearchDao searchDao;

        public InsertAsyncTask(SearchDao searchDao) {
            this.searchDao = searchDao;
        }

        @Override
        protected Void doInBackground(SearchRecord...searchRecords) {

            searchDao.InsertItems(searchRecords);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private  SearchDao searchDao;

        public DeleteAllAsyncTask(SearchDao searchDao) {
            this.searchDao = searchDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            searchDao.DeleteAll();
            return null;
        }
    }

    static class getAllAsyncTask extends AsyncTask<Void,Void,List<SearchRecord>>{
        private  SearchDao searchDao;

        public getAllAsyncTask(SearchDao searchDao) {
            this.searchDao = searchDao;
        }

        @Override
        protected List<SearchRecord> doInBackground(Void... voids) {
            return searchDao.getAllItems();
        }
    }

    static class UpdateAsyncTask extends AsyncTask<SearchRecord,Void,Void> {
        private  SearchDao searchDao;

        public UpdateAsyncTask(SearchDao searchDao) {
            this.searchDao = searchDao;
        }

        @Override
        protected Void doInBackground(SearchRecord...searchRecords) {

            searchDao.UpdateItems(searchRecords);
            return null;
        }
    }
    static class getOneAsyncTask extends AsyncTask<String,Void,SearchRecord>{
        private  SearchDao searchDao;

        public getOneAsyncTask(SearchDao searchDao) {
            this.searchDao = searchDao;
        }


        @Override
        protected SearchRecord doInBackground(String... strings) {
            return searchDao.getOneItem(strings[0]);
        }
    }

}
