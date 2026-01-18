package com.javarush.stockvault.repository;

import com.javarush.stockvault.entity.UserStockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface UserStockPriceRepository extends JpaRepository<UserStockPrice, Long> {

    @Query("""
        select usp.date
        from UserStockPrice usp
        where usp.user.id = :userId
        and usp.ticker.id = :tickerId
        and usp.date between :start and :end
""")
    Set<LocalDate> findExistingDates(
            @Param("userId") Long userId,
            @Param("tickerId") Long tickerId,
            @Param("start")LocalDate start,
            @Param("end") LocalDate end
    );

    List<UserStockPrice> findByUserIdAndTickerIdOrderByDateAsc(Long userId, Long tickerId);
}
