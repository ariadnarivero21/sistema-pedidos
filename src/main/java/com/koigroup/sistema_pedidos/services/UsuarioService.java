package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.entities.Usuario;
import com.koigroup.sistema_pedidos.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> findById(Long id){
        return usuarioRepository.findById(id);
    }
}
