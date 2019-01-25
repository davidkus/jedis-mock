package com.github.fppt.jedismock.storage;

import com.github.fppt.jedismock.operations.RedisOperation;
import com.github.fppt.jedismock.server.RedisClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OperationExecutorState {
    private final RedisClient owner;
    private final Map<Integer, RedisBase> redisBases;
    private boolean isTransactionModeOn = false;
    private List<RedisOperation> tx = new ArrayList<>();
    private int selectedRedisBase = 0;

    public OperationExecutorState(RedisClient owner, Map<Integer, RedisBase> redisBases){
        this.owner = owner;
        this.redisBases = redisBases;
    }

    public RedisBase base(){
        return redisBases.computeIfAbsent(selectedRedisBase, key -> new RedisBase());
    }

    public RedisClient owner(){
        return owner;
    }

    public List<RedisOperation> tx(){
        return tx;
    }

    public void changeActiveRedisBase(int selectedRedisBase) {
        this.selectedRedisBase = selectedRedisBase;
    }

    public void transactionMode(boolean isTransactionModeOn){
        this.isTransactionModeOn = isTransactionModeOn;
    }

    public boolean isTransactionModeOn(){
        return isTransactionModeOn;
    }

    public synchronized void newTransaction(){
        if(isTransactionModeOn) throw new RuntimeException("Redis mock does not support more than one transaction");
        isTransactionModeOn = true;
    }
}
