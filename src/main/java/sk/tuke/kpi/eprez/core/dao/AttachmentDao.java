package sk.tuke.kpi.eprez.core.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import sk.tuke.kpi.eprez.core.model.Attachment;

public interface AttachmentDao extends MongoRepository<Attachment, String> {
}
