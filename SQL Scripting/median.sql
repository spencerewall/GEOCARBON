DROP PROCEDURE IF EXISTS procMedian
GO

CREATE PROCEDURE procMedian (IN e INT, t INT)
BEGIN
SELECT count(*), x.co2 
    FROM (SELECT B.exp_id, A.* FROM gcRunArrays A JOIN gcRuns B ON A.run_id=B.run_id WHERE B.exp_id=e and A.time_id=t) x,
         (SELECT B.exp_id, A.* FROM gcRunArrays A JOIN gcRuns B ON A.run_id=B.run_id WHERE B.exp_id=e and A.time_id=t) y
    GROUP BY x.co2
    HAVING SUM(SIGN(1-SIGN(y.co2-x.co2))) = CEILING((COUNT(*)+1)/2);
END
GO

DROP FUNCTION IF EXISTS funMedian
GO

CREATE FUNCTION funMedian (e INT, t INT)
RETURNS DOUBLE
READS SQL DATA
BEGIN
    DECLARE selectMedian DOUBLE;
    SET selectMedian = (SELECT x.co2 FROM 
        (SELECT * FROM arrays_expid WHERE exp_id=e and time_id=t) x,
        (SELECT * FROM arrays_expid WHERE exp_id=e and time_id=t) y
        GROUP BY x.co2
        HAVING SUM(SIGN(1-SIGN(y.co2-x.co2))) = CEILING((COUNT(*)+1)/2));
    RETURN selectMedian;
END
GO

DROP FUNCTION IF EXISTS funMedian_STATIC
GO

CREATE FUNCTION funMedian_STATIC (e INT, t INT)
RETURNS DOUBLE
READS SQL DATA
BEGIN
    DECLARE selectMedian DOUBLE;
    SET selectMedian = (SELECT CO2 FROM (SELECT * FROM arrays_expid WHERE exp_id=e and time_id=t
        ORDER BY CO2 desc 
        LIMIT 5000) x
    ORDER BY x.CO2 asc LIMIT 1);
    RETURN selectMedian;
END
GO

DROP FUNCTION IF EXISTS funGoodMedian
GO

CREATE FUNCTION funGoodMedian (e INT, t INT)
RETURNS DOUBLE
READS SQL DATA
BEGIN
    DECLARE selectMedian DOUBLE;
    SET selectMedian = (SELECT x.co2 FROM 
        (SELECT * FROM goodcarbon_expid WHERE exp_id=e and time_id=t) x,
        (SELECT * FROM goodcarbon_expid WHERE exp_id=e and time_id=t) y
        GROUP BY x.co2
        HAVING SUM(SIGN(1-SIGN(y.co2-x.co2))) = CEILING((COUNT(*)+1)/2));
    RETURN selectMedian;
END
GO

DROP FUNCTION IF EXISTS funHighPercentile

CREATE FUNCTION funHighPercentile (e INT, t INT)
RETURNS DOUBLE
READS SQL DATA
BEGIN
    DECLARE selectMedian DOUBLE;
    SET selectMedian = (SELECT CO2 FROM (SELECT * FROM arrays_expid WHERE exp_id=e and time_id=t
        ORDER BY CO2 desc 
        LIMIT 250) x
    ORDER BY x.CO2 asc LIMIT 1);
    RETURN selectMedian;
END
GO

CREATE FUNCTION funLowPercentile (e INT, t INT)
RETURNS DOUBLE
READS SQL DATA
BEGIN
    DECLARE selectMedian DOUBLE;
    SET selectMedian = (SELECT CO2 FROM (SELECT * FROM arrays_expid WHERE exp_id=e and time_id=t
        ORDER BY CO2 ASC 
        LIMIT 250) x
    ORDER BY x.CO2 DESC LIMIT 1);
    RETURN selectMedian;
END
GO