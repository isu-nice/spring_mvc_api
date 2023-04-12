package com.codestates.coffee;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/coffees")
public class CoffeeController {

    @PostMapping
    public String postCoffee(
            @RequestParam("engName") String engName,
            @RequestParam("korName") String korName
    ) {
        System.out.println("# engName = " + engName);
        System.out.println("# korName = " + korName);

        String response =
                "{\"" +
                        "engName\":\"" + engName + "\"," +
                        "\"korName\":\"" + korName + "\"" +
                        "}";

        return response;
    }

    @GetMapping("/{coffee-id}")
    public String getCoffee(@PathVariable("coffee-id") long coffeeId) {
        System.out.println("# coffeeId = " + coffeeId);

        // not implementation
        return null;
    }

    @GetMapping
    public String getCoffees() {
        System.out.println("# get Coffees");

        // not implementation
        return null;
    }

}
