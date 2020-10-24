package com.lexiaoyao.controlleradvice_.annos;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ResponseBody//继承
public @interface ResponseResultBody {

}