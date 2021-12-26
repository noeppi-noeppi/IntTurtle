package io.github.noeppi_noeppi.intturtle.engine;

public interface CraftCodeInterface {

    void write(long value) throws IntCodeException;

    boolean canRead() throws IntCodeException;
    long read() throws IntCodeException;

    boolean canInvoke(long call, Memory memory) throws IntCodeException;
    void invoke(long call, Memory memory) throws IntCodeException;
}
