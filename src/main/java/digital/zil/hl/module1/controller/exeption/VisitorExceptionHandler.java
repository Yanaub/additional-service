package digital.zil.hl.module1.controller.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VisitorExceptionHandler {

    private static final String VISITOR_HAS_EXCURSIONS_MSG = "linked to existing excursions";

    @ExceptionHandler(VisitorException.class)
    public ResponseEntity<String> onVisitorException(VisitorException e) {
        String message = e.getMessage();

        if (message.contains(VISITOR_HAS_EXCURSIONS_MSG)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }
}