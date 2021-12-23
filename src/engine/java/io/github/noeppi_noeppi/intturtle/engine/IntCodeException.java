package io.github.noeppi_noeppi.intturtle.engine;

public class IntCodeException extends Exception {
    
    public final long at;
    
    public IntCodeException(String message, long at) {
        super(message);
        this.at = at;
    }
}
