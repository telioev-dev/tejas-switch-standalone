package com.teliolabs.tejas.gpon.domain;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TEJAS_GPON_OLT_ONT_TOPOLOGY")
@Slf4j
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_LABEL")
    private String userLabel;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "TECHNOLOGY")
    private String technology;

    @Column(name = "SPECIFICATION")
    private String specification;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "Z_END_VENDOR")
    private String zEndVendor;

    @Column(name = "A_END_VENDOR")
    private String aEndVendor;

    @Column(name = "A_END_NODE")
    private String aEndNode;

    @Column(name = "Z_END_NODE")
    private String zEndNode;

    @Column(name = "A_END_PORT")
    private String aEndPort;
    
    @Column(name = "Z_END_PORT")
    private String zEndPort;
    
    @Column(name = "CIRCLE")
    private String circle;
}
