import java.util.Scanner;
import java.sql.Connection;
public class App {
    public static void main(String[] args) {
        Scanner teclado=new Scanner(System.in);
        Connection con;
        String contraseña="12345",cont,id_departamento;
        int opcion1,opcion2,opcion3;
        Boolean lectura1=true,lectura2=true,lecrura3=true;
        if (GestorConexion.crearConexion("practica37", "user10", "A12345a")) {
            con = GestorConexion.getConexion();
            while(lectura1){
                System.out.println("1)Login de Administrador");
                System.out.println("2)Login de Departamento");
                System.out.println("3)Salir");
                opcion1=Integer.parseInt(teclado.nextLine());
                lectura2=true;
                lecrura3=true;
                if(opcion1==1){
                    System.out.println("dime la contraseña");
                    cont=teclado.nextLine();
                    if(cont.equals(contraseña)) {
                        while (lectura2 == true) {
                            System.out.println("1)Listar departamentos \n2)Añadir departamento \n3)Eliminar departamento \n4)Listar salas \n5)Añadir sala \n6)Eliminar sala \n7)Listar todas las reservas \n8)cerrar sesion");
                            opcion2=Integer.parseInt(teclado.nextLine());
                            if(opcion2==1){
                                Departamento.listar(con);
                            } else if (opcion2==2) {
                                Departamento.añadir(con);
                            } else if (opcion2==3) {
                                Departamento.eliminar(con);
                            } else if (opcion2==4) {
                                Salas.listar(con);
                            } else if (opcion2==5) {
                                Salas.añadir(con);
                            } else if (opcion2==6) {
                                Salas.eliminar(con);
                            } else if (opcion2==7) {
                                Reservas.listar(con);
                            } else if (opcion2==8) {
                                lectura2=false;
                            }else{
                                System.out.println("error");
                            }
                        }
                    }else{
                        System.out.println("contraseña erronea");
                    }
                } else if (opcion1==2) {
                    System.out.println("dime el codigo departamento");
                    id_departamento=teclado.nextLine();
                    if(Departamento.comprobar(con,id_departamento)==1) {
                        while (lecrura3) {
                            System.out.println("1)añadir reserva");
                            System.out.println("2)cancelar reserva");
                            System.out.println("3)listar todas reserva");
                            System.out.println("4)cerrar sesion");
                            opcion3=Integer.parseInt(teclado.nextLine());
                            if(opcion3==1){
                                Reservas.añadirReserva(con,id_departamento);
                            } else if (opcion3==2) {
                                Reservas.cancelar(con,id_departamento);
                            } else if (opcion3==3) {
                                Reservas.mostrar(con,id_departamento);
                            } else if (opcion3==4) {
                                lecrura3=false;
                            }else {
                                System.out.println("error");
                            }

                        }
                    }else{
                        System.out.println("ese departamento no existe");
                    }
                } else if (opcion1==3) {
                    lectura1=false;
                } else{
                    System.out.println("error");
                }
            }
        }else{
            System.out.println(GestorConexion.getError());
        }
    }
}
