Grails Phonenumbers Plugin
==========================

[![Build Status](https://travis-ci.org/ataylor284/grails-phonenumbers.png?branch=master)](https://travis-ci.org/ataylor284/grails-phonenumbers)

This plugin adds phone number validation to grails based on google's
libphonenumber (http://code.google.com/p/libphonenumber/) project.


Getting Started
---------------

Install the grails plugin.  Add the phoneNumber constraint to fields
of type String to validate them with libphonenumber.

    class MyDomain {
        String phoneNumber
        static constraints = {
            phoneNumber(phoneNumber: true)
        }
    }


By default, the validator runs in loose mode.  This rejects strings
that have obvious problems that would prevent them from being parsed
as phone numbers.  


Region Sensitive Validation
---------------------------

A strict mode is available, uses region specific rules.  It ensures
that there is at least one region where the phone number is valid.
Strict mode can be enabled on an individual field as shown below, or
it can be enabled globally by adding
`grails.plugins.phonenumbers.defaultStrict = true` to `Config.groovy`.

    class MyDomain {
        String phoneNumber
        static constraints = {
            phoneNumber(phoneNumber: [strict: true])
        }
    }

The list of regions can be further restricted, either per-field, or
globally, as well.  By default, the phone number must be valid in any
region supported by libphonenumber.  To restrict it to one particular
region, or set of regions, the allowRegion can be set to a list of
two-character country codes.  To accept only US phone numbers, set
`grails.plugins.phonenumbers.defaultAllowedRegions = ['US']` in
`Config.groovy`, or set it in the constraint as follows.

    class MyDomain {
        String phoneNumber
        static constraints = {
            phoneNumberField(phoneNumber: [strict: true, allowedRegions: ['US']])
        }
    }


Getting the Region at Validation Time
-------------------------------------

If the region is available in a country field on the domain object, it
can be used by the validator dynamically.  `allowRegions` can be set
to a closure returning a country code or list of country codes.  The
closure delegate will be the domain object being validated.  Example:

    class MyDomain {
        String country
        String phoneNumber
        static constraints = {
            phoneNumber(phoneNumber: [strict: true, allowedRegions: { -> country }])
        }
    }


Formatting Phone Numbers
------------------------

PhoneNumberService exposes a simple format method for reformatting
phone number strings.  For Example:

    class MyDomain {
        def phoneNumberService

        String phoneNumber

        void setPhoneNumber(String val) {
            phoneNumber = phoneNumberService?.format(val) ?: val
        }
    }


Phone Number Geolocation 
------------------------

PhoneNumberService also provides a geolocation service that can
determine the country and region from phone number strings.  For
Example:

    class MyDomain {
        def phoneNumberService

        String phoneNumber
        String geoCountryName
        String geoCountryCode
        String geoDescription

        void setPhoneNumber(String val) {
            phoneNumber = phoneNumberService?.format(val) ?: val
            def geolocationInfo = phoneNumberService?.geolocate(val)
            if (geolocationInfo) {
                geoCountryName = geolocationInfo?.country
                geoCountryCode = Locale.availableLocales.find { it.displayCountry == geoCountryName }?.country
                geoDescription = geolocationInfo?.description
            }
        }
    }


Using PhoneNumberUtil Directly
------------------------------

The phonenumbers plugin publishes the PhoneNumberUtil object as a
spring bean so it can be autowired into your controllers and services.
Define a field called phoneNumberUtil and it will be automatically
initialized by spring.

    class MyController {
        def phoneNumberUtil
    }


TODO
----

* GSP tag for javascript AsYouTypeFormatter


See Also
--------

http://code.google.com/p/libphonenumber
