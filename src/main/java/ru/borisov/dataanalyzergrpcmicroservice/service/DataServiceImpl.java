package ru.borisov.dataanalyzergrpcmicroservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.borisov.dataanalyzergrpcmicroservice.model.Data;
import ru.borisov.dataanalyzergrpcmicroservice.repository.DataRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    @Override
    public void handle(Data data) {

        log.info("Data object {} saved", data);
        dataRepository.save(data);
    }

    @Override
    public List<Data> getWithBatch(Long batchSize) {

        List<Data> data = dataRepository.findAllWithOffset(batchSize);
        if (data.size() > 0) {
            dataRepository.incrementOffset(Long.min(batchSize, data.size()));
        }
        return data;
    }
}
