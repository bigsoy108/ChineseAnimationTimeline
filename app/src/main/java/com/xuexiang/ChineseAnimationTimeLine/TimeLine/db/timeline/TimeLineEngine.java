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

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TimeLineEngine {

    private TLItemDao tlItemDao;

    public TimeLineEngine(Context context) {
        TLItemDataBase itemDataBase = TLItemDataBase.getInstance(context);
        tlItemDao = itemDataBase.getItemDao();
    }

    public void InsertItems(TLItem...tlItems){
        new InsertAsyncTask(tlItemDao).execute(tlItems);
    }

    public void DeleteAllItems(){
        new DeleteAllAsyncTask(tlItemDao).execute();
    }
    public List<TLItem> getAllItems() throws ExecutionException, InterruptedException {
        return new getAllAsyncTask(tlItemDao).execute().get();
    }


    static class InsertAsyncTask extends AsyncTask<TLItem,Void,Void>{
        private  TLItemDao tlItemDao;

        public InsertAsyncTask(TLItemDao tlItemDao) {
            this.tlItemDao = tlItemDao;
        }

        @Override
        protected Void doInBackground(TLItem...tlItems) {

            tlItemDao.InsertItems(tlItems);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private  TLItemDao tlItemDao;

        public DeleteAllAsyncTask(TLItemDao tlItemDao) {
            this.tlItemDao = tlItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tlItemDao.DeleteAll();
            return null;
        }
    }

    static class getAllAsyncTask extends AsyncTask<Void,Void,List<TLItem>>{
        private  TLItemDao tlItemDao;

        public getAllAsyncTask(TLItemDao tlItemDao) {
            this.tlItemDao = tlItemDao;
        }

        @Override
        protected List<TLItem> doInBackground(Void... voids) {
            return tlItemDao.getAllItems();
        }
    }

}
