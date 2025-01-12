import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Departamento {
    public static void añadir(Connection con){
        Scanner teclado=new Scanner(System.in);
        String sql,nombre,codigo;
        System.out.println("dime el nombre del departamento");
        nombre=teclado.nextLine();
        System.out.println("dime el codigo del departamento");
        codigo=teclado.nextLine();
        try{
            sql="insert into departamentos (id_departamento,nombre) values(?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,codigo);
            ps.setString(2,nombre);
            ps.executeUpdate();
            System.out.println("departamento añadido");
        }catch(SQLException sqle){
            if(sqle.getErrorCode()==1062){
                System.out.println("ese departamento ya esta metido");
            } else if (sqle.getErrorCode()==1406) {
                System.out.println("error al introducir el valor del id");
            } else {
                sqle.printStackTrace();
            }
        }
    }

    public static void eliminar(Connection con){
        Scanner teclado=new Scanner(System.in);
        String sql,nombre,codigo;
        System.out.println("dime el codigo del departamento que quieres eliminar");
        codigo=teclado.nextLine();
        try{
            if(Departamento.comprobar(con,codigo)==1) {
                sql = "delete from departamentos where id_departamento=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, codigo);
                ps.executeUpdate();
                System.out.println("departamento eliminado");
            }else if(Departamento.comprobar(con,codigo)==0){
                System.out.println("ese departamento no existe");
            }
        }catch(SQLException sqle){
                sqle.printStackTrace();

        }
    }

    public static int comprobar(Connection con,String codigo){
        String sql;
        try{
            sql="select id_departamento from departamentos where id_departamento=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,codigo);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return 1;
            }else{
                return 0;
            }
        }catch (SQLException sqle){
            sqle.printStackTrace();
            return -1;
        }
    }

    public static void listar(Connection con){
        String sql,id,nombre;
        try{
            sql="select id_departamento,nombre from departamentos";
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                id=rs.getString("id_departamento");
                nombre=rs.getString("nombre");
                System.out.println(id+" "+nombre);
            }
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

}
