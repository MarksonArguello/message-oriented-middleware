package br.com.marksonarguello.entities.queue;

public enum QueueType {
    PUB_SUB,
    P2P;

    public static QueueType fromString(String type) {
        System.out.println("QueueType.fromString: " + type);
   if (type != null && type.equals("POINT_TO_POINT")) {
           return P2P;
       }

         return PUB_SUB;
    }
}
