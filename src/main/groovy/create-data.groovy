import groovy.sql.Sql

File sqlFile = new File("data.sql")
if (!sqlFile.exists()) {
    println "File does not exist"
} else {
    def sql = Sql.newInstance("jdbc:${project.properties.url}", "${project.properties.user}", "${project.properties.password}", 'org.postgresql.Driver')

    try {
        sqlFile.eachLine() { line ->
            if (line.trim().size() == 0 || line.startsWith("--")) {
                return null
            } else {
                sql.execute(line)
            }
        }
    } finally {
        sql.close()
    }
}