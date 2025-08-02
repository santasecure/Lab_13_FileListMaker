import java.util.ArrayList;
import java.util.Scanner;

public class FileListMaker {
    private static boolean needsToBeSaved = false;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<>();
        String cmd;

        do {
            displayList(list);
            displayMenu();

            cmd = SafeInput.getRegExString(in,
                    "Choose [A]dd, [D]elete, [I]nsert, [V]iew, [M]ove, [O]pen, [S]ave, [C]lear, or [Q]uit",
                    "[AaDdIiVvMmOoSsCcQq]").toUpperCase();

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
                    if (needsToBeSaved) {
                        if (SafeInput.getYNConfirm(in, "You have unsaved changes. Save before opening new file?")) {
                            // Save logic will go here later
                        }
                    }
                    // Open logic will go here later
                    break;
                case "S":
                    // Save logic will go here later
                    break;
                case "C":
                    if (list.size() > 0 && SafeInput.getYNConfirm(in, "Clear the entire list?")) {
                        list.clear();
                        needsToBeSaved = true;
                        System.out.println("List cleared.");
                    }
                    break;
                case "Q":
                    if (needsToBeSaved) {
                        if (SafeInput.getYNConfirm(in, "You have unsaved changes. Save before quitting?")) {
                            // Save logic will go here later
                        }
                    }
                    if (!SafeInput.getYNConfirm(in, "Are you sure you want to quit?")) {
                        cmd = "X"; // cancel quit
                    }
                    break;
            }

        } while (!cmd.equals("Q"));
    }

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

    private static void displayList(ArrayList<String> list) {
        System.out.println("\nCurrent List:");
        if (list.size() == 0) {
            System.out.println("   (List is empty)");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%3d: %s\n", i + 1, list.get(i));
            }
        }
        System.out.println();
    }

    private static void addItem(ArrayList<String> list, Scanner in) {
        String item = SafeInput.getNonZeroLenString(in, "Enter item to add");
        list.add(item);
    }

    private static void deleteItem(ArrayList<String> list, Scanner in) {
        if (list.size() == 0) {
            System.out.println("The list is empty. Nothing to delete.");
            return;
        }

        displayList(list);
        int choice = SafeInput.getRangedInt(in, "Enter the number of the item to delete", 1, list.size());
        String removed = list.remove(choice - 1);
        System.out.printf("\"%s\" has been removed.\n", removed);
    }

    private static void insertItem(ArrayList<String> list, Scanner in) {
        int position = SafeInput.getRangedInt(in,
                "Enter the position to insert the new item (1 to " + (list.size() + 1) + "):",
                1, list.size() + 1);
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to insert");
        list.add(position - 1, item);
    }

    private static void moveItem(ArrayList<String> list, Scanner in) {
        if (list.size() < 2) {
            System.out.println("You need at least two items in the list to move one.");
            return;
        }

        int from = SafeInput.getRangedInt(in, "Enter the item number to move", 1, list.size());
        int to = SafeInput.getRangedInt(in, "Enter the new position for the item", 1, list.size());

        String item = list.remove(from - 1);
        list.add(to - 1, item);

        System.out.printf("Moved \"%s\" to position %d.\n", item, to);
    }
}
