package backend.types;

import java.util.Optional;

public class Location {
    double latitude;
    double longitude;
    Optional<String> postalCode;
    Optional<String> address;
}
