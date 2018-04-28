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

    public static List<List> listarProvas(String tipoProva)throws Exception {
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
    
    public static List buscarDadosProva(int idProva)throws Exception {
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
        return rs.getString("image");
    }
}
