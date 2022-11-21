package com.example.statrystesting.controller;

import com.example.statrystesting.entity.IbfData;
import com.example.statrystesting.entity.response.CommonResponse;
import com.example.statrystesting.ibf.InvertibleBloomFilter;
import com.example.statrystesting.service.IbfService;
import com.example.statrystesting.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ibf")
@Slf4j
public class IbfController {

    @Autowired
    private IbfService ibfService;

    @GetMapping(value = "/runIbf", consumes = "application/json", produces = "application/json")
    public CommonResponse runIbf() {
        InvertibleBloomFilter filter = ibfService.get();

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .value(filter)
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
