package com.epam.training.taskmanager.helper;

import com.epam.training.taskmanager.os.AsyncTask;
import android.os.Handler;

import com.epam.training.taskmanager.processing.Processor;
import com.epam.training.taskmanager.source.DataSource;


/**
 * Created by IstiN on 15.10.2014.
 */
public class DataManager {

    public static final boolean IS_ASYNC_TASK = true;

    public static interface Callback<Result> {
        void onDataLoadStart();
        void onDone(Result data);
        void onError(Exception e);
    }

    public static <ProcessingResult, DataSourceResult, Params> void
        loadData(
            final Callback<ProcessingResult> callback,
            final Params params,
            final DataSource<DataSourceResult, Params> dataSource,
            final Processor<ProcessingResult, DataSourceResult> processor) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }
        if (IS_ASYNC_TASK) {
            executeInAsyncTask(callback, params, dataSource);
        } else {
            executeInThread(callback, params, dataSource, processor);
        }
    }

    private static <ProcessingResult, DataSourceResult, Params> void executeInAsyncTask(final Callback<ProcessingResult> callback, Params params, final DataSource<DataSourceResult, Params> dataSource) {
        new AsyncTask<Params, Void, ProcessingResult>() {

            private Exception mE;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onDataLoadStart();
            }

            @Override
            protected void onPostExecute(ProcessingResult processingResult) {
                super.onPostExecute(processingResult);
                if (mE != null) {
                    callback.onError(mE);
                } else {
                    callback.onDone(processingResult);
                }
            }

            @Override
            protected ProcessingResult doInBackground(Params... params) {
                try {
                    return (ProcessingResult) dataSource.getResult((Params) params);
                } catch (Exception e) {
                    mE = e;
                    return null;
                }
            }

        }.execute(params);
    }

    private static <ProcessingResult, DataSourceResult, Params> void executeInThread(final Callback<ProcessingResult> callback, final Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor) {
        final Handler handler = new Handler();
        callback.onDataLoadStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final DataSourceResult result = dataSource.getResult(params);
                    final ProcessingResult processingResult = processor.process(result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(processingResult);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();
    }

}
