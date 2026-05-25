import java.sql.*;

public class Main {
    public static void main(String[] args) {
        controlarRollBackCommit();
    }

    public  static  void  abrirConexao() {
        try{
            // 1 - Metodo tradicional
            Connection connection = DriverManager.getConnection(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha);
            System.out.println("Conectado com sucesso!");
            connection.close();

            // 2 - Ou com try with resources
            try(Connection conexao = DriverManager.getConnection(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha)) {
                System.out.println("Conectado com sucesso! Usando try-with-resources");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void criarTabela() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS clientes (
                    id INT NOT NULL AUTO_INCREMENT,
                    nomeCliente VARCHAR(150) NOT NULL,
                    PRIMARY KEY (id))
                """;

        try {
            try(Connection conexao = DriverManager.getConnection(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha)) {
                PreparedStatement ps = conexao.prepareStatement(sql);
                ps.execute();
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void inserirRegistro() {
        try {
            try(Connection conexao = DriverManager.getConnection(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha)) {

                String sql = """
                            INSERT INTO clientes (id, nomeCliente) VALUES (default, ?);
                        """;

                PreparedStatement ps = conexao.prepareStatement(sql);

                ps.setString(1, "Caique");

                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void atualizarRegistro() {
        try {
            try(Connection conexao = DriverManager.getConnection(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha)) {

                String sql = """
                            UPDATE clientes SET nomeCliente = ? WHERE id = ?;
                        """;

                PreparedStatement ps = conexao.prepareStatement(sql);

                ps.setString(1, "Caique Piazzaroli");
                ps.setString(2, "1");

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void excluirRegistro() {
        try {
            try(Connection conexao = DriverManager.getConnection(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha)) {

                String sql = """
                            DELETE FROM clientes WHERE id = ?;
                        """;

                PreparedStatement ps = conexao.prepareStatement(sql);

                ps.setString(1, "1");

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void buscarRegistro() {
        try {
            try(Connection conexao = DriverManager.getConnection(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha)) {
                // 1. Criação e preparação do SQL
                String sql = """
                            SELECT * FROM clientes;
                        """;
                PreparedStatement ps = conexao.prepareStatement(sql);

                // 2. Objeto ResultSet que recebe o resultado
                ResultSet resultado = ps.executeQuery();

                // 3. Iterando sobre o resultado e exibindo seus dados
                while (resultado.next()) {
                    int id = resultado.getInt("id");
                    String nomeCliente = resultado.getString("nomeCliente");

                    System.out.println("O id do cliente é " + id + " e seu nome é " + nomeCliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  static void controlarRollBackCommit() {
        Connection conexao = null;
        try {
                conexao = DriverManager.getConnection(DadosBanco.url, DadosBanco.usuario, DadosBanco.senha);

                // 1. Desativa o auto-commit (inicia a transação)
                conexao.setAutoCommit(false);

                // 2. Construindo uma query de exemplo
                String sql = """
                    INSERT INTO clientes(id, nomeCliente) VALUES (?, ?)
                """;
                PreparedStatement ps = conexao.prepareStatement(sql);
                ps.setString(1, "1");
                ps.setString(2, "Domingues");

                //3. Executando a query
                ps.executeUpdate();

                //4. se der tudo certo commita
                conexao.commit();
                System.out.println("Transação concluída com sucesso!");

        } catch (Exception e) {
            //5. Se der errado, rollback
            e.printStackTrace();
            if(conexao != null) {
                try {
                    conexao.rollback();
                    System.out.println("Rollback executado com sucesso! Os dados foram preservados.");
                } catch (SQLException ex) {
                    System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
                }
            }
        } finally {
            //6. Fecha a conexão
            if (conexao != null) {
                try {
                    conexao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
