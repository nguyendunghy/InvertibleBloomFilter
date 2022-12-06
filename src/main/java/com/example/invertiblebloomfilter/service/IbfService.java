package com.example.invertiblebloomfilter.service;

import com.example.invertiblebloomfilter.entity.DataTable;
import com.example.invertiblebloomfilter.entity.Ibf;
import com.example.invertiblebloomfilter.entity.IbfData;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import com.example.invertiblebloomfilter.repo.IbfDataRepo;
import com.example.invertiblebloomfilter.repo.IbfRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IbfService {
    @Autowired
    private IbfDataRepo ibfDataRepo;

    @Autowired
    private IbfRepo ibfRepo;

    public void streamIbfData(InvertibleBloomFilter invertibleBloomFilter)  {
        try{
            ibfDataRepo.streamIbfData(invertibleBloomFilter);
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveIbf(InvertibleBloomFilter invertibleBloomFilter){
        Long maxId = ibfRepo.getMaxId();
        maxId = maxId == null ? 0L: maxId;

        String divisors = invertibleBloomFilter.getDivisors()[0] + "";
        for(int i = 1; i< invertibleBloomFilter.getDivisors().length; i++){
            divisors = divisors + "-" + invertibleBloomFilter.getDivisors()[i];
        }

        Ibf ibf = Ibf.builder()
                .id(maxId + 1)
                .divisors(divisors)
                .keyLengthSum((long) invertibleBloomFilter.getKeyLengthsSum())
                .build();

        ibfRepo.save(ibf);
    }

    public List<IbfData> findAll(){
        return ibfDataRepo.findAll();
    }

    public List<DataTable> retrieveAllData(String rowHash){
        return ibfDataRepo.retrieveAllData( rowHash);
    }

    public List<DataTable> retrieveAllHistoryData(String rowHash){
        return ibfDataRepo.retrieveAllHistoryData( rowHash);
    }

}
