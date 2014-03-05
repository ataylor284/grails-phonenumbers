grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        runtime('com.googlecode.libphonenumber:libphonenumber:6.0') {
            excludes 'junit'
        }
        runtime('com.googlecode.libphonenumber:geocoder:2.11') {
            excludes 'junit', 'libphonenumber'
        }
    }

    plugins {
        test ":codenarc:0.20", {
            export = false
        }
        test ":code-coverage:1.2.7", {
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
