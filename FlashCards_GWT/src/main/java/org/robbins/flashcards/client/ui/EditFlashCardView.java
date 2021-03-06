package org.robbins.flashcards.client.ui;

import java.util.List;

import org.robbins.flashcards.model.FlashCard;
import org.robbins.flashcards.model.Tag;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public interface EditFlashCardView extends IsValidatable, HasSubmitButtons {

	HasText getQuestion();
	HasHTML getAnswer();
	List<String> getTags();
	List<String> getLinks();
	
	void displayQuestionValidationMessage(String message);
	
	HasText getCreatedDate();
	HasText getModifiedDate();

	boolean validate();

	void setFlashCardData(FlashCard data);
	void setTagsData(List<Tag> tags);

	Widget asWidget();
}
