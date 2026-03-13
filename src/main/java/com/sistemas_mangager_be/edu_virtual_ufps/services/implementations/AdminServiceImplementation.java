package com.sistemas_mangager_be.edu_virtual_ufps.services.implementations;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Admin;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.*;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.AdminRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.security.JwtTokenGenerator;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IAdminService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.AdminDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImplementation implements IAdminService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "No esta permitido %s ";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcta";

    private final PasswordEncoder passwordEncoder;

    private final AdminRepository adminRepository;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final EmailService emailService;

    @Value("${app.frontend.mail.url}")
    private String frontendMailUrl;

    @Override
    public AdminDTO registrarAdmin(AdminDTO adminDTO) {
        Admin admin = new Admin();
        boolean existeAdmin = adminRepository.existsByEmail(adminDTO.getEmail());
        if (existeAdmin) {
            throw new IllegalArgumentException(String.format(IS_ALREADY_USE, "El correo"));
        }
        BeanUtils.copyProperties(adminDTO, admin);
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        admin.setActivo(true);
        adminRepository.save(admin);

        AdminDTO adminCreado = new AdminDTO();
        BeanUtils.copyProperties(admin, adminCreado);
        return adminCreado;
    }

    public AdminDTO actualizarAdmin(Integer id, AdminDTO adminDTO) throws UserNotFoundException {
        Admin admin = adminRepository.findById(id).orElse(null);

        if (admin == null) {
            throw new UserNotFoundException("El administrador no fue encontrado");
        }

        boolean existeAdmin = adminRepository.existsByEmail(adminDTO.getEmail());
        if (existeAdmin && !admin.getEmail().equals(adminDTO.getEmail())) {
            throw new IllegalArgumentException(String.format(IS_ALREADY_USE, "El correo"));
        }
        BeanUtils.copyProperties(adminDTO, admin, "id", "activo");
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        adminRepository.save(admin);

        AdminDTO adminActualizado = new AdminDTO();
        BeanUtils.copyProperties(admin, adminActualizado);
        return adminActualizado;
    }

    public void desactivarAdmin(Integer id) throws UserNotFoundException, ChangeNotAllowedException {
        Admin admin = adminRepository.findById(id).orElse(null);

        if (admin == null) {
            throw new UserNotFoundException("El administrador no fue encontrado");
        }

        if (admin.getEsSuperAdmin() == true) {
            throw new ChangeNotAllowedException(String.format(IS_NOT_ALLOWED, "desactivar un superadmin"));
        }

        admin.setActivo(false);
        adminRepository.save(admin);
    }

    public void activarAdmin(Integer id) throws UserNotFoundException, ChangeNotAllowedException {
        Admin admin = adminRepository.findById(id).orElse(null);

        if (admin == null) {
            throw new UserNotFoundException("El administrador no fue encontrado");
        }

        if (admin.getEsSuperAdmin() == true) {
            throw new ChangeNotAllowedException(String.format(IS_NOT_ALLOWED, "activar un superadmin"));
        }

        admin.setActivo(true);
        adminRepository.save(admin);
    }

    public List<AdminDTO> listarAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream().map(admin -> {
            AdminDTO adminDTO = new AdminDTO();
            BeanUtils.copyProperties(admin, adminDTO);
            return adminDTO;
        }).toList();
    }

    public void enviarTokenRecuperacion(String email) throws UserNotFoundException {
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "El administrador"));
        }

        String tokenRecuperacion = jwtTokenGenerator.generarTokenRecuperacion(email);

        String urlRecuperacion = frontendMailUrl + tokenRecuperacion;

        emailService.sendEmailPassword(email, "Solicitud de Cambio de Contraseña",
                "Para cambiar la contraseña y completar la solicitud, por favor presione el siguiente boton:",
                urlRecuperacion);
    }

    public void restablecerpassword(String token, String nuevaPassword, String nuevaPassword2)
            throws TokenNotValidException, EmailNotFoundException, PasswordNotEqualsException {
        String email = jwtTokenGenerator.obtenerCorreoDeJWT(token);

        if (email == null) {
            throw new TokenNotValidException(String.format(IS_NOT_VALID, "EL TOKEN").toUpperCase());
        }

        Admin usuario = adminRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "EL CORREO").toUpperCase());
        }

        if (!nuevaPassword.equals(nuevaPassword2)) {
            throw new PasswordNotEqualsException(String.format(ARE_NOT_EQUALS, "LAS NUEVAS CONTRASEÑAS").toUpperCase());
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        adminRepository.save(usuario);
    }
}
