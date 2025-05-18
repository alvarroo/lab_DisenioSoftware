CREATE DATABASE Qgen_Users;

USE Qgen_Users;

USE master;
GO

CREATE LOGIN [QGenApp] WITH PASSWORD = 'OdY8/u3oZ*1q0_Awg';
GO

USE Qgen_Users;
GO

CREATE USER [QGenApp] FOR LOGIN [QGenApp];
GO

GRANT ALTER ON SCHEMA::dbo TO QGenApp;
GRANT CREATE TABLE TO QGenApp;

EXEC sp_addrolemember 'db_datareader', 'QGenApp';
EXEC sp_addrolemember 'db_datawriter', 'QGenApp';
