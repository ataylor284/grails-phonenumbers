// -*- mode: Groovy; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-

package ca.redtoad.phonenumber

import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PhoneNumberServiceTests extends GroovyTestCase {

    def service

    void setUp() {
        super.setUp()

        service = new PhoneNumberService()
        service.phoneNumberUtil = PhoneNumberUtil.instance

        ConfigurationHolder.config = new ConfigObject()
    }

    void testFormatValid() {
        def phoneNumber = '2012345678'

        def formatted = service.format(phoneNumber)

        assert formatted == '+1 201-234-5678'
    }

    void testFormatInvalid() {
        def phoneNumber = 'xyz'

        def formatted = service.format(phoneNumber)

        assert formatted == 'xyz'
    }
    
    void testGeolocateValid() {
        def phoneNumber = '2012345678'

        def geoinfo = service.geolocate(phoneNumber)

        assert geoinfo == [country: "United States", description: "New Jersey"]
    }

    void testGeolocateInvalid() {
        def phoneNumber = 'xyz'

        def geoinfo = service.geolocate(phoneNumber)

        assert geoinfo == [:]
    }
}
