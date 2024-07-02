package types;

import java.util.Date;
import location.Location;

public class TPSEvent {
    String eventUniqueID;
    Date occDate;
    int numDeath;
    int numInjuries;
    Location occLocation;
    String incidentType;
}