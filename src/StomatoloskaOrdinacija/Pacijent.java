package StomatoloskaOrdinacija;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Pacijent {
    private Connection connection;
    private Scanner scanner;

    public Pacijent(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void dodajPacijenta(){
        System.out.print("Unesite ime pacijenta:");
        String ime = scanner.next();
        System.out.print("Unesite godine pacijenta:");
        int godine = scanner.nextInt();
        System.out.print("Unesite pol pacijenta:");
        String pol = scanner.next();

        try {
            String query = "INSERT INTO stomatoloskaordinacija.pacijenti(ime, godine, pol) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ime);
            preparedStatement.setInt(2, godine);
            preparedStatement.setString(3, pol);
            int trenutniRed = preparedStatement.executeUpdate();
            if (trenutniRed > 0){
                System.out.println("Pacijent je uspjesno dodan!");
            }else {
                System.out.println("Greska pri dodavanju!");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void pregledPacijenata() {
        String query = "SELECT * FROM stomatoloskaordinacija.pacijenti";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Pacijenti: ");
            System.out.println("+-------------+------------------+-------------+--------------+");
            System.out.println("| Pacijent Id | Ime              | Godine      | Pol          |");
            System.out.println("+-------------+------------------+-------------+--------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String ime = resultSet.getString("ime");
                int godine = resultSet.getInt("godine");
                String pol = resultSet.getString("pol");
                System.out.printf("| %-11s | %-16s | %-11s | %-12s |\n", id, ime, godine, pol);
            }
            System.out.println("+-------------+------------------+-------------+--------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean getPacijentById(int id) {
        String query = "SELECT * FROM stomatoloskaordinacija.pacijenti WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
