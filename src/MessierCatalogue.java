import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessierCatalogue {

    // member variable
    private final List<MessierObject> catalogue;

    // constructor
    public MessierCatalogue() {
        catalogue = new ArrayList<>();
    }

    // adds messier object to catalogue
    public void add(MessierObject object) {
        catalogue.add(object);
    }

    // returns a list of messier objects in the catalogue
    public ArrayList<MessierObject> getCatalogue() {
        return new ArrayList<>(catalogue);
    }

    // toString of catalogue where every object is on a new line
    @Override
    public String toString() {
        StringBuilder list = new StringBuilder();

        for (MessierObject object : catalogue) {
            list.append(object.toString());
            list.append("\n");
        }

        return list.toString();
    }

    // helper method
    private ArrayList<String> splitLine(String line) {

        ArrayList<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            // toggle quotes
            if (c == '"') {
                insideQuotes = !insideQuotes;
            }
            // split only if comma outside quotes
            else if (c == ',' && !insideQuotes) {
                parts.add(current.toString().trim());
                current.setLength(0);
                continue;
            }

            current.append(c);
        }

        // add last field
        parts.add(current.toString().trim());
        return parts;
    }

    // loading messier objects from the file
    public void fileLoading(String fileName) {

        // reading file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = reader.readLine()) != null) {

                // splitting information at every comma
                ArrayList<String> part = splitLine(line);

                if (part.size() != 9) {
                    // testing for lines that don't work
                    System.out.println("Skipped line: " + line);
                    continue;
                }

                // assigning each part into the messier information
                String messierNumber = part.get(0).trim();
                String ngcNumber = part.get(1);
                String commonName = part.get(2);
                String type = part.get(3);

                // calculates average of the range values to allow them to print as a double
                String distKlyString = part.get(4);
                double distanceKly;
                if (distKlyString.contains("-")) {
                    String[] range = distKlyString.split("-");
                    double minimum = Double.parseDouble(range[0]);
                    double maximum = Double.parseDouble(range[1]);
                    distanceKly = (minimum + maximum) / 2;
                } else {
                    distanceKly = Double.parseDouble(distKlyString);
                }

                String constellation = part.get(5);
                double apparentMagnitude = Double.parseDouble(part.get(6));

                // using parse method to allow the right ascension and declination angle to be printed as doubles
                double rightAscension = MessierObject.parseRightAscension(part.get(7));
                double declination = MessierObject.parseDeclinationAngle(part.get(8));

                // creating a new messier object with these values taken from the file
                MessierObject object = new MessierObject(messierNumber,
                        ngcNumber, commonName, type, distanceKly,
                        constellation, apparentMagnitude,
                        rightAscension, declination
                );

                // adds messier object to catalogue and loops to next one
                catalogue.add(object);
            }

        } catch (IOException e) {
            System.out.println("Error reading " + e.getMessage());
        }
    }

    // sorting catalogue lexicographically by constellation
    public void sortByConstellation() {
        catalogue.sort(MessierObject.CONSTELLATION_COMPARATOR);
    }

    // average apparent magnitude of globular clusters
    public double averageMagOfGlobularClusters() {

        double result = 0;
        int count = 0;

        for (MessierObject object : catalogue) {
            if (object.getType().trim().equalsIgnoreCase("Globular Cluster")) {
                result = result + object.getApparentMagnitude();
                count++;
            }
        }

        // throw exception
        if (count == 0) {
            return 0;
        }

        return result / count;
    }

    // closest open cluster
    public MessierObject findClosestOpenCluster() {

        double smallestDistance = 1000;
        MessierObject closest = null;

        for (MessierObject object : catalogue) {
            if (object.getType().trim().equalsIgnoreCase("Open cluster")) {
                if (object.getDistanceKly() < smallestDistance) {
                    smallestDistance = object.getDistanceKly();
                    closest = object;
                }
            }
        }
        return closest;
    }

    // Sagittarius object with the highest declination
    public MessierObject sagittariusHighestDec() {

        double highestDeclination = Double.NEGATIVE_INFINITY;
        MessierObject highest = null;

        for (MessierObject object : catalogue) {
            if (object.getConstellation().trim().equalsIgnoreCase("Sagittarius")) {
                if (object.getDeclination() > highestDeclination) {
                    highestDeclination = object.getDeclination();
                    highest = object;
                }
            }
        }
        return highest;
    }

    // the closest object to M45 by angular distance
    public MessierObject closestToM45() {

        MessierObject M45 = null;
        double smallestDistance = Double.POSITIVE_INFINITY;
        MessierObject closest = null;

        // find m45 in the catalogue
        for (MessierObject object : catalogue) {
            if (object.getMessierNumber().trim().equalsIgnoreCase("M45")) {
                M45 = object;
                break;
            }
        }

        if (M45 == null) {
            return null;
        }

        // convert into radians
        double rightAscension45 = Math.toRadians(M45.getRightAscension() * 15);
        double declination45 = Math.toRadians(M45.getDeclination());

        // loop through each messier object to find closest
        for (MessierObject object2 : catalogue) {
            // skip M45
            if (object2 == M45) continue;

            // convert to radians
            double distance = getDistance(object2, declination45, rightAscension45);

            if (distance < smallestDistance) {
                smallestDistance = distance;
                closest = object2;
            }
        }
        return closest;
    }

    private static double getDistance(MessierObject object2, double declination45, double rightAscension45) {
        double rightAscension = Math.toRadians(object2.getRightAscension() * 15);
        double declination = Math.toRadians(object2.getDeclination());

        // angular distance formula
        // sin(d)sin(d2) + cos(d)cos(d2)cos(a-a2)
        double cosDistance = Math.sin(declination45) * Math.sin(declination)
                + Math.cos(declination45) * Math.cos(declination) * Math.cos(rightAscension45 - rightAscension);

        // convert back to degrees and clamping
        cosDistance = Math.max(-1.0, Math.min(1.0, cosDistance));
        return Math.toDegrees(Math.acos(cosDistance));
    }


}
