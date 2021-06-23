/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP (1000) [id]
                ,[product]
                ,[value]
                ,[starting]
FROM [mmm].[dbo].[price]
WHERE starting IN (SELECT max(starting) FROM [mmm].[dbo].[price]);

DELETE FROM [mmm].[dbo].[price]