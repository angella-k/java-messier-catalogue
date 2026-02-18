import java.util.Comparator;

public class MessierObject implements Comparable<MessierObject> {

    // object information
    private final String messierNumber, ngcNumber, commonName, type, constellation;
    private final double distanceKly, apparentMagnitude, rightAscension, declination;

    // constructor method
    public MessierObject(String messierNumber, String ngcNumber, String commonName, String type, double distanceKly, String constellation, double apparentMagnitude, double rightAscension, double declination) {

        if (distanceKly < 0) {
            throw new IllegalArgumentException(
                    "Distance cannot be negative");
        }

        this.messierNumber = messierNumber;
        this.ngcNumber = ngcNumber;
        this.commonName = commonName;
        this.type = type;
        this.distanceKly = distanceKly;
        this.constellation = constellation;
        this.apparentMagnitude = apparentMagnitude;
        this.rightAscension = rightAscension;
        this.declination = declination;
    }

    // accessor/getter methods
    public String getMessierNumber() {
        return messierNumber;
    }

    public String getNgcNumber() {
        return ngcNumber;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getType() {
        return type;
    }

    public double getDistanceKly() {
        return distanceKly;
    }

    public String getConstellation() {
        return constellation;
    }

    public double getApparentMagnitude() {
        return apparentMagnitude;
    }

    public double getRightAscension() {
        return rightAscension;
    }

    public double getDeclination() {
        return declination;
    }

    // comparable interface
    // allows messier objects to be sorted in order of apparent magnitude
    @Override
    public int compareTo(MessierObject MessierObject2) {
        return Double.compare(apparentMagnitude, MessierObject2.apparentMagnitude);
    }

    // formats a double of right ascension hours into a string
    private String formatRightAscension(double rightAscensionHours) {
        int hours = (int) rightAscensionHours;
        double hourDecimal = rightAscensionHours - hours;
        hourDecimal = hourDecimal * 60;
        int minutes = (int) hourDecimal;
        double seconds = hourDecimal - minutes;
        seconds = seconds * 60;

        return String.format("%dh %dm %.4fs", hours, minutes, seconds);
    }

    // parses a string of right ascension hours into a double
    public static double parseRightAscension(String rightAscensionHours) {

        rightAscensionHours = rightAscensionHours.replace("h", " ");
        rightAscensionHours = rightAscensionHours.replace("m", " ");
        rightAscensionHours = rightAscensionHours.replace("s", " ");

        String[] part = rightAscensionHours.trim().split("\\s+");
        double hours = Double.parseDouble(part[0]);
        double minutes = Double.parseDouble(part[1]);
        double seconds = Double.parseDouble(part[2]);

        return hours + (minutes / 60) + (seconds / 3600);
    }

    // formats a double of declination angle into a string
    private String formatDeclination(double declinationAngle) {
        int degrees = (int) declinationAngle;
        double minuteDecimal = declinationAngle - degrees;
        minuteDecimal = minuteDecimal * 60;
        int arcMinutes = (int) minuteDecimal;
        double arcSeconds = minuteDecimal - arcMinutes;
        arcSeconds = arcSeconds * 60;

        return String.format("%d° %d' %.4f\"", degrees, arcMinutes, arcSeconds);
    }

    // parses a string of declination angle into a double
    public static double parseDeclinationAngle(String declinationAngle) {

        declinationAngle = declinationAngle.replace("°", " ");
        declinationAngle = declinationAngle.replace("'", " ");
        declinationAngle = declinationAngle.replace("\"", " ");

        String[] part = declinationAngle.trim().split("\\s+");
        double angle = Double.parseDouble(part[0]);
        double minutes = Double.parseDouble(part[1]);
        double seconds = Double.parseDouble(part[2]);

        double value = Math.abs(angle)
                + minutes / 60
                + seconds / 3600;

        if (angle < 0) {
            value = -value;
        }

        return value;
    }

    // produces a string representing objects in data file format
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %.1f, %s, %.1f, %s, %s", messierNumber, ngcNumber, commonName, type, distanceKly, constellation, apparentMagnitude, formatRightAscension(rightAscension), formatDeclination(declination) );
    }

    // comparator method for sorting lexicographically by constellation
    public static final Comparator<MessierObject> CONSTELLATION_COMPARATOR = Comparator.comparing(MessierObject::getConstellation);

}

