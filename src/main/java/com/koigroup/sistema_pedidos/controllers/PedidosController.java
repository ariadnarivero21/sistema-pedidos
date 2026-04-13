package com.koigroup.sistema_pedidos.controllers;

import com.koigroup.sistema_pedidos.DTO.ProductoCarritoDTO;
import com.koigroup.sistema_pedidos.DTO.UsuarioCarritoDTO;
import com.koigroup.sistema_pedidos.DTO.request.NuevoProductoRequest;
import com.koigroup.sistema_pedidos.services.CarritoItemService;
import com.koigroup.sistema_pedidos.services.CarritoService;
import com.koigroup.sistema_pedidos.services.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    //PATRON Service Layer
    private final CarritoService carritoService;
    private final CarritoItemService carritoItemService;
    private final PedidoService pedidoService;

    public PedidosController(CarritoService carritoService, CarritoItemService carritoItemService,
                             PedidoService pedidoService) {
        this.carritoService = carritoService;
        this.carritoItemService = carritoItemService;
        this.pedidoService = pedidoService;
    }

    @PostMapping("/usuarios/{usuarioId}/carrito")
    public ResponseEntity<?> createCarritoForUsuario(@PathVariable("usuarioId") Long usuarioId) {
        carritoService.createCarritoForUsuario(usuarioId);
        return ResponseEntity.ok().body("Carrito creado con éxito para el usuario con ID " + usuarioId);
    }

    @PostMapping("/carrito/producto")
    public ResponseEntity<?> addProductoToCarrito(@Validated @RequestBody NuevoProductoRequest request) {
        carritoService.addProducto(request);
        String mensaje = String.format(
                "%d unidades del producto con código %s agregadas correctamente",
                request.getCantidadProducto(),
                request.getCodigoProducto()
        );

        return ResponseEntity.ok().body(mensaje);
    }

    @DeleteMapping("/carritos/{codigoCarrito}/productos/{codigoProducto}")
    public ResponseEntity<?> deleteProductoFromCarrito(@PathVariable("codigoProducto") String codigoProducto,
                                                       @PathVariable("codigoCarrito") String codigoCarrito) {
        carritoService.deleteProducto(codigoProducto, codigoCarrito);
        String mensaje = String.format("El producto con código %s fue eliminado con éxito del carrito con código %s",
                codigoProducto, codigoCarrito);
        return ResponseEntity.ok().body(mensaje);
    }

    @GetMapping("/carritos/{codigoCarrito}/productos")
    public List<ProductoCarritoDTO> getAllProductosByCarrito(@PathVariable("codigoCarrito") String codigoCarrito) {
        return carritoItemService.getAllProductosByCarrito(codigoCarrito);
    }

    @GetMapping("/usuarios/{usuarioId}/carritos")
    public List<UsuarioCarritoDTO> getAllCarritosByUsuario(@PathVariable("usuarioId") Long usuarioId) {
        return carritoService.getAllByUsuarioId(usuarioId);
    }

    @PostMapping("carritos/{codigoCarrito}/pedido")
    public ResponseEntity<?> processPedido(@PathVariable("codigoCarrito") String codigoCarrito) {
        pedidoService.procesarPedido(codigoCarrito);
        return ResponseEntity.accepted().body("Estamos procesando su orden");
    }

}
