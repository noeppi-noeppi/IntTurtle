package io.github.noeppi_noeppi.intturtle.engine;

public interface CraftCodeInterface {

    void write(long value);

    boolean canRead();
    long read();

    boolean canInvoke(long call, Memory memory);
    void invoke(long call, Memory memory);
}
