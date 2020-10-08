CREATE MEMORY TABLE intranet(
	id VARCHAR(50) NOT NULL,
	password VARCHAR(50),
	desthost VARCHAR(50) NOT NULL,
PRIMARY KEY(id,desthost));
