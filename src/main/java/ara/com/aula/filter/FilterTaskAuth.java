package ara.com.aula.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ara.com.aula.repository.IUserRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
      
            var servletPath = request.getServletPath();
            if (servletPath.startsWith("/tasks/")) {
                //Pegar autenticação: user e password
                    var auth = request.getHeader("Authorization");
                    var authEncoded = auth.substring("Basic".length()).trim();

                //Fazendo o Decoder do nosso Basic
                    byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
                    var authString = new String(authDecoded);

                //Separando as credentials
                    String[] credentials = authString.split(":");
                    var username = credentials[0];
                    var password = credentials[1];
            
                //Validar user
                    var userValidated = userRepository.findByUsername(username);
                    if(userValidated == null)
                        response.sendError(401, "Usuario sem autorizacao");
                    else{
                    //Validar senha
                        var passwordVerfy =  BCrypt.verifyer().verify(password.toCharArray(), userValidated.getPassword());
                        if (passwordVerfy.verified) {
                            //Seguir viagem
                            request.setAttribute("userID", userValidated.getId() );
                            filterChain.doFilter(request, response);   
                        }else{
                            response.sendError(401, "Senha incorreta");
                        }
                        
                    }
            }else{
                  //Seguir viagem
                    filterChain.doFilter(request, response); 
            }
            
           
            
    }

   
    
}
