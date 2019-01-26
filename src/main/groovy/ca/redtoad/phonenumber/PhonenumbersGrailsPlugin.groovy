package ca.redtoad.phonenumber

import grails.plugins.*
import grails.validation.ConstrainedProperty

import com.google.i18n.phonenumbers.PhoneNumberUtil

class PhonenumbersGrailsPlugin extends Plugin {

    def version = "0.11"
    def grailsVersion = "3.1.9 > *"
    def author = "Andrew Taylor"
    def authorEmail = "ataylor@redtoad.ca"
    def title = "Grails Phonenumbers Plugin"
    def description = "Adds support for using Google's libphonenumber library to validate phone numbers"
    def documentation = "https://github.com/ataylor284/grails-phonenumbers"

    def license = "APACHE"
    def issueManagement = [ system: "GITHUB", url: "https://github.com/ataylor284/grails-phonenumbers/issues" ]
    def scm = [ url: "https://github.com/ataylor284/grails-phonenumbers" ]

    Closure doWithSpring() {{->
        ConstrainedProperty.registerNewConstraint(
            PhoneNumberConstraint.PHONE_NUMBER_CONSTRAINT,
            PhoneNumberConstraint)

        phoneNumberUtil(PhoneNumberUtil) { bean ->
            bean.factoryMethod = 'getInstance'
        }
    }}
}
