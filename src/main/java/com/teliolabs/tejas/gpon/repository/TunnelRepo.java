package com.teliolabs.tejas.gpon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.teliolabs.tejas.gpon.domain.Connection;
import com.teliolabs.tejas.gpon.domain.Tunnels;

@Repository
public interface TunnelRepo extends JpaRepository<Tunnels, Integer> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE TEJAS_GPON_OLT_ONT_TUNNEL", nativeQuery = true)
    void truncateTable();
}