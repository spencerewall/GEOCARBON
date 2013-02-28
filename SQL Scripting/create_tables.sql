CREATE TABLE arrayData  ( 
	run_id      int(11) NOT NULL,
	time_id 	int(11) NOT NULL,
	co2     	float   NOT NULL,
	fA      	float   NOT NULL,
	fD      	float   NOT NULL,
	fL      	float   NOT NULL,
	dlS     	float   NOT NULL,
	dlC     	float   NOT NULL,
	fSr     	float   NOT NULL,
	Sr      	float   NOT NULL,
	temp    	float   NOT NULL,
	CONSTRAINT pk_ArrayID PRIMARY KEY (run_id,time_id),
	CONSTRAINT fk_RunID FOREIGN KEY(run_id) REFERENCES geocarbRun(run_id)
)

CREATE TABLE constantData(
    constant_id int     NOT NULL,
    run_id      int     NOT NULL,
    act         float	NOT NULL,
    fert        float   NOT NULL,
    life        float   NOT NULL,
   	gym     	float   NOT NULL,
   	glac    	float   NOT NULL,
   	deltat		float	NOT NULL,
    PRIMARY KEY (run_id)
)
GO

CREATE TABLE testGroups(
    group_id        int     NOT NULL,
    defFactor_id  int     NOT NULL,
    description     varchar(25),
    PRIMARY KEY (group_id),
    FOREIGN KEY (defFactor_id) REFERENCES defaultRun
)
GO