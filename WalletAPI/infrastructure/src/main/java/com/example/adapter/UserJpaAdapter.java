package com.example.adapter;

import com.example.data.JwtRequest;
import com.example.data.JwtResponse;
import com.example.cfg.JwtTokenUtil;
import com.example.data.UserDTO;
import com.example.entity.User;
import com.example.ports.spi.UserPersistencePort;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserJpaAdapter implements UserPersistencePort, UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    /**
     * Método para crear un nuevo usuario.
     * @param userDTO - Objeto UserDTO que contiene la información necesaria del nuevo usuario a registrar:
     *                  nombre, apellidos, login y la contraseña en claro.
     * @return ResponseEntity indicando si se ha realizado correctamente el registro del usuario o ha ocurrido algún error.
     */
    @Override
    public ResponseEntity createUser(UserDTO userDTO){
        String outputMsg = "";

        User newUser = new User(userDTO);

        List<User> userList = userRepository.findAll();

        boolean found = false;
        for (User user: userList){
            if(newUser.getLogin().equals(user.getLogin()))
                found = true;
        }
        if (found){
            outputMsg = "No se puede dar de alta al empleado con login '" + newUser.getLogin() + "' porque ya esta dado de alta";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg +"\"}", HttpStatus.BAD_REQUEST);
        }
        else {
            newUser.setPasswordHash(BCrypt.hashpw(userDTO.getPasswordRaw(),BCrypt.gensalt()));
            userRepository.save(newUser);

            outputMsg = "Usuario con login " + newUser.getLogin() + " creado correctamente.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.ACCEPTED);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);

        if (user == null)
            throw new UsernameNotFoundException("No se ha encontrado el usuario con login: " + username);

        return new org.springframework.security.core.userdetails.User(user.getLogin(),user.getPasswordHash(),new ArrayList<>());
    }

    /**
     * Método para autenticar el usuario y contraseña introducidos y generar el JWT necesario para acceder al resto de recursos
     * @param authRequest - Objeto de tipo JwtRequest que contiene el usuario y contraseña enviados por la petición
     * @return ResponseEntity que contiene el JWT en caso de que el usuario y contraseña estén registrados en la BBDD.
     * @throws Exception
     */
    @Override
    public ResponseEntity createAuthenticationToken(JwtRequest authRequest) throws Exception {
        String outputMsg = "";
        authenticate(authRequest.getLogin(), authRequest.getPassword());

        UserDetails userDetails = loadUserByUsername(authRequest.getLogin());

        if (BCrypt.checkpw(authRequest.getPassword(),userDetails.getPassword())){
            final String token = jwtTokenUtil.generateToken(userDetails);
            return new ResponseEntity<>("{\"token\": \"" + token + "\"}", HttpStatus.ACCEPTED);
        } else {
            outputMsg = "Login y/o contraseña incorrectos.";
            return new ResponseEntity<>("{\"message\": \"" + outputMsg + "\"}", HttpStatus.UNAUTHORIZED);
        }
    }

    private void authenticate(String login, String password) throws Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login,password));
        } catch (BadCredentialsException e){
            throw new Exception("INVALID CREDENTIALS", e);
        }
    }
}
