package com.afn.realstat.ui;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.LazyComboBox;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.AbstractEntityRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Lov<T extends AbstractEntity> {

	public static String popUpWindow = "popUpWindow";
	public static String comboBox = "comboBox";

	AbstractEntityRepository<T> repo;

	private LazyComboBox<T> comboBoxLov;
	private Window popUpWindowLov;
	private T selected = null;
	private Class<T> type;
	private StringPath strPath;
	private Function<T, Boolean> action;

	public Lov(Class<T> type, AbstractEntityRepository<T> repo, StringPath strPath, Function<T,Boolean> action) {
		this.type = type;
		this.repo = repo;
		comboBoxLov = createLov();
		this.strPath = strPath;
		this.action = action;
		this.selected = null;
	}

	public LazyComboBox<T> getComboBox() {
		return comboBoxLov;
	}

	public Window getPopUpWindowLov() {

		VerticalLayout popupContent = new VerticalLayout();

		popupContent.addComponent(comboBoxLov);
		// popupContent.addComponent(new Button("Button"));

		// The component itself
		popUpWindowLov = new Window("Select Agent", popupContent);
		popUpWindowLov.setWidth(25.0f,Unit.PERCENTAGE);
		popUpWindowLov.setModal(true);
		popUpWindowLov.setContent(popupContent);
		return popUpWindowLov;

	}

	private LazyComboBox<T> createLov() {
		LazyComboBox<T> lov = new LazyComboBox<T>(getType(),

				new LazyComboBox.FilterablePagingProvider<T>() {

					@Override
					public List<T> findEntities(int firstRow, String filter) {
						
						Predicate predicate = strPath.containsIgnoreCase(filter);
						int batchSize = LazyList.DEFAULT_PAGE_SIZE;
						int pageNumber = firstRow / batchSize;
						PageRequest pr = new PageRequest(pageNumber, batchSize);
						Page<T> page = (Page<T>) repo.findAll(predicate, pr);
						List<T> list = page.getContent();
						return list;
					}
				}, new LazyComboBox.FilterableCountProvider() {

					@Override
					public int size(String filter) {
						// Todo move up
						Predicate predicate = strPath.containsIgnoreCase(filter);

						long count = repo.count(predicate);
						return (int) count;
					}

				});

		// TODO figure out what to do with this one
		@SuppressWarnings("serial")
		MValueChangeListener<T> listener = new MValueChangeListener<T>() {
			@Override
			public void valueChange(MValueChangeEvent<T> event) {
				// TOOD figure out why case it necessary
				T entity = event.getValue();
				
				action.apply(entity);
				
				if (popUpWindowLov != null) {
					popUpWindowLov.close();
				}
				if (comboBoxLov != null) {
					//TODO test combobox LOV
					// comboBoxLov.close();
				}
			}
		};
		lov.addMValueChangeListener((MValueChangeListener<T>) listener);
		lov.setCaption("Enter Agent as 'LastName, FirstName' and then select");
		lov.setSizeFull();
		return (LazyComboBox<T>) lov;

	}

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

	public T getSelected() {
		return selected;
	}

	public void setSelected(T selected) {
		this.selected = selected;
	}

}
