package model;

import lombok.Getter;
import lombok.Setter;
import model.enums.State;

import java.util.Date;

@Setter
@Getter
public abstract class BaseEntity {
     private  int id;
     private Date createdAt;
     private Date lastUpdatedAt;
     private State state;
}
