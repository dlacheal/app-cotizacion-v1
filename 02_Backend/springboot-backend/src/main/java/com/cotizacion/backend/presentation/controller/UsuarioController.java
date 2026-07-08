package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.presentation.dto.request.UsuarioRegistroDTO;
import com.cotizacion.backend.presentation.dto.request.UsuarioUpdateDTO;
import com.cotizacion.backend.presentation.dto.request.LoginRequestDTO;
import com.cotizacion.backend.presentation.dto.response.LoginResponseDTO;
import com.cotizacion.backend.service.interfaces.UsuarioService;
import com.cotizacion.backend.service.interfaces.RolService;
import com.cotizacion.backend.persistence.entity.Usuario;
import com.cotizacion.backend.persistence.repository.PersonaRepository;
import com.cotizacion.backend.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PersonaRepository personaRepository;
    private final RolService rolService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/usuarios")
    public ResponseEntity<Page<Usuario>> listarTodos(
            @PageableDefault(size = 10, sort = "idUsuario") Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        Usuario usuario = usuarioService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + userDetails.getUsername()));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtTokenProvider.generateToken(userDetails.getUsername(), roles);

        String nombrePersona = usuario.getPersona() != null ? usuario.getPersona().getNombre() : usuario.getUsername();

        return ResponseEntity.ok(new LoginResponseDTO(token, userDetails.getUsername(), nombrePersona, roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid UsuarioRegistroDTO registroDTO) {
        try {
            if (usuarioService.findByUsername(registroDTO.username()).isPresent()) {
                return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
            }
            var personaOpt = personaRepository.findById(registroDTO.idPersona());
            if (personaOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("La persona no existe");
            }

            Usuario usuario = new Usuario();
            usuario.setUsername(registroDTO.username());
            usuario.setPassword(registroDTO.password());
            usuario.setPersona(personaOpt.get());
            usuario.setEnable(true);

            Usuario registrado = usuarioService.registrarUsuario(usuario);
            
            if (registroDTO.roles() != null && registroDTO.roles().length > 0) {
                for (String rolName : registroDTO.roles()) {
                    rolService.addRoleToUsuario(registrado.getIdUsuario(), rolName.startsWith("ROLE_") ? rolName : "ROLE_" + rolName);
                }
            } else {
                rolService.addRoleToUsuario(registrado.getIdUsuario(), "ROLE_USER");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("message", "Usuario registrado con éxito"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error al registrar usuario: " + e.getMessage()));
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody @Valid UsuarioUpdateDTO updateDTO) {
        return usuarioService.findById(id)
                .map(usuarioExistente -> {
                    var otroUser = usuarioService.findByUsername(updateDTO.username());
                    if (otroUser.isPresent() && !otroUser.get().getIdUsuario().equals(id)) {
                        return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
                    }
                    
                    var personaOpt = personaRepository.findById(updateDTO.idPersona());
                    if (personaOpt.isEmpty()) {
                        return ResponseEntity.badRequest().body("La persona vinculada no existe");
                    }

                    usuarioExistente.setUsername(updateDTO.username());
                    usuarioExistente.setPersona(personaOpt.get());
                    if (updateDTO.enable() != null) {
                        usuarioExistente.setEnable(updateDTO.enable());
                    }

                    if (updateDTO.password() != null && !updateDTO.password().isBlank()) {
                        usuarioExistente.setPassword(passwordEncoder.encode(updateDTO.password()));
                    }

                    Usuario actualizado = usuarioService.save(usuarioExistente);

                    if (updateDTO.roles() != null) {
                        List<com.cotizacion.backend.persistence.entity.Rol> currentRoles = rolService.findRolesByUsuario(id);
                        List<String> currentRoleNames = currentRoles.stream()
                                .map(com.cotizacion.backend.persistence.entity.Rol::getNombre)
                                .toList();
                        
                        List<String> targetRoleNames = java.util.Arrays.stream(updateDTO.roles())
                                .map(roleName -> roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName)
                                .toList();

                        for (String targetRole : targetRoleNames) {
                            if (!currentRoleNames.contains(targetRole)) {
                                rolService.addRoleToUsuario(id, targetRole);
                            }
                        }

                        for (String currentRole : currentRoleNames) {
                            if (!targetRoleNames.contains(currentRole)) {
                                rolService.removeRoleFromUsuario(id, currentRole);
                            }
                        }
                    }

                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        if (usuarioService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
