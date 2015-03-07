package sk.tuke.kpi.eprez.web.controllers;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.dao.AttachmentDao;
import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Attachment;
import sk.tuke.kpi.eprez.core.model.Presentation;
import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;

@Controller
@Scope("view")
public class ViewPresentationController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewPresentationController.class);

	@Autowired
	transient PresentationDao presentationDao;
	@Autowired
	transient AttachmentDao attachmentDao;

	String id;

	Presentation presentation;

	public void init() {
		presentation = id == null ? new Presentation() : presentationDao.findOne(id);
	}

	public void onSave() {
		LOGGER.info("Saving presentation:\n" + presentation);
		presentation = presentationDao.save(presentation);
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

		final Attachment attachment = attachmentDao.save(new Attachment(uploadedFile.getFileName(), uploadedFile.getContents()));
		presentation.getAttachments().add(attachment);
		presentation = presentationDao.save(presentation);
	}

	public void onDocumentDelete(final Attachment attachment) {
		LOGGER.info("Deleting attachment:\n" + attachment);
		presentation.getAttachments().remove(attachment);
		presentation = presentationDao.save(presentation);
		attachmentDao.delete(attachment);
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

	public String getImageBase64() {
		return presentation == null || presentation.getImage().length == 0 ? null : Base64.getEncoder().encodeToString(presentation.getImage());
	}

	public StreamedContent getAttachment(final Attachment attachment) {
		return new ByteArrayContent(attachment.getData(), null, attachment.getName());
	}

	public Date getMinDate() {
		return new Date();
	}
}
