package com.example.invertiblebloomfilter.service;

import com.example.invertiblebloomfilter.entity.CellEntity;
import com.example.invertiblebloomfilter.entity.DataTable;
import com.example.invertiblebloomfilter.entity.IbfData;
import com.example.invertiblebloomfilter.entity.IbfEntity;
import com.example.invertiblebloomfilter.ibf.Cell;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import com.example.invertiblebloomfilter.repo.CellRepo;
import com.example.invertiblebloomfilter.repo.IbfDataRepo;
import com.example.invertiblebloomfilter.repo.IbfRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.invertiblebloomfilter.utils.StringUtils.join;
import static com.example.invertiblebloomfilter.utils.StringUtils.split;

@Service
public class IbfService {
    @Autowired
    private IbfDataRepo ibfDataRepo;

    @Autowired
    @Qualifier("IbfRepoFileImpl")
    private IbfRepo ibfRepo;

    @Autowired
    @Qualifier("CellRepoFileImpl")
    private CellRepo cellRepo;

    @Value("${ibf.db.aggregator}")
    private boolean ibfDbAgg;

    public void streamIbfData(InvertibleBloomFilter invertibleBloomFilter) {
        try {
            if (ibfDbAgg) {
                ibfDataRepo.streamDbAggIbfData(invertibleBloomFilter);
            } else {
                ibfDataRepo.streamIbfData(invertibleBloomFilter);
            }
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InvertibleBloomFilter retrieveIbf() {
        Long maxIbfId = ibfRepo.getMaxId();
        IbfEntity ibfEntity = ibfRepo.get(maxIbfId);
        InvertibleBloomFilter ibf = new InvertibleBloomFilter(
                ibfEntity.getKeyLengthSum().intValue(),
                split(ibfEntity.getDivisors())
        );

        List<CellEntity> cellEntityList = cellRepo.findAll(maxIbfId);
        for (CellEntity cell : cellEntityList) {
            ibf.loadFromDatabase(
                    cell.getCellIndex().intValue(),
                    split(cell.getKeySums()),
                    Long.parseLong(cell.getRowHashSum()),
                    cell.getCount());
        }

        return ibf;
    }

    //    @Transactional
    public void saveIbf(InvertibleBloomFilter invertibleBloomFilter) {
        Long maxId = ibfRepo.getMaxId();
        Long newIbfId = maxId + 1L;

        saveIbfEntity(invertibleBloomFilter, newIbfId);
        saveCellEntities(invertibleBloomFilter, newIbfId);
    }

    private void saveIbfEntity(InvertibleBloomFilter ibf, Long newIbfId) {
        String divisors = join(ibf.getDivisors());
        IbfEntity ibfEntity = IbfEntity.builder()
                .id(newIbfId)
                .divisors(divisors)
                .keyLengthSum((long) ibf.getKeyLengthsSum())
                .build();

        ibfRepo.save(ibfEntity);
    }


    private void saveCellEntities(InvertibleBloomFilter ibf, Long ibfId) {
        Long maxId = cellRepo.getMaxId();
        int count = 0;
        List<CellEntity> cells = new ArrayList<>();
        for (Cell cell : ibf.getCells()) {
            CellEntity cellEntity = new CellEntity();
            cellEntity.setId(maxId + count + 1);
            cellEntity.setIbfId(ibfId);
            cellEntity.setCellIndex((long) count);
            cellEntity.setRowHashSum(cell.rowHashSum() + "");
            String keySum = join(cell.keySums());
            cellEntity.setKeySums(keySum);
            cellEntity.setCount(cell.getCount());
            count++;

            cells.add(cellEntity);

        }
        cellRepo.save(cells);
    }


    public List<IbfData> findAll() {
        return ibfDataRepo.findAll();
    }

    public List<DataTable> retrieveAllData(String rowHash) {
        if (ibfDbAgg) {
            return ibfDataRepo.retrieveDbAggAllData(rowHash);
        } else {
            return ibfDataRepo.retrieveAllData(rowHash);
        }
    }

    public List<DataTable> retrieveAllHistoryData(String rowHash) {
        if (ibfDbAgg) {
            return ibfDataRepo.retrieveDbAggAllHistoryData(rowHash);
        } else {
            return ibfDataRepo.retrieveAllHistoryData(rowHash);
        }
    }

}
