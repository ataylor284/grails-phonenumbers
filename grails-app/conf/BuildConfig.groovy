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
        runtime('com.googlecode.libphonenumber:libphonenumber:5.8') {
            transitive = false
        }
        runtime('com.googlecode.libphonenumber:geocoder:2.6') {
            transitive = false
        }
    }

    plugins {
        test ":codenarc:0.18.1", {
					export = false
				}
        test ":code-coverage:1.2.6", {
					export = false
				}
				build ':release:2.2.1', ':rest-client-builder:1.0.3', {
					export = false
				}
    }
}

coverage {
    enabledByDefault = true
}
