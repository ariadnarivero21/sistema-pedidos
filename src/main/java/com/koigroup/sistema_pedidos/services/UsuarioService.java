package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Usuario;
import com.koigroup.sistema_pedidos.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
}
