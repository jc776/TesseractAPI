package tesseract.api.gt;

import tesseract.api.Consumer;
import tesseract.graph.Path;

import java.util.Comparator;

import static java.lang.Integer.compare;

/**
 * A class that acts as a container for an electrical consumer.
 */
public class GTConsumer extends Consumer<IGTCable, IGTNode> {

    private int loss;
    private int minVoltage = Integer.MAX_VALUE;
    private int minAmperage = Integer.MAX_VALUE;

    // Way of the sorting by the loss and the distance to the node
    public static final Comparator<GTConsumer> COMPARATOR = (t1, t2) -> (t1.getDistance() == t2.getDistance()) ? compare(t1.getLoss(), t2.getLoss()) : compare(t1.getDistance(), t2.getDistance());

    /**
     * Creates instance of the consumer.
     *
     * @param consumer The consumer node.
     * @param path The path information.
     */
    protected GTConsumer(IGTNode consumer, Path<IGTCable> path) {
        super(consumer, path);
        init();
    }

    /**
     * Adds energy to the node. Returns quantity of energy that was accepted.
     *
     * @param maxReceive Amount of energy to be inserted.
     * @param simulate If true, the insertion will only be simulated.
     */
    public void insert(long maxReceive, boolean simulate) {
        node.insert(maxReceive, simulate);
    }

    /**
     * @return Gets the amperage required for the consumer.
     */
    public int getRequiredAmperage(int voltage) {
        return (int) Math.min(((node.getCapacity() - node.getEnergy())) / voltage, node.getInputAmperage());
    }

    /**
     * @return Returns the priority of this node as a number.
     */
    public int getPriority() {
        return 0;
    }

    /**
     * @return Gets the total loss for the given consumer.
     */
    public int getLoss() {
        return loss;
    }

    /**
     * @param voltage The current voltage.
     *
     * @return Checks that the consumer is able to receive energy.
     */
    public boolean canHandle(int voltage) {
        return minVoltage >= voltage;
    }

    public boolean canHandleAmp(int minAmperage) {
        return this.minAmperage >= minAmperage;
    }

    /**
     * Copy the data from another consumer instance.
     *
     * @param consumer An another consumer.
     */
    public void copy(GTConsumer consumer) {
        loss = consumer.loss;
        full = consumer.full;
        cross = consumer.cross;
        minVoltage = consumer.minVoltage;
        minAmperage = consumer.minAmperage;
    }

    @Override
    protected void onConnectorCatch(IGTCable cable) {
        loss += cable.getLoss();
        minVoltage = Math.min(minVoltage, cable.getVoltage());
        minAmperage = Math.min(minAmperage, cable.getAmps());
    }

    public static class State {
        int ampsReceived;
        int ampsSent;
        long euReceived;
        long euSent;
        public final IGTNode handler;

        public State(IGTNode handler) {
            ampsReceived = 0;
            euReceived = 0;
            this.handler = handler;
        }

        public void onTick() {
            ampsReceived = 0;
            euReceived = 0;
            ampsSent = 0;
            euSent = 0;
        }

        public boolean extract(boolean simulate, int amps, long eu) {
            if (handler.canOutput()) {
                if (simulate) {
                    return ampsSent+amps <= handler.getOutputAmperage();
                }
                if (ampsSent+amps > handler.getInputAmperage()) {
                    return false;
                }
            }
            ampsSent += amps;
            euSent += eu;
            return true;
        }

        public boolean receive(boolean simulate, int amps, long eu) {
            if (handler.canInput()) {
                if (simulate) {
                    return ampsReceived+amps <= handler.getInputAmperage();
                }
                if (ampsReceived+amps > handler.getInputAmperage()) {
                    return false;
                }
            }
            ampsReceived += amps;
            euReceived += eu;
            return true;
        }
    }
}
