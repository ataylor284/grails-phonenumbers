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
        runtime('com.googlecode.libphonenumber:libphonenumber:7.0.7') {
            excludes 'junit'
        }
        runtime('com.googlecode.libphonenumber:geocoder:2.23') {
            excludes 'junit', 'libphonenumber'
        }
    }

    plugins {
        test ":codenarc:0.24", {
            export = false
        }
        test ":code-coverage:2.0.3-3", {
            export = false
        }
        build ':release:3.1.1', {
            export = false
        }
    }
}

coverage {
    enabledByDefault = true
}
