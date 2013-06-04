grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

    inherits 'global'

    log 'warn'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenCentral()
    }

    dependencies {
        runtime('com.googlecode.libphonenumber:libphonenumber:5.5') {
            transitive = false
        }
        runtime('com.googlecode.libphonenumber:geocoder:2.6') {
            transitive = false
        }
    }

    plugins {
        compile ":codenarc:0.18.1"
        compile ":code-coverage:1.2.6"
    }
}

coverage {
    enabledByDefault = true
}
