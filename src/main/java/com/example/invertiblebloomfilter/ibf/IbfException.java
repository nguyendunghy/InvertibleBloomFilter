package com.example.invertiblebloomfilter.ibf;

import java.io.IOException;

public class IbfException extends IOException {
    IbfException(String message) {
        super(message);
    }
}
