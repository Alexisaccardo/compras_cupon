import java.sql.*;
import java.util.Scanner;

public class Compras {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*****BIENVENIDOS*****");

        System.out.println("Deseas agregar un pedido?: ");
        String add = scanner.nextLine();

        while (add.equals("si")) {

            System.out.print("Ingrese el Id Producto: ");
            String IdProduct = scanner.nextLine();

            System.out.print("Ingrese el nombre del producto: ");
            String name = scanner.nextLine();

            System.out.print("Ingrese el tipo producto: ");
            String type = scanner.nextLine();

            System.out.print("Ingrese la cantidad del producto: ");
            int count = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese el valor del producto: ");
            int value = Integer.parseInt(scanner.nextLine());

            System.out.println("Ingrese el codigo del cupon con descuento: ");
            int coupon = Integer.parseInt(scanner.nextLine());

            int discount = (Select_One(coupon));

            if (IdProduct.equals("") || name.equals("") || type.equals("")) {
                System.out.println("No se admiten datos vacios.");
            } else {
                int multiplication = value * count;
                System.out.println("El total a pagar es: "+ multiplication);
            int result = multiplication - discount;
                System.out.println("El valor a pagar con descuento es: "+ result);


                Insert(IdProduct, name, type, count, result); //

                System.out.println("Deseas agregar otro producto?: ");
                add = scanner.nextLine();

            }
        }
    }

    private static void Insert(String idProduct, String name, String type, int count, int result) {

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/compras";
        String username = "root";
        String password = "";

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM productos");


            // Sentencia INSERT
            String sql = "INSERT INTO productos (Idproducto, nombre, tipo, cantidad, valor) VALUES (?, ?, ?, ?, ?)";

            // Preparar la sentencia
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, idProduct);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, type);
            preparedStatement.setInt(4, count);
            preparedStatement.setInt(5, result);


            // Ejecutar la sentencia
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Producto agregado exitosamente.");
            } else {
                System.out.println("No se pudo agregar el producto.");
            }

            preparedStatement.close();
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static int Select_One(int coupon) throws ClassNotFoundException, SQLException {

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/compras";
        String username = "root";
        String password = "";

        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);

        String consultaSQL = "SELECT * FROM cuponeria WHERE codigo = ?";

        PreparedStatement statement = connection.prepareStatement(consultaSQL);
        statement.setInt(1, coupon); // Establecer el valor del parámetro

        // Ejecutar la consulta
        ResultSet resultSet = statement.executeQuery();

        // Procesar el resultado si existe
        if (resultSet.next()) {
            String codigo = resultSet.getString("codigo");
            int valor_cupon = resultSet.getInt("valor_cupon");

            return valor_cupon;
        }
        // Cerrar recursos
        resultSet.close();
        statement.close();
        connection.close();


        return 0;
    }
}
