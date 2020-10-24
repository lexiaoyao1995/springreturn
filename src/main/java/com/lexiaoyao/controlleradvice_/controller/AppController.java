package com.lexiaoyao.controlleradvice_.controller;

import com.lexiaoyao.controlleradvice_.annos.ResponseResultBody;
import com.lexiaoyao.controlleradvice_.model.Person;
import com.lexiaoyao.controlleradvice_.model.Result;
import com.lexiaoyao.controlleradvice_.model.ResultException;
import com.lexiaoyao.controlleradvice_.model.ResultStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseResultBody//自定义的统一处理注解
public class AppController {

    /**
     * 会被自动包装为result
     *
     * @return
     */
    @GetMapping
    public Person get() {
        return new Person("tom");
    }

    @GetMapping("result")
    public Result get1() {
        return Result.success(new Person("tom"));
    }

    @PostMapping
    public void post() {
        throw new ResultException(ResultStatus.INTERNAL_SERVER_ERROR);
    }

}