
package com.project.shopping.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private String country;
    private String userRole;
    private int avatar;

}
