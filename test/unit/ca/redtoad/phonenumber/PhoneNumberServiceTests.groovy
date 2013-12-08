// -*- mode: Groovy; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-

package ca.redtoad.phonenumber

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

import com.google.i18n.phonenumbers.PhoneNumberUtil

class PhoneNumberServiceTests extends GroovyTestCase {

    private service = new PhoneNumberService()

    protected void setUp() {
        super.setUp()

        service.phoneNumberUtil = PhoneNumberUtil.instance
        service.grailsApplication = new DefaultGrailsApplication()
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
