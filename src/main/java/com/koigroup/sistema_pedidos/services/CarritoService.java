package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.DTO.CarritoDTO;
import com.koigroup.sistema_pedidos.DTO.UsuarioCarritoDTO;
import com.koigroup.sistema_pedidos.DTO.request.NuevoProductoRequest;
import com.koigroup.sistema_pedidos.entities.Carrito;
import com.koigroup.sistema_pedidos.entities.CarritoItem;
import com.koigroup.sistema_pedidos.entities.Producto;
import com.koigroup.sistema_pedidos.entities.Usuario;
import com.koigroup.sistema_pedidos.exception.*;
import com.koigroup.sistema_pedidos.repositories.CarritoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);
    private final CarritoRepository carritoRepository;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final CarritoItemService carritoItemService;

    public CarritoService(CarritoRepository carritoRepository, UsuarioService usuarioService,
                          ProductoService productoService, CarritoItemService carritoItemService) {
        this.carritoRepository = carritoRepository;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.carritoItemService = carritoItemService;
    }

    public Optional<Carrito> getByCodigo(String codigo) {
        return carritoRepository.findByCodigo(codigo);
    }

    public List<UsuarioCarritoDTO> getAllByUsuarioId(Long idUsuario) {

        List<Carrito> carritos = carritoRepository.findByUsuarioId(idUsuario);

        return carritos.stream()
                .map(carrito -> new UsuarioCarritoDTO(
                        carrito.getId(),
                        carrito.getUsuario().getId(),
                        carrito.getCodigo())).collect(Collectors.toList());
    }

    public CarritoDTO createCarritoForUsuario(Long idUsuario) {
        Carrito carrito = new Carrito();

        Usuario usuario = usuarioService.findById(idUsuario).orElseThrow(() -> new UsuarioNoEncontradoException(idUsuario));
        carrito.setUsuario(usuario);
        carrito.setCodigo("CAR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        log.info("Creando nuevo carrito para usuario {}", usuario.getId());

        Carrito saved = carritoRepository.save(carrito);
        return CarritoDTO.from(saved);
    }

    public void addProducto(NuevoProductoRequest request) {
        Carrito carrito = carritoRepository.findByCodigo(request.getCodigoCarrito())
                .orElseThrow(() -> new CarritoNoEncontradoException(request.getCodigoCarrito()));
        Producto producto = productoService.findByCodigo(request.getCodigoProducto())
                .orElseThrow(() -> new ProductoNoEncontradoException(request.getCodigoProducto()));

        validateUsuarioLogueado(carrito);
        CarritoItem carritoItem = carritoItemService.findByCarritoAndProducto(request.getCodigoCarrito(),
                        request.getCodigoProducto())
                .orElseGet(() -> {
                    CarritoItem nuevo = new CarritoItem();
                    nuevo.setProducto(producto);
                    nuevo.setCarrito(carrito);
                    nuevo.setCantidad(0);
                    return nuevo;
                });

        carritoItem.setCantidad(carritoItem.getCantidad() + request.getCantidadProducto());
        carritoItemService.saveItem(carritoItem);
    }

    public void deleteProducto(String codigoProducto, String codigoCarrito) {
        Carrito carrito = carritoRepository.findByCodigo(codigoCarrito)
                .orElseThrow(() -> new CarritoNoEncontradoException(codigoCarrito));
        validateUsuarioLogueado(carrito);
        CarritoItem carritoItem = carritoItemService
                .findByCarritoAndProducto(codigoCarrito, codigoProducto)
                .orElseThrow(() -> new CarritoItemNoEncontradoException(codigoCarrito, codigoProducto));

        carritoItemService.deleteItem(carritoItem);
    }

    private void validateUsuarioLogueado(Carrito carrito) {
        String usernameAuthenticated = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String usernameLogueado = carrito.getUsuario().getUsername();
        if (!usernameLogueado.equals(usernameAuthenticated)) {
            throw new AccesoDenegadoException(usernameLogueado);
        }
    }
}
