package model;

public class Player {
    private final String name;
    private final int id; // Unique identifier for the player

    public static Player PLAYER1;
    public static Player PLAYER2;

    private Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static void initializePlayers(String name1, String name2) {
        if (PLAYER1 == null && PLAYER2 == null) {
            PLAYER1 = new Player(name1, 1);
            PLAYER2 = new Player(name2, 2);
        } else {
            throw new IllegalStateException("Players are already initialized.");
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return name;
    }
}