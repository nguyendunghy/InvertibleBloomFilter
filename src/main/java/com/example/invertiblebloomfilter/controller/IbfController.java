package com.example.invertiblebloomfilter.controller;

import com.example.invertiblebloomfilter.entity.DataTable;
import com.example.invertiblebloomfilter.entity.IbfData;
import com.example.invertiblebloomfilter.entity.response.CommonResponse;
import com.example.invertiblebloomfilter.ibf.IBFDecodeResult;
import com.example.invertiblebloomfilter.ibf.InvertibleBloomFilter;
import com.example.invertiblebloomfilter.service.IbfService;
import com.example.invertiblebloomfilter.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ibf")
@Slf4j
public class IbfController {
    private static InvertibleBloomFilter invertibleBloomFilter = new InvertibleBloomFilter(4, 7);

    @Autowired
    private IbfService ibfService;

    @GetMapping(value = "/init", consumes = "application/json", produces = "application/json")
    public CommonResponse init() {
       ibfService.streamIbfData(invertibleBloomFilter);

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .value(invertibleBloomFilter)
                .build();
    }

    @GetMapping(value = "/diff", consumes = "application/json", produces = "application/json")
    public CommonResponse diff() {
        InvertibleBloomFilter newIBF = new InvertibleBloomFilter(4, 7);
        ibfService.streamIbfData(newIBF);

        InvertibleBloomFilter diff = newIBF.subtract(invertibleBloomFilter);
        IBFDecodeResult result = diff.decode();

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .value(result)
                .build();
    }

    @GetMapping(value = "/retrieve/{rowHash}", consumes = "application/json", produces = "application/json")
    public CommonResponse retrieve(@PathVariable("rowHash") String rowHash) {
        List<DataTable> ibfDataList = ibfService.retrieveAllData(rowHash);

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .value(ibfDataList)
                .build();
    }

    @GetMapping(value = "/retrieve/history/{rowHash}", consumes = "application/json", produces = "application/json")
    public CommonResponse retrieveHistory(@PathVariable("rowHash") String rowHash) {
        List<DataTable> ibfDataList = ibfService.retrieveAllHistoryData(rowHash);

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .value(ibfDataList)
                .build();
    }

    @GetMapping(value = "/findAll", consumes = "application/json", produces = "application/json")
    public CommonResponse findAll() {
        List<IbfData> ibfDataList = ibfService.findAll();

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .value(ibfDataList)
                .build();
    }

}
