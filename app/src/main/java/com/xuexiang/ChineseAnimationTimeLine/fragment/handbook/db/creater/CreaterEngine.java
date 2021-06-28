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

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreaterEngine {
    private CreaterItemDao createrItemDao;

    public CreaterEngine(Context context) {
        CreaterItemDataBase itemDataBase = CreaterItemDataBase.getInstance(context);
        createrItemDao = itemDataBase.getItemDao();
    }

    public void InsertItems(CreaterItem... createrItems){
        new CreaterEngine.InsertAsyncTask(createrItemDao).execute(createrItems);
    }

    public void DeleteAllItems(){
        new CreaterEngine.DeleteAllAsyncTask(createrItemDao).execute();
    }
    public List<CreaterItem> getAllItems() throws ExecutionException, InterruptedException {
        return new CreaterEngine.getAllAsyncTask(createrItemDao).execute().get();
    }

    public CreaterItem getOneItem(String... strings) throws  Exception,InterruptedException{
        return new CreaterEngine.getOneAsyncTask(createrItemDao).execute(strings).get();
    }

    static class InsertAsyncTask extends AsyncTask<CreaterItem,Void,Void> {
        private  CreaterItemDao createrItemDao;

        public InsertAsyncTask(CreaterItemDao createrItemDao) {
            this.createrItemDao = createrItemDao;
        }

        @Override
        protected Void doInBackground(CreaterItem... createrItems) {

            createrItemDao.InsertItems(createrItems);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private  CreaterItemDao createrItemDao;

        public DeleteAllAsyncTask(CreaterItemDao createrItemDao) {
            this.createrItemDao = createrItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            createrItemDao.DeleteAll();
            return null;
        }
    }

    static class getAllAsyncTask extends AsyncTask<Void,Void,List<CreaterItem>>{
        private  CreaterItemDao createrItemDao;

        public getAllAsyncTask(CreaterItemDao createrItemDao) {
            this.createrItemDao = createrItemDao;
        }

        @Override
        protected List<CreaterItem> doInBackground(Void... voids) {
            return createrItemDao.getAllItems();
        }
    }

    static class getOneAsyncTask extends AsyncTask<String,Void,CreaterItem>{
        private  CreaterItemDao createrItemDao;

        public getOneAsyncTask(CreaterItemDao createrItemDao) {
            this.createrItemDao = createrItemDao;
        }


        @Override
        protected CreaterItem doInBackground(String... strings) {
            return createrItemDao.getOneItem(strings[0]);
        }
    }

}
