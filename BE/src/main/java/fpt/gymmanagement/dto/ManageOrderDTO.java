package fpt.gymmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManageOrderDTO extends OrderDTO {
    private String assignId;
    private UserDTO assignedPerson;
}
