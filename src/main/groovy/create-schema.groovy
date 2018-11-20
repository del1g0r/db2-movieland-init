import groovy.sql.Sql

def sql = Sql.newInstance("jdbc:${project.properties.url}", "${project.properties.user}", "${project.properties.password}", 'org.postgresql.Driver')

try {

    sql.execute("CREATE EXTENSION IF NOT EXISTS pgcrypto")

    sql.execute("""
CREATE TABLE IF NOT EXISTS "user"
(
  id            SERIAL     NOT NULL PRIMARY KEY
, login         TEXT       NOT NULL UNIQUE
, email         TEXT       NOT NULL UNIQUE
, name          TEXT       NOT NULL
, role_name     TEXT       NOT NULL CHECK (role_name IN ('GUEST', 'USER', 'ADMIN'))
, salt          TEXT       NOT NULL
, pswhash       TEXT       NOT NULL
)
""")

    sql.execute("""
CREATE TABLE IF NOT EXISTS genre
( id            SERIAL     NOT NULL PRIMARY KEY
, name          TEXT       NOT NULL
)
""")

    sql.execute("""
CREATE TABLE IF NOT EXISTS country
( id            SERIAL     NOT NULL PRIMARY KEY
, name          TEXT       NOT NULL
)
""")

    sql.execute("""
CREATE TABLE IF NOT EXISTS movie
(
  id            SERIAL     NOT NULL PRIMARY KEY
, name          TEXT       NOT NULL
, original_name TEXT       NOT NULL
, year          INTEGER    NOT NULL CHECK (year >= 1884)
, description   TEXT
, rating        FLOAT
, price         FLOAT
)
""")

    sql.execute("""
CREATE TABLE IF NOT EXISTS movie_genre
(
  movie_id      INTEGER    NOT NULL REFERENCES movie (id)
, genre_id      INTEGER    NOT NULL REFERENCES genre (id)
, UNIQUE (movie_id, genre_id)
)
""")

    sql.execute("""
CREATE TABLE IF NOT EXISTS movie_country
(
  movie_id      INTEGER    NOT NULL REFERENCES movie (id)
, country_id    INTEGER    NOT NULL REFERENCES country (id)
, UNIQUE (movie_id, country_id)
)
""")

    sql.execute("""
CREATE TABLE IF NOT EXISTS review
(
  id            SERIAL     NOT NULL PRIMARY KEY
, user_id       INTEGER    NOT NULL REFERENCES "user" (id)
, movie_id      INTEGER    NOT NULL REFERENCES movie (id)
, review_text   TEXT       NOT NULL
)
""")

    sql.execute("""
CREATE TABLE IF NOT EXISTS poster
(
  id            SERIAL     NOT NULL PRIMARY KEY
, movie_id      INTEGER    NOT NULL REFERENCES movie (id)
, poster_url    TEXT       NOT NULL
)
""")

    sql.execute("""
CREATE TABLE IF NOT EXISTS rating
(
  movie_id      INTEGER    NOT NULL REFERENCES movie (id)
, user_id       INTEGER    NOT NULL REFERENCES "user" (id)
, rating        FLOAT      NOT NULL
)
""")

} finally {
    sql.close()
}

;



