package sk.tuke.kpi.core.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sk.tuke.kpi.core.model.Presentation;

@Repository
@Transactional
public interface PresentationDao extends JpaRepository<Presentation, Long> {
}
