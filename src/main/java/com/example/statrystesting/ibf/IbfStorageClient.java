package com.example.statrystesting.ibf;

import java.io.InputStream;

public interface IbfStorageClient {
    InputStream get(String objectId);

    void put(String objectId, byte[] encrypt);
}
