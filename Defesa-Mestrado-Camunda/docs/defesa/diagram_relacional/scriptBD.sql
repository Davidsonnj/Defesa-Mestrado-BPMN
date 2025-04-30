CREATE TABLE Aluno
(
    idAluno SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE Banca
(
    idBanca SERIAL PRIMARY KEY,
    minicurriculo TEXT,
    instituicao VARCHAR(100),
    nome VARCHAR(100),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE Defesa
(
    idDefesa SERIAL PRIMARY KEY,
    FK_aluno INT NOT NULL,
    dataDefesa DATE,
    localDefesa VARCHAR(100),
    tituloTrabalho VARCHAR(200),
    FOREIGN KEY (FK_aluno) REFERENCES Aluno(idAluno)
);

CREATE TABLE Defesa_Banca (
    id SERIAL PRIMARY KEY,
    idDefesa INT NOT NULL,
    idBanca INT NOT NULL,
    FOREIGN KEY (idDefesa) REFERENCES Defesa(idDefesa),
    FOREIGN KEY (idBanca) REFERENCES Banca(idBanca)
);
