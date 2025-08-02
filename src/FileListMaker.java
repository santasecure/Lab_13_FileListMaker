import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.swing.JFileChooser;
import java.nio.file.*;



public class FileListMaker {
    // Flag to track if the list has unsaved changes
    private static boolean needsToBeSaved = false;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<>();
        String cmd;

        // Outer loop to process commands until the user quits
        do {
            displayList(list);         // Show current list
            displayMenu();             // Show menu

            // Get user input command
            cmd = SafeInput.getRegExString(in,
                    "Choose [A]dd, [D]elete, [I]nsert, [V]iew, [M]ove, [O]pen, [S]ave, [C]lear, or [Q]uit",
                    "[AaDdIiVvMmOoSsCcQq]").toUpperCase();

            // Handle commands
            try {
                switch (cmd) {
                    case "A":
                        addItem(list, in);
                        needsToBeSaved = true;
                        break;
                    case "D":
                        deleteItem(list, in);
                        needsToBeSaved = true;
                        break;
                    case "I":
                        insertItem(list, in);
                        needsToBeSaved = true;
                        break;
                    case "V":
                        displayList(list);
                        break;
                    case "M":
                        moveItem(list, in);
                        needsToBeSaved = true;
                        break;
                    case "O":
                        if (needsToBeSaved && SafeInput.getYNConfirm(in, "You have unsaved changes. Save before opening a new file?")) {
                            saveList(list, in);
                        }
                        list = loadList(in);
                        needsToBeSaved = false;
                        break;
                    case "S":
                        saveList(list, in);
                        needsToBeSaved = false;
                        break;
                    case "C":
                        if (SafeInput.getYNConfirm(in, "Are you sure you want to clear the list?")) {
                            list.clear();
                            needsToBeSaved = true;
                            System.out.println("List cleared.");
                        }
                        break;
                    case "Q":
                        if (needsToBeSaved && SafeInput.getYNConfirm(in, "You have unsaved changes. Save before quitting?")) {
                            saveList(list, in);
                        }
                        if (!SafeInput.getYNConfirm(in, "Are you sure you want to quit?")) {
                            cmd = "X"; // Cancel quit
                        }
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error during file operation: " + e.getMessage());
            }

        } while (!cmd.equals("Q"));

        System.out.println("Exiting program. Goodbye!");
    }

    // Display the command menu
    private static void displayMenu() {
        System.out.println("\nMenu Options:");
        System.out.println("[A] Add an item");
        System.out.println("[D] Delete an item");
        System.out.println("[I] Insert an item");
        System.out.println("[V] View the list");
        System.out.println("[M] Move an item");
        System.out.println("[O] Open a list from file");
        System.out.println("[S] Save the list to file");
        System.out.println("[C] Clear the list");
        System.out.println("[Q] Quit");
    }

    // Display the current list
    private static void displayList(ArrayList<String> list) {
        System.out.println("\nCurrent List:");
        if (list.isEmpty()) {
            System.out.println("   (List is empty)");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%3d: %s\n", i + 1, list.get(i));
            }
        }
    }

    // Add an item
    private static void addItem(ArrayList<String> list, Scanner in) {
        String item = SafeInput.getNonZeroLenString(in, "Enter item to add");
        list.add(item);
    }

    // Delete an item
    private static void deleteItem(ArrayList<String> list, Scanner in) {
        if (list.isEmpty()) {
            System.out.println("List is empty. Nothing to delete.");
            return;
        }
        int index = SafeInput.getRangedInt(in, "Enter item number to delete", 1, list.size());
        String removed = list.remove(index - 1);
        System.out.println("Removed: " + removed);
    }

    // Insert an item
    private static void insertItem(ArrayList<String> list, Scanner in) {
        int position = SafeInput.getRangedInt(in, "Enter position to insert at (1 to " + (list.size() + 1) + "):", 1, list.size() + 1);
        String item = SafeInput.getNonZeroLenString(in, "Enter item to insert");
        list.add(position - 1, item);
    }

    // Move an item
    private static void moveItem(ArrayList<String> list, Scanner in) {
        if (list.size() < 2) {
            System.out.println("Need at least two items to move.");
            return;
        }
        int from = SafeInput.getRangedInt(in, "Enter the item number to move", 1, list.size());
        int to = SafeInput.getRangedInt(in, "Enter new position for the item", 1, list.size());
        String item = list.remove(from - 1);
        list.add(to - 1, item);
        System.out.printf("Moved \"%s\" to position %d.%n", item, to);
    }

    // Save list to a file
    private static void saveList(ArrayList<String> list, Scanner in) throws IOException {
        String filename = SafeInput.getNonZeroLenString(in, "Enter filename to save (no extension)") + ".txt";
        Path file = Paths.get("src", filename);
        Files.write(file, list);
        System.out.println("List saved to " + file);
    }

    // Load list from a file
    private static ArrayList<String> loadList(Scanner in) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/src"));

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            Path file = chooser.getSelectedFile().toPath();
            ArrayList<String> list = new ArrayList<>(Files.readAllLines(file));
            System.out.println("List loaded from " + file);
            return list;
        } else {
            System.out.println("No file selected. Returning empty list.");
            return new ArrayList<>();
        }
    }
}
