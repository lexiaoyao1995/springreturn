package com.lexiaoyao.controlleradvice_.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultException extends RuntimeException {

    private ResultStatus resultStatus;


}