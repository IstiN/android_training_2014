package com.epam.training.taskmanager.processing;

/**
 * Created by IstiN on 17.10.2014.
 */
public interface Processor<ProcessingResult, Source> {

    ProcessingResult process(Source source) throws Exception;

}
