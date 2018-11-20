import groovy.sql.Sql

def sql = Sql.newInstance("jdbc:${project.properties.url}", "${project.properties.user}", "${project.properties.password}", 'org.postgresql.Driver')

try {

    sql.execute("DROP TABLE IF EXISTS rating")

    sql.execute("DROP TABLE IF EXISTS poster")

    sql.execute("DROP TABLE IF EXISTS review")

    sql.execute("DROP TABLE IF EXISTS movie_country")

    sql.execute("DROP TABLE IF EXISTS movie_genre")

    sql.execute("DROP TABLE IF EXISTS movie")

    sql.execute("DROP TABLE IF EXISTS country")

    sql.execute("DROP TABLE IF EXISTS genre")

    sql.execute("DROP TABLE IF EXISTS \"user\"")

} finally {
    sql.close()
}




