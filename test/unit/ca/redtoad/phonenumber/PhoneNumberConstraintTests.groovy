// -*- mode: Groovy; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-

package ca.redtoad.phonenumber

import org.springframework.validation.BeanPropertyBindingResult
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.NumberParseException

class PhoneNumberConstraintTests extends GroovyTestCase {

    def constraint
    def phonenumber
    def country
    def errors
    int dummyInt
    def defaultRegion = 'US'
    def allowedRegions = PhoneNumberUtil.instance.supportedRegions
    def strict = false

    void setUp() {
        super.setUp()

        country = country
        phonenumber = PhoneNumberUtil.instance.format(PhoneNumberUtil.instance.getExampleNumber('US'), PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
        constraint = new PhoneNumberConstraint()
        constraint.phoneNumberService = this
        constraint.owningClass = this.class
        constraint.propertyName = 'phonenumber'
        errors = new BeanPropertyBindingResult(this, 'ObjToValidate')
    }

    void testSupports() {
        assert constraint.supports(String) == true
        assert constraint.supports(null) == false
        assert constraint.supports(Integer) == false
        assert constraint.supports(Long) == false
        assert constraint.supports(Object) == false
    }

    void testResolveAllowedRegionsNull() {
        shouldFail (IllegalArgumentException) {
            constraint.resolveAllowedRegions(null, null)
        }
    }

    void testResolveAllowedRegionsUnsupportedObject() {
        shouldFail (IllegalArgumentException) {
            constraint.resolveAllowedRegions(42, null)
        }
    }

    void testResolveAllowedRegionsUnsupportedRegion() {
        shouldFail (IllegalArgumentException) {
            constraint.resolveAllowedRegions('XYZ', null)
        }
    }

    void testResolveAllowedRegionsString() {
        def result = constraint.resolveAllowedRegions('US', null)
        assert result == ['US']
    }

    void testResolveAllowedRegionsListOfStrings() {
        def result = constraint.resolveAllowedRegions(['US', 'CA', 'MX'], null)
        assert result == ['US', 'CA', 'MX']
    }

    void testResolveAllowedRegionsClosure() {
        def result = constraint.resolveAllowedRegions({ myRegion }, [myRegion: 'CA'])
        assert result == ['CA']
    }

    void testProcessValidateFalseParam() {
        constraint.parameter = false
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 0
    }

    void testProcessValidateNonStrictPass() {
        constraint.parameter = true
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 0
    }

    void testProcessValidateNonStrictFailParseException() {
        phonenumber = 'ABCDEFG'
        constraint.parameter = true
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 1
        assert errors.getFieldError('phonenumber').arguments[3] == NumberParseException.ErrorType.NOT_A_NUMBER
    }

    void testProcessValidateNonStrictFailImpossible() {
        phonenumber = phonenumber + '1'
        constraint.parameter = true
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 1
        assert errors.getFieldError('phonenumber').arguments[3] == PhoneNumberUtil.ValidationResult.TOO_LONG
    }

    void testProcessValidateStrictPass() {
        constraint.parameter = [strict: true]
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 0
    }

    void testProcessValidateStrictFail() {
        phonenumber = '5552345678'
        constraint.parameter = [strict: true]
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 1
        assert errors.getFieldError('phonenumber').arguments[4] == 'not valid for any supported region'
    }

    void testProcessValidateStrictWithRegionPass() {
        constraint.parameter = [strict: true, allowedRegions: ['US']]
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 0
    }

    void testProcessValidateStrictWithRegionFail() {
        phonenumber = '6042345678'
        constraint.parameter = [strict: true, allowedRegions: ['US']]
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 1
        assert errors.getFieldError('phonenumber').arguments[4] == 'not valid for US'
    }

    void testProcessValidateOverrideDefaultAllowedRegions() {
        defaultRegion = 'CA'
        allowedRegions = ['CA']
        strict = true
        constraint.processValidate(this, phonenumber, errors)

        assert errors.errorCount == 1
        assert errors.getFieldError('phonenumber').arguments[4] == 'not valid for any supported region'
    }
}
