CREATE MEMORY TABLE csession(
	host VARCHAR(50) NOT NULL,
	port VARCHAR(10) DEFAULT '22' NOT NULL,
	user VARCHAR(50) NOT NULL,
	protocol VARCHAR(10) NOT NULL,
	key VARCHAR(100),
	password VARCHAR(50),
PRIMARY KEY(host,port,user,protocol));
