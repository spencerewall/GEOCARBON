SELECT A.exp_id, A.time_id, MIN(A.dlS), MAX(A.dlS), count(A.run_id) 
FROM arrays_expid A JOIN (SELECT DISTINCT run_id FROM arrays_expid WHERE CO2<=0 AND exp_id=14) bad_ids
USING(run_id)
WHERE A.exp_id=14 AND run_id<>136285
GROUP BY time_id
GO
/*
SELECT exp_id, time_id, MIN(dlS), MAX(dlS), count(run_id), avg(CO2) FROM goodcarbon_expid
WHERE exp_id=14
GROUP BY time_id
GO
*/
/*
select * from experiments where exp_id=17
GO

select max(run_id) as mx from gcRuns
GO*/