# Design

The ER diagram (db-design.pdf) is the visual representation of the three tables
necessary to model the data in the design.

**Player**
This table contains the fields necessary to model the _Player_ object.

**Cryptogram**
This table contains the fields necessary to model the _Cryptogram_ object.

**Puzzle**
This table is joined on the _Player_ table via the _userName_ field and on the
_Cryptogram_ table via the _cryptoName_ field. It contains the fields necessary
to model the _Puzzle_ object.

# SQL Statements
SQL Statements are organized as follows:
1. DB / Table Creation
2. INSERT Queries (to place data in database)
3. SELECT Queries (to retrieve data from database)

## Table Creation
```
CREATE TABLE `Cryptogram` ( `cryptoName` TEXT, `unencodedPhrase` TEXT NOT NULL, `encryptionKey` TEXT NOT NULL, `maxSolutionAttempts` INTEGER NOT NULL, PRIMARY KEY(`cryptoName`) )
```

```
CREATE TABLE "Player" ( `userName` TEXT NOT NULL UNIQUE, `email` TEXT NOT NULL UNIQUE, `firstName` TEXT NOT NULL, `lastName` TEXT NOT NULL, `userPassword` TEXT NOT NULL, PRIMARY KEY(`userName`) )
```

```
CREATE TABLE `Puzzle` ( `userName` TEXT NOT NULL, `cryptoName` TEXT NOT NULL, `dateCompleted` NUMERIC NOT NULL, `Successful` INTEGER NOT NULL )
```

## INSERT Queries

## SELECT Queries

### List all existing user names
```
SELECT userName FROM Player;
```

### List all existing cryptograms
```
SELECT cryptoName FROM Cryptogram;
```

### Get encoded cryptogram
```
SELECT encryptionKey FROM Cryptogram WHERE cryptoName='crypto1';
```

### Get unencoded cryptogram
```
SELECT unencodedPhrase FROM Cryptogram WHERE cryptoName='crypto1';
```


# API

API interface specification goes here.

# Tests

Test information goes here.
