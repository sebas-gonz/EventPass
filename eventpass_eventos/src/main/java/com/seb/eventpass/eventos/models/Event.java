package com.seb.eventpass.eventos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "events", schema = "events")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Event extends BaseEntity{
    private @Id Long id;

    @Column("event_name")
    private String eventName;

    @Column("event_description")
    private String eventDescription;

    @Column("event_date")
    private Instant eventDate;

    @Column("event_address")
    private String eventAddress;

    @Column("total_event_capacity")
    private Integer totalEventCapacity;

}
