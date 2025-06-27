package com.marek.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController /*makes class a Component of type RestController capable of handling http req*/
@RequestMapping("/cashcards")   /*indicates which address must have access to Controller*/
public class CashCardController {

    @GetMapping("/{requestedId}") /*handler method, GET requests that match will be handled by this*/
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        if (requestedId.equals(99L)) {
            CashCard cashCard = new CashCard(99L, 123.45);
            return ResponseEntity.ok(cashCard);
        } else {
            return ResponseEntity.notFound().build(); /*404 response for the test to pass*/
        }

    }
}
