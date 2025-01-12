import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;
import java.time.DayOfWeek;

public class Reservas {
    protected LocalDateTime fecha_ini;
    protected LocalDateTime fecha_fin;
    protected String nombre_sala;
    protected String id_departamento;

    public Reservas(int año,int mes,int dia,int hora,int duracion,String sala,String departamento,Connection con){
            this.id_departamento=departamento;
            this.nombre_sala=sala;
            this.fecha_ini = LocalDateTime.of(año, mes, dia, hora, 0, 0);
            this.fecha_fin = LocalDateTime.of(año, mes, dia, hora + duracion, 0, 0);


    }
    public static boolean verificar(int hora,int duracion){
        if(hora>=9 && hora<=14 && hora+duracion<=14) {
            return true;
        }else {
            return false;
        }
    }

    public static void añadirReserva(Connection con,String departamento){
        Scanner scanner=new Scanner(System.in);
        int año,hora,dia,duracion,mes;
        String sala;
        System.out.println("Por favor, dime el año:");
        año = Integer.parseInt(scanner.nextLine());

        System.out.println("Por favor, dime el mes:");
        mes =  Integer.parseInt(scanner.nextLine());

        System.out.println("Por favor, dime el día:");
        dia =  Integer.parseInt(scanner.nextLine());

        System.out.println("Por favor, dime la hora:");
        hora =  Integer.parseInt(scanner.nextLine());

        System.out.println("Por favor, dime la duración:");
        duracion =  Integer.parseInt(scanner.nextLine());

        System.out.println("dime el codigo sala");
        sala=scanner.nextLine();
        if(Salas.comprobar(con,sala)==1) {
            if(Reservas.comprobarDia(con,año,mes,dia)) {
                if (Reservas.comprobarHorario(con, sala, año, mes, dia, hora, duracion, departamento) == 0) {
                    if (Reservas.verificar(hora, duracion)) {
                        Reservas r = new Reservas(año, mes, dia, hora, duracion, sala, departamento, con);
                        Reservas.meter(con, r);
                    } else {
                        System.out.println("reserva fuera de horario");
                    }
                } else {
                    System.out.println("ese horario no es correcto");

                }
            }else{
                System.out.println("error, has metido una fecha en fin de semana");
            }
        }else{
            System.out.println("esa sala no existe,por lo que no se ha podido guardar la reserva");
        }


    }

    public static boolean comprobarDia(Connection con,int año,int mes,int dia){
        LocalDate date=LocalDate.of(año,mes,dia);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    public static int comprobarHorario(Connection con,String id_sala,int año,int mes,int dia,int hora,int duracion,String id_departamento){
        String sql;
        Timestamp timestamp1=Timestamp.valueOf(LocalDateTime.of(año,mes,dia,hora,0,0));
        Timestamp timestamp2=Timestamp.valueOf(LocalDateTime.of(año,mes,dia,hora+duracion,0,0));
        try {
            sql="SELECT * FROM reservas WHERE id_departamento = ? \n" +
                    "  AND id_sala = ? AND (YEAR(fecha_inicio) = YEAR(?) \n" +
                    "    AND MONTH(fecha_inicio) = MONTH(?) AND (\n" +
                    "  HOUR(?) BETWEEN HOUR(fecha_inicio) AND HOUR(fecha_fin) \n" +
                    "      OR HOUR(?) BETWEEN HOUR(fecha_inicio) AND HOUR(fecha_fin)))";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,id_departamento);
            ps.setString(2,id_sala);
            ps.setTimestamp(3,timestamp1);
            ps.setTimestamp(4,timestamp1);
            ps.setTimestamp(5,timestamp2);
            ps.setTimestamp(6,timestamp1);

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

    public static int meter(Connection con,Reservas r){
        String sql;
        Timestamp timestamp1=Timestamp.valueOf(r.fecha_ini);
        Timestamp timestamp2=Timestamp.valueOf(r.fecha_fin);
        try{
            sql="insert into reservas( id_departamento, id_sala, fecha_inicio, fecha_fin) values(?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,r.id_departamento);
            ps.setString(2,r.nombre_sala);
            ps.setTimestamp(3,timestamp1);
            ps.setTimestamp(4,timestamp2);
            ps.executeUpdate();
            return 1;
        }catch (SQLException sqle){
            if(sqle.getErrorCode()==1062){
                System.out.println("esa reserva ya existe");
            }else {
                sqle.printStackTrace();
            }
            return -1;
        }
    }

    public static void listar(Connection con){
        String sql;
        try{
            sql="select id_departamento, id_sala, fecha_inicio, fecha_fin from reservas";
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("id_departamento")+" "+rs.getString("id_sala")+" "+rs.getTimestamp("fecha_inicio")+" "+rs.getTimestamp("fecha_fin"));
            }
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public static void mostrar(Connection con,String id_departamento){
        String sql;
        try{
            sql="select id_departamento, id_sala, fecha_inicio, fecha_fin from reservas where id_departamento=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,id_departamento);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("id_departamento")+" "+rs.getString("id_sala")+" "+rs.getTimestamp("fecha_inicio")+" "+rs.getTimestamp("fecha_fin"));
            }
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }

    }

    public static void cancelar(Connection con,String id_departamento){
        Scanner teclado=new Scanner(System.in);
        String id_sala;
        int año,mes,dia,hora;
        LocalDateTime fecha;
        try {
            System.out.println("dime el id sala");
            id_sala = teclado.nextLine();
            System.out.println("dime el año,el mes,el dia y la hora");
            año = Integer.parseInt(teclado.nextLine());
            mes = Integer.parseInt(teclado.nextLine());
            dia = Integer.parseInt(teclado.nextLine());
            hora = Integer.parseInt(teclado.nextLine());

            fecha = LocalDateTime.of(año, mes, dia, hora, 0, 0);
            if (Reservas.comprobar(con, id_sala, fecha, id_departamento) == 1) {
                String sql;
                Timestamp timestamp1 = Timestamp.valueOf(fecha);
                try {
                    sql = "delete from reservas where id_sala=? and fecha_inicio=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, id_sala);
                    ps.setTimestamp(2, timestamp1);
                    ps.executeUpdate();
                    System.out.println("reserva cancelada");
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }

            } else {
                System.out.println("reserva no encontrada");
            }
        }catch (NumberFormatException nfe){
            System.out.println("error");
        }

    }


    public static int comprobar(Connection con,String id_sala,LocalDateTime fecha,String id_departamento){
        String sql;
        Timestamp timestamp1=Timestamp.valueOf(fecha);
        try {
            sql="select * from reservas where id_departamento=? and id_sala=? and fecha_inicio=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,id_departamento);
            ps.setString(2,id_sala);
            ps.setTimestamp(3,timestamp1);
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


}
