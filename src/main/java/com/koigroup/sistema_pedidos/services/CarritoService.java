package com.koigroup.sistema_pedidos.services;

import com.koigroup.sistema_pedidos.DTO.UsuarioCarritoDTO;
import com.koigroup.sistema_pedidos.DTO.request.NuevoProductoRequest;
import com.koigroup.sistema_pedidos.entities.Carrito;
import com.koigroup.sistema_pedidos.entities.CarritoItem;
import com.koigroup.sistema_pedidos.entities.Producto;
import com.koigroup.sistema_pedidos.entities.Usuario;
import com.koigroup.sistema_pedidos.exception.CarritoItemNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.CarritoNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.ProductoNoEncontradoException;
import com.koigroup.sistema_pedidos.exception.UsuarioNoEncontradoException;
import com.koigroup.sistema_pedidos.repositories.CarritoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public void createCarritoForUsuario(Long idUsuario) {
        Carrito carrito = new Carrito();

        Usuario usuario = usuarioService.findById(idUsuario).orElseThrow(() -> new UsuarioNoEncontradoException(idUsuario));
        carrito.setUsuario(usuario);
        carrito.setCodigo("CAR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        carritoRepository.save(carrito);

        log.info("Creando nuevo carrito para usuario {}", usuario.getId());
    }

    public void addProducto(NuevoProductoRequest request) {
        Carrito carrito = carritoRepository.findByCodigo(request.getCodigoCarrito())
                .orElseThrow(() -> new CarritoNoEncontradoException(request.getCodigoCarrito()));
        Producto producto = productoService.findByCodigo(request.getCodigoProducto())
                .orElseThrow(() -> new ProductoNoEncontradoException(request.getCodigoProducto()));

        CarritoItem carritoItem = new CarritoItem();
        carritoItem.setProducto(producto);
        carritoItem.setCarrito(carrito);
        carritoItem.setCantidad(request.getCantidadProducto());
        carrito.getItems().add(carritoItem);
        carritoItemService.saveItem(carritoItem);
        carritoRepository.save(carrito);
    }

    public void deleteProducto(String codigoProducto, String codigoCarrito) {
        CarritoItem carritoItem = carritoItemService
                .findByCarritoAndProducto(codigoCarrito, codigoProducto)
                .orElseThrow(() -> new CarritoItemNoEncontradoException(codigoCarrito, codigoProducto));

        carritoItemService.deleteItem(carritoItem);
    }
}
