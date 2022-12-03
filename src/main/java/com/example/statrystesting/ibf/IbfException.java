package com.example.statrystesting.ibf;

import java.io.IOException;

public class IbfException extends IOException {
    IbfException(String message) {
        super(message);
    }
}
