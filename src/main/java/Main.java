import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {

            BancoDados db = new BancoDados(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha, false);
            Cliente cl = new Cliente("Marquinho Pe de frango", LocalDate.of(1986, 02, 04), 8.8, 456687L);
            db.inserirRegistro("clientes", cl);
            db.buscarTodosRegistros("clientes");

        } catch (Exception e) {
            System.out.println(e);
        }


    }
}
