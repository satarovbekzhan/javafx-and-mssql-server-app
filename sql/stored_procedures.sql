CREATE OR ALTER PROCEDURE sp_create_user @email nvarchar(55), @pass nvarchar(55), @role nvarchar(5)
AS BEGIN
    IF NOT EXISTS(SELECT * FROM [user] WHERE [email] = @email) BEGIN
        INSERT INTO [user] ([email], [pass], [role]) VALUES (@email, @pass, @role);
        DECLARE @sql nvarchar(255);
        SET @sql = 'CREATE USER ' + @email + ' WITH PASSWORD = ''' + @pass + ''';';
        EXEC(@sql);
        RETURN 1;
    END ELSE RETURN 0;
END

GO

CREATE OR ALTER PROCEDURE sp_delete_user @email nvarchar(55)
AS BEGIN
    IF EXISTS(SELECT * FROM [user] WHERE [email] = @email) BEGIN
        DELETE FROM [user] WHERE [email] = @email;
        DECLARE @sql nvarchar(255);
        SET @sql = 'DROP USER ' + @email + ';';
        EXEC(@sql);
        RETURN 1;
    END ELSE RETURN 0;
END

GO

-- EXEC sp_create_user @email = 'bekzhan', @pass = '234', @role = 'BUYER';
-- EXEC sp_delete_user @email = 'bekzhan';
--
-- EXEC sp_create_user @email = 'asan', @pass = '234', @role = 'STAFF';
-- EXEC sp_delete_user @email = 'asan';
--
-- EXEC sp_create_user @email = 'aiym', @pass = '234', @role = 'ADMIN';
-- EXEC sp_delete_user @email = 'aiym';