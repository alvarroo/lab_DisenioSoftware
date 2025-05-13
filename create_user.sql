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

EXEC sp_addrolemember N'db_ddladmin', N'QGenApp';
GO