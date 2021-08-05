package shape_shooter_td;

final public class UserAccountsTableEntry implements TableEntryInterface {

    private final String Username;
    private final String Password;
    private final String EmailAddress;

    public UserAccountsTableEntry(final String Username, final String Password, final String EmailAddress) {
        //Creates new user accounts table entry with specified parameters
        this.Username = Username;
        this.Password = Password;
        this.EmailAddress = EmailAddress;
    }

    @Override
    final public String getUsername() {
        //Returns the username of this user accounts table entry
        return Username;
    }

    final public String getPassword() {
        //Returns the password of this user accounts table entry
        return Password;
    }

    final public String getEmailAddress() {
        //Returns the email address of this user accounts table entry
        return EmailAddress;
    }

}
