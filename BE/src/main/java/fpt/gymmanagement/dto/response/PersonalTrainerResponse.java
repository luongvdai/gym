package fpt.gymmanagement.dto.response;

import fpt.gymmanagement.dto.UserDTO;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalTrainerResponse extends UserDTO {
    private StatsFeedback statsFeedback;

    public static PersonalTrainerResponse getResponse(User user) {
        PersonalTrainerResponse response = new PersonalTrainerResponse();
        CommonAppUtils.copyPropertiesNotNull(user, response);
        return response;
    }

    @Builder
    @Getter
    @Setter
    public static class StatsFeedback {
        private double average;
        private int total;
    }
}
