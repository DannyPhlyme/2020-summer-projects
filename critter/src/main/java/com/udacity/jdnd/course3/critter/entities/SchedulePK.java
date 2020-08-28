package com.udacity.jdnd.course3.critter.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SchedulePK implements Serializable {
    private Long employeeId;
    private Long petId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchedulePK)) return false;
        SchedulePK that = (SchedulePK) o;
        return getEmployeeId().equals(that.getEmployeeId()) &&
                getPetId().equals(that.getPetId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getPetId());
    }
}
