import java.sql.*;
import java.util.Scanner;



class Hotel_Management_System {
    private static final String url = "jdbc:mysql://localhost:3306/Reservation";
    private static final String user = "root";
    private static final String password = "tiktikrr";


    public static void main(String[] args) throws  SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            while (true)  {
                System.out.println();
                Scanner scanner = new Scanner(System.in);
                System.out.println("WELCOME TO THE HOTEL MANAGEMENT SYSTEM:");
                System.out.println("1 : RESERVE A  ROOM");
                System.out.println("2 : VIEW A RESERVATION");
                System.out.println("3 : GET ROOM NUMBER");
                System.out.println("4 : UPDATE RESERVATION");
                System.out.println("5 : DELETE A RESERVATION");
                System.out.println("6 : EXIT TO THE SYSTEM");
                System.out.println("CHOSE THE ABOVE OPTION TO SEE THE DETAILS:-");
                int result = scanner.nextInt();


                switch (result) {
                    case 1:
                        Reserve_room(connection, scanner);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        GETRoomNumber(connection,scanner);
                        break;
                    case 4:
                        updatereservation(connection,scanner);
                        break;
                    case 5:
                        Deletereservation(connection,scanner);
                        break;
                    case 6:
                        Exist();
                        break;
                }
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static void Reserve_room(Connection connection, Scanner scanner) {
        try {
            System.out.println("ENTER THE RESERVATION ID:");
            int reservation_id = scanner.nextInt();
            scanner.nextLine();  // consume newline
            System.out.println("ENTER THE GUEST NAME");
            String guestname = scanner.nextLine();
            System.out.println("ENTER THE ROOM NUMBER YOU WANT");
            int guestroomno = scanner.nextInt();
            System.out.println("ENTER THE CONTACT NUMBER");
            int guestnumber = scanner.nextInt();
            scanner.nextLine();

            String query = "INSERT INTO Reservation ( reservation_id, guestname , guestroomno, guestnumber) " + "VALUES ('"+ reservation_id +"','" + guestname + "','" + guestroomno + "', '" + guestnumber + "')";
            try (Statement statement = connection.createStatement()) {
                int effectedrow = statement.executeUpdate(query);
                if (effectedrow > 0) {
                    System.out.println("Reservation successfully done");
                } else {
                    System.out.println("Reservation has occurred Error");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private  static void viewReservation(Connection connection) throws SQLException {
        String Query = "SELECT reservation_id, guestname, guestroomno, guestnumber FROM Reservation";
        try(Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(Query)) {

            System.out.println("CURRENT RESERVATION :- ");
            System.out.println("+-------------------+------------+------------+-----------------+");
            System.out.println("|  | RESERVATION_ID | GUEST_NAME | GUEST_ROOM | GUEST_NUMBER |  |");
            System.out.println("+-------------------+------------+------------+-----------------+");

            while(set.next()){
                int id = set.getInt("Reservation_id");
                String guestna = set.getString("guestname");
                int roomno = set.getInt("guestroomno");
                String guestno = set.getString("guestnumber");

                System.out.printf("| %-15d | %-15s | %-15d | %-15s |\n",
                        id,guestna, roomno,guestno);
            }
            System.out.println("+-------------------+------------+------------+-----------------+");
        }
    }

    private static void GETRoomNumber(Connection connection, Scanner scanner){
        try{
            System.out.println("ENTER THE RESERVATION ID:-");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("ENTER THE RESERVATION NAME :-");
            String guestna= scanner.nextLine();


            String sql = "SELECT guestroomno FROM Reservation WHERE reservation_id = " + id;
            try(Statement statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sql)) {
                if (set.next()) {
                    System.out.println();
                    String number = set.getString("guestroomno");
                    System.out.println("ROOM NUMBER FOR RESERVATION ID IS" +
                            " AND GUEST NAME " + guestna + " is: " +number);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void updatereservation(Connection connection, Scanner scanner){
        try {
            System.out.println("ENTER USER GUEST_ID :-");
            int user_id = scanner.nextInt();
            scanner.nextLine();

            if(!reservationExist(connection ,user_id)){
                System.out.println("THE RESERVATION DOES NOT EXIST ON THE GIVEN NAME:-");
                return;
            }
            System.out.println("ENTER THE GUESTNAME :-");
            String name = scanner.nextLine();
            System.out.println("ENTER THE GUEST_ROOM_NUMBER :-");
            int room = scanner.nextInt();
            System.out.println("ENTER THE  CONTACT NUMBER OF THE GUEST ;-");
            int contact = scanner.nextInt();

            String sql = "UPDATE Reservations SET guestname = '" +
                    name +"'," + "guestroomno = '" +room +","+ "guestnumber ='"+contact+"'"+"WHERE Resrvation_id =" +user_id;
            try(Statement statement = connection.createStatement()){
                 int effectrow= statement.executeUpdate(sql);

                 if(effectrow>0){
                     System.out.println("RESERVATION UPDATED SUCCESSFULL ");
                 }else{
                     System.out.println("ERROR HAPPEND");
                 }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void Deletereservation(Connection connection, Scanner scanner){
        System.out.println("ENTER THE RESERVATION ID :-");
        int id = scanner.nextInt();
        scanner.nextLine();

        if(!reservationExist(connection , id)){
            System.out.println("THE RESERVATION DOES NOT EXIST ON THE GIVEN RESERVATION ID");
            return;
        }
        String sql ="DELETE FROM Reservation WHERE Reservation_id="+id;
        try(Statement statement = connection.createStatement()){
            int effectedrow = statement.executeUpdate(sql);

            if (effectedrow>0){
                System.out.println("DELETE HAS BEEN SUCCESSFULL");
            }else{
                System.out.println("ERROR HAS BEEN OQURED");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static boolean reservationExist(Connection connection, int reservation_id){
        try{
            String sql ="SELECT reservation_id FROM Reservation WHERE Reservation_id = "+reservation_id;

            try (Statement statement = connection.createStatement();
                 ResultSet set = statement.executeQuery(sql)){
                 return  set.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  static void Exist()throws InterruptedException{
        System.out.println("EXISTING SYSTEM:-");
        int i =5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("THANKS FOR USING THE HOTEL MANAGEMENT SYSTEM:-");
    }
}
