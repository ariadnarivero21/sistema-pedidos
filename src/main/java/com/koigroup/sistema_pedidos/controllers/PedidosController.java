package com.koigroup.sistema_pedidos.controllers;

import com.koigroup.sistema_pedidos.DTO.CarritoDTO;
import com.koigroup.sistema_pedidos.DTO.ProductoCarritoDTO;
import com.koigroup.sistema_pedidos.DTO.UsuarioCarritoDTO;
import com.koigroup.sistema_pedidos.DTO.request.NuevoProductoRequest;
import com.koigroup.sistema_pedidos.DTO.response.ResponseDTO;
import com.koigroup.sistema_pedidos.services.CarritoItemService;
import com.koigroup.sistema_pedidos.services.CarritoService;
import com.koigroup.sistema_pedidos.services.PedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/pedidos")
public class PedidosController {

    //PATRON Service Layer
    private final CarritoService carritoService;
    private final CarritoItemService carritoItemService;
    private final PedidoService pedidoService;

    @PostMapping("/usuarios/{usuarioId}/carrito")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<CarritoDTO> createCarritoForUsuario(@PathVariable("usuarioId") Long usuarioId) {
        CarritoDTO carrito = carritoService.createCarritoForUsuario(usuarioId);
        return new ResponseDTO<>(carrito,
                String.format("Carrito %s creado con éxito para el usuario con ID %d", carrito.getCodigo(), usuarioId));
    }

    @PostMapping("/carrito/producto")
    @ResponseStatus(HttpStatus.OK)
    public String addProductoToCarrito(@Validated @RequestBody NuevoProductoRequest request) {
        carritoService.addProducto(request);

        return String.format(
                "%d unidades del producto con código %s agregadas correctamente",
                request.getCantidadProducto(),
                request.getCodigoProducto()
        );
    }

    @DeleteMapping("/carritos/{codigoCarrito}/productos/{codigoProducto}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteProductoFromCarrito(@PathVariable("codigoProducto") String codigoProducto,
                                            @PathVariable("codigoCarrito") String codigoCarrito) {
        carritoService.deleteProducto(codigoProducto, codigoCarrito);
        return String.format("El producto con código %s fue eliminado con éxito del carrito con código %s",
                codigoProducto, codigoCarrito);
    }

    @GetMapping("/carritos/{codigoCarrito}/productos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<ProductoCarritoDTO>> getAllProductosByCarrito(@PathVariable("codigoCarrito") String codigoCarrito) {
        List<ProductoCarritoDTO> productos = carritoItemService.getAllProductosByCarrito(codigoCarrito);
        return new ResponseDTO<>(productos, String.format("Productos del carrito %s", codigoCarrito));
    }

    @GetMapping("/usuarios/{usuarioId}/carritos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<UsuarioCarritoDTO>> getAllCarritosByUsuario(@PathVariable("usuarioId") Long usuarioId) {
        List<UsuarioCarritoDTO> carritos = carritoService.getAllByUsuarioId(usuarioId);
        return new ResponseDTO<>(carritos, String.format("Carritos del usuario %d", usuarioId));
    }

    @PostMapping("carritos/{codigoCarrito}/pedido")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String processPedido(@PathVariable("codigoCarrito") String codigoCarrito) {
        //PATRON Fire and forget
        pedidoService.procesarPedido(codigoCarrito);
        return "Estamos procesando su orden";
    }

}
