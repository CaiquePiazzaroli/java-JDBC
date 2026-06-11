import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Map;

public class BancoDados {

    private static Connection conexao = null;

    public BancoDados(String url, String usuario, String senha, boolean autoCommit) {
        try {
            conexao = DriverManager.getConnection(url,usuario,senha);
            conexao.setAutoCommit(autoCommit);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean deletarTabela(String nomeTabela) {
        try {
            String sql = String.format("""
                    DROP TABLE %s;
                    """, nomeTabela);
            PreparedStatement ps = conexao.prepareStatement(sql);
            if(!ps.execute()) {
                throw new Exception("Erro ao excluir a tabela");
            };
            conexao.commit();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            try {
                conexao.rollback();
                return false;
            } catch(SQLException ex) {
                System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
                return false;
            }
        }
    }

    public void criarTabela(String nomeTabela, Map<String, String> colunasTabela) {
        try {
            String sql = String.format(
                    """
                        CREATE TABLE IF NOT EXISTS %s (
                        %s);
                    """, nomeTabela, gerarEstruturaTabela(colunasTabela)
            );
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.execute();
            conexao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conexao.rollback();
            } catch(SQLException ex) {
                System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
            }
        }
    }

    private String gerarEstruturaTabela(Map<String, String> colunasTabela) {
        StringBuilder sb = new StringBuilder();

        for(String chave : colunasTabela.keySet()) {
            sb.append(chave.concat(" ").concat(colunasTabela.get(chave)).concat(","));
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    // CREATE
    public void inserirRegistro(String nomeTabela, Map<String, String> dadosInsercao) {
        try {
            String sql = String.format(
                    """
                        INSERT INTO %s (%s) VALUES (%s);
                    """, nomeTabela, resgatarNomeColunas(dadosInsercao), resgatarplaceholders(dadosInsercao)
            );

            PreparedStatement ps = conexao.prepareStatement(sql);

            int indice = 1;
            for(String chave: dadosInsercao.keySet()) {
                ps.setString(indice, dadosInsercao.get(chave));
                indice++;
            }
            ps.execute();
            conexao.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conexao.rollback();
            } catch(SQLException ex) {
                System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
            }
        }
    }

    public void inserirRegistro(String nomeTabela, Object objetoInsersao) {
        try {
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", nomeTabela, resgatarNomeColunas(objetoInsersao) , resgatarplaceholders(objetoInsersao));
            PreparedStatement ps = conexao.prepareStatement(sql);

            int indice = 1;

            Field[] atributosDaClasse = objetoInsersao.getClass().getDeclaredFields();
            Class<?> classeObjeto = objetoInsersao.getClass();

            for(Field atributo : atributosDaClasse) {

                String nomeAtributo = atributo.getName();
                String nomeGetter = "get" + nomeAtributo.substring(0,1).toUpperCase() + nomeAtributo.substring(1);

                try {
                    Method getter = classeObjeto.getMethod(nomeGetter);
                    Object valor = getter.invoke(objetoInsersao);
                    ps.setObject(indice, valor);
                    indice++;
                } catch (NoSuchMethodException e) {
                    // Ignora campos que não possuem getter padrão (como atributos de controle internos)
                    System.out.println("Aviso: Getter não encontrado para o campo " + nomeAtributo);
                }
            }

            ps.execute();
            conexao.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conexao.rollback();
            } catch(SQLException ex) {
                System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String resgatarNomeColunas(Object objetoInsersao) {
        StringBuilder nomeColunas = new StringBuilder();

        Field[] colunas = objetoInsersao.getClass().getDeclaredFields();

        for(Field coluna: colunas) {
            nomeColunas.append(coluna.getName());
            nomeColunas.append(",");
        }

        nomeColunas.deleteCharAt(nomeColunas.length() - 1);
        return nomeColunas.toString();
    }

    private String resgatarplaceholders(Object objetoInsersao) {
        StringBuilder placeholders = new StringBuilder();

        Field[] colunas = objetoInsersao.getClass().getDeclaredFields();

        for(int i = 0; i < colunas.length; i++) {
            placeholders.append("?");
            placeholders.append(",");
        }

        placeholders.deleteCharAt(placeholders.length() - 1);
        return placeholders.toString();
    }

    private String resgatarNomeColunas(Map<String, String> dadosInsercao) {
        StringBuilder nomeColunas = new StringBuilder();
        for(String coluna: dadosInsercao.keySet()) {
            nomeColunas.append(coluna);
            nomeColunas.append(",");
        }
        nomeColunas.deleteCharAt(nomeColunas.length() - 1);
        return nomeColunas.toString();
    }

    private String resgatarplaceholders(Map<String, String> dadosInsercao) {
        StringBuilder placeholders = new StringBuilder();
        for(int i = 0; i < dadosInsercao.size(); i++) {
            placeholders.append("?");
            placeholders.append(",");
        }
        placeholders.deleteCharAt(placeholders.length() - 1);
        return placeholders.toString();
    }

    // READ
    public void buscarRegistroPorId(String tabela, Integer id, Connection conexao) {
        try {

            String sql = String.format("SELECT * FROM %s WHERE ID = %d;", tabela, id);
            PreparedStatement ps = conexao.prepareStatement(sql);
            ResultSet resultado = ps.executeQuery();
            conexao.commit();

            while (resultado.next()) {
                System.out.println(resultado.getString("nome"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conexao.rollback();
            } catch(SQLException ex) {
                System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
            }

        }
    }

    // UPDATE
    public  void atualizarRegistroPorId(String tabela, String coluna, Integer id, String valor) {
        try {
            String sql = String.format(
                    """
                        UPDATE %s SET %s = ? WHERE id = ?;
                    """, tabela, coluna
            );
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(id, valor);
            ps.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            System.err.println(e);
            try {
                conexao.rollback();
            } catch(SQLException ex) {
                System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
            }
        }
    }

    // DELETE
    public  void excluirRegistroPorId(String tabela, Integer id) {
        try {
            String sql = String.format(
                    """
                        DELETE FROM %s WHERE id = ?;
                    """, tabela
            );
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, id.toString());
            ps.executeUpdate();
            conexao.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conexao.rollback();
            } catch(SQLException ex) {
                System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
            }
        }
    }

    public void buscarTodosRegistros(String tabela) {
        try {
            String sql = String.format("SELECT * FROM %s;", tabela);
            PreparedStatement ps = conexao.prepareStatement(sql);
            ResultSet resultado = ps.executeQuery();
            conexao.commit();
            while (resultado.next()) {
                System.out.println(resultado.getString("nome"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conexao.rollback();
            } catch(SQLException ex) {
                System.err.println("Erro ao tentar aplicar o rollback: " + ex.getMessage());
            }
        }
    }
}
