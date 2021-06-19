CREATE OR ALTER PROCEDURE sp_create_user @email nvarchar(55), @pass nvarchar(55), @role nvarchar(5)
AS BEGIN
    IF NOT EXISTS(SELECT * FROM [user] WHERE [email] = @email) BEGIN
        INSERT INTO [user] ([email], [pass], [role]) VALUES (@email, @pass, @role);

        DECLARE @sql nvarchar(255);
        SET @sql = 'CREATE USER ' + @email + ' WITH PASSWORD = ''' + @pass + ''';';
        EXEC(@sql);

        IF (@role = 'BUYER') EXEC sp_grant_buyer_user @username = @email;
        ELSE EXEC sp_grant_staff_user @username = @email;

        RETURN 1;
    END ELSE RAISERROR (15600,-1,-1, 'create user error');
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
    END ELSE RAISERROR (15600,-1,-1, 'delete user error');
END

GO

CREATE OR ALTER PROCEDURE sp_update_user @id integer, @email nvarchar(55), @pass nvarchar(55), @role nvarchar(5)
AS BEGIN
    IF EXISTS(SELECT * FROM [user] WHERE @id = @id) BEGIN
        IF NOT EXISTS(SELECT * FROM [user] WHERE [email] = @email) BEGIN
            DECLARE @oldEmail nvarchar(55);
            SELECT @oldEmail = CONVERT(nvarchar(20), [email]) FROM [user] WHERE [id] = @id;
            PRINT(@oldEmail);
            UPDATE [user] SET [email] = @email WHERE [id] = @id;

            DECLARE @sql nvarchar(255);
            SET @sql = 'DROP USER ' + @oldEmail + ';';
            EXEC(@sql);
            SET @sql = 'CREATE USER ' + @email + ' WITH PASSWORD = ''' + @pass + ''';';
            EXEC(@sql);

            IF (@role = 'BUYER') EXEC sp_grant_buyer_user @username = @email;
            ELSE EXEC sp_grant_staff_user @username = @email;

            RETURN 1;
        END ELSE BEGIN
            UPDATE [user] SET [pass] = @pass, [role] = @role WHERE [id] = @id;
            IF (@role = 'BUYER') EXEC sp_grant_buyer_user @username = @email;
            ELSE EXEC sp_grant_staff_user @username = @email;
        END
    END ELSE RAISERROR (15600,-1,-1, 'update user error');
END

GO

CREATE OR ALTER PROCEDURE sp_grant_buyer_user @username nvarchar(55)
AS BEGIN
    DECLARE @sql nvarchar(255);
    SET @sql = 'GRANT EXECUTE ON dbo.pr_loginData TO ' + @username + ';';
    EXEC(@sql);
    SET @sql = 'GRANT SELECT ON dbo.category TO ' + @username + ';';
    EXEC(@sql);
    SET @sql = 'GRANT SELECT ON dbo.product TO ' + @username + ';';
    EXEC(@sql);
    SET @sql = 'GRANT SELECT ON dbo.composition TO ' + @username + ';';
    EXEC(@sql);
    SET @sql = 'GRANT SELECT ON dbo.nutrient TO ' + @username + ';';
    EXEC(@sql);
    SET @sql = 'GRANT SELECT ON dbo.unit TO ' + @username + ';';
    EXEC(@sql);
    SET @sql = 'GRANT SELECT ON dbo.price TO ' + @username + ';';
    EXEC(@sql);
    SET @sql = 'GRANT SELECT ON dbo.ref_product_category TO ' + @username + ';';
    EXEC(@sql);
END

GO

CREATE OR ALTER PROCEDURE sp_grant_staff_user @username nvarchar(55)
AS BEGIN
    DECLARE @sql nvarchar(255);
    SET @sql = 'GRANT EXECUTE ON dbo.pr_loginData TO ' + @username + ';';
    EXEC(@sql);
END

GO

EXEC sp_create_user @email = 'bek', @pass = '123', @role = 'BUYER';
EXEC sp_update_user @id = 52, @email = 'usonbek', @pass = '1304', @role = 'BUYER';
EXEC sp_delete_user @email = 'bek';
--
-- EXEC sp_create_user @email = 'asan', @pass = '234', @role = 'STAFF';
-- EXEC sp_delete_user @email = 'asan';
--
-- EXEC sp_create_user @email = 'aiym', @pass = '234', @role = 'ADMIN';
-- EXEC sp_delete_user @email = 'aiym';
