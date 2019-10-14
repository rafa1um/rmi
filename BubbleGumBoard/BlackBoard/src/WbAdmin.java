import java.rmi.Naming;

public class WbAdmin {
    public static void main(String[] args) {
        if (args[0].compareTo("-q") == 0) {
            try {
                WriteBoard bs = (WriteBoard) Naming.lookup("rmi://" + args[1] + ":" + args[2] + "/BlackBoard");
                bs.wbAdminConsulta();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (args[0].compareTo("-t") == 0) {
            //Troca de servidor
        } else {
            System.out.println("Comando inexistente.");
        }
    }
}
