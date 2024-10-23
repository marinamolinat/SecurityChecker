import java.security.PublicKey;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.Random;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;

public class Main {

    //password complexity functions:
    public static Boolean checkPasswordUpercase (String password){
        return password.matches(".*[A-Z].*");

    }
    public static Boolean checkPasswordLowercase (String password){
        return password.matches(".*[a-z].*");

    }
    public static Boolean checkPasswordNumber (String password){
        return password.matches(".*[0-9].*");

    }
    public static Boolean checkPasswordCharachter (String password){
        return password.matches(".*[$!%*?&].*");

    }
    public static Boolean checkPasswordLongEnough (String password){
        return password.matches(".*[$!%*?&].*");

    }

    public static Boolean isSecure(String password) {
        if (!checkPasswordUpercase(password)) {
            System.out.println("Sorry, password isn't secure as it doesn't have uppercase characters");

        }if (!checkPasswordLowercase(password)) {
            System.out.println("Sorry, password isn't secure as it doesn't have lowercase characters");

        }if (!checkPasswordCharachter(password)) {
            System.out.println("Sorry, password isn't secure as it should contain a character $!%*?&");

        }if (!checkPasswordCharachter(password)) {
            System.out.println("Sorry, password isn't secure as it should contain a character $!%*?&");

        }if (!checkPasswordLongEnough(password)) {
            System.out.println("Sorry, password isn't secure as it should be 8 characters long");

        }
        else{
            System.out.println("Password secure");
            return true;
        }
        return false;

    }

    public static boolean doesUsernamePasswordExist(String usernameToCheck, String passwordToCheck)
    {

        String url = "jdbc:mysql://localhost:3306/usernamepassword";
        String user = "root";
        String password = "rootroot";
        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            PreparedStatement select_statement;
            select_statement = conn.prepareStatement("SELECT username from usernamepassword.users WHERE username = ? and password = ?" );

            select_statement.setString(1, usernameToCheck );
            select_statement.setString(2, passwordToCheck);
            ResultSet results = select_statement.executeQuery();;

            if (results.next()) {

                String databaseusername = results.getString("username");
                String databasepassword = results.getString("password");


                if (databaseusername.equals(usernameToCheck) && databasepassword.equals(passwordToCheck)) {
                    System.out.println("Username and password are valid");
                    return true;
                }
            }

            System.out.println("Username or password is invalid");


        }
        catch( SQLException e)
        {
            System.out.println("Password and Username does not exist. Try again");

        }
        return false;

    }

    public static void InsertUsernameAndPassword(){

        Scanner scanner = new Scanner(System.in);


        System.out.println("Okay, I will add a username and password");

        System.out.print("Enter username: ");
        String usernameToBeInserted = scanner.next();
        String passwordToBeInserted;


        // check if password is secure
        do{
            System.out.print("Enter password: ");
            passwordToBeInserted = scanner.next();
        }
        while (!isSecure(passwordToBeInserted));

        String url = "jdbc:mysql://localhost:3306/usernamepassword";
        String user = "root";
        String password = "rootroot";
        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            PreparedStatement insert_statement;
            insert_statement = conn.prepareStatement("insert into usernamepassword.users(username, password) values (?, ?)" );

            insert_statement.setString(1, usernameToBeInserted );
            insert_statement.setString(2, passwordToBeInserted);
            int n = insert_statement.executeUpdate();
            System.out.println("Username and Password successfully inserted");


        }
        catch( SQLException e)
        {
            e.printStackTrace();

        }




    }

    public static void ModifyUserOrPassword(){
        Scanner scanner = new Scanner(System.in);

        String url = "jdbc:mysql://localhost:3306/usernamepassword";
        String user = "root";
        String password = "rootroot";

        System.out.print("Enter username: ");
        String usernameToBeModified = scanner.next();
        System.out.print("Enter password: ");
        String passwordToBeModified = scanner.next();
        String response;

        //check if password and username are valid
        if (!doesUsernamePasswordExist(usernameToBeModified, passwordToBeModified)){
            return;
        }

        do{
            System.out.print("Would you like to change a username or a password? (username/password): ");
            response = scanner.next();
        }
        while (!(response.equalsIgnoreCase("username") || response.equalsIgnoreCase("password")));




        try (Connection conn = DriverManager.getConnection(url, user, password))
        {

            if(response.equalsIgnoreCase("username")){
                System.out.println("Okay, changing username");
                System.out.print("Enter new username: ");
                String newUsername = scanner.next();

                PreparedStatement insert_statement;
                insert_statement = conn.prepareStatement("UPDATE usernamepassword.users set username = ? where username = ?");
                insert_statement.setString(1, newUsername);
                insert_statement.setString(1, usernameToBeModified);


            }
            else if (response.equalsIgnoreCase("password")) {
                System.out.print("Enter new password: ");
                String newPassword = scanner.next();
                PreparedStatement insert_statement;
                insert_statement = conn.prepareStatement("UPDATE usernamepassword.users set password = ? where username = ?");
                insert_statement.setString(1, newPassword);
                insert_statement.setString(1, usernameToBeModified);



            }


        }
        catch( SQLException e)
        {
            e.printStackTrace();

        }


    }
    public static void ListUsersAndPasswords()
    {
        String url = "jdbc:mysql://localhost:3306/usernamepassword";
        String user = "root";
        String password = "rootroot";
        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            PreparedStatement select_statement;
            select_statement = conn.prepareStatement("SELECT * FROM usernamepassword.users;");
            ResultSet table = select_statement.executeQuery();
            while (table.next())
            {
                String databaseusername = table.getString("username");
                String databasepassword = table.getString("password");
                System.out.printf("Username: %s \t password: %s \n", databaseusername, databasepassword);
            }
        }
        catch( SQLException e)
        {
            e.printStackTrace();

        }




    }
    public static void main(String[] args) {

        //
        boolean menu = true;
        int response = 0;
        Scanner scanner = new Scanner(System.in);

        //Ask for username and password
        //Menu
        while(menu){
            System.out.println("Menu \n" +
                    "1. List usernames and passwords\n" +
                    "2. Add username and password \n" +
                    "3. Modify username and/or password \n" +
                    "4. Delete username and password \n" +
                    "5. Exit");


            response = scanner.nextInt();

            switch (response)
            {
                case 1:
                    System.out.println("Okay, I will list all the usernames and passwords");
                    ListUsersAndPasswords();
                    break;
                case 2:
                    InsertUsernameAndPassword();

                    break;


                case 3:
                    System.out.println("Okay, I will modify username or password");
                    ModifyUserOrPassword();
                    break;
                case 4:
                    System.out.println("Delete");
                    break;
                case 5:
                    System.out.println("Existing the program");
                    menu = false;
                    break;
                default:
                    System.out.println("Sorry try again, your input wasn't one of the option allowed");
            }




        }

        String username = scanner.nextLine();
        String user_password = scanner.nextLine();
        System.out.printf("%s and password: %s ", username, user_password);






    }
}
