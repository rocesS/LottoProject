package pl.domain.numberreceiver;

import static org.junit.jupiter.api.Assertions.*;

class HashGeneratorTestImpl implements HashGenerable {

    private final String hash;

    HashGeneratorTestImpl(String hash) {
        this.hash = hash;
    }

    public HashGeneratorTestImpl() {
        this.hash = "123";
    }

    @Override
    public String getHash() {
        return hash;
    }
}