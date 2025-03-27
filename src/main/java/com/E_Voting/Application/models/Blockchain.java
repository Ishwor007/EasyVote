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

//    public Block addBlock(String voterId, String candidateId) {
//        Block previousBlock = getLastBlock();
//        Block newBlock = new Block(voterId, candidateId, previousBlock.getHash());
//        chain.add(newBlock);
//        return newBlock;
//    }

//    public boolean isValid() {
//        for (int i = 1; i < chain.size(); i++) {
//            Block currentBlock = chain.get(i);
//            Block previousBlock = chain.get(i - 1);
//
//            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
//                return false;
//            }
//            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
//                return false;
//            }
//        }
//        return true;
//    }

    public List<Block> getChain() {
        return chain;
    }
    public void addBlockFromDatabase(Block block) {
        chain.add(block);
    }

}
