package StomatoloskaOrdinacija;

import java.sql.*;
import java.util.Scanner;
import java.sql.SQLException;
import java.sql.Connection;

public class SistemRezervacija {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/stomatoloskaordinacija";
    private static final String username = "root";
    private static final String password = "Bojan0205";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Pacijent pacijent = new Pacijent(connection, scanner);
            Doktor doktor = new Doktor(connection);
            while (true) {
                System.out.println("SISTEM REZERVACIJA ");
                System.out.println("1. Dodaj Pacijenta");
                System.out.println("2. Pregled Pacijenata");
                System.out.println("3. Pregled Doktora");
                System.out.println("4. Rezervisi Termin");
                System.out.println("5. Izlaz");
                System.out.println("Unesite svoj izbor: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        pacijent.dodajPacijenta();
                        System.out.println();
                        break;
                    case 2:
                        pacijent.pregledPacijenata();
                        System.out.println();
                        break;
                    case 3:
                        doktor.pregledDoktora();
                        System.out.println();
                        break;
                    case 4:
                        rezervacije(pacijent, doktor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Hvala Vam sto koristite nase usluge!!");
                        return;
                    default:
                        System.out.println("Unesite ispravan izbor!!!");
                        break;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void rezervacije(Pacijent pacijent, Doktor doktor, Connection connection, Scanner scanner) {
        System.out.print("Unesite Pacijent Id: ");
        int pacijentId = scanner.nextInt();
        System.out.print("Unesite Doktor Id: ");
        int doktorId = scanner.nextInt();
        System.out.print("Unesite datum pregleda (YYYY-MM-DD): ");
        String datumZakazivanja = scanner.next();
        if (pacijent.getPacijentById(pacijentId) && doktor.getDoktorById(doktorId)) {
            if (dostupnostDoktora(doktorId, datumZakazivanja, connection)) {
                String appointmentQuery = "INSERT INTO zakazivanjetermina(pacijent_id, doktor_id, datum_zakazivanja) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, pacijentId);
                    preparedStatement.setInt(2, doktorId);
                    preparedStatement.setString(3, datumZakazivanja);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Termin rezervisan!");
                    } else {
                        System.out.println("Greska pri rezervaciji!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doktor nije dostupan na taj datum!!");
            }
        } else {
            System.out.println("Izabrani doktor ili pacijent ne postoje!!!");
        }
    }

    public static boolean dostupnostDoktora(int doktorId, String datumZakazivanja, Connection connection) {
        String query = "SELECT COUNT(*) FROM zakazivanjetermina WHERE doktor_id = ? AND datum_zakazivanja = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doktorId);
            preparedStatement.setString(2, datumZakazivanja);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

