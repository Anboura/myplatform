package com.myplatform.media.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationError {
    private int status;
    private String reason;
    private String errorCode;
    private String frontCode;
    @Builder.Default
    private String timeStamp = new Timestamp(System.currentTimeMillis()).toString() ;
    private String message;
    private Map<String, String> frontParams;
}
