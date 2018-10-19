package com.springboot.three;

import java.lang.annotation.Annotation;

import org.springframework.transaction.annotation.Transactional;

import com.springboot.three.service.UserService;

public class TransTest {

	public static void main(String[] args) {
		Annotation parentAnnotation = UserService.class.getAnnotation(Transactional.class);
	}

}
