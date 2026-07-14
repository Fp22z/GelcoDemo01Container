CREATE DATABASE IF NOT EXISTS gelco_pedidos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gelco_capacitacion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gelco_distribucion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON gelco_pedidos.* TO 'gelco_app'@'%';
GRANT ALL PRIVILEGES ON gelco_capacitacion.* TO 'gelco_app'@'%';
GRANT ALL PRIVILEGES ON gelco_distribucion.* TO 'gelco_app'@'%';
FLUSH PRIVILEGES;