package com.epam.training.taskmanager.source;

/**
 * Created by IstiN on 17.10.2014.
 */
public interface DataSource<Result,Params>{

    Result getResult(Params params) throws Exception;

}
