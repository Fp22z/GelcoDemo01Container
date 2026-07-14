#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE gelco_catalogo;
    CREATE DATABASE gelco_consultoras;
    CREATE DATABASE gelco_devoluciones;

    GRANT ALL PRIVILEGES ON DATABASE gelco_catalogo TO gelco_admin;
    GRANT ALL PRIVILEGES ON DATABASE gelco_consultoras TO gelco_admin;
    GRANT ALL PRIVILEGES ON DATABASE gelco_devoluciones TO gelco_admin;
EOSQL