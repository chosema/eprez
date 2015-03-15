package sk.tuke.kpi.eprez.web.controllers;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.dao.AttachmentDao;
import sk.tuke.kpi.eprez.core.dao.DataDao;
import sk.tuke.kpi.eprez.core.dao.DocumentPageDao;
import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Attachment;
import sk.tuke.kpi.eprez.core.model.Data;
import sk.tuke.kpi.eprez.core.model.DocumentPage;
import sk.tuke.kpi.eprez.core.model.Presentation;
import sk.tuke.kpi.eprez.core.model.PresentationDocument;
import sk.tuke.kpi.eprez.core.model.enums.DocumentState;
import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;
import sk.tuke.kpi.eprez.docs.DocumentProcessor;

@Controller
@Scope("view")
public class ViewPresentationController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewPresentationController.class);

	@Autowired
	transient PresentationDao presentationDao;
	@Autowired
	transient AttachmentDao attachmentDao;
	@Autowired
	transient DataDao dataDao;
	@Autowired
	transient DocumentPageDao documentPageDao;
	@Autowired
	transient TaskExecutor taskExecutor;

	String id;

	Presentation presentation;
	boolean userAuthor;

	DocumentState lastDocumentState;

	@Override
	public void init() {
		super.init();
		presentation = id == null ? new Presentation() : presentationDao.findOne(id);
		if (presentation == null) {
			throw new IllegalArgumentException("Presentation with id=" + id + " not found");
		} else {
			userAuthor = id == null ? true : presentation.getCreatedBy().equals(getLoggedUser());
			if (!userAuthor) {
				if (presentation.isPublished()) {
					presentation.setAttachments(presentation.getAttachments().stream().filter(att -> att.isAvailable()).collect(Collectors.<Attachment>toList()));
				} else {
					throw new IllegalArgumentException("Can not access to unpublished presentation");
				}
			}
		}

		startDocumentPolling();
	}

	public void onSave() {
		if (presentation.isPublished() && (presentation.getStartTime() == null || presentation.getDuration() == 0)) {
			presentation.setPublished(false);
			showErrorMessage("presentation", "To publish presentation you have to set start time and duration");
		} else {
			LOGGER.info("Saving presentation:\n" + presentation);
			presentation = presentationDao.save(presentation);
		}
	}

	public void onCategorySelect(final SelectEvent event) {
		final Object selected = event.getObject();
		final PresentationCategory category;
		if (selected instanceof PresentationCategory) {
			category = (PresentationCategory) selected;
		} else if (selected instanceof String) {
			category = PresentationCategory.valueOfByLabel(String.valueOf(selected));
		} else {
			throw new IllegalArgumentException("Unknown selected category: " + selected);
		}

		if (presentation.getCategories().contains(category)) {
			showWarnMessage("presentation", "Category '" + category.getLabel() + "' already exists");
		} else {
			LOGGER.info("Adding presentation category " + category);
			presentation.getCategories().add(category);
			presentation = presentationDao.save(presentation);
			// showInfoMessage("presentation", "Category '" + category.getLabel() + "' added");
		}
	}

	public void onCategoryRemove(final String category) {
		LOGGER.info("Removing presentation category: " + category);
		final PresentationCategory presentationCategory = PresentationCategory.valueOf(category);
		presentation.getCategories().remove(presentationCategory);
		presentation = presentationDao.save(presentation);
		// showInfoMessage("presentation", "Category '" + presentationCategory.getLabel() + "' removed");
	}

	public void onImageUpload(final FileUploadEvent event) {
		presentation.setImage(event.getFile().getContents());
		presentation = presentationDao.save(presentation);
	}

	public void onDocumentUpload(final FileUploadEvent event) {
		final UploadedFile uploadedFile = event.getFile();
		LOGGER.info("Saving uploaded attachment: " + uploadedFile.getFileName() + " [" + uploadedFile.getSize() / 1024 + " kB]");

		final Data data = dataDao.save(new Data(uploadedFile.getContents(), uploadedFile.getContentType(), uploadedFile.getContents().length));
		final Attachment attachment = attachmentDao.save(new Attachment(uploadedFile.getFileName(), data));
		presentation.getAttachments().add(attachment);
		presentation = presentationDao.save(presentation);
	}

	public void onDocumentDelete(final Attachment attachment) {
		LOGGER.info("Deleting attachment:\n" + attachment);
		presentation.getAttachments().remove(attachment);
		presentation = presentationDao.save(presentation);

		dataDao.delete(attachment.getData());
		attachmentDao.delete(attachment);
	}

	public void onDocumentPrepare(final Attachment attachment) {
		if (isDocumentPreparing()) {
			showErrorMessage(null, "Document preparation is already running, can not start another.");
			return;
		}

		LOGGER.info("Preparing new document from attachment: " + attachment.getName());

		final PresentationDocument document = new PresentationDocument(attachment.getName());
		presentation.setDocument(document);
		presentation = presentationDao.save(presentation);
		lastDocumentState = document.getState();

		showInfoMessage(null, "Preparing document for presentation. Please be patient, this process may take some time to complete.");

		taskExecutor.execute(() -> {
			try {
				final DocumentProcessor documentProcessor = new DocumentProcessor();
				final String contentType = documentProcessor.getImageContentType();
				final String format = documentProcessor.getImageFormat();

				documentProcessor.process(new ByteArrayInputStream(attachment.getData().getContent()), (index, imageData, image) -> {
					final Data data = dataDao.save(new Data(imageData, contentType, imageData.length));
					final DocumentPage documentPage = documentPageDao.save(new DocumentPage(index, format, data));
					document.getPages().add(documentPage);

				});

				document.setState(DocumentState.SUCCESSFULL);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
				document.setState(DocumentState.FAILED);
			}

			LOGGER.info("Document converted: " + document);
			presentation.setDocument(document);
			presentation = presentationDao.save(presentation);
		});
	}

	public void onPollDocument() {
		if (presentation != null && presentation.getId() != null) {
			final PresentationDocument document = presentationDao.findOne(presentation.getId()).getDocument();
			presentation.setDocument(document);

			LOGGER.debug("Polling " + document);

			stopDocumentPolling(document);
		}

		if (documentStateChanged()) { // update only on state change to reduce bandwidth
			RequestContext.getCurrentInstance().update("presentationForm:presentationDocumentState");
		}
	}

	private boolean documentStateChanged() {
		if (presentation.getDocument() == null || presentation.getDocument().getState() == lastDocumentState) {
			return false;
		} else {
			lastDocumentState = presentation.getDocument() == null ? null : presentation.getDocument().getState();
			return true;
		}
	}

	public void onSave(final Attachment attachment) {
		LOGGER.info("Saving attachment:\n" + attachment);
		attachmentDao.save(attachment);
	}

	public List<PresentationCategory> completeCategory(final String query) {
		final Predicate<PresentationCategory> availableCategoriesPredicate = category -> !presentation.getCategories().contains(category);
		final Predicate<PresentationCategory> queryPredicate = category -> StringUtils.isEmpty(query) ? true : StringUtils.startsWithIgnoreCase(category.getLabel(), query);
		return Arrays.stream(PresentationCategory.values()).filter(availableCategoriesPredicate).filter(queryPredicate).collect(Collectors.toList());
	}

	private void startDocumentPolling() {
		if (presentation.getDocument() != null && presentation.getDocument().getState() == DocumentState.PREPARING) {
			LOGGER.debug("Sending start on polling for " + presentation.getDocument());
			RequestContext.getCurrentInstance().execute("PF('pollDocumentWV').start()");
		}
	}

	private void stopDocumentPolling(final PresentationDocument document) {
		if (document.getState() == DocumentState.FAILED || document.getState() == DocumentState.SUCCESSFULL) {
			LOGGER.debug("Sending stop on polling for " + document);
			RequestContext.getCurrentInstance().execute("PF('pollDocumentWV').stop()");
			LOGGER.debug("Updating launchButtons panel");
			RequestContext.getCurrentInstance().update("presentationForm:launchButtons");
		}
	}

	public boolean isDocumentPrepared() {
		return presentation.getDocument() != null && presentation.getDocument().getState() == DocumentState.SUCCESSFULL;
	}

	public boolean isDocumentPreparing() {
		return presentation.getDocument() != null && presentation.getDocument().getState() == DocumentState.PREPARING;
	}

	public String getPublishMessageClass() {
		return presentation.isPublished() ? "info" : "warn";
	}

	public String getDocumentStateMessage() {
		if (presentation.getDocument() == null) {
			return "Document has not been prepared yet";
		} else if (presentation.getDocument().getState() == DocumentState.PREPARING) {
			return "Preparing document...";
		} else if (presentation.getDocument().getState() == DocumentState.SUCCESSFULL) {
			return "Document has been prepared from " + presentation.getDocument().getName();
		} else {
			return "Document preparing failed";
		}
	}

	public String getDocumentStateMessageClass() {
		if (presentation.getDocument() == null || presentation.getDocument().getState() == DocumentState.PREPARING) {
			return "warn";
		} else if (presentation.getDocument().getState() == DocumentState.SUCCESSFULL) {
			return "info";
		} else {
			return "error";
		}
	}

	public long getRemainingSecondsToStart() {
		return ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDateTime.ofInstant(presentation.getStartTime().toInstant(), ZoneId.systemDefault()));
	}

	public boolean isPresentationRunning() {
		return presentation.getInstance() != null && presentation.getInstance().isRunning();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public void setPresentation(final Presentation presentation) {
		this.presentation = presentation;
	}

	public boolean isUserAuthor() {
		return userAuthor;
	}

	public StreamedContent getAttachment(final Attachment attachment) {
		return new ByteArrayContent(attachment.getData().getContent(), attachment.getData().getContentType(), attachment.getName());
	}

	public Date getMinDate() {
		return new Date();
	}
}
