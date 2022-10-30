package com.example.blockchains.model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
public class Block {
    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);

    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String data, String previousHash, long timeStamp) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
    }

    /**
     * TODO:
     * - We're starting with the default value of nonce here and incrementing it by one. But there are more
     * sophisticated strategies to start and increment a nonce in real-world applications.
     * <p>
     * - We're not verifying our data (often in the form of multiple transactions) here. A typical implementation of
     * blockchain sets a restriction on how much data can be part of a block. It also sets up rules on how a transaction
     * can be verified.
     */
    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix).equals(prefixString)) {
            nonce++;
            hash = calculateBlockHash();
        }
        return hash;
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash + timeStamp + nonce + data;

        MessageDigest digest;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("Failed to calculate block's hash: {}", ex.getMessage());
        }

        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }
}
