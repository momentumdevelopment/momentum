package me.linus.momentum.util.client.system;

/**
 * @author linustouchtips
 * @since 11/26/2020
 */

public class Timer {
    private long time;

    public Timer() {
        this.time = -1L;
    }

    public long getTime(final long time) {
        return time / 1000000L;
    }

    public boolean passed(final long ms) {
        return this.getMs(System.nanoTime() - this.time) >= ms;
    }

    public void reset() {
        this.time = System.nanoTime();
    }

    public boolean sleep(final long time) {
        if (time() >= time) {
            reset();
            return true;
        }

        return false;
    }

    public long time() {
        return System.nanoTime() / 1000000L - time;
    }

    public long getMs(final long time) {
        return time / 1000000L;
    }

}

