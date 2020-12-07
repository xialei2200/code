package vip.xialei.cas;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class DecimalAccountSafeCas {
    AtomicReference<BigDecimal> ref;

    public DecimalAccountSafeCas(BigDecimal balance) {
        ref = new AtomicReference<>(balance);
    }

    public BigDecimal getBalance() {
        return ref.get();
    }

    public void withdraw(BigDecimal amount) {
        while (true) {
            BigDecimal prev = ref.get();
            BigDecimal next = prev.subtract(amount);
            if (ref.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}

class AtomicStampedReferenceTest {
    public static void main(String[] args) {
        AtomicStampedReference<BigDecimal> stampedReference = new AtomicStampedReference<>(new BigDecimal(0), 0);
        while (true) {
            BigDecimal prev = stampedReference.getReference();
            int stamp = stampedReference.getStamp();
            if (stampedReference.compareAndSet(prev,new BigDecimal(2),stamp,stamp+1)) {
                break;
            }
        }
    }
}
