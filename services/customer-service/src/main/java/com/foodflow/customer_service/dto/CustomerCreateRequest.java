    package com.foodflow.customer_service.dto;

    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;
    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class CustomerCreateRequest {

        private String name;
        @NotBlank(message = "Phone number should not be blank")
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Phone number must be a valid 10-digit Indian number"
        )
        private String phone;
        @NotBlank
        @Size(min = 8, message = "Password should be at least 8 characters")
        private String password;
    }
