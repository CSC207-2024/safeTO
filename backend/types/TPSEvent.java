package backend.types;

import java.util.Date;

/**
 * TPSEvent
 */
public class TPSEvent {
    String eventUniqueID;
    Date occDate;
    int numDeath;
    int numInjuries;
    Location occLocation;
    String incidentType;
}