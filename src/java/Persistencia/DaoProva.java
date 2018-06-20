package Persistencia;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * @param provaId identificador da base de dados para a referente prova
     * @return Lista contendo todas as questões da prova
     * @throws SQLException
     * @throws Exception 
     */
    public static List<Prova.Questao> buscarQuestoesProva(int provaId) throws SQLException, Exception{
        Connection conexao = Conexao.getConexao();
        
        String sql = "SELECT * FROM Questoes WHERE idProva = ?";
        
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setInt(1, provaId);
        ResultSet resultSet = ps.executeQuery();
        FileWriter arq = new FileWriter("D:\\Projetos Android\\webServiceIc\\resultset.txt");
        PrintWriter grava = new PrintWriter(arq);
        List<Prova.Questao> quest = new ArrayList<>();
        while(resultSet.next()){
            Prova.Questao q = new Prova.Questao();
            q.setBody(resultSet.getString("body")); grava.println("Body === "+resultSet.getString("body"));grava.println();
            q.setOptionA(resultSet.getString("optionA"));grava.println("optionA === "+resultSet.getString("optionA"));grava.println();
            q.setOptionB(resultSet.getString("optionB"));grava.println("optionB ==="+resultSet.getString("optionB"));grava.println();
            q.setOptionC(resultSet.getString("optionC"));grava.println("optionC === "+resultSet.getString("optionC"));grava.println();
            q.setOptionD(resultSet.getString("optionD"));grava.println("optionD === "+resultSet.getString("optionD"));grava.println();
            q.setOptionE(resultSet.getString("optionE"));grava.println("optionE === "+resultSet.getString("optionE"));grava.println();
            q.setAnswer(resultSet.getString("answer"));grava.printf("Answer === "+resultSet.getString("answer"));grava.println();
            grava.println("numero >>>>>>>>>> "+resultSet.getString("numero"));grava.println();
            if(resultSet.getString("image") != null){
                String imgId = resultSet.getString("idQuestoes");grava.println("Image === "+resultSet.getString("image").length());grava.println();
                q.setImage(imgId);
            }else{
                q.setImage("");grava.println("img null");grava.println("===============");grava.println();
            }
            quest.add(q);
        }
        arq.close();
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
    public static List buscarDadosProva(int idProva) throws Exception{
        Connection conexao = Conexao.getConexao();
        String sql = "SELECT idProva, Nome, QtdQuestoes, idProva FROM prova WHERE idProva = ?";
        PreparedStatement ps;
        ps = conexao.prepareStatement(sql);
        ps.setInt(1, idProva);
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

    static String buscarImagemQuestao(String imageId)throws Exception {
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

    static boolean validarLogin(String email, String senha)throws Exception {
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

    static String cadastrarNovoUsuario(String nome, String email, String senha
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
        if(executeUpdate == 1)
            return "Cadastrado";
        else
            return "Não foi possivel realizar o cadastro";
    }

    static String salvarDadosProva(String acertos, String erros, String email) throws SQLException, Exception {
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

    static String[] buscarDadosUsuario(String email) throws Exception {
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
}
