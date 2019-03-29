package org.landy.commons.core.utils.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Additionally, classes are provided only for those types that are commonly useful in intended applications.
 * For example, there is no atomic class for representing byte.
 * In those infrequent cases where you would like to do so, you can use an AtomicInteger to hold byte values, and cast appropriately. You can also hold floats using Float.floatToRawIntBits(float) andFloat.intBitsToFloat(int) conversions,
 * and doubles using Double.doubleToRawLongBits(double) andDouble.longBitsToDouble(long) conversions.
 */
public class AtomicFloat extends Number {

    private AtomicInteger bits;

    public AtomicFloat() {
        this(0f);
    }

    public AtomicFloat(float initialValue) {
        bits = new AtomicInteger(Float.floatToIntBits(initialValue));
    }

    public final float addAndGet(float delta){
        float expect;
        float update;
        do {
            expect = get();
            update = expect + delta;
        } while(!this.compareAndSet(expect, update));

        return update;
    }

    public final float getAndAdd(float delta){
        float expect;
        float update;
        do {
            expect = get();
            update = expect + delta;
        } while(!this.compareAndSet(expect, update));

        return expect;
    }

    public final float getAndDecrement(){
        return getAndAdd(-1);
    }

    public final float decrementAndGet(){
        return addAndGet(-1);
    }

    public final float getAndIncrement(){
        return getAndAdd(1);
    }

    public final float incrementAndGet(){
        return addAndGet(1);
    }

    public final float getAndSet(float newValue) {
        float expect;
        do {
            expect = get();
        } while(!this.compareAndSet(expect, newValue));

        return expect;
    }

    public final boolean compareAndSet(float expect, float update) {
        return bits.compareAndSet(Float.floatToIntBits(expect), Float.floatToIntBits(update));
    }

    public final void set(float newValue) {
        bits.set(Float.floatToIntBits(newValue));
    }

    public final float get() {
        return Float.intBitsToFloat(bits.get());
    }

    public float floatValue() {
        return get();
    }

    public double doubleValue() {
        return (double) floatValue();
    }

    public int intValue() {
        return (int) get();
    }

    public long longValue() {
        return (long) get();
    }

    public String toString() {
        return Float.toString(get());
    }
}