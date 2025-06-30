package com.marek.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController /*makes class a Component of type RestController capable of handling http req*/
@RequestMapping("/cashcards")   /*indicates which address must have access to Controller*/
class CashCardController {
    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository){
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}") /*handler method, GET requests that match will be handled by this*/
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
        Optional<CashCard> cashCardOptional = Optional.ofNullable(cashCardRepository.findByIdAndOwner(requestedId, principal.getName()));
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build(); /*404 response*/
        }

    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb, Principal principal) {
        CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.amount(), principal.getName());
        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner); /*id is created here by database*/
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}") /*construction of URI*/
                .buildAndExpand(savedCashCard.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                ));
        return ResponseEntity.ok(page.getContent());
    }
}
