package com.example.web_honeypot.repository;

import com.example.web_honeypot.model.AttackLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttackLogRepository extends MongoRepository<AttackLog, String> {
    List<AttackLog> findByTimestampAfter(LocalDateTime timestamp);
    List<AttackLog> findByIpAndTimestampAfter(String ip, LocalDateTime timestamp);
    long countByAttackType(String attackType);
    long countByIp(String ip);
    
    @Query(value = "{'blocked': true}", count = true)
    long countDistinctIpByBlocked(boolean blocked);
    
    Optional<AttackLog> findFirstByIpOrderByTimestampDesc(String ip);
    
    @Query(value = "{'timestamp': {$gte: ?0}}", fields = "{'attackType': 1}")
    List<AttackLog> findAttackTypesByTimestampAfter(LocalDateTime timestamp);
    
    @Query(value = "{'ip': ?0}", sort = "{'timestamp': -1}")
    List<AttackLog> findByIpOrderByTimestampDesc(String ip);
} 