package com.bu.MoneyManage.security;

import com.bu.MoneyManage.utility.AESUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PasswordEncryptConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String password) {

        if (password == null)
            return null;

        return AESUtil.encrypt(password);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {

        if (dbData == null)
            return null;

        return AESUtil.decrypt(dbData);
    }
}
