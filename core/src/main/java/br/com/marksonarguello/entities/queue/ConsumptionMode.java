package br.com.marksonarguello.entities.queue;

public enum ConsumptionMode {
    PULL,
    PUSH;

    public static ConsumptionMode fromString(String queueMode) {
        if (queueMode != null && queueMode.equals("PUSH")) {
            return PUSH;
        }

        return PULL;
    }
}
