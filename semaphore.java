class semaphore
{
    protected int value = 0;

    // Default constructor
    protected semaphore() {
        value = 0;
    }

    // Constructor with an initial value
    protected semaphore(int initial) {
        value = initial;
    }

    // P (wait) operation
    public synchronized void P() {
        value--;
        if (value < 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // V (signal) operation
    public synchronized void V() {
        value++;
        if (value <= 0) {
            notify();
        }
    }
}
