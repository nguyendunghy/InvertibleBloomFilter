package com.example.statrystesting.controller;

import com.example.statrystesting.entity.DataTable;
import com.example.statrystesting.entity.IbfData;
import com.example.statrystesting.entity.response.CommonResponse;
import com.example.statrystesting.ibf.IBFDecodeResult;
import com.example.statrystesting.ibf.InvertibleBloomFilter;
import com.example.statrystesting.service.IbfService;
import com.example.statrystesting.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
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

    @GetMapping(value = "/insert", consumes = "application/json", produces = "application/json")
    public CommonResponse insert() {
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
