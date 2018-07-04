package Persistencia;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
  Classe de pércistência de provas
 * @author Davy Lima
 */
public class DaoProva {
    
    /**
     * Busca na base de dados os tipos de provas cadastrados
     * @return Uma lista contendo todos os tipos de provas
     * @throws SQLException 
     */
    public static List<String> buscarTipoProva() throws SQLException, Exception{
        Connection conexao = Conexao.getConexao();
            if(conexao != null){
                String sql = "SELECT Tipo FROM TipoProva";

                PreparedStatement preparedStatement;
                preparedStatement = conexao.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                List tipos = new ArrayList<>();

                while(resultSet.next()){
                    tipos.add(resultSet.getString("Tipo"));
                }
                preparedStatement.close();
                conexao.close();
                return tipos;
        }
            return null;
    }
    
   
    /**
     * Busca na base de dados as questões que compoem uma prova
     * @param nomeProva identificador da base de dados para a referente prova
     * @return Lista contendo todas as questões da prova
     * @throws SQLException
     * @throws Exception 
     */
    public static List<Prova.Questao> buscarQuestoesProva(String nomeProva) throws SQLException, Exception{
        Connection conexao = Conexao.getConexao();
        
        String sql = "SELECT * FROM Questoes q join prova p where q.idProva = "
                + "p.idProva and p.Nome like ?";
        
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setString(1, nomeProva);
        ResultSet resultSet = ps.executeQuery();
        List<Prova.Questao> quest = new ArrayList<>();
        while(resultSet.next()){
            Prova.Questao q = new Prova.Questao();
            q.setBody(resultSet.getString("body"));
            q.setOptionA(resultSet.getString("optionA"));
            q.setOptionB(resultSet.getString("optionB"));
            q.setOptionC(resultSet.getString("optionC"));
            q.setOptionD(resultSet.getString("optionD"));
            q.setOptionE(resultSet.getString("optionE"));
            q.setAnswer(resultSet.getString("answer"));
            if(resultSet.getString("image") != null){
                String imgId = resultSet.getString("idQuestoes");
                q.setImage(imgId);
            }else{
                q.setImage("");
            }
            quest.add(q);
        }
        ps.close();
        conexao.close();
        return quest;
    }

    /**
     * Consulta todas as provas de um determinado tipo
     * @param tipoProva Tipo de prova a ser consultado
     * @return lista contendo todas as ptovas
     * @throws java.sql.SQLException Ocorre quando há alguma inconsistencia na 
     * base de dados de acorodo com os paramaetros passados
     */
    public static List<List> listarProvas(String tipoProva) throws SQLException, Exception {
        Connection conexao = Conexao.getConexao();
        String sql = "SELECT * FROM prova WHERE idTipoProva = (SELECT idTipoProva FROM tipoprova WHERE tipo LIKE ?)";
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setString(1, tipoProva);
        ResultSet rs = ps.executeQuery();
        List<List> lista = new ArrayList();
        while(rs.next()){
            List prova = new ArrayList();
            prova.add(rs.getString("idProva"));
            prova.add(rs.getString("Nome"));
            prova.add(rs.getString("QtdQuestoes"));
            prova.add(rs.getString("idTipoProva"));
            lista.add(prova);
        }
        ps.close();
        conexao.close();
        return lista;
    }
    
    /**
     * Busca na base de dados de uma questão, isso inclui todos os textos e id's 
     * das imagens relacionadas as questões
     * @param idProva id da prova
     * @return lista contendo todas as questões da prova em texto acompanhadas 
     * dos id's das imagens
     * @throws Exception 
     */
    public static List buscarDadosProva(String tipoProva) throws Exception{
        Connection conexao = Conexao.getConexao();
        String sql = "SELECT idProva, Nome, QtdQuestoes, idProva FROM prova WHERE Nome like ?";
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setString(1, tipoProva);
        ResultSet rs = ps.executeQuery();
        List lista = new ArrayList();
        if(rs.next()){
            lista.add(rs.getString("idProva"));
            lista.add(rs.getString("Nome"));
            lista.add(rs.getString("QtdQuestoes"));
            lista.add(rs.getString("idProva"));
            return lista;
        }
        ps.close();
        conexao.close();
        return null;
    }

    public static String buscarImagemQuestao(String imageId)throws Exception {
        Connection conexao = Conexao.getConexao();
        String sql = "SELECT image FROM questoes WHERE idQuestoes = ?";
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setInt(1, Integer.valueOf(imageId));
        ResultSet rs = ps.executeQuery();
        rs.first();
        String result = rs.getString("image");
        conexao.close();
        return result;
    }

    public static boolean validarLogin(String email, String senha)throws Exception {
        Connection conexao = Conexao.getConexao();
        String sql = "SELECT EXISTS ( SELECT u.email, u.senha FROM usuario u WHERE u.email like ? and u.senha = ? ) as 'validacao'";
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, senha);
        ResultSet rs = ps.executeQuery();
        rs.first();
        boolean res = rs.getString("validacao").equals("1");
        conexao.close();
        return res;
    }

    public static String cadastrarNovoUsuario(String nome, String email, String senha
            ,String estado, String cidade, String sexo, String data) throws Exception {
        Connection conexao = Conexao.getConexao();
        conexao.setAutoCommit(false);
        String sql = "INSERT INTO usuario(email, Nome, senha, uf, cidade, gen, dataNasc) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, nome);
        ps.setString(3, senha);
        ps.setString(4, estado);
        ps.setString(5, cidade);
        ps.setString(6, sexo);
        ps.setString(7, data);
        int executeUpdate = ps.executeUpdate();
        conexao.commit();
        conexao.setAutoCommit(true);
        conexao.close();
        if(executeUpdate == 1){
            newHistory(email);
            return "Cadastrado";
        }else
            return "Não foi possivel realizar o cadastro";
    }

    public static String salvarDadosProva(String acertos, String erros, String email) throws SQLException, Exception {
        Connection conexao = Conexao.getConexao();
        conexao.setAutoCommit(false);
        
        String sql = "UPDATE usuario SET numQuestAcert = numQuestAcert + ?,  "
                + "numQuestErr =  numQuestErr + ?, numProv = numProv + 1"
                + " WHERE email = ?";
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(acertos));
        ps.setInt(2, Integer.parseInt(erros));
        ps.setString(3, email);
        int result = ps.executeUpdate();
        conexao.commit();
        conexao.setAutoCommit(true);
        conexao.close();
        if(result == 1)
            return "ok";
        else
            return "Não foi possivel atualizar os dados do usuário com relação à prova";
    }
    
    public static String getNomeUsuariio(String email) throws SQLException, Exception{
         Connection conexao = Conexao.getConexao();
        conexao.setAutoCommit(false);
        
        String sql = "SELECT Nome FROM usuario WHERE email like ?";
        
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setString(1, email);
        String retorno;
        ResultSet executeQuery = ps.executeQuery();
        if(executeQuery.first()){
            retorno = executeQuery.getString(1);
            conexao.close();
            return retorno;
        }else{
            conexao.close();
            throw new Exception("Dados não encontrados");
        }
    }

    public static String[] buscarDadosUsuario(String email) throws Exception {
        Connection conexao = Conexao.getConexao();
        conexao.setAutoCommit(false);
        
        String sql = "SELECT numQuestAcert, numQuestErr FROM usuario WHERE email like ?";
        
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet executeQuery = ps.executeQuery();
        String[] retorno = new String[2];
        if(executeQuery.first()){
            retorno[0] = executeQuery.getString(1);
            retorno[1] = executeQuery.getString(2);
            conexao.close();
            return retorno;
        }else{
            conexao.close();
            throw new Exception("Dados não encontrados");
        }
    }
        
    public static void newHistory(String email) throws Exception {
        String sql = "insert into historico_acertos (idUsuario) select u.idUsuario"
                + " from usuario u where u.email like ? ";
        PreparedStatement sp = Conexao.getConexao().prepareStatement(sql);
        sp.setString(1, email);
        int exup = sp.executeUpdate();
        if(exup != 1)
            throw new ExecutionException("Impossivel criar historico", new Exception("Canot execute insert"));
    }

    public static void updateHistory(int acertos, String email) throws Exception{
        String sql = "select idUsuario from usuario where email like ?";
        PreparedStatement ps = Conexao.getConexao().prepareStatement(sql);
        ps.setString(1, email);
        ResultSet executeQuery = ps.executeQuery();
        String idUsuario = executeQuery.getString("idUsuario");
        String sql2 = "call updateHistory(?,?)";
        CallableStatement prepareCall = Conexao.getConexao().prepareCall(sql2);
        prepareCall.setInt(1, acertos);
        prepareCall.setInt(2, Integer.valueOf(idUsuario));
        ResultSet rs = prepareCall.executeQuery();
        prepareCall.close();
        executeQuery.close();
   }

    static String[] getHistorico(String email) throws Exception {
        String sql = "select h.teste1, h.teste2, h.teste3, h.teste4, h.teste5 "
                + "from historico_acertos h where h.idUsuario = (select u.idUsuario "
                + "from usuario u where u.email like ? )";
        PreparedStatement sp = Conexao.getConexao().prepareStatement(sql);
        sp.setString(1, email);
        ResultSet exq = sp.executeQuery();
        if(exq.first()){
            String[] res = new String[5];
            res[0] = exq.getString("teste1");
            res[1] = exq.getString("teste2");
            res[2] = exq.getString("teste3");
            res[3] = exq.getString("teste4");
            res[4] = exq.getString("teste5");
            
            return res;
        }
        return null;
    }
    
}
