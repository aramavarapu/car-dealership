package com.pluralsight;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
public class UserInterface {

    private Dealership dealership;
    private DealershipFileManager fileManager;
    private final Scanner scanner = new Scanner(System.in);

    public UserInterface() {
    }

    public void display() {
        init();

        boolean running = true;
        while (running) {
            displayMenu();

            int command;
            try {
                command = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number from the menu.");
                continue;
            }
            switch (command) {
                case 1:
                    processGetByPriceRequest();
                    break;
                case 2:
                    processGetByMakeModelRequest();
                    break;
                case 3:
                    processGetByYearRequest();
                    break;
                case 4:
                    processGetByColorRequest();
                    break;
                case 5:
                    processGetByMileageRequest();
                    break;
                case 6:
                    processGetByVehicleTypeRequest();
                    break;
                case 7:
                    processGetAllVehiclesRequest();
                    break;
                case 8:
                    processAddVehicleRequest();
                    break;
                case 9:
                    processRemoveVehicleRequest();
                    break;
                case 99:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        }
    }

    private void init() {
        fileManager = new DealershipFileManager();
        this.dealership = fileManager.getDealership();
    }

    public void displayMenu() {
        System.out.println();
        System.out.println("=== Dealership Menu ===");
        System.out.println("1 - Find vehicles within a price range");
        System.out.println("2 - Find vehicles by make / model");
        System.out.println("3 - Find vehicles by year range");
        System.out.println("4- Find vehicles by color");
        System.out.println("5 - Find vehicles by mileage range");
        System.out.println("6 - Find vehicles by type (car, truck, SUV, and van)");
        System.out.println("7 - List ALL vehicles");
        System.out.println("8 - Add a vehicle");
        System.out.println("9 - Remove a vehicle");
        System.out.println("99 - Quit");
        System.out.print("Enter your choice: ");
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles to display.");
            return;
        }

        System.out.printf("%-8s %-6s %-10s %-12s %-8s %-8s %-10s %10s%n",
                "VIN", "Year", "Make", "Model", "Type", "Color", "Mileage", "Price");
        System.out.println("-".repeat(80));

        for (Vehicle vehicle : vehicles) {
            System.out.printf("%-8d %-6d %-10s %-12s %-8s %-8s %-10d %10s%n", vehicle.getVin(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel(), vehicle.getVehicleType(), vehicle.getColor(), vehicle.getOdometer(), "$" + vehicle.getPrice());
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a whole number");
            }
        }
    }

    private BigDecimal readPrice(String prompt) {
        while (true) {
            try {
                return new BigDecimal(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid amount like, 7777.77");
            }
        }
    }

    public void processGetByPriceRequest() {
        BigDecimal min = readPrice("Minimum Price: ");
        BigDecimal max = readPrice("Maximum Price: ");
        displayVehicles(dealership.getVehiclesByPrice(min, max));
    }

    public void processGetByMakeModelRequest() {
        String make = readText("Make: ");
        String model = readText("Model: ");
        displayVehicles(dealership.getVehiclesByMakeModel(make, model));
    }

    public void processGetByYearRequest() {
        int min = readInt("Minimum Year: ");
        int max = readInt("Maximum Year: ");
        displayVehicles(dealership.getVehiclesByYear(min, max));
    }

    public void processGetByColorRequest() {
        String color = readText("Color: ");
        displayVehicles(dealership.getVehiclesByColor(color));
    }

    public void processGetByMileageRequest() {
        int min = readInt("Minimum Mileage: ");
        int max = readInt("Maximum Mileage: ");
        displayVehicles(dealership.getVehiclesByMileage(min, max));
    }

    public void processGetByVehicleTypeRequest() {
        String type = readText("Vehicle type (car, truck, SUV, and van)");
        displayVehicles(dealership.getVehiclesByType(type));
    }

    public void processAddVehicleRequest() {
        int vin = readInt("VIN: ");
        int year = readInt("Year: ");
        String make = readText("Make: ");
        String model = readText("Model: ");
        String type = readText("Vehicle type (car, truck, SUV, van): ");
        String color = readText("Color: ");
        int odometer = readInt("Odometer: ");
        BigDecimal price = readPrice("Price: ");

        dealership.addVehicle(new Vehicle(vin, year, make, model, type, color, odometer, price));
        fileManager.saveDealership(dealership);
        System.out.println("Vehicle added.");
    }

    public void processRemoveVehicleRequest() {
        int vin = readInt("VIN of the vehicle to remove: ");

        Vehicle match = null;
        for (Vehicle vehicle : dealership.getAllVehicles()) {
            if (vehicle.getVin() == vin) {
                match = vehicle;
                break;
            }
        }

        if (match == null) {
            System.out.println("No vehicle found with that VIN.");
            return;
        }

        dealership.removeVehicle(match);
        fileManager.saveDealership(dealership);
        System.out.println("Vehicle removed.");
    }

    public void processGetAllVehiclesRequest() {
        List<Vehicle> vehicles = dealership.getAllVehicles();
        displayVehicles(vehicles);
    }
}
