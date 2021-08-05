package shape_shooter_td;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

final public class Shape_Shooter_TD_Database {

    private UserAccountsTableEntry CurrentUserAccountEntry = null;
    private UserSettingsTableEntry CurrentUserSettingsEntry = null;
    private ArrayList<LeaderboardTableEntry> LeaderboardEntriesArray = null;

    private Connection Connection;
    private Statement Stmt;
    private PreparedStatement PrepStmt;
    private ResultSet RS;

    final public UserAccountsTableEntry getCurrentUserAccountEntry() {
        //Returns the current user's user account entry
        return CurrentUserAccountEntry;
    }

    final public UserSettingsTableEntry getCurrentUserSettingsEntry() {
        //Returns the current user's user settings entry
        return CurrentUserSettingsEntry;
    }

    final public ArrayList<LeaderboardTableEntry> getLeaderboardEntriesArray() {
        //Returns an array of all leaderboard entries
        return LeaderboardEntriesArray;
    }

    final public boolean OpenConnections() {
        /*
        This method attempts to connect to the database
        It is called whenever the database needs to be accessed
        Returns whether or no it successfully connected
         */

        boolean SuccessfulConnection;
        try {
            //Connects to the database
            Connection = DriverManager.getConnection("jdbc:derby://localhost:1527/Shape_Shooter_TD_Database", "Shape_Shooter_TD_DatabaseUsername", "Shape_Shooter_TD_DatabasePassword");
            Stmt = Connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //Successfully connected to the database
            SuccessfulConnection = true;
        } catch (SQLException e) {
            //Unsuccessfully connected to the database
            SuccessfulConnection = false;
            System.out.println("SQL open connections error: " + e);
        }
        //Returns whether or no it successfully connected to the database
        return SuccessfulConnection;
    }

    private PreparedStatement GetPreparedStatement(final String SQL) {
        /*
        This method compiles the given SQL statement so that only the parameters need to be supplied
        This is done to prevent SQL injection attacks
        It then returns the prepared statement
         */

        PreparedStatement NewPrepStmt = null;
        try {
            //Creates prepared statement from given SQL statement
            NewPrepStmt = Connection.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            System.out.println("SQL get prepared statement error: " + ex);
        }
        //Returns the prepared statement
        return NewPrepStmt;
    }

    final public boolean FindAccountWithUsername(final String Username) {
        /*
        This method check if a user account entry already exists with specified username
        It then returns whether or not it found an entry
         */

        //Existing entry is not found 
        boolean AccountEntryFound = false;
        try {
            //If successfully connected to database 
            if (OpenConnections()) {
                //Finds user account entry with specified username inside user accounts table
                final String SQL = "Select * From app.TBL_UserAccountsTable WHERE Username=? FETCH NEXT 1 ROWS ONLY";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, Username);
                RS = PrepStmt.executeQuery();
                //If entry is found
                if (RS.next()) {
                    //Existing entry found
                    AccountEntryFound = true;
                }
            }//If unsuccessful connected to database
            else {
                System.out.println("Please connect to database");
            }
            //Closes connection to database
            CloseConnections();

        } catch (SQLException e) {
            System.out.println("SQL find account with username error: " + e);
        }
        //Returns whether or not it found an entry
        return AccountEntryFound;
    }

    final public boolean FindAccountWithEmailAddress(final String EmailAddress) {
        /*
        This method check if a user account entry already exists with specified email address
        It then returns whether or not it found an entry
         */

        //Existing entry is not found 
        boolean AccountEntryFound = false;
        try {
            //If successfully connected to database  
            if (OpenConnections()) {
                //Finds user account entry with specified email address inside user accounts table
                final String SQL = "Select * From app.TBL_UserAccountsTable WHERE EmailAddress=? FETCH NEXT 1 ROWS ONLY";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, Encrypt(EmailAddress, false));
                RS = PrepStmt.executeQuery();
                //If entry is found
                if (RS.next()) {
                    //Existing entry found
                    AccountEntryFound = true;
                }
            }//If unsuccessful connected to database
            else {
                System.out.println("Please connect to database");
            }
            //Closes connection to database
            CloseConnections();

        } catch (SQLException e) {
            System.out.println("SQL find account with email address error: " + e);
        }
        //Returns whether or not it found an entry
        return AccountEntryFound;
    }

    final public void FindCurrentUserAccountEntryFromUsername(final String Username) {
        /*
        This method retrieves the user account entry from the user accounts table with the specified username
         */

        //If username is blank
        if (Username.equals("")) {
            //Sets current user account entry to nothing 
            CurrentUserAccountEntry = null;
        } else {
            String Password;
            String EmailAddress;
            try {
                //If successfully connected to database  
                if (OpenConnections()) {
                    //Finds user account entry with specified username inside user accounts table
                    final String SQL = "Select * From app.TBL_UserAccountsTable WHERE Username=? FETCH NEXT 1 ROWS ONLY";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, Username);
                    RS = PrepStmt.executeQuery();
                    //If entry found
                    if (RS.next()) {
                        //Retrieves entry from user accounts table in database
                        Password = RS.getString("Password");
                        EmailAddress = RS.getString("EmailAddress");
                        //Sets the username that will be used to derive an encryption key
                        SetUsernameForKey(Username);
                        //Sets current user account entry to nothing 
                        CurrentUserAccountEntry = new UserAccountsTableEntry(Username, Decrypt(Password, true), Decrypt(EmailAddress, false));
                    } //If entry not found
                    else {
                        //Sets all current user account entry to nothing 
                        CurrentUserAccountEntry = null;
                    }
                }//If unsuccessful connected to database  
                else {
                    System.out.println("Please connect to database");
                    CurrentUserAccountEntry = null;
                }
                //Closes connection to database
                CloseConnections();

            } catch (SQLException e) {
                //Sets current user account entry to nothing 
                System.out.println("SQL find current user account entry from username error: " + e);
                CurrentUserAccountEntry = null;
            }
        }
    }

    final public void FindCurrentUserAccountEntryFromEmailAddress(final String EmailAddress) {
        /*
        This method retrieves the user account entry from the user accounts table with the specified email address
         */

        //If email address is blank
        if (EmailAddress.equals("")) {
            //Sets current user account entry to nothing 
            CurrentUserAccountEntry = null;
        } else {
            String Username;
            String Password;
            try {
                //If successfully connected to database  
                if (OpenConnections()) {
                    //Finds user account entry with specified email address inside user accounts table
                    final String SQL = "Select * From app.TBL_UserAccountsTable WHERE EmailAddress=? FETCH NEXT 1 ROWS ONLY";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, Encrypt(EmailAddress, false));
                    RS = PrepStmt.executeQuery();
                    //If entry found
                    if (RS.next()) {
                        //Retrieves entry from user accounts table in database
                        Username = RS.getString("Username");
                        Password = RS.getString("Password");
                        //Sets the username that will be used to derive an encryption key
                        SetUsernameForKey(Username);
                        CurrentUserAccountEntry = new UserAccountsTableEntry(Username, Decrypt(Password, true), EmailAddress);
                    }//If entry not found
                    else {
                        //Sets all current user account entry to nothing 
                        CurrentUserAccountEntry = null;
                    }
                }//If unsuccessful connected to database  
                else {
                    System.out.println("Please connect to database");
                    CurrentUserAccountEntry = null;
                }
                //Closes connection to database
                CloseConnections();

            } catch (SQLException e) {
                System.out.println("SQL find current user account entry from email address error: " + e);
                //Sets all current user account entry to nothing 
                CurrentUserAccountEntry = null;
            }
        }
    }

    final public void FindCurrentUserSettingsEntry(final String Username) {
        /*
        This method retrieves the user setting entry from the user settings table with the specified username
        If it does not find an entry then it returns the default settings entry
         */

        //If guest login
        if (Username.equals("")) {
            //Sets current user settings entry to nothing 
            CurrentUserSettingsEntry = null;
        } else {
            //Sets default settings
            boolean FullscreenSetting = false;
            boolean ImageBackgroundSetting = false;
            boolean AntialiasingSetting = true;
            int RenderQualitySetting = 3;
            int MusicVolumeSetting = 20;
            int SoundVolumeSetting = 50;
            boolean AllSoundMutedSetting = false;
            boolean MuteSoundInBackgroundSetting = false;

            try {
                //If successfully connected to database  
                if (OpenConnections()) {
                    //Finds user settings entry with specified username inside user settings table
                    final String SQL = "Select * From app.TBL_UserSettingsTable WHERE Username=? FETCH NEXT 1 ROWS ONLY";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, Username);
                    RS = PrepStmt.executeQuery();
                    //If entry found
                    if (RS.next()) {
                        //Retrieves variables from settings entry in database
                        FullscreenSetting = RS.getBoolean("FullscreenSetting");
                        ImageBackgroundSetting = RS.getBoolean("ImageBackgroundSetting");
                        AntialiasingSetting = RS.getBoolean("AntialasisingSetting");
                        RenderQualitySetting = RS.getInt("RenderQualitySetting");
                        MusicVolumeSetting = RS.getInt("MusicVolumeSetting");
                        SoundVolumeSetting = RS.getInt("SoundVolumeSetting");
                        AllSoundMutedSetting = RS.getBoolean("AllSoundMutedSetting");
                        MuteSoundInBackgroundSetting = RS.getBoolean("MuteSoundInBackgroundSetting");
                    }

                }//If unsuccessful connected to database  
                else {
                    System.out.println("Please connect to database");
                }
                //Closes connection to database
                CloseConnections();
                //Sets current user settings entry to the same as the users settings entry
                CurrentUserSettingsEntry = new UserSettingsTableEntry(Username, FullscreenSetting, ImageBackgroundSetting, AntialiasingSetting, RenderQualitySetting, MusicVolumeSetting, SoundVolumeSetting, AllSoundMutedSetting, MuteSoundInBackgroundSetting);
            } catch (SQLException e) {
                //Sets current user settings entry to the same as the default settings
                System.out.println("SQL find user settings entry error: " + e);
                CurrentUserSettingsEntry = new UserSettingsTableEntry(Username, FullscreenSetting, ImageBackgroundSetting, AntialiasingSetting, RenderQualitySetting, MusicVolumeSetting, SoundVolumeSetting, AllSoundMutedSetting, MuteSoundInBackgroundSetting);
            }
            //Adds "UpdateSettings" operation to queue
            Shape_Shooter_TD.MainClass.AddOperationToQueue("UpdateSettings", null, CurrentUserSettingsEntry, null, "");

        }
    }

    final public void FindAllLeaderboardEntries() {
        /*
        This method retrieves all leaderboard entries from the leaderboard table and stores them in array
        It also sorts the array using a merge sort
        It is only called the first time the leaderboard panel is accessed
         */

        String Username;
        int Score;
        String GameMode;
        String Difficulty;
        String MapCode;
        LeaderboardEntriesArray = new ArrayList<>();
        try {
            //If successfully connected to database  
            if (OpenConnections()) {
                //Finds every entry in leaderboard table
                final String SQL = "Select * From app.TBL_LeaderboardDetailsTable";
                RS = Stmt.executeQuery(SQL);
                //Loops while there is a next entry
                while (RS.next()) {
                    //Retrieves entry and adds it to the array
                    Username = RS.getString("Username");
                    Score = RS.getInt("Score");
                    GameMode = RS.getString("Gamemode");
                    Difficulty = RS.getString("Difficulty");
                    MapCode = RS.getString("MapCode");
                    LeaderboardEntriesArray.add(new LeaderboardTableEntry(Username, Score, GameMode, Difficulty, MapCode));
                }
            } //If unsuccessful connected to database
            else {
                System.out.println("Please connect to database");
            }
            //Closes connection to database
            CloseConnections();
            //Uses a merge sort to sort the array into descending score order
            MergeSortLeaderboardEntriesArray(LeaderboardEntriesArray);

        } catch (SQLException e) {
            System.out.println("SQL find all leaderboard entries error: " + e);
            //Sets all leaderboard entries array to nothing 
            LeaderboardEntriesArray = null;
        }
    }

    final public void AddOrUpdateAccountsEntry(final UserAccountsTableEntry UserAccountsEntry) {
        /*
        This method searches for a user account entry with matching username in the user accounts table
        If it finds one then it replaces it with the new entry
        Otherwise it adds the new user accounts entry into the user accounts table
        Certain details are encrypted before being stored
         */

        try {
            //If successfully connected to database  
            if (OpenConnections()) {
                //Sets the username that will be used to derive an encryption key
                SetUsernameForKey(UserAccountsEntry.getUsername());
                //Searches or a user account entry with matching username in the user accounts table
                String SQL = "Select * From app.TBL_UserAccountsTable WHERE Username=? FETCH NEXT 1 ROWS ONLY";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, UserAccountsEntry.getUsername());
                RS = PrepStmt.executeQuery();
                //If it finds a user account entry with matching username in the user accounts table
                if (RS.next()) {
                    //Replaces user account entry with matching username in the user accounts table
                    SQL = "UPDATE app.TBL_UserAccountsTable Set Username =?, Password =?, EmailAddress =? WHERE Username=?";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, UserAccountsEntry.getUsername());
                    PrepStmt.setString(2, Encrypt(UserAccountsEntry.getPassword(), true));
                    PrepStmt.setString(3, Encrypt(UserAccountsEntry.getEmailAddress(), false));
                    PrepStmt.setString(4, UserAccountsEntry.getUsername());
                    PrepStmt.executeUpdate();
                }//If it does not find a user account entry with matching username in the user accounts table
                else {
                    //Adds new user account entry in the user accounts table
                    SQL = "INSERT INTO app.TBL_UserAccountsTable VALUES (?,?,?)";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, UserAccountsEntry.getUsername());
                    PrepStmt.setString(2, Encrypt(UserAccountsEntry.getPassword(), true));
                    PrepStmt.setString(3, Encrypt(UserAccountsEntry.getEmailAddress(), false));
                    PrepStmt.executeUpdate();
                }
            } //If unsuccessful connected to database
            else {
                System.out.println("Please connect to database");
            }
            //Closes connection to database
            CloseConnections();
        } catch (SQLException e) {
            System.out.println("SQL add account entry error: " + e);
        }
    }

    final public void AddOrUpdateSettingsEntry(final UserSettingsTableEntry UserSettingsEntry) {
        /*
        This method searches for a user setting entry with matching username in the user settings table
        If it finds one then it replaces it with the new entry
        Otherwise it adds the new user setting entry into the user settings table
         */

        try {
            //If successfully connected to database 
            if (OpenConnections()) {
                //Searches for a user setting entry with matching username in the user settings table
                String SQL = "Select * From app.TBL_UserSettingsTable WHERE Username=? FETCH NEXT 1 ROWS ONLY";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, UserSettingsEntry.getUsername());
                RS = PrepStmt.executeQuery();
                //If it finds a user settings entry with matching username in the user settings table
                if (RS.next()) {
                    //Replaces user settings entry with matching username in the user settings table
                    SQL = "UPDATE app.TBL_UserSettingsTable Set FullscreenSetting = " + UserSettingsEntry.isFullscreenSetting() + ", ImageBackgroundSetting = " + UserSettingsEntry.isImageBackgroundSetting() + ", AntialasisingSetting = "
                            + UserSettingsEntry.isAntialiasingSetting() + ", RenderQualitySetting = " + UserSettingsEntry.getRenderQualitySetting() + ", MusicVolumeSetting = " + UserSettingsEntry.getMusicVolumeSetting() + ", SoundVolumeSetting = " + UserSettingsEntry.getSoundVolumeSetting()
                            + ", AllSoundMutedSetting = " + UserSettingsEntry.isAllSoundMutedSetting() + ", MuteSoundInBackgroundSetting = " + UserSettingsEntry.isMuteSoundInBackgroundSetting() + " WHERE Username=?";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, UserSettingsEntry.getUsername());
                    PrepStmt.executeUpdate();

                } //If it does not find a user setting entry with matching username in the user settings table
                else {
                    //Adds new user settings entry in the user settings table
                    SQL = "INSERT INTO app.TBL_UserSettingsTable VALUES (?, " + UserSettingsEntry.isFullscreenSetting() + ", " + UserSettingsEntry.isImageBackgroundSetting() + ", "
                            + UserSettingsEntry.isAntialiasingSetting() + ", " + UserSettingsEntry.getRenderQualitySetting() + ", " + UserSettingsEntry.getMusicVolumeSetting() + ", " + UserSettingsEntry.getSoundVolumeSetting()
                            + ", " + UserSettingsEntry.isAllSoundMutedSetting() + ", " + UserSettingsEntry.isMuteSoundInBackgroundSetting() + ")";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, UserSettingsEntry.getUsername());
                    PrepStmt.executeUpdate();

                }
            }//If unsuccessful connected to database
            else {
                System.out.println("Please connect to database");
            }
            //Closes connection to database
            CloseConnections();
        } catch (SQLException e) {
            System.out.println("SQL add settings entry error: " + e);
        }
    }

    final public void AddOrUpdateLeaderboardEntry(final LeaderboardTableEntry LeaderboardEntry) {
        /*
        This method searches for a matching leaderboard entry inside leaderboard details table, ignoring score
        If it finds one, with lesser score, then it replaces it with the new entry
        Otherwise it adds the new leaderboard entry into the leaderboard details table
         */

        try {
            //If successfully connected to database  
            if (OpenConnections()) {
                //Searches for a leaderboard entry with matching game details, ignoring score, to new leaderboard entry inside the leaderboard details table
                String SQL = "Select * From app.TBL_LeaderboardDetailsTable WHERE Username=?"
                        + " AND Gamemode='" + LeaderboardEntry.getGameMode() + "'" + " AND Difficulty='" + LeaderboardEntry.getDifficulty()
                        + "' AND MapCode='" + LeaderboardEntry.getMapCode() + "' FETCH NEXT 1 ROWS ONLY";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, LeaderboardEntry.getUsername());
                RS = PrepStmt.executeQuery();
                //If it finds a leaderboard entry with matching game details, ignoring score, to new leaderboard entry inside leaderboard details table
                if (RS.next()) {
                    //If the matching entry has a lesser score
                    if (RS.getInt("Score") < LeaderboardEntry.getScore()) {
                        //Replaces entry with the new leaderboard entry inside leaderboard details table
                        SQL = "UPDATE app.TBL_LeaderboardDetailsTable Set Score = " + LeaderboardEntry.getScore() + " WHERE Username=?"
                                + " AND Gamemode='" + LeaderboardEntry.getGameMode() + "'" + " AND Difficulty='" + LeaderboardEntry.getDifficulty()
                                + "' AND MapCode='" + LeaderboardEntry.getMapCode() + "'";
                        PrepStmt = GetPreparedStatement(SQL);
                        PrepStmt.setString(1, LeaderboardEntry.getUsername());
                        PrepStmt.executeUpdate();
                    }
                }//If it does not find a leaderboard entry with matching game details, ignoring score, to new leaderboard entry
                else {
                    //Adds new leaderboard entry inside leaderboard details table
                    SQL = "INSERT INTO app.TBL_LeaderboardDetailsTable VALUES (?," + LeaderboardEntry.getScore() + ",'"
                            + LeaderboardEntry.getGameMode() + "','" + LeaderboardEntry.getDifficulty() + "','" + LeaderboardEntry.getMapCode() + "')";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, LeaderboardEntry.getUsername());
                    PrepStmt.executeUpdate();
                }
            } //If unsuccessful connected to database
            else {
                System.out.println("Please connect to database");
            }
            //Closes connection to database
            CloseConnections();

        } catch (SQLException e) {
            System.out.println("SQL add leaderboard entry error: " + e);
        }
    }

    final public void UpdateUsername(final String OldUsername, final String NewUsername) {
        /*
        This method searches though all tables in the database and finds entries with usernames matching the old username
        It then replaces the username on all entries found with the new username
        It also re-encrypts the password with the a key derived from the new username
         */

        try {
            //If successfully connected to database  
            if (OpenConnections()) {

                //Searches for a user account entry with matching username in the user accounts table
                String SQL = "Select * From app.TBL_UserAccountsTable WHERE Username=? FETCH NEXT 1 ROWS ONLY";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, OldUsername);
                RS = PrepStmt.executeQuery();
                //Loops through all entries found
                while (RS.next()) {
                    //Replaces the username of this user account entry within the user accounts table
                    SQL = "UPDATE app.TBL_UserAccountsTable Set Username =?, Password =? WHERE Username=?";
                    PrepStmt = GetPreparedStatement(SQL);
                    //Sets the username that will be used to derive an encryption key
                    SetUsernameForKey(OldUsername);
                    //Decrypts the password using the old username
                    String DecryptedPassword = Decrypt(RS.getString("Password"), true);
                    //Sets the username that will be used to derive an encryption key
                    SetUsernameForKey(NewUsername);
                    PrepStmt.setString(1, NewUsername);
                    //Encrypts the password using the new username
                    PrepStmt.setString(2, Encrypt(DecryptedPassword, true));
                    PrepStmt.setString(3, OldUsername);
                    PrepStmt.executeUpdate();

                }
                //Empties result set
                if (!RS.isClosed()) {
                    RS.close();
                }

                //Searches for a user settings entry with matching username in the user settings table
                SQL = "Select * From app.TBL_UserSettingsTable WHERE Username=? FETCH NEXT 1 ROWS ONLY";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, OldUsername);
                RS = PrepStmt.executeQuery();
                //Loops through all entries found
                while (RS.next()) {
                    //Replaces the username of this user settings entry within the user settings table
                    SQL = "UPDATE app.TBL_UserSettingsTable Set Username =? WHERE Username=?";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, NewUsername);
                    PrepStmt.setString(2, OldUsername);
                    PrepStmt.executeUpdate();
                }
                //Empties result set
                if (!RS.isClosed()) {
                    RS.close();
                }
                //Searches for all leaderboard entry with matching username in the leaderboard details table
                SQL = "Select * From app.TBL_LeaderboardDetailsTable WHERE Username=?";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, OldUsername);
                RS = PrepStmt.executeQuery();
                //Loops through all entries found
                while (RS.next()) {
                    //Replaces the username of this leaderboard entry within the leaderboard details table
                    SQL = "UPDATE app.TBL_LeaderboardDetailsTable Set Username =? WHERE Username=?";
                    PrepStmt = GetPreparedStatement(SQL);
                    PrepStmt.setString(1, NewUsername);
                    PrepStmt.setString(2, OldUsername);
                    PrepStmt.executeUpdate();
                }
                //If leaderboard entries array is populated
                if (LeaderboardEntriesArray != null) {
                    //Loops through all leaderboard entries
                    for (int LeaderboardEntriesArrayIndex = 0; LeaderboardEntriesArrayIndex < LeaderboardEntriesArray.size(); LeaderboardEntriesArrayIndex++) {
                        //Replaces the username of this leaderboard entry with new username if it had the old username
                        if (LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getUsername().equals(OldUsername)) {
                            LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).setUsername(NewUsername);
                        }
                    }
                }
            }//If unsuccessful connected to database
            else {
                System.out.println("Please connect to database");
            }
            //Closes connection to database
            CloseConnections();

        } catch (SQLException e) {
            System.out.println("SQL DeleteAccount error: " + e);
        }

    }

    final public void DeleteAccount(final String Username) {
        /*
        This method searches through every table for all entries with matching username
        It deletes any entry it finds from its respective table
         */

        try {
            //If successfully connected to database  
            if (OpenConnections()) {
                //Deletes the entry with the username from the user accounts table
                String SQL = "DELETE FROM app.TBL_UserAccountsTable WHERE Username=?";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, Username);
                PrepStmt.executeUpdate();
                //Deletes the entry with the username from the user settings table
                SQL = "DELETE FROM app.TBL_UserSettingsTable WHERE Username=?";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, Username);
                PrepStmt.executeUpdate();
                //Deletes all entries with the username from the leaderboard details table
                SQL = "DELETE FROM app.TBL_LeaderboardDetailsTable WHERE Username=?";
                PrepStmt = GetPreparedStatement(SQL);
                PrepStmt.setString(1, Username);
                PrepStmt.executeUpdate();

                //If leaderboard entries array is populated
                if (LeaderboardEntriesArray != null) {
                    //Loops through all leaderboard entries
                    for (int LeaderboardEntriesArrayIndex = 0; LeaderboardEntriesArrayIndex < LeaderboardEntriesArray.size(); LeaderboardEntriesArrayIndex++) {
                        //Deletes the leaderboard entry if it has this username
                        if (LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getUsername().equals(Username)) {
                            LeaderboardEntriesArray.remove(LeaderboardEntriesArrayIndex);
                            LeaderboardEntriesArrayIndex--;
                        }
                    }
                }
            }//If unsuccessful connected to database
            else {
                System.out.println("Please connect to database");
            }
            //Closes connection to database
            CloseConnections();
        } catch (SQLException e) {
            System.out.println("SQL DeleteAccount error: " + e);
        }
    }

    private void CloseConnections() {
        /*
        This method closes any open connections to database
        It is called whenever the database no longer needs to be accessed and is done to prevent errors
         */

        try {
            //Closer result set if it is open
            if (RS != null && !RS.isClosed()) {
                RS.close();
            }
            //Closer database connection if it is open
            if (Connection != null && !Connection.isClosed()) {
                Connection.close();
            }
            //Closer statement if it is open
            if (Stmt != null && !Stmt.isClosed()) {
                Stmt.close();
            }
            //Closer prepared statement if it is open
            if (PrepStmt != null && !PrepStmt.isClosed()) {
                PrepStmt.close();
            }
        } catch (SQLException ex) {
            System.out.println("SQL close connections error: " + ex);
        }
    }

    private String UsernameForKey = "";

    private void SetUsernameForKey(final String NewUsernameForKey) {
        //Sets the username that will be used to derive an encryption  key
        UsernameForKey = NewUsernameForKey;
    }

    private Key GenerateKey(final boolean UseDerivedKey) throws Exception {
        /*
        This method either creates a derived or standard encryption key
        It then returns the created key
         */

        //Finds standard key value
        byte[] keyValue = "abcdefghijklmnop".getBytes();
        //If key value should be derived
        if (UseDerivedKey) {
            try {
                //Key value set to MD5 hash of the username
                MessageDigest MD = MessageDigest.getInstance("MD5");
                MD.reset();
                MD.update(UsernameForKey.getBytes());
                keyValue = MD.digest();
            } catch (NoSuchAlgorithmException ex) {
                System.out.println("Generating derived key error" + ex);
            }
        }
        //Creates and returns encryption key
        Key key = new SecretKeySpec(keyValue, "AES");
        return key;
    }

    private String Encrypt(final String Data, final boolean UseDerivedKey) {
        /*
        This method encrypts the entered data
        It then returns the encrypted data
         */

        String EncryptedValue = Data;
        try {
            //Generates encryption key
            Key key = GenerateKey(UseDerivedKey);
            //Encrypts data using the key
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] EncryptedValueBytes = cipher.doFinal(Data.getBytes());
            EncryptedValue = new BASE64Encoder().encode(EncryptedValueBytes);
        } catch (Exception e) {
            System.out.println("Encryption error: " + e);
        }
        //Returns the encrypted data
        return EncryptedValue;
    }

    private String Decrypt(String EncryptedData, boolean UseDerivedKey) {
        /*
        This method decrypts the entered data
        It then returns the decrypted data
         */

        String DecryptedValue = EncryptedData;
        try {
            //Generates deception key
            Key key = GenerateKey(UseDerivedKey);
            //decrypts data using the key
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] DecodedValue = new BASE64Decoder().decodeBuffer(EncryptedData);
            byte[] DecryptedValueBytes = cipher.doFinal(DecodedValue);
            DecryptedValue = new String(DecryptedValueBytes);
        } catch (Exception e) {
            System.out.println("Decryption error: " + e);
        }
        //Returns the decrypted data
        return DecryptedValue;
    }

    final public boolean ValidEncryptedLength(final String NewUsernameForKey, final String Data, final boolean UseDerivedKey) {
        /*
        This method returns whether or not the encrypted length of the entered data is less than 65 characters
         */

        //Sets the username that will be used to derive an encryption  key
        SetUsernameForKey(NewUsernameForKey);
        //Returns whether or not the encrypted length of the entered data is less than 65 characters
        return Encrypt(Data, UseDerivedKey).length() <= 64;
    }

    private void MergeSortLeaderboardEntriesArray(final ArrayList<LeaderboardTableEntry> UnsortedLeaderboardEntriesArray) {
        /*
        This method uses a merge sort to sort the leaderboard entry score into descending order
        It recursively repeats until the array has been divided into single elements
         */

        //Creates two sub arrays
        final ArrayList<LeaderboardTableEntry> LeftSubArray = new ArrayList<>();
        final ArrayList<LeaderboardTableEntry> RightSubArray = new ArrayList<>();
        //If the unsorted array contains more then one element
        if (UnsortedLeaderboardEntriesArray.size() > 1) {
            //Finds the midpoint of the unsorted array
            int MidPoint = UnsortedLeaderboardEntriesArray.size() / 2;

            //Splits and copies the values between the two sub arrays
            LeftSubArray.addAll(UnsortedLeaderboardEntriesArray.subList(0, MidPoint));
            RightSubArray.addAll(UnsortedLeaderboardEntriesArray.subList(MidPoint, UnsortedLeaderboardEntriesArray.size()));
            //Recursively repeats the merge sort method
            MergeSortLeaderboardEntriesArray(LeftSubArray);
            MergeSortLeaderboardEntriesArray(RightSubArray);
            //Merges the two sub arrays
            MergeLeaderboardEntriesSubArrays(UnsortedLeaderboardEntriesArray, LeftSubArray, RightSubArray);
        }
    }

    private void MergeLeaderboardEntriesSubArrays(final ArrayList<LeaderboardTableEntry> SortedLeaderboardEntriesArray, final ArrayList<LeaderboardTableEntry> LeftSubArray, final ArrayList<LeaderboardTableEntry> RightSubArray) {
        /*
        This method merges the two sub arrays into a sorted array
         */

        //Initial index pointer for merged array
        int LeaderboardEntriesArrayIndex = 0;
        //Initial index pointer for two sub arrays
        int LeftSubArrayIndex = 0;
        int RightSubArrayIndex = 0;
        //While loop until all values of one sub arrays are merged
        while (LeftSubArrayIndex < LeftSubArray.size() && RightSubArrayIndex < RightSubArray.size()) {
            //Merges the left and right sub arrays
            if (LeftSubArray.get(LeftSubArrayIndex).getScore() > RightSubArray.get(RightSubArrayIndex).getScore()) {
                SortedLeaderboardEntriesArray.set(LeaderboardEntriesArrayIndex, LeftSubArray.get(LeftSubArrayIndex));
                //Increments the left sub array index pointer
                LeftSubArrayIndex++;
            } else {
                SortedLeaderboardEntriesArray.set(LeaderboardEntriesArrayIndex, RightSubArray.get(RightSubArrayIndex));
                //Increments the right sub array index pointer
                RightSubArrayIndex++;
            }
            //Increments the merged array index pointer
            LeaderboardEntriesArrayIndex++;
        }
        //Loops through any remaining elements in the left sub array and adds them to the end of the merged array
        while (LeftSubArrayIndex < LeftSubArray.size()) {
            SortedLeaderboardEntriesArray.set(LeaderboardEntriesArrayIndex, LeftSubArray.get(LeftSubArrayIndex));
            //Increments both the left sub array and merged array index pointer
            LeftSubArrayIndex++;
            LeaderboardEntriesArrayIndex++;
        }
        //Loops through any remaining elements in the right sub array and adds them to the end of the merged array
        while (RightSubArrayIndex < RightSubArray.size()) {
            SortedLeaderboardEntriesArray.set(LeaderboardEntriesArrayIndex, RightSubArray.get(RightSubArrayIndex));
            //Increments both the right sub array and merged array index pointer
            RightSubArrayIndex++;
            LeaderboardEntriesArrayIndex++;
        }
    }

    public void InsertLeaderboardEntryIntoSortedArray(final LeaderboardTableEntry NewLeaderboardEntry) {
        /*
        This method inserts or updates a new leaderboard entry int the array containing all leaderboard entries
         */

        //Finds what index the new entry should be inserted at
        final int IndexFound = BinarySearchLeaderboardEntriesArray(NewLeaderboardEntry.getScore());
        //An entry with matching details, ignoring score, has not been found
        boolean MatchingLeaderboardEntry = false;
        //Checks right of index (lower scores) for entry with matching details, ignoring score
        for (int LeaderboardEntriesArrayIndex = IndexFound; LeaderboardEntriesArrayIndex < LeaderboardEntriesArray.size(); LeaderboardEntriesArrayIndex++) {
            //If this entry has matching details and lower score
            if (LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getUsername().equals(NewLeaderboardEntry.getUsername())
                    && LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getGameMode().equals(NewLeaderboardEntry.getGameMode())
                    && LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getDifficulty().equals(NewLeaderboardEntry.getDifficulty())
                    && LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getMapCode().equals(NewLeaderboardEntry.getMapCode())
                    && LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getScore() != NewLeaderboardEntry.getScore()) {
                //An entry with matching details, ignoring score, has been found
                MatchingLeaderboardEntry = true;
                //Removes the existing entry with matching details, ignoring score
                LeaderboardEntriesArray.remove(LeaderboardEntriesArrayIndex);
                //Adds new entry into the array at the found index, this is the same as overwriting the existing entry and moving it to the correct position within the array
                LeaderboardEntriesArray.add(IndexFound, NewLeaderboardEntry);
                //Ends loop
                LeaderboardEntriesArrayIndex = LeaderboardEntriesArray.size() + 1;
            }
        }
        //If an entry with matching details, ignoring score, has not been found
        if (!MatchingLeaderboardEntry) {
            //Checks left of index (higher scores) for entry with matching details, ignoring score
            for (int LeaderboardEntriesArrayIndex = IndexFound - 1; LeaderboardEntriesArrayIndex >= 0; LeaderboardEntriesArrayIndex--) {
                //If this entry has matching details and higher score
                if (LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getUsername().equals(NewLeaderboardEntry.getUsername())
                        && LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getGameMode().equals(NewLeaderboardEntry.getGameMode())
                        && LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getDifficulty().equals(NewLeaderboardEntry.getDifficulty())
                        && LeaderboardEntriesArray.get(LeaderboardEntriesArrayIndex).getMapCode().equals(NewLeaderboardEntry.getMapCode())) {
                    //An entry with matching details, ignoring score, has been found
                    MatchingLeaderboardEntry = true;
                    //Ends loop
                    LeaderboardEntriesArrayIndex = -1;
                }
            }
        }
        //If an entry with matching details, ignoring score, has not been found
        if (!MatchingLeaderboardEntry) {
            //Adds new entry into the array at the found index
            LeaderboardEntriesArray.add(IndexFound, NewLeaderboardEntry);
        }
    }

    private int BinarySearchLeaderboardEntriesArray(final int NewScore) {
        /*
        The method used a modified binary insertion sort algorithm to determine what index the new entry should be inserted at into the leaderboards entries array
        It then returns the index found 
         */

        //Initialises pointer locations
        int LeftPointer = 0;
        int RightPointer = LeaderboardEntriesArray.size() - 1;
        int IndexPointer = 0;
        //If the leaderboard entries array is populated
        if (!LeaderboardEntriesArray.isEmpty()) {

            boolean FinalIndexOnRight = true;
            //While the left pointer is less than or equal to the right pointer
            while (LeftPointer <= RightPointer) {
                //Index pointer is halfway between left and right pointer
                IndexPointer = ((RightPointer - LeftPointer) / 2) + LeftPointer;
                //If the entry's score at the index pointer is equal to the new score
                if (LeaderboardEntriesArray.get(IndexPointer).getScore() == NewScore) {
                    FinalIndexOnRight = false;
                    //Ends the loop
                    RightPointer = LeftPointer - 1;
                } //If the entry's score at the index pointer is different to the new score
                else {
                    //If the new score is lesser than then entry's score at the index pointer (index of new score is on right side of the index pointer)
                    if (LeaderboardEntriesArray.get(IndexPointer).getScore() > NewScore) {
                        FinalIndexOnRight = true;
                        //Left pointer is moved one to the right of the index pointer
                        LeftPointer = IndexPointer + 1;

                    } //If the new score is larger than then entry's score at the index pointer (index of new score is on lest side of the index pointer)
                    else {
                        //Right pointer is moved one to the left of the index pointer
                        RightPointer = IndexPointer - 1;
                        FinalIndexOnRight = false;
                    }
                }
            }
            //If the index of the new score was found to be on the right of the index pointer after the binary search
            if (FinalIndexOnRight) {
                //The index pointer is moved one to the right
                IndexPointer++;
            } //If the index of the new score was not found to be on the right of the index pointer after the binary search 
            else {
                //Loops through all scores on the left of the index pointer until a score different to the new score is found
                for (int DuplicateIndex = IndexPointer; DuplicateIndex > 0; DuplicateIndex--) {
                    //If the score to the left is the same as the new score
                    if (LeaderboardEntriesArray.get(DuplicateIndex - 1).getScore() == NewScore) {
                        //The index pointer is moved one to the left
                        IndexPointer--;
                    } //If the score to the left is different to the new score
                    else {
                        //Ends the loop
                        DuplicateIndex = 0;
                    }
                }

            }
        }
        //Returns the index where the new score should be inserted at
        return IndexPointer;

    }

}
