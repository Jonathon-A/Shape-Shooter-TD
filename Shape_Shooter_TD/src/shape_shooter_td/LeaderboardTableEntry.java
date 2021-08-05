package shape_shooter_td;

final public class LeaderboardTableEntry implements TableEntryInterface {

    private String Username;
    private final int Score;
    private final String GameMode;
    private final String Difficulty;
    private final String MapCode;

    public LeaderboardTableEntry(final String Username, final int Score, final String GameMode, final String Difficulty, final String MapCode) {
        //Creates new leaderboard table entry with specified parameters
        this.Username = Username;
        this.Score = Score;
        this.GameMode = GameMode;
        this.Difficulty = Difficulty;
        this.MapCode = MapCode;
    }

    public void setUsername(final String Username) {
        //Changes the username of this leaderboard table entry
        this.Username = Username;
    }

    @Override
    final public String getUsername() {
        //Returns the username of this leaderboard table entry
        return Username;
    }

    final public int getScore() {
        //Returns the score of this leaderboard table entry
        return Score;
    }

    final public String getGameMode() {
        //Returns the game mode of this leaderboard table entry
        return GameMode;
    }

    final public String getDifficulty() {
        //Returns the difficulty of this leaderboard table entry
        return Difficulty;
    }

    final public String getMapCode() {
        //Returns the map code of this leaderboard table entry
        return MapCode;
    }

}
