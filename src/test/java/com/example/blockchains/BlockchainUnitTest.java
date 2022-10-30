package com.example.blockchains;

import com.example.blockchains.model.Block;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockchainUnitTest {
    private static final int PREFIX = 4;
    private static final String PREFIX_STRING = new String(new char[PREFIX]).replace('\0', '0');

    @Test
    public void givenBlockchain_whenNewBlockAdded_thenSuccess() {
        List<Block> blockchain = buildInitialChain();

        Block newBlock = new Block("The is a New Block.", blockchain.get(blockchain.size() - 1).getHash(), new Date().getTime());
        newBlock.mineBlock(PREFIX);
        assertEquals(PREFIX_STRING, newBlock.getHash().substring(0, PREFIX));

        assertTrue(blockchain.add(newBlock));
    }

    @Test
    public void givenBlockchain_whenValidated_thenSuccess() {
        List<Block> blockchain = buildInitialChain();

        boolean flag = false;
        for (int i = 0; i < blockchain.size(); i++) {
            String previousHash = i == 0 ? "0" : blockchain.get(i - 1).getHash();

            Block currentBlock = blockchain.get(i);
            String currentBlockHash = currentBlock.getHash();

            flag = currentBlockHash.equals(currentBlock.calculateBlockHash())
                    && previousHash.equals(currentBlock.getPreviousHash())
                    && currentBlockHash.substring(0, PREFIX).equals(PREFIX_STRING);
            if (!flag) {
                break;
            }
        }

        assertTrue(flag);
    }

    private List<Block> buildInitialChain() {
        List<Block> blockchain = new ArrayList<>();
        Block genesisBlock = new Block("The is the Genesis Block.", "0", new Date().getTime());
        genesisBlock.mineBlock(PREFIX);
        blockchain.add(genesisBlock);
        Block firstBlock = new Block("The is the First Block.", genesisBlock.getHash(), new Date().getTime());
        firstBlock.mineBlock(PREFIX);
        blockchain.add(firstBlock);
        return blockchain;
    }
}
