package digital.zil.hl.module1.controller.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExhibitExceptionHandler {


    private static final String EXHIBIT_HAS_EXCURSIONS_MSG = "linked to existing excursions";

    @ExceptionHandler(ExhibitException.class)
    public ResponseEntity<String> onExhibitException(ExhibitException e) {
        String message = e.getMessage();

        if (message.contains(EXHIBIT_HAS_EXCURSIONS_MSG)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }
}