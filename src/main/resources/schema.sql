CREATE TABLE MEMBER (
                        id IDENTITY PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) UNIQUE NOT NULL,
                        phone_number VARCHAR(20) NOT NULL
);
