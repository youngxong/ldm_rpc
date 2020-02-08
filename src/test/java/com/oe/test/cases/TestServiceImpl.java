package com.oe.test.cases;

import com.oe.test.cases.ITestService;

public class TestServiceImpl implements ITestService {
    @Override
    public String test(String text) {
        return "respose:"+text;
    }
}
