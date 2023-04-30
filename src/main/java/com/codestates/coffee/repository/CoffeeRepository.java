package com.codestates.coffee.repository;

import com.codestates.coffee.entity.Coffee;
// import org.springframework.data.jdbc.repository.query.Query;
// import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    Optional<Coffee> findByCoffeeCode(String coffeeCode);

    // @Query("SELECT * FROM COFFEE WHERE COFFEE_ID = :coffeeId")
    @Query(value = "SELECT c FROM Coffee c WHERE c.coffeeId = :coffeeId") // JPQL
    Optional<Coffee> findByCoffee(Long coffeeId);
}
