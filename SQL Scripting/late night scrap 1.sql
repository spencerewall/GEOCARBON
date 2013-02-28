SELECT CO2, run_id, count(*)
FROM arrays_expid
WHERE exp_id=14 AND CO2=0
GROUP BY run_id
GO

SELECT CO2, time_id, count(*)
FROM arrays_expid
WHERE exp_id=13 AND CO2=0
group by time_id, co2
GO

SELECT CO2, time_id, count(*)
FROM arrays_expid
WHERE exp_id=13
group by time_id, co2
HAVING CO2=0
GO

/*
SELECT co2, dls
FROM arrays_expid
WHERE exp_id=14 and time_id=40 AND CO2=0
GO

SELECT co2, dls
FROM arrays_expid
WHERE exp_id=14 and time_id=40 AND CO2!=0
GO*/