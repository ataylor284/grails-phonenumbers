import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.codehaus.groovy.grails.validation.ConstrainedProperty
import ca.redtoad.phonenumber.PhoneNumberConstraint

class PhonenumbersGrailsPlugin {

    def version = "0.9"
    def grailsVersion = "2.0 > *"
    def author = "Andrew Taylor"
    def authorEmail = "ataylor@redtoad.ca"
    def title = "Grails Phonenumbers Plugin"
    def description = "Adds support for using Google's libphonenumber library to validate phone numbers"
    def documentation = "https://github.com/ataylor284/grails-phonenumbers"

    def license = "APACHE"
    def issueManagement = [ system: "GITHUB", url: "https://github.com/ataylor284/grails-phonenumbers/issues" ]
    def scm = [ url: "https://github.com/ataylor284/grails-phonenumbers" ]

    def doWithSpring = {
        ConstrainedProperty.registerNewConstraint(
            PhoneNumberConstraint.PHONE_NUMBER_CONSTRAINT,
            PhoneNumberConstraint)

        phoneNumberUtil(PhoneNumberUtil) { bean ->
            bean.factoryMethod = 'getInstance'
        }
    }
}
