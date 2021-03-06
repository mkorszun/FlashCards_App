package org.robbins.flashcards.service.springdata;

import javax.inject.Inject;

import org.robbins.flashcards.model.Tag;
import org.robbins.flashcards.repository.springdata.TagRepository;
import org.robbins.flashcards.service.TagService;
import org.robbins.flashcards.service.springdata.base.GenericJpaServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends GenericJpaServiceImpl<Tag, Long> implements TagService {
	
	@Inject
	private TagRepository repository;
	
	@Override
	protected JpaRepository <Tag, Long> getRepository() {
		return repository;
	}
	
	@Override
	public Tag findByName(String name) {
		return repository.findByName(name);
	}
}
