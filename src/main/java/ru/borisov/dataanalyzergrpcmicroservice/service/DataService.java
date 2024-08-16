package ru.borisov.dataanalyzergrpcmicroservice.service;

import ru.borisov.dataanalyzergrpcmicroservice.model.Data;

import java.util.List;

public interface DataService {

    void handle(Data data);

    List<Data> getWithBatch(Long batchSize);

}
