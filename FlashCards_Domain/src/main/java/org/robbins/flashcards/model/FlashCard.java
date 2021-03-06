package org.robbins.flashcards.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.robbins.flashcards.model.common.FlashCardsAppAbstractAuditable;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "flashcard")
@AttributeOverride(name="id", column=@Column(name="FlashCardId"))
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
@JsonFilter("apiFilter")
public class FlashCard extends FlashCardsAppAbstractAuditable<User, Long> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3461056579037652853L;

	@Column(name = "Question", unique = true, nullable = false)
	private String question;

	@Column(name = "Answer", nullable = false, columnDefinition = "LONGTEXT")
	private String answer;

	@ManyToMany(targetEntity = org.robbins.flashcards.model.Tag.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "flashcard_tag", joinColumns = @JoinColumn(name = "FlashCardId"), inverseJoinColumns = @JoinColumn(name = "TagId"))
	@OrderBy("name")
	private Set<Tag> tags = new HashSet<Tag>(0);

	@ElementCollection
	@CollectionTable(name = "flashcard_link", joinColumns = @JoinColumn(name = "FlashCardId"))
	@Column(name = "Link", nullable = false)
	@OrderColumn(name = "link_idx")
	private List<String> links = new ArrayList<String>(0);

	public FlashCard() {
	}

	public FlashCard(Long flashCardId) {
		setId(flashCardId);
	}

	public FlashCard(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}

	public FlashCard(String question, String answer, Set<Tag> tags,
			List<String> links) {
		this.question = question;
		this.answer = answer;
		this.tags = tags;
		this.links = links;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Set<Tag> getTags() {
		return this.tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@JsonIgnore
	@Transient
	public List<Tag> getTagsAsList() {
		if (getTags() == null) {
			return null;
		}
		List<Tag> tags = new ArrayList<Tag>();
		for (Tag tag : getTags()) {
			tags.add(tag);
		}
		return tags;
	}

	public List<String> getLinks() {
		return this.links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("id").append("='").append(getId()).append("' ");
		buffer.append("question").append("='").append(getQuestion())
				.append("' ");
		buffer.append("answer").append("='").append(getAnswer()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}
}
