package org.robbins.flashcards.client.activity;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;
import org.robbins.flashcards.client.factory.ClientFactory;
import org.robbins.flashcards.client.place.EditTagPlace;
import org.robbins.flashcards.client.place.ListTagsPlace;
import org.robbins.flashcards.client.place.NewTagPlace;
import org.robbins.flashcards.client.ui.EditTagView;
import org.robbins.flashcards.model.Tag;
import org.robbins.flashcards.service.FlashCardRestService;
import org.robbins.flashcards.service.TagRestService;
import org.robbins.flashcards.util.ConstsUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class EditTagActivity extends AppAbstractActivity {

	private Tag tag;
	private final TagRestService tagService;
	private final FlashCardRestService flashCardService;
	private EditTagView display;


	public EditTagActivity(ClientFactory clientFactory) {
		super(clientFactory);
		GWT.log("Creating 'EditTagActivity'");
		
		this.tagService = clientFactory.getTagService();
		this.flashCardService = clientFactory.getFlashCardService();
		this.display = clientFactory.getEditTagView();

		((RestServiceProxy)flashCardService).setResource(new Resource(""));
		((RestServiceProxy)tagService).setResource(new Resource(""));
		
		EditTagActivity.this.display.initFormValidation();
//		if (this.display instanceof RequiresLogin) {
//			requireLogin(this.display);
//		}
		bind();
	}
	
	public EditTagActivity(NewTagPlace place, ClientFactory clientFactory) {
		this(clientFactory);
		
		setTag(new Tag());
		this.display.setFlashCardsData(null);
		this.display.setTagData(null);
	}

	public EditTagActivity(EditTagPlace place, ClientFactory clientFactory) {
		this(clientFactory);
		
		Long tagId = Long.parseLong(place.getPlaceName());
		String fields = ConstsUtil.DEFAULT_TAGS_FIELDS;
		
		tagService.getTag(ConstsUtil.DEFAULT_AUTH_HEADER, tagId, fields, new MethodCallback<Tag>() {
			public void onSuccess(Method method, Tag tag) {
				GWT.log("EditTagActivity: 'Load Tag' TagId: " + tag.getId());
				setTag(tag);
				EditTagActivity.this.display.setTagData(tag);
				EditTagActivity.this.display.setFlashCardsData(tag.getFlashcardsAsList());
			}
			public void onFailure(Method method, Throwable caught) {
				GWT.log("EditTagActivity: Error loading data");
				Window.alert(getConstants().errorLoadingTag());
			}
		});
	}
	
	public void bind() {
		getRegistrations().add(display.getSaveButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("EditTagActivity: 'Submit' button clicked");
				if (!display.validate()) return;
				EditTagActivity.this.display.getSubmitEnabled().setEnabled(false);
				doSave();
			}
		}));

		getRegistrations().add(display.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("EditTagActivity: 'Cancel' button clicked");
				History.back();
			}
		}));
	}

	private Tag getTag() {
		return this.tag;
	}

	private void setTag(Tag tag) {
		this.tag = tag;
	}
	
	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
	}
	
	private void doSave() {
		getTag().setName(display.getName().getText());

		// if we have a Tag Id then it's an update so go ahead and persist 
		if ( (getTag().getId() != null) && (getTag().getId() != 0) ) {
			updateTag(getTag());
		} else {
			// since we don't have a Tag Id then let's see if the name exists before creating a new Tag
			tagService.getTagsSearch(ConstsUtil.DEFAULT_AUTH_HEADER, getTag().getName(), new MethodCallback<Tag>() {
				public void onSuccess(Method method, Tag tag) {
					if (tag == null) {
						GWT.log("EditTagActivity: Tag does not exist.  Creating new Tag");
	
						saveTag(getTag());
					}
					else {
						GWT.log("EditTagActivity: Tag already exists: '" + tag.toString() + "'");
						display.displayTagNameValidationMessage(getConstants().tagNameAlreadyExists());
					}
				}
				public void onFailure(Method method, Throwable caught) {
					GWT.log("EditTagActivity: Error finding tag");
					Window.alert(getConstants().errorLoadingTag());
				}
			});
			EditTagActivity.this.display.getSubmitEnabled().setEnabled(true);
		}
	}
	
	private void saveTag(Tag tag) {
		tagService.postTags(ConstsUtil.DEFAULT_AUTH_HEADER, tag, new MethodCallback<Tag>() {
			public void onSuccess(Method method, Tag result) {
				GWT.log("EditTagActivity: Tag Saved:" + result);
				EditTagActivity.this.display.getSubmitEnabled().setEnabled(true);
				getPlaceController().goTo(new ListTagsPlace(""));
			}
			public void onFailure(Method method, Throwable caught) {
				GWT.log("EditTagActivity: Error saving data");
				Window.alert(getConstants().errorSavingTag());
				EditTagActivity.this.display.getSubmitEnabled().setEnabled(true);
			}
		});
	}
	
	private void updateTag(Tag tag) {
		tagService.putTag(ConstsUtil.DEFAULT_AUTH_HEADER, tag.getId(), tag, new MethodCallback<java.lang.Void>() {
			public void onSuccess(Method method, java.lang.Void result) {
				GWT.log("EditTagActivity: Tag updated");
				EditTagActivity.this.display.getSubmitEnabled().setEnabled(true);
				getPlaceController().goTo(new ListTagsPlace(""));
			}
			public void onFailure(Method method, Throwable caught) {
				GWT.log("EditTagActivity: Error saving data");
				Window.alert(getConstants().errorSavingTag());
				EditTagActivity.this.display.getSubmitEnabled().setEnabled(true);
			}
		});
	}
}
