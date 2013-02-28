CREATE OR REPLACE VIEW viewRun
AS SELECT R.exp_id, C2.time_id, C2.co2, A.fA, A.fD, A.fL, A.dlS, A.dlC, C.act, C.fert, C.life, C.gym, C.glac, C.deltat
    FROM tblRun R INNER JOIN tblIOArray A
            ON R.param_id=A.a_id
        INNER JOIN  tblIOConstant C
            ON R.param_id=C.c_id
        INNER JOIN tblCO2 C2
            ON R.param_id=C2.co2_id
GO



CREATE OR REPLACE VIEW viewMeanCO2 
AS
SELECT exp_id, time_id, avg(co2) as 'avg_co2'
    FROM (tblRun
    INNER JOIN tblCO2
        ON( param_id=co2_id))
        GROUP BY exp_id, time_id
        ORDER BY exp_id, time_id
GO



-- FUNCTION countRun(int) 
-- PARAMS:  int - cooresponding to an exp_id contained in tblExperiment
-- RETURNS: int - the number of records in tblRun with the given exp_id
DROP FUNCTION IF EXISTS countRun
GO

CREATE FUNCTION countRun (expid int) 
RETURNS INT 
BEGIN 
    RETURN (SELECT count(exp_id) FROM tblRun WHERE exp_id = expid);
END
GO



DROP FUNCTION IF EXISTS medianCO2AtTime
GO

CREATE FUNCTION medianCO2AtTime (expid INT, timeid INT)
    RETURNS FLOAT
BEGIN
    DECLARE mindex int;
    SELECT floor(countRun(expid)/2) INTO mindex;
    
    RETURN (SELECT co2
        FROM (tblCO2 INNER JOIN tblRun ON (co2_id = param_id))
        WHERE (exp_id=expid AND time_id=timeid)
        ORDER BY co2 ASC limit mindex,1);
END
GO



DROP PROCEDURE IF EXISTS medianCO2
GO

CREATE PROCEDURE medianCO2(IN expid INT)
BEGIN
    SELECT time_id, medianCO2AtTime(expid, time_id) as 'co2'
        FROM (tblCO2 JOIN tblRun ON (co2_id=param_id))
        WHERE (expid=exp_id);
END
GO

/*
CREATE FUNCTION co2Range(low float, high float)  
RETURNS TABLE  
AS
RETURN (SELECT dt FROM dbo.Calendar  
WHERE dt BETWEEN @sDate AND @eDate)  
GO*/
--select count(*) from table
--median_row = floor(count / 2)
--select val from table order by val asc limit median_row,1