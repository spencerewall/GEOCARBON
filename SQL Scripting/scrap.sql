--16 experiments * 58 times = 
--select * from (SELECT exp_id, time_id FROM arrays_expid WHERE exp_id!='null' GROUP BY exp_id, time_id) x
--GO

--INSERT INTO medians SELECT exp_id, time_id, funMedian_STATIC(exp_id, time_id) FROM arrays_expid WHERE exp_id!='null' GROUP BY exp_id, time_id
--GO

CREATE VIEW highlowarrays
as
SELECT (A.co2>10000) as isHigh, (A.co2<0) as isLow, A.* from arrays_expid A
ORDER BY isHigh DESC, isLow DESC
LIMIT 500
GO

TRUNCATE TABLE goodmedians
GO

--INSERT INTO goodmedians(exp_id, time_id, co2) 
--    SELECT e, t, funGoodMedian(e, t)  FROM TestTable
--GO
