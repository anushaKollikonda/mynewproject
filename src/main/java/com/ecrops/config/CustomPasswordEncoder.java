package com.ecrops.config;

import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;




public class CustomPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		// TODO Auto-generated method stub
		//return DigestUtils.sha256Hex(rawPassword.toString().getBytes());
	return DigestUtils.md5Hex(DigestUtils.md5Hex(rawPassword.toString().getBytes()).getBytes());
	//	return rawPassword.toString();
		//return Sha512DigestUtils.shaHex(rawPassword.toString().getBytes());
	}

	 @Override
	    public boolean matches(CharSequence rawPassword, String encodedPassword) {
	        String rawEncoded = encode(rawPassword);
	        return Objects.equals(rawEncoded, encodedPassword);
	    }
	 
	 

}