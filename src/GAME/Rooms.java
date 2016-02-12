package GAME;

/**
 * Created by dot on 09-02-2016.
 */
import java.util.ArrayList;

class Rooms {

    public static void build(Room[][] room, final int WIDTH, final int HEIGHT) {

        // Initialize rooms
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                room[i][j] = new Room(i, "", "", null);
            }
        }

        room[0][0].setNumber(1);
        room[0][0].setName("House");
        room[0][0].setDescription("You are in your house.");
        room[0][0].setItems("sword");
        room[0][0].setItems("shield");

        room[0][1].setNumber(2);
        room[0][1].setName("Town");
        room[0][1].setDescription("You are in Town. At the moment it looks fairly deserted");
        room[0][1].setItems("tumbleweed");
        room[0][1].setItems("trash");

        room[1][0].setNumber(3);
        room[1][0].setName("Road");
        room[1][0].setDescription("You walk the road out of town.");
        room[1][0].setItems("tumbleweed");
        room[1][0].setItems("city sign");

        room[1][1].setNumber(4);
        room[1][1].setName("Forrest");
        room[1][1].setDescription("You are entering the forrest, insert spooky noise.");
        room[1][1].setItems("branches");
        room[1][1].setItems("leaves");
        room[1][1].setItems("bees!");
    }

    public static void print(Room[][] room, int x, int y) {

        System.out.println();
        System.out.println(room[x][y].getDescription());
        System.out.println("You see: " + room[x][y].getItems());
    }

    public static void itemPrint(Room[][] room, int x, int y) {

        System.out.println();
        System.out.println("You see: " + room[x][y].getItems());
    }

    public static boolean roomHasItems(Room[][] room, int x, int y) {
        if (room[x][y].getItems() != null)
        {
            return true;
        }
        else{
            return false;
        }
    }

    // Remove item from room when added to inventory
    public static void removeItem(Room[][] room, int x, int y, String item) {

        room[x][y].deleteItem(item);
    }
}

class Room {

    private int number;
    private String name;
    private String description;
    public ArrayList<String> items = new ArrayList<>();

    public Room(int number, String name, String description,
                ArrayList<String> items) {
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setItems(String item) {
        this.items.add(item);
    }

    public void deleteItem(String item) {
        this.items.remove(item);
    }

    public ArrayList<String> getItems() {
        return this.items;
    }
}
