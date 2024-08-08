package br.com.marksonarguello.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class IdUtil {
    private static final Set<String> usedIds = new HashSet<>();

    private IdUtil() {

    }

    public static String newId() {
        String uniqueID;
        do {
            uniqueID = UUID.randomUUID().toString();
        } while (usedIds.contains(uniqueID));

        usedIds.add(uniqueID);
        return uniqueID;
    }
}
