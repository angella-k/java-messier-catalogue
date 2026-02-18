import java.io.*;
import java.util.Scanner;

public class MessierProgram {

    public static void main(String[] args) {

        MessierCatalogue program = new MessierCatalogue();

        program.fileLoading("messier.txt");

        // System.out.println(program.catalogue);

        System.out.printf("\nThe average apparent magnitude of globular clusters is ~%.3f%n", program.averageMagOfGlobularClusters());

        System.out.println("\nThe details of the least distant open cluster are: " + program.findClosestOpenCluster());

        System.out.println("\nThe details of the Sagittarius object with the highest declination are: " + program.sagittariusHighestDec());

        System.out.println("\nThe details of the closest Messier Object to M45 are: " + program.closestToM45());

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nDo you want to print the entire catalogue sorted by constellation? (Y/N)");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Y")) {
            program.sortByConstellation();
            for (MessierObject obj : program.getCatalogue()) {
                System.out.println(obj);
            }
        } else {
            System.out.println("Catalogue not printed.");
        }
        scanner.close();

    }
}
