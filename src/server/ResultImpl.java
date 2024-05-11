package server;

import interfaces.Result;
import java.io.Serializable;

public class ResultImpl implements Result, Serializable {
    private Object output;
    private double scoreTime;

    public ResultImpl(Object output, double scoreTime) {
        this.output = output;
        this.scoreTime = scoreTime;
    }

    @Override
    public Object output() {
        return output;
    }

    @Override
    public double scoreTime() {
        return scoreTime;
    }
}
