
package com.project.shopping.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UpdateUserPasswordDto {

    private String oldPassword;
    private String newPassword;
    @Schema(hidden = true)
    private boolean valid;

    public UpdateUserPasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.valid = isValid(oldPassword, newPassword);
    }

    private boolean isValid(String oldPassword, String newPassword) {
        return oldPassword != null && !oldPassword.trim().isEmpty() &&
                newPassword != null && !newPassword.trim().isEmpty();
    }

}
