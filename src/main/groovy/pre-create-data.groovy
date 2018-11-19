def sqlFile = new File("data.sql")
sqlFile.delete()
sqlFile.createNewFile()

def lineClassificator
def lineNum = 0

File userFile = new File("src\\main\\resources\\data\\user.txt")
if (!userFile.exists()) {
    println "File does not exist"
} else {
    sqlFile << "-- USER\n"
    def name
    def email
    def password
    userFile.eachLine() { line ->
        if (line.trim().size() == 0) {
            return null
        } else {
            lineClassificator = lineNum % 3;
            switch (lineClassificator) {
                case 0: name = line.trim()
                    break
                case 1: email = line.trim()
                    break
                case 2: password = line.trim()
                    sqlFile << "INSERT INTO \"user\" (login, email, name, salt, pswhash, role_name) SELECT '${email}', '${email}', '${name}', t.md5, crypt('${password}', t.md5), 'USER' FROM (SELECT gen_salt('md5') md5) t;\n"
                    break
            }
            lineNum++
        }
    }
}

File genreFile = new File("src\\main\\resources\\data\\genre.txt")
if (!genreFile.exists()) {
    println "File does not exist"
} else {
    sqlFile << "\n-- GENRE\n"
    genreFile.eachLine { line ->
        if (line.trim().size() != 0) {
            sqlFile << "INSERT INTO genre (name) VALUES('${line.trim()}');\n"
        }
    }
}

File countryFile = new File("src\\main\\resources\\data\\movie.txt")
if (!countryFile.exists()) {
    println "File does not exist"
} else {
    sqlFile << "\n-- COUNTRY\n"
    def countries = []
    lineNum = 0;
    countryFile.eachLine { line ->
        if (line.trim().size() != 0) {
            lineClassificator = lineNum % 7;
            if (lineClassificator == 2) {
                countries.addAll(line.split(',').collect { it.trim() })
            }
            lineNum++
        }
    }
    countries.unique().each {
        countryName -> sqlFile << "INSERT INTO country (name) VALUES('${countryName}');\n"
    }
}

File movieFile = new File("src\\main\\resources\\data\\movie.txt")
if (!movieFile.exists()) {
    println "File does not exist"
} else {
    sqlFile << "\n-- MOVIE\n"
    lineNum = 0;
    def name
    def year
    def countries
    def genres
    def description
    movieFile.eachLine { line ->
        if (line.trim().size() != 0) {
            lineClassificator = lineNum % 7;
            switch (lineClassificator) {
                case 0: name = line.replace("'", "''").split("/")
                    break
                case 1: year = line.trim()
                    break
                case 2: countries = line.trim()
                    break
                case 3: genres = line.trim()
                    break
                case 4: description = line.trim()
                    break
                case 5: rating = line.trim().replace("rating:", "")
                    break
                case 6: price = line.trim().replace("price:", "")
                    sqlFile << "INSERT INTO movie(name, original_name, year, description, rating, price) VALUES ('${name[0]}', '${name[1]}', '${year}', '${description}', ${rating}, ${price});\n"
                    genres.split(",").each { genre ->
                        sqlFile << "  INSERT INTO movie_genre(movie_id, genre_id) SELECT m.id, g.id FROM movie m, genre g WHERE m.name = '${name[0]}' AND g.name = '${genre.trim()}';\n"
                    }
                    countries.split(",").each { country ->
                        sqlFile << "  INSERT INTO movie_country(movie_id, country_id) SELECT m.id, c.id FROM movie m, country c WHERE m.name = '${name[0]}' AND c.name = '${country.trim()}';\n"
                    }
                    break
            }
            lineNum++
        }
    }
}

File reviewFile = new File("src\\main\\resources\\data\\review.txt")
if (!reviewFile.exists()) {
    println "File does not exist"
} else {
    sqlFile << "\n-- REVIEW\n"
    lineNum = 0;
    def movieName
    def userName
    def reviewText
    reviewFile.eachLine { line ->
        if (line.trim().size() != 0) {
            lineClassificator = lineNum % 3;
            switch (lineClassificator) {
                case 0: movieName = line.trim()
                    break
                case 1: userName = line.trim()
                    break
                case 2: reviewText = line.trim()
                    sqlFile << "INSERT INTO review(user_id, movie_id, review_text) SELECT u.id, m.id, '${reviewText}' FROM movie m, \"user\" u WHERE m.name = '${movieName}' AND u.name = '${userName}';\n"
                    break
            }
            lineNum++
        }
    }
}

File posterFile = new File("src\\main\\resources\\data\\poster.txt")
if (!posterFile.exists()) {
    println "File does not exist"
} else {
    sqlFile << "\n-- POSTER\n"
    posterFile.eachLine { line ->
        if (line.trim().size() != 0) {
            def values = line.split(" http")
            sqlFile << "INSERT INTO poster(movie_id, poster_url) SELECT m.id, 'http${values[1]}' FROM movie m WHERE m.name = '${values[0].trim()}';\n"
        }
    }
}