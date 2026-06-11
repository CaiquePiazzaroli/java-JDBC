import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class Mock {

    public static Map<String, String> tabelaCliente = Map.of(
            "id", "INT NOT NULL AUTO_INCREMENT PRIMARY KEY",
            "nome", "VARCHAR(150) NOT NULL",
            "dataNascimento", "date not null",
            "pontuacao", "DECIMAL(10,2) NOT NULL DEFAULT 0.00",
            "cpf", "INT NOT NULL"
    );


    public static ArrayList<LinkedHashMap<String, String>> buscarDadosUsuario() {
        // Inicializa a lista que guardará todos os usuários
        ArrayList<LinkedHashMap<String, String>> listaUsuarios = new ArrayList<>();

        // Usuário 1 (Caique Mendes)
        LinkedHashMap<String, String> usuario1 = new LinkedHashMap<>();
        usuario1.put("nome", "Caique Mendes");
        usuario1.put("dataNascimento", "1996-08-03");
        usuario1.put("pontuacao", "8.50");
        usuario1.put("cpf", "45612");
        listaUsuarios.add(usuario1);

        // Usuário 2 (Fictício)
        LinkedHashMap<String, String> usuario2 = new LinkedHashMap<>();
        usuario2.put("nome", "Amanda Oliveira");
        usuario2.put("dataNascimento", "1993-11-22");
        usuario2.put("pontuacao", "9.20");
        usuario2.put("cpf", "78945");
        listaUsuarios.add(usuario2);

        // Usuário 3 (Fictício)
        LinkedHashMap<String, String> usuario3 = new LinkedHashMap<>();
        usuario3.put("nome", "Bruno Santos");
        usuario3.put("dataNascimento", "2001-05-14");
        usuario3.put("pontuacao", "7.10");
        usuario3.put("cpf", "12365");
        listaUsuarios.add(usuario3);

        // Usuário 4 (Fictício)
        LinkedHashMap<String, String> usuario4 = new LinkedHashMap<>();
        usuario4.put("nome", "Camila Rocha");
        usuario4.put("dataNascimento", "1988-02-30"); // Exemplo simulado
        usuario4.put("pontuacao", "6.80");
        usuario4.put("cpf", "95175");
        listaUsuarios.add(usuario4);

        // Usuário 5 (Fictício)
        LinkedHashMap<String, String> usuario5 = new LinkedHashMap<>();
        usuario5.put("nome", "Daniel Almeida");
        usuario5.put("dataNascimento", "1995-07-09");
        usuario5.put("pontuacao", "9.90");
        usuario5.put("cpf", "35742");
        listaUsuarios.add(usuario5);

        // Retorna a lista completa com os 5 registros
        return listaUsuarios;
    }




}
