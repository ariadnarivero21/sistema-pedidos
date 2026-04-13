package com.koigroup.sistema_pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAsync
public class SistemaPedidosApplication {

	public static void main(String[] args) {
		System.out.println("BCrypt de 'admin': " + new BCryptPasswordEncoder().encode("admin"));
		SpringApplication.run(SistemaPedidosApplication.class, args);
	}

}
