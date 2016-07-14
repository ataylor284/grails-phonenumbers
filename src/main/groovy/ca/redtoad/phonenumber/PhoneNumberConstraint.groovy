// -*- mode: Groovy; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-

package ca.redtoad.phonenumber

import grails.util.Holders
import org.codehaus.groovy.grails.validation.AbstractConstraint
import org.springframework.validation.Errors
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.NumberParseException

class PhoneNumberConstraint extends AbstractConstraint {

    final static String PHONE_NUMBER_CONSTRAINT = 'phoneNumber'
    final static String DEFAULT_INVALID_PHONE_NUMBER_MESSAGE_CODE = 'default.invalid.phoneNumber.message'

    def phoneNumberService

    def getPhoneNumberService() {
        if (!phoneNumberService) {
            phoneNumberService = Holders.applicationContext.getBean('phoneNumberService')
        }
        phoneNumberService
    }

    String getName() { PHONE_NUMBER_CONSTRAINT }

    boolean supports(Class type) {
        type && String.isAssignableFrom(type)
    }

    private resolveAllowedRegions(String region, target) {
        if (!PhoneNumberUtil.instance.supportedRegions.contains(region)) {
            throw new IllegalArgumentException("Parameter for constraint [$name] of property [$constraintPropertyName] of class [$constraintOwningClass] region $region is not supported")
        }
        return [region]
    }

    private resolveAllowedRegions(List regions, target) {
        regions.inject([], { rs, it -> rs << it })
    }

    private resolveAllowedRegions(Closure regions, target) {
        regions.delegate = target
        regions.resolveStrategy = Closure.DELEGATE_FIRST
        resolveAllowedRegions(regions.call(), target)
    }

    private resolveAllowedRegions(Object regions, target) {
        throw new IllegalArgumentException("Parameter for constraint [$name] of property [$constraintPropertyName] of class [$constraintOwningClass] allowedRegions must be a String, List of Strings, or Closure")
    }

    protected void processValidate(target, propertyValue, Errors errors) {
        if (constraintParameter instanceof Boolean && !constraintParameter) {
            return
        }

        def phoneNumberUtil = PhoneNumberUtil.instance
        def phoneNumberInstance
        def allowedRegions = getPhoneNumberService().allowedRegions
        def allowedRegionsString = 'any supported region'
        def strict = getPhoneNumberService().strict

        if (constraintParameter instanceof Map) {
            if (constraintParameter.strict) {
                strict = true
            }
            if (constraintParameter.allowedRegions) {
                allowedRegions = resolveAllowedRegions(constraintParameter.allowedRegions, target)
                allowedRegionsString = allowedRegions.join(', ')
            }
        }

        try {
            phoneNumberInstance = phoneNumberUtil.parse(propertyValue.toString(), getPhoneNumberService().defaultRegion)
        } catch (NumberParseException e) {
            Object[] args = [constraintPropertyName, constraintOwningClass, propertyValue, e.errorType, e.toString()]
            rejectValue(target, errors, DEFAULT_INVALID_PHONE_NUMBER_MESSAGE_CODE,
                "phoneNumber.${e.errorType.toString().toLowerCase()}", args)
            return
        }

        def isPossible = phoneNumberUtil.isPossibleNumberWithReason(phoneNumberInstance)

        if (isPossible != PhoneNumberUtil.ValidationResult.IS_POSSIBLE) {
            Object[] args = [constraintPropertyName, constraintOwningClass, propertyValue, isPossible, isPossible.toString()]
            rejectValue(target, errors, DEFAULT_INVALID_PHONE_NUMBER_MESSAGE_CODE,
                "phoneNumber.${isPossible.toString().toLowerCase()}", args)

        } else if (strict && !allowedRegions.any{phoneNumberUtil.isValidNumberForRegion(phoneNumberInstance, it)}) {
            Object[] args = [constraintPropertyName, constraintOwningClass, propertyValue, 0, "not valid for $allowedRegionsString"]
            rejectValue(target, errors, DEFAULT_INVALID_PHONE_NUMBER_MESSAGE_CODE,
                "phoneNumber.invalidForRegion", args)
        }
    }
}
