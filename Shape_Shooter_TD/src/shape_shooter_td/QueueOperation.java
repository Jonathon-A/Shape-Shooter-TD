package shape_shooter_td;

final public class QueueOperation {

    private final String Operation;
    private final UserAccountsTableEntry UserAccountsTableEntry;
    private final UserSettingsTableEntry UserSettingsTableEntry;
    private final LeaderboardTableEntry LeaderboardTableEntry;
    private final String StringData;

    public QueueOperation(String Operation, UserAccountsTableEntry UserAccountsTableEntry, UserSettingsTableEntry UserSettingsTableEntry, LeaderboardTableEntry LeaderboardTableEntry, String StringData) {
        this.Operation = Operation;
        this.UserAccountsTableEntry = UserAccountsTableEntry;
        this.UserSettingsTableEntry = UserSettingsTableEntry;
        this.LeaderboardTableEntry = LeaderboardTableEntry;
        this.StringData = StringData;
    }

    final public String getOperation() {
        //Return the operation that should be executed
        return Operation;
    }

    final public UserAccountsTableEntry getUserAccountsTableEntry() {
        //Returns the user accounts table entry that should be used with the operation
        return UserAccountsTableEntry;
    }

    final public UserSettingsTableEntry getUserSettingsTableEntry() {
        //Returns the user settings table entry that should be used with the operation
        return UserSettingsTableEntry;
    }

    final public LeaderboardTableEntry getLeaderboardTableEntry() {
        //Returns the leaderboard table entry that should be used with the operation
        return LeaderboardTableEntry;
    }

    final public String getStringData() {
        //Returns string that should be used with the operation, for example the username
        return StringData;
    }
}
