package com.teliolabs.tejas.l2swt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.teliolabs.tejas.l2swt.domain.Tunnels;

@Repository
public interface TunnelRepo extends JpaRepository<Tunnels, Integer> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE TEJAS_SWITCH_TUNNEL", nativeQuery = true)
    void truncateTable();
}