
CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;

CREATE TABLE IF NOT EXISTS personas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  genero VARCHAR(20) NOT NULL,
  edad INT NOT NULL,
  identificacion VARCHAR(60) NOT NULL UNIQUE,
  direccion VARCHAR(200) NOT NULL,
  telefono VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS clientes (
  id BIGINT PRIMARY KEY,
  cliente_id VARCHAR(60) NOT NULL UNIQUE,
  password VARCHAR(120) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_clientes_personas FOREIGN KEY (id) REFERENCES personas(id)
);

CREATE TABLE IF NOT EXISTS cuentas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  numero_cuenta VARCHAR(60) NOT NULL UNIQUE,
  tipo_cuenta VARCHAR(20) NOT NULL,
  saldo_inicial DECIMAL(18,2) NOT NULL,
  saldo_actual DECIMAL(18,2) NOT NULL,
  estado BOOLEAN NOT NULL DEFAULT TRUE,
  cliente_fk BIGINT NOT NULL,
  CONSTRAINT fk_cuentas_cliente FOREIGN KEY (cliente_fk) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS movimientos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  tipo_movimiento VARCHAR(20) NOT NULL,
  valor DECIMAL(18,2) NOT NULL,
  saldo_disponible DECIMAL(18,2) NOT NULL,
  cuenta_fk BIGINT NOT NULL,
  CONSTRAINT fk_movimientos_cuenta FOREIGN KEY (cuenta_fk) REFERENCES cuentas(id)
);

