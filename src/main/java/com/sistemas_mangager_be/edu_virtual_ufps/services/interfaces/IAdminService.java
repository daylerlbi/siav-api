package com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.*;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.AdminDTO;

import java.util.List;

public interface IAdminService {

    AdminDTO registrarAdmin(AdminDTO adminDTO);

    List<AdminDTO> listarAdmins();

    AdminDTO actualizarAdmin(Integer id, AdminDTO adminDTO) throws UserNotFoundException;

    void activarAdmin(Integer id) throws UserNotFoundException, ChangeNotAllowedException;

    void desactivarAdmin(Integer id) throws UserNotFoundException, ChangeNotAllowedException;

    void enviarTokenRecuperacion(String email) throws UserNotFoundException;

    void restablecerpassword(String token, String nuevaPassword, String nuevaPassword2)
            throws TokenNotValidException, EmailNotFoundException, PasswordNotEqualsException;
}
