package GAME;

/**
 * Created by dot on 08-02-2016.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // IO stuff
    private static File data;
    private static Scanner sc;
    private static int temp = 0;
    // Player related stuff
    private static boolean hasPlayer;
    private static String name;
    private static int level;
    private static int[] stats = new int[3];
    // stats is an array of size 3, format is {atk, def, maxHP}
    private static int xp;
    private static String lastCommand;
    // Random other stuff
    private static int[] xpTable;

    private static boolean inStartMenu = true;

    public static void main(String args[]) throws IOException {

        // Build rooms
        final int WIDTH = 2;
        final int HEIGHT = 2;
        Room[][] room = new Room[WIDTH][HEIGHT];
        Rooms.build(room, WIDTH, HEIGHT);
        int x = 0;
        int y = 0;
        int score = 0;

        // Load inventory
        ArrayList<String> inventory = new ArrayList<>();

        // Title Screen
        System.out.println("+-------------------------------+");
        System.out.println("| TLRPG: The Little Text RPG    |");
        System.out.println("|            (\\_/)              |");
        System.out.println("|           (='.'=)             |");
        System.out.println("|          (\\\")_(\\\")            |");
        System.out.println("+-------------------------------+");

        Sounds.playTitleMusic();

        hasPlayer = false;
        getXP();

        sc = new Scanner(System.in);

        System.out.println("Enter Command. Type help to get a Help menu.");


        while(inStartMenu) {
            String menuChoice = Input.getInput();

            switch (menuChoice) {
                case "new":
                    newGame();
                    inStartMenu = false;
                    break;

                case "load":
                    System.out.println("What is the player's name?");
                    loadGame(sc.next());
                    System.out.println("Player was " + (hasPlayer == true ? "" : "not ") + "loaded");
                    inStartMenu = false;
                    break;

                case "quit":
                    System.exit(0);
                    break;

                case "help":

                    System.out.println("Type 'new' to start a new game");
                    System.out.println("Type 'load' to load a game");
                    System.out.println("Type 'quit' to quit the game");
                    System.out.println("Type 'help' get to the help menu.");
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }

        // THE ADVENTURE BEGINS
        System.out.println("And so your quest begins...");

        // Print starting room description
        Rooms.print(room, x, y);

        // Start game loop
        boolean playing = true;
        while (playing) {

            // Get user input
            String input = Input.getInput();

            //Last typed command explore
            if (input.length() == 0) {
                input = lastCommand;
            } else
                lastCommand = input;

            switch (input){

                //See and touch options
                case "look":
                    Rooms.print(room, x, y);
                    break;

                case "get":
                    if (Rooms.roomHasItems(room, x, y))
                    {
                        Rooms.itemPrint(room, x, y);

                        System.out.println("Type the name of the item you wish to pick up");

                        String getInput = Input.getInput();
                        String item = getInput.substring(getInput.indexOf(' ') + 1);
                        score = Inventory.checkItem(x, y, item, inventory, room, score);
                        break;
                    }
                    else {
                        System.out.println("There are no more objects to pick up");
                        break;
                    }

                //Movement
                case "n":
                    if (y > 0) {
                        y--;
                        Rooms.print(room, x, y);
                    } else {
                        System.out.println("You can't go that way.");
                    }
                    break;
                case "s":
                    if (y < HEIGHT - 1) {
                        y++;
                        Rooms.print(room, x, y);
                    } else {
                        System.out.println("You can't go that way.");
                    }
                    break;

                case "w":
                    if (x < WIDTH - 1) {
                        x++;
                        Rooms.print(room, x, y);
                    } else {
                        System.out.println("You can't go that way.");
                    }
                    break;
                case "e":
                    if (x > 0) {
                        x--;
                        Rooms.print(room, x, y);
                    } else {
                        System.out.println("You can't go that way.");
                    }
                    break;

                case "explore":
                    explore();
                    break;

                //Game options
                case "i":
                    Inventory.print(inventory);
                    break;

                case "save":
                    save(name);
                    break;

                case "stats":
                    dispStats();
                    break;

                case "score":
                    System.out.println("Score: " + score + "/500");
                    break;

                case "restart":
                    System.out.println();
                    Main.main(args);
                    break;

                case "help":
                    System.out.println("Type 'n'/'e'/'s'/'w' to move around");
                    System.out.println("Type 'look' for a description of the room you're in");
                    System.out.println("Type 'get' to pick up objects");
                    System.out.println("Type 'i' to view your inventory");
                    System.out.println("Type 'score' to view your score");
                    System.out.println("Type 'stats' to see your stats");
                    System.out.println("Type 'save' to save your progress");
                    System.out.println("Type 'explore' to search for an enemy");
                    System.out.println("Type 'restart' to restart the game");
                    System.out.println("Type 'quit' to quit the game");
                    break;

                case "quit":
                    System.out.println("Goodbye!");
                    playing = false;
                    break;

                default:
                    System.out.println("You can't do that.");
            }
        }
    }

    // starts a new game with lvl 0 player
    public static void newGame() {
        data = null;
        // we have to instantiate stats here...
        stats = new int[] { 2, 2, 10 };
        xp = 0;
        level = 0;
        name = "";
        System.out.print("What is your name? ==> ");
        name = sc.next();
        sc.nextLine();
        hasPlayer = true;
    }

    public static void save(String name) throws IOException {
        if (hasPlayer) {
            File playerData = new File(name);
            FileWriter fileWriter = new FileWriter(playerData, false);
            fileWriter.write(xp + "\n");
            for (int sn : stats) {
                fileWriter.write(sn + " ");
            }
            System.out.println("Saved");
            fileWriter.close();
        } else {
            System.out.println("Error 404 player not found");
        }
    }

    public static void loadGame(String Name) throws FileNotFoundException {

        // Stores a backup copy of stuff in case FileNotFound
        String tempName = name;
        name = Name;
        int tempXp = xp;

        // Reads file
        data = new File(name);
        if (!data.exists())
            return;
        Scanner Sc = new Scanner(data);
        try {
            xp = Sc.nextInt();
        } catch (Exception e) {
            Sc.close();
            return;
        }
        Sc.nextLine();
        String[] tempStatsArray = Sc.nextLine().split(" ");

        // Displays Stats
        System.out.println("Player: " + name);
        System.out.println("Total XP: " + xp);
        System.out.println("Stats: " + Arrays.toString(tempStatsArray));

        // Asks if player is correct
        System.out.println("Are you sure you want to load " + name + "? (y/n)");
        String temp = sc.next();
        if (temp.toLowerCase().charAt(0) == 'y') {
            try {
                stats[0] = Integer.parseInt(tempStatsArray[0]);
                stats[1] = Integer.parseInt(tempStatsArray[1]);
                stats[2] = Integer.parseInt(tempStatsArray[2]);
                System.out.println("Player " + name + " loaded");
                hasPlayer = true;
                levelUp();
            } catch (Exception e) {
                Sc.close();
                return;
            }
        } else {
            name = tempName;
            xp = tempXp;
        }

        // Finalizes the load
        Sc.close();
    }

    // creates xp table for level up purposes
    private static void getXP() {
        xpTable = new int[61];
        for (int w = 0; w < 61; w++) {
            xpTable[w] = (int) Math.pow(w, 3);
        }
    }

    private static String dispStats() {
        if (hasPlayer) {
            String temp = "Level: " + level + " HP: " + stats[2] + " Atk: " + stats[0] + " Def: " + stats[1]
                    + " XP to next level: " + (xp < 60 ? xpTable[level + 1] - xp : 0);
            System.out.println(temp);
            return "" + name + " " + level + " " + xp + temp;
        } else {
            System.out.println("No player found");
            return "";
        }
    }

    public static void levelUp() {
        while (xp >= xpTable[level]) {
            if (level != 60) {
                xp -= xpTable[level];
                System.out.println("Level up: " + level + " to " + ++level);
                stats[2] += 3; // increasing hp/atk/def
                stats[0] += 1;
                stats[1] += 1;
            } else {
                System.out.println("LEVEL MAXED");
                break;
            }
        }
    }

    public static int hit(Enemy enemy) {
        int dmg = stats[0] - enemy.getDef() / 2;
        return dmg;
    }

    public static void battle(Enemy en) {
        System.out.println("Enemy " + en.getName() + " found. Initating battle.");

        if (en.isSpecial()) {
            en.runScript();
        }
        {
            while (en.getHP() > 0 && stats[2] > 0) {

                int enhit = hit(en);
                int plhit = en.hit(level, stats[1]);

                en.changeHP(-1 * enhit);
                if (en.getHP() <= 0) {
                    System.out.println("You have defeated enemy " + en.getName());
                    xp += en.getXp();
                    System.out.println("You have gained: " + en.getXp() + " xp.");
                    levelUp();
                    break;
                } else {
                    if (plhit <= 0)
                        plhit = 0;
                    System.out.println("Enemy deals " + plhit + " damage!");
                    stats[2] -= plhit;
                    System.out.print("Player stats:");
                    dispStats();
                    System.out.println();
                    if (stats[2] <= 0) {
                        System.out.println("player " + name + " has died!");
                        hasPlayer = false;
                        break;
                    } else {
                        System.out.println("Player deals " + enhit + "damage!");
                        System.out.print("Enemy health:" + en.getHP());
                        System.out.println();
                    }
                }

            }
        }
    }

    public static void inBattle() {
        String cmd = sc.nextLine();
        while (!cmd.equals("exit")) {
            cmd = sc.nextLine();
        }
    }

    public static void explore() {
        if (hasPlayer && stats[2] > 0) {
            if (num(100) > 50) {
                Enemy enemy = new Enemy(level);
                battle(enemy);
            } else {
                System.out.println("You explored the area, but nothing was found");
            }
        } else {
            System.out.println("Player is dead. Sorry m8");
        }
    }

    public static int num(int n) {
        return (int) (Math.random() * n) + 1;
    }

}
