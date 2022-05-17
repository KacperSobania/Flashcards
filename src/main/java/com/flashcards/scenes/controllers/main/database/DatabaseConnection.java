package com.flashcards.scenes.controllers.main.database;

import java.sql.*;

public final class DatabaseConnection {

    private static DatabaseConnection setsManager;
    public int indexOfChosenSet;

    private DatabaseConnection(){
        createDatabase();
    }

    public static DatabaseConnection getInstance(){
        if(setsManager == null){
            setsManager = new DatabaseConnection();
        }
        return setsManager;
    }

    private void createDatabase(){
        try(Connection connection = connect(); Statement statement = connection.createStatement()){
            String createTableOfTitlesStatement = "CREATE TABLE IF NOT EXISTS \"Titles of sets\" (\n"
                    + "Title string\n"
                    + ");";
            statement.execute(createTableOfTitlesStatement);

        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void addTitleOfSet(String titleOfSet){
        String addTitleToTable = "INSERT INTO \"Titles of sets\"(Title) VALUES(?)";
        try(Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(addTitleToTable)){

            preparedStatement.setString(1, titleOfSet);
            preparedStatement.executeUpdate();
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        createTableForCards(titleOfSet);
    }

    private void createTableForCards(String titleOfSet){
        String createTableForCards = "CREATE TABLE IF NOT EXISTS \"" + titleOfSet + "\" (\n"
                + "Definition string,\n"
                + "Answer string\n"
                + ");";
        try(Connection connection = connect(); Statement statement = connection.createStatement()){
            statement.execute(createTableForCards);
        } catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    public String getTitleOfSet(int index){
        String getTitleOfSet = "SELECT Title FROM \"Titles of sets\" WHERE rowid = " + index;
        String title = "";
        try(Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getTitleOfSet)){

            title = resultSet.getString("Title");

        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        return title;
    }

    public int getNumberOfSets(){
        String query = "SELECT COUNT(*) FROM \"Titles of sets\"";
        int count = 0;
        try(Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){

            resultSet.next();
            count = resultSet.getInt(1);
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        return count;
    }

    public void addCard(String definition, String answer, int indexOfSet){
        String titleOfSet = getTitleOfSet(indexOfSet);
        String addCardStatement = "INSERT INTO " + "\"" + titleOfSet + "\"(Definition,Answer) VALUES(?,?)";
        try(Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(addCardStatement)){
            preparedStatement.setString(1, definition);
            preparedStatement.setString(2, answer);
            preparedStatement.executeUpdate();

        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    public int getNumberOfCards(int indexOfSet){
        String titleOfSet = getTitleOfSet(indexOfSet);
        String query = "SELECT COUNT(*) FROM " + "\"" + titleOfSet + "\"";
        int count = 0;
        try(Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){

            resultSet.next();
            count = resultSet.getInt(1);
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        return count;
    }

    public String getCardDefinition(int indexOfSet, int indexOfCard){
        String titleOfSet = getTitleOfSet(indexOfSet);
        String definition = "";
        String getDefinition = "SELECT Definition FROM " + "\"" + titleOfSet + "\" WHERE rowid = " + indexOfCard;
        try(Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getDefinition)){

            definition = resultSet.getString("Definition");
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        return definition;
    }

    public String getCardAnswer(int indexOfSet, int indexOfCard){
        String titleOfSet = getTitleOfSet(indexOfSet);
        String answer = "";
        String getAnswer = "SELECT Answer FROM " + "\"" + titleOfSet + "\" WHERE rowid = " + indexOfCard;
        try(Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAnswer)){

            answer = resultSet.getString("Answer");
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        return answer;
    }

    public void deleteSet(int indexOfSet){
        String query = "DELETE FROM \"Titles of sets\" WHERE rowid = ?";
        String titleOfSet = getTitleOfSet(indexOfSet);
        try(Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            Statement statement = connection.createStatement()){


            preparedStatement.setInt(1, indexOfSet);
            preparedStatement.executeUpdate();
            query = "DROP TABLE IF EXISTS \"" + titleOfSet + "\"";
            statement.executeUpdate(query);
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void deleteCard(int indexOfSet, int indexOfCard){
        String titleOfSet = getTitleOfSet(indexOfSet);
        String query = "DELETE FROM \"" + titleOfSet + "\" WHERE rowid = ?";
        try(Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, indexOfCard);
            preparedStatement.executeUpdate();
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void updateTitleOfSet(int indexOfSet, String newTitleOfSet){

        String renameTable = "ALTER TABLE \"" + getTitleOfSet(indexOfSet) + "\" RENAME TO \"" + newTitleOfSet + "\"";
        try(Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(renameTable)){

            preparedStatement.executeUpdate();

        } catch (SQLException exception){
            System.out.println(exception.getMessage());
        }

        String query = "UPDATE \"Titles of sets\" SET Title = ? WHERE rowid = ?";
        try(Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, newTitleOfSet);
            preparedStatement.setInt(2, indexOfSet);
            preparedStatement.executeUpdate();
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

    }

    public void updateDefinition(int indexOfSet, int indexOfCard, String newDefinition){
        String titleOfSet = getTitleOfSet(indexOfSet);
        String query = "UPDATE \"" + titleOfSet + "\" SET Definition = ? WHERE rowid = ?";
        try(Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, newDefinition);
            preparedStatement.setInt(2, indexOfCard);
            preparedStatement.executeUpdate();
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void updateAnswer(int indexOfSet, int indexOfCard, String newAnswer){
        String titleOfSet = getTitleOfSet(indexOfSet);
        String query = "UPDATE \"" + titleOfSet + "\" SET Answer = ? WHERE rowid = ?";
        try(Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, newAnswer);
            preparedStatement.setInt(2, indexOfCard);
            preparedStatement.executeUpdate();
        } catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    private static Connection connect() {
        String url = "jdbc:sqlite:Sets.db";
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url);
        } catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return connection;
    }
}
