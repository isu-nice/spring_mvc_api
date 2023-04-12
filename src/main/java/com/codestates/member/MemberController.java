package com.codestates.member;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/members", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MemberController {

    @PostMapping
    public String postMember(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone
    ) {
        System.out.println("# email = " + email);
        System.out.println("# name = " + name);
        System.out.println("# phone = " + phone);

        String response =
                "{\"" +
                        "email\":\"" + email + "\"," +
                        "\"name\":\"" + name + "\",\"" +
                        "phone\":\"" + phone +
                        "\"}";

        return response;
    }


}
