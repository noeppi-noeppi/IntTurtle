package io.github.noeppi_noeppi.intturtle.engine;

public class IntCodeException extends Exception {
    
    public final long at;
    
    public IntCodeException(String message) {
        this(message, Long.MIN_VALUE);
    }
    
    public IntCodeException(IntCodeException parent, long at) {
        super(parent.getMessage(), parent);
        this.at = at;
    }
    
    public IntCodeException(String message, long at) {
        super(message);
        this.at = at;
    }
}
