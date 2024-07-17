import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Users {
    private Connection connection;
    private Scanner scanner;

    public Users(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (user_exists(email)){
            System.out.println("User Already Exists For This Email Address!");
            return;
        }

        String register_query = "INSERT INTO users(full_name, email, password) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                System.out.println("Registration Successful");
            } else {
                System.out.println("Registration Failed!");
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String login_query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return email;
            } else {
                return null;
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean user_exists(String email){
        String user_exists_query = "SELECT * FROM users WHERE email = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(user_exists_query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();

           if (resultSet.next()){
               return true;
           } else {
               return false;
           }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }
}
