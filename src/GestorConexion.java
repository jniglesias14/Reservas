import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GestorConexion {
    private static Connection conexion;
    private static String error="";
    public static boolean crearConexion(String bd,String usr,String pass){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + bd, usr, pass);
            PreparedStatement ps;
            System.out.println("conexion correcta");
            return true;


        }catch (ClassNotFoundException cncf){
            error="Error al preparar la conexion con la base de datos";

            return false;
        }catch (SQLException sqle){
            error="Error al conectar con la base de datos";


            return false;
        }
    }

    public static Connection getConexion() {
        return conexion;
    }

    public static String getError() {
        return error;
    }

    public static boolean cerrarConexion(){
        try{
            conexion.close();
            return true;
        }catch (SQLException sqle){
            error="no se pudo cerrar la conexion";
            return false;
        }
    }
}
