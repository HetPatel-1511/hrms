package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelDocumentRepo extends JpaRepository<TravelDocument, Long> {

    @Query("SELECT td FROM TravelDocument td JOIN td.travelPlanEmployee tpe WHERE tpe.id = :travelPlanEmployeeId")
    List<TravelDocument> findAllByTravelPlanEmployeeId(@Param("travelPlanEmployeeId") Long travelPlanEmployeeId);
}
