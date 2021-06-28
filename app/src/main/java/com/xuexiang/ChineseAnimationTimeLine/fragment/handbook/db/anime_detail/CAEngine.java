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

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CAEngine {
    private CAItemDao caItemDao;

    public CAEngine(Context context) {
        CAItemDataBase itemDataBase = CAItemDataBase.getInstance(context);
        caItemDao = itemDataBase.getItemDao();
    }

    public void InsertItems(CAItem...caItems){
        new CAEngine.InsertAsyncTask(caItemDao).execute(caItems);
    }

    public void DeleteAllItems(){
        new CAEngine.DeleteAllAsyncTask(caItemDao).execute();
    }
    public List<CAItem> getAllItems() throws ExecutionException, InterruptedException {
        return new CAEngine.getAllAsyncTask(caItemDao).execute().get();
    }
    public String[] getAllTitles() throws ExecutionException, InterruptedException {
        List<CAItem> list = new CAEngine.getAllAsyncTask(caItemDao).execute().get();
        String[] str = new String[list.size()];
        for(int i=0;i<list.size();i++){
            str[i] = list.get(i).getTitle();
        }
        return str;
    }

    public CAItem getOneItem(String... strings) throws  Exception,InterruptedException{
        return new CAEngine.getOneAsyncTask(caItemDao).execute(strings).get();
    }

    public String getOneDetailID(String... strings) throws  Exception,InterruptedException{
        return new CAEngine.getDetailIDAsyncTask(caItemDao).execute(strings).get();
    }

    public List<String> searchItems(String... strings) throws  Exception{
        return new CAEngine.searchItemsAsyncTask(caItemDao).execute(strings).get();
    }


    static class InsertAsyncTask extends AsyncTask<CAItem,Void,Void> {
        private  CAItemDao caItemDao;

        public InsertAsyncTask(CAItemDao caItemDao) {
            this.caItemDao = caItemDao;
        }

        @Override
        protected Void doInBackground(CAItem...caItems) {

            caItemDao.InsertItems(caItems);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private  CAItemDao caItemDao;

        public DeleteAllAsyncTask(CAItemDao caItemDao) {
            this.caItemDao = caItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            caItemDao.DeleteAll();
            return null;
        }
    }

    static class getAllAsyncTask extends AsyncTask<Void,Void,List<CAItem>>{
        private  CAItemDao caItemDao;

        public getAllAsyncTask(CAItemDao caItemDao) {
            this.caItemDao = caItemDao;
        }

        @Override
        protected List<CAItem> doInBackground(Void... voids) {
            return caItemDao.getAllItems();
        }
    }

    static class getOneAsyncTask extends AsyncTask<String,Void,CAItem>{
        private  CAItemDao caItemDao;

        public getOneAsyncTask(CAItemDao caItemDao) {
            this.caItemDao = caItemDao;
        }


        @Override
        protected CAItem doInBackground(String... strings) {
            return caItemDao.getOneItem(strings[0]);
        }
    }
    static class getDetailIDAsyncTask extends AsyncTask<String,Void,String>{
        private  CAItemDao caItemDao;

        public getDetailIDAsyncTask(CAItemDao caItemDao) {
            this.caItemDao = caItemDao;
        }


        @Override
        protected String doInBackground(String... strings) {
            return caItemDao.getOneDetailID(strings[0]);
        }
    }

    static class searchItemsAsyncTask extends AsyncTask<String,Void,List<String>>{
        private  CAItemDao caItemDao;

        public searchItemsAsyncTask(CAItemDao caItemDao) {
            this.caItemDao = caItemDao;
        }


        @Override
        protected List<String> doInBackground(String... strings) {
            return caItemDao.searchItems(strings[0]);
        }
    }
}
