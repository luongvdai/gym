package fpt.gymmanagement.dto.request.user;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String address;
    private String phoneNumber;
    private Boolean gender;
    private Long dateOfBirth;
    private String avatar;
}
