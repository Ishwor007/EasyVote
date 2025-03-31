package com.E_Voting.Application.models;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> chain;
    public static int difficulty = 1; // Adjust difficulty for PoW

    public Blockchain() {
    	this.chain = new ArrayList<>();
        Block genesisBlock = new Block("Genesis Block","1", "0");
       // genesisBlock.mineBlock(difficulty); 
        this.chain.add(genesisBlock);
    }

    public Block getLastBlock() {
        return chain.get(chain.size());
    }

    public List<Block> getChain() {
        return chain;
    }
    public void addBlockFromDatabase(Block block) {
        chain.add(block);
    }

}
