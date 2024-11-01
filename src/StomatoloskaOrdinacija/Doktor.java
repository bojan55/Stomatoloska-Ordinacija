package StomatoloskaOrdinacija;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doktor {
    private Connection connection;

    public Doktor(Connection connection){
        this.connection = connection;
    }


public void pregledDoktora() {
    String query = "SELECT * FROM stomatoloskaordinacija.doktori";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        System.out.println("Doktori: ");
        System.out.println("+-----------+-----------------+-------------------------------+");
        System.out.println("| Doktor Id | Ime             | Specijalizacija               |");
        System.out.println("+-----------+-----------------+-------------------------------+");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String ime = resultSet.getString("ime");
            String specijalizacija = resultSet.getString("specijalizacija");
            System.out.printf("| %-12s | %-20s | %-18s |\n", id, ime, specijalizacija);
        }
        System.out.println("+-----------+-----------------+-------------------------------+");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public boolean getDoktorById(int id){
    String query = "SELECT * FROM doktori WHERE id = ?";
    try{
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return true;
        }else{
            return false;
        }
    }catch (SQLException e){
        e.printStackTrace();
    }
    return false;
}
    }




