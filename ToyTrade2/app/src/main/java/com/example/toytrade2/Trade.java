package com.example.toytrade2;

public class Trade {
    private final int user1Id;
    private final int user2Id;
    private final int toy1Id;
    private final int toy2Id;
    private final boolean status;

    public Trade(int user1Id, int user2Id, int toy1Id, int toy2Id, boolean status) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.toy1Id = toy1Id;
        this.toy2Id = toy2Id;
        this.status = status;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public int getToy1Id() {
        return toy1Id;
    }

    public int getToy2Id() {
        return toy2Id;
    }

    public boolean getStatus() {
        return status;
    }
}
