import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.codehaus.groovy.grails.validation.ConstrainedProperty
import ca.redtoad.phonenumber.PhoneNumberConstraint

class PhonenumbersGrailsPlugin {

    def version = "0.5"

    def grailsVersion = "1.3 > *"

    def dependsOn = [:]

    def author = "Andrew Taylor"
    def authorEmail = "ataylor@redtoad.ca"
    def title = "Grails Phonenumbers Plugin"
    def description = '''\\
        |A grails plugin for using google's libphonenumber library to validate phonenumbers.
    '''.stripMargin()

    def documentation = "https://github.com/ataylor284/grails-phonenumbers"

    def doWithSpring = {
        ConstrainedProperty.registerNewConstraint(
            PhoneNumberConstraint.PHONE_NUMBER_CONSTRAINT,
            PhoneNumberConstraint.class);

        def phoneNumberUtilBean = phoneNumberUtil(PhoneNumberUtil)
        phoneNumberUtilBean.factoryMethod = 'getInstance'
    }
}
