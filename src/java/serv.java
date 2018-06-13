
import Persistencia.JsonProvaFactory;
import org.json.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author reida
 */
@WebServlet(urlPatterns = {"/serv"})
public class serv extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("conecção");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String tipoReq = request.getHeader("tipo");
        String req  = request.getParameter("tipo");
        System.out.println("Tipo: "+tipoReq+" req: "+req);
        
        /*try*/ PrintWriter out = response.getWriter(); //{
            System.out.println("REQUEISIÇÃO: "+tipoReq);
            switch(tipoReq){
                case "buscarProva":
                    String idProva = request.getHeader("idProva");
                    JSONObject jsonProva = JsonProvaFactory.getProva(idProva);
                    out.print(jsonProva);
                    System.out.println("buscarProva ===>"+jsonProva);
                    break;
                case "listar":
                    JSONObject jsonTipoProva = JsonProvaFactory.jsonTipoProva();
                    out.print(jsonTipoProva);
                    System.out.println("listar ===>"+jsonTipoProva);
                    break;
                case "listarProvas":
                    String tipoProva = request.getHeader("tipoProva");
                    JSONObject jsonListaProvas = JsonProvaFactory.getListaProvas(tipoProva);
                    out.print(jsonListaProvas);
                    System.out.println("listarProvas ===>"+jsonListaProvas);
                    break;
                case "getImageQuest":
                    String imgId = request.getHeader("imageId");
                    JSONObject imagem = JsonProvaFactory.getImagem(imgId);
                    out.print(imagem);
                    System.out.println("imagem ===>"+imagem);
                    break;
                case "getDadosUsuario":
                    String email = request.getHeader("email");
                    JSONObject jsonDataUser = JsonProvaFactory.getUserData(email);
                    System.out.println("DATAUSER "+email+" data:"+jsonDataUser);
                    out.print(jsonDataUser);
                    break;
                case "cadastro":
                    String emailC = request.getHeader("email");
                    String senhaC = request.getHeader("senha");
                    String nome = request.getHeader("nome");
                    String result = JsonProvaFactory.cadastrarNovoUsuario(nome, emailC, senhaC);
                    out.print(result);
                    System.out.println("cadastro de :"+emailC+" : "+result);
                    break;
                case "atualizarEstatisticasUsuario":
                    String acertos = request.getHeader("acertos");
                    String erros = request.getHeader("erros");
                    String emailAt = request.getHeader("email");
                    String resultAt = JsonProvaFactory.salvarDadosProva(acertos, erros, emailAt);
                    System.out.println("INFORME email "+emailAt+" acertos "+acertos
                            +" erros "+erros+ "result "+resultAt);
                    out.print(resultAt);
                    break;
                 
                 
            }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
