package com.example.invertiblebloomfilter.ibf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;

public class IbfSyncData {
    public  ResizableInvertibleBloomFilter persistedIBF;
    public  int lastRecordCount; // the number of record we synced in the last successful sync
    private static ObjectMapper mapper = new ObjectMapper();

    public IbfSyncData() {
    }

    public IbfSyncData(ResizableInvertibleBloomFilter persistedIBF, int lastRecordCount) {
        this.persistedIBF = persistedIBF;
        this.lastRecordCount = lastRecordCount;
    }

    public IbfSyncData(String json) {
        try {
            IbfSyncData ibfSyncData = mapper.readValue(json, IbfSyncData.class);
            this.persistedIBF = ibfSyncData.persistedIBF;
            this.lastRecordCount = ibfSyncData.lastRecordCount;
            this.persistedIBF.indicesHash = OneHashingBloomFilterUtils.indexHashes(this.persistedIBF.divisors);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public int dataSize() {
        // 4 bytes for lastRecordCount
        return persistedIBF.dataSize() + 4;
    }

    @Override
    public String toString() {
        return "{persistedIBF=" + persistedIBF.toString() + ", lastRecordCount=" + lastRecordCount + "}";
    }

    public String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Serializer extends ByteBufSerializer<IbfSyncData> {
        ByteBufSerializer<ResizableInvertibleBloomFilter> ibfSerializer = new ResizableInvertibleBloomFilter.Serializer();

        @Override
        public IbfSyncData decode(ByteBuf byteBuf) {
            return new IbfSyncData(ibfSerializer.decode(byteBuf), ByteBufSerializer.int32.decode(byteBuf));
        }

        @Override
        public void encode(IbfSyncData ibfSyncData, ByteBuf byteBuf) {
            ibfSerializer.encode(ibfSyncData.persistedIBF, byteBuf);
            ByteBufSerializer.int32.encode(ibfSyncData.lastRecordCount, byteBuf);
        }


        //        @Override
        public String getName() {
            return "IbfSyncData";
        }
    }
}
