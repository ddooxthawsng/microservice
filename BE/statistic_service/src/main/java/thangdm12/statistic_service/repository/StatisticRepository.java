package thangdm12.statistic_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import thangdm12.statistic_service.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Long>{

}