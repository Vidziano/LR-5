package Client;

import interfaces.Executable;
import java.math.BigInteger;
import java.io.Serializable;

public class JobOne implements Executable, Serializable {
    private static final long serialVersionUID = 1L;
    private int n;

    public JobOne(int n) {
        this.n = n;
    }

    @Override
    public Object execute() {
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
