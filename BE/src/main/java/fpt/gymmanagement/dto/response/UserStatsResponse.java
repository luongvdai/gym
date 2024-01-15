package fpt.gymmanagement.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStatsResponse {
    Long total;
    Long totalMale;
    Long totalFemale;
}
