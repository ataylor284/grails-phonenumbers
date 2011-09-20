// -*- mode: Groovy; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-

package ca.redtoad.phonenumber

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder
import org.codehaus.groovy.grails.commons.ConfigurationHolder
 
class PhoneNumberService {

    static transactional = false

    def phoneNumberUtil

    String getDefaultRegion() {
        ConfigurationHolder.config.grails.plugins.phonenumbers.defaultRegion ?: 'US'
    }

    Collection getAllowedRegions() {
        ConfigurationHolder.config.grails.plugins.phonenumbers.defaultAllowedRegions ?: phoneNumberUtil.supportedRegions
    }

    boolean getStrict() {
        ConfigurationHolder.config.grails.plugins.phonenumbers.defaultStrict ?: false
    }

    String format(String phoneNumber, region = null) {
        try {
            def phoneNumberInstance = phoneNumberUtil.parse(phoneNumber, region ?: defaultRegion)
             phoneNumberUtil.format(phoneNumberInstance, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        } catch (NumberParseException e) {
            phoneNumber
        }
    }

    Map geolocate(String phoneNumber, region = null, locale = null) {
        try {
            def geocoder = PhoneNumberOfflineGeocoder.instance
            def phoneNumberInstance = phoneNumberUtil.parse(phoneNumber, region ?: defaultRegion)
            [
                country: geocoder.getCountryNameForNumber(phoneNumberInstance, locale ?: Locale.default),
                description: geocoder.getDescriptionForNumber(phoneNumberInstance, locale ?: Locale.default)
            ]
        } catch (NumberParseException e) {
            [:]
        }
    }
}
