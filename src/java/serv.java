
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
                //getParameter("tipo");
        
        /*try*/ PrintWriter out = response.getWriter(); //{
            
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
                case "login":
                    String email = request.getHeader("email");
                    String senha = request.getHeader("senha");
                    boolean validaAcesso = JsonProvaFactory.validarLogin(email, senha);
                    out.print(validaAcesso);
                    System.out.println("Acesso de :"+email+" : "+validaAcesso);
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
