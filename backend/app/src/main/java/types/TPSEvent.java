package backend.types;

import java.util.Date;

public class TPSEvent {
    String eventUniqueID;
    Date occDate;
    int numDeath;
    int numInjuries;
    Location occLocation;
    String incidentType;
}