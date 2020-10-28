package ru.pentragon.java2.networkserver.auth;

import ru.pentragon.java2.clientserver.user.User;

import java.sql.*;
//этот класс не рабочий
//все работает в MS SQL
public class SqlLiteAuthService implements AuthService{

    Connection connection;
    Statement statement;

    @Override
    public void start()  {

        try {
            Class.forName("org.sqlite.jdbc");
            connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            statement = connection.createStatement();
            System.out.println("Hello, SQL Lite connect success");
            createNewUsersTable(statement);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void createNewUsersTable(Statement statement) throws SQLException {
        int createTableRes = 0;
        createTableRes = statement.executeUpdate("CREATE TABLE IF NOT EXISTS 'Users'" +
                " UserId INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Login text NOT NULL," +
                " Password text NOT NULL," +
                " Username text NOT NULL);");
        if (createTableRes == 0) {
            String sqlRequestPartOne = "INSERT INTO Users (Login, Password, Username)";
            statement.addBatch(sqlRequestPartOne + " VALUES('login1', 'pass1', 'User1')");
            statement.addBatch(sqlRequestPartOne + " VALUES('login2', 'pass2', 'User2')");
            statement.addBatch(sqlRequestPartOne + " VALUES('login3', 'pass3', 'User3')");
            statement.addBatch(sqlRequestPartOne + " VALUES('login4', 'pass4', 'User4')");
            statement.addBatch(sqlRequestPartOne + " VALUES('login5', 'pass5', 'User5')");
            statement.addBatch(sqlRequestPartOne + " VALUES('login6', 'pass6', 'User6')");
            statement.addBatch(sqlRequestPartOne + " VALUES('login7', 'pass7', 'User7')");
            statement.addBatch(sqlRequestPartOne + " VALUES('login8', 'pass8', 'User8')");
            statement.addBatch(sqlRequestPartOne + " VALUES('SERVER', 'SERVER', 'Server')");
            statement.executeBatch();
        }
    }

    @Override
    public synchronized User getUserByLoginAndPassword(String login, String password) {
        try {
            String userName = null;
            String sqlRequest = "SELECT Login, Password, Username FROM Users WHERE Login='" + login + "' AND Password='" + password + "'";
            if (!statement.isClosed() && !connection.isClosed()) {
                ResultSet sqlResult = statement.executeQuery(sqlRequest);
                while (sqlResult.next()) {
                    userName = sqlResult.getString(3);
                }
                if (userName != null) {
                    sqlResult.close();
                    return new User(login, password, userName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Empty SQL request");
        return null;
    }

    @Override
    public void updateUsername(User user, String newUsername) {

    }

    @Override
    public void stop() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
