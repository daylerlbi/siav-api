package com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces;


import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.RoleNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.UserExistException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.UserNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.UsuarioDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.DocenteRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.LoginGoogleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MoodleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.UsuarioResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;


// import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.RoleNotFoundException;
// import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.UserNotFoundException;
// import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.UsuarioDTO;

public interface IUsuarioService {

    //public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) throws RoleNotFoundException, UserNotFoundException;

    void registraroActualizarUsuarioGoogle(LoginGoogleRequest loginGoogleRequest) throws UserExistException;

    void guardarOActualizarUsuario(OAuth2User oAuth2User);

    UsuarioDTO crearProfesor(DocenteRequest docenteRequest) throws RoleNotFoundException, UserExistException;

    void vincularMoodleId(MoodleRequest moodleRequest) throws UserNotFoundException, UserExistException;

    UsuarioDTO actualizarProfesor(DocenteRequest docenteRequest, Integer id)
            throws RoleNotFoundException, UserExistException, UserNotFoundException;

    List<UsuarioResponse> listarUsuariosPorRol(Integer rolId) throws RoleNotFoundException;

    UsuarioResponse listarUsuario(Integer id) throws UserNotFoundException;

    List<UsuarioResponse> listarUsuarios();
} 