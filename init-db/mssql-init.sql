IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'gelco_auth')
BEGIN
    CREATE DATABASE gelco_auth;
END