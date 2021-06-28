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

package com.xuexiang.ChineseAnimationTimeLine.fragment.handbook.db;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TimeItemEngine {

    private TimeItemDao timeItemDao;

    public TimeItemEngine(Context context) {
        TimeItemDataBase itemDataBase = TimeItemDataBase.getInstance(context);
        timeItemDao = itemDataBase.getItemDao();
    }

    public void InsertItems(TimeItem... timeItems){
        new InsertAsyncTask(timeItemDao).execute(timeItems);
    }

    public void DeleteAllItems(){
        new DeleteAllAsyncTask(timeItemDao).execute();
    }
    public List<TimeItem> getAllItems() throws ExecutionException, InterruptedException {
        return new getAllAsyncTask(timeItemDao).execute().get();
    }

    public TimeItem getOneItem(String... strings) throws  Exception,InterruptedException{
        return new getOneAsyncTask(timeItemDao).execute(strings).get();
    }


    static class InsertAsyncTask extends AsyncTask<TimeItem,Void,Void> {
        private  TimeItemDao timeItemDao;

        public InsertAsyncTask(TimeItemDao timeItemDao) {
            this.timeItemDao = timeItemDao;
        }

        @Override
        protected Void doInBackground(TimeItem... timeItems) {

            timeItemDao.InsertItems(timeItems);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private  TimeItemDao timeItemDao;

        public DeleteAllAsyncTask(TimeItemDao timeItemDao) {
            this.timeItemDao = timeItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            timeItemDao.DeleteAll();
            return null;
        }
    }

    static class getAllAsyncTask extends AsyncTask<Void,Void,List<TimeItem>>{
        private  TimeItemDao timeItemDao;

        public getAllAsyncTask(TimeItemDao timeItemDao) {
            this.timeItemDao = timeItemDao;
        }

        @Override
        protected List<TimeItem> doInBackground(Void... voids) {
            return timeItemDao.getAllItems();
        }
    }

    static class getOneAsyncTask extends AsyncTask<String,Void,TimeItem>{
        private  TimeItemDao timeItemDao;

        public getOneAsyncTask(TimeItemDao timeItemDao) {
            this.timeItemDao = timeItemDao;
        }


        @Override
        protected TimeItem doInBackground(String... strings) {
            return timeItemDao.getOneItem(strings[0]);
        }
    }

}