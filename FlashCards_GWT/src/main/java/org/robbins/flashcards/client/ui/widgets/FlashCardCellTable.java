package org.robbins.flashcards.client.ui.widgets;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.robbins.flashcards.events.AppUtils;
import org.robbins.flashcards.events.LoadFlashCardEvent;
import org.robbins.flashcards.events.LoadTagEvent;
import org.robbins.flashcards.model.FlashCard;
import org.robbins.flashcards.model.Tag;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.ListDataProvider;

public class FlashCardCellTable extends CellTable<FlashCard>{
    
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
	
	private LinkCell questionCell;
	private Column<FlashCard, String> questionColumn;
	
	private TagListCell tagsCell;
	private Column<FlashCard, List<Tag>> tagsColumn;
	
	private DateCell dateCreatedCell;
	private Column<FlashCard, Date> dateCreatedColumn;
	
	private DateCell dateUpdatedCell;
	private Column<FlashCard, Date> dateUpdatedColumn;
	
	private ListDataProvider<FlashCard> dataProvider;
	private List<FlashCard> flashCardsList;
	
    public FlashCardCellTable() {
    	super();
    	this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
    }
    
    public void init() {
    	createColumns();
        createColumnSorting();
        formatCellTable();
    }
    
    public void setInput(List<FlashCard> tags) {

        bindData(tags);

        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        this.setRowCount(flashCardsList.size(), true);
    }
    
	private void bindData(List<FlashCard> tags) {
		dataProvider = new ListDataProvider<FlashCard>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(this);

        flashCardsList = dataProvider.getList();
        for (FlashCard tag : tags) {
        	flashCardsList.add(tag);
        }
	}

	private void formatCellTable() {
        // Set the width of the table and put the table in fixed width mode.
        this.setWidth("100%", true);
        
		// Set the width of each column.
        this.setColumnWidth(questionColumn, 70.0, Unit.PCT);
        this.setColumnWidth(tagsColumn, 10.0, Unit.PCT);
        this.setColumnWidth(dateCreatedColumn, 10.0, Unit.PCT);
        this.setColumnWidth(dateUpdatedColumn, 10.0, Unit.PCT);
	}

	private void createColumnSorting() {
		// Add a ColumnSortEvent.ListHandler to connect sorting to the List
        ListHandler<FlashCard> nameColumnSortHandler = new ListHandler<FlashCard>(flashCardsList);
        nameColumnSortHandler.setComparator(questionColumn,
            new Comparator<FlashCard>() {
              public int compare(FlashCard o1, FlashCard o2) {
                  if (o1 == o2) {
                      return 0;
                    }

                    // Compare the name columns.
                    if (o1 != null) {
                      return (o2 != null) ? o1.getQuestion().compareTo(o2.getQuestion()) : 1;
                    }
                    return -1;
                  }

            });
        this.addColumnSortHandler(nameColumnSortHandler);
        
        // Add a ColumnSortEvent.ListHandler to connect sorting to the List
        ListHandler<FlashCard> createdColumnSortHandler = new ListHandler<FlashCard>(flashCardsList);
        createdColumnSortHandler.setComparator(dateCreatedColumn,
            new Comparator<FlashCard>() {
              public int compare(FlashCard o1, FlashCard o2) {
                  if (o1 == o2) {
                      return 0;
                    }

                    // Compare the name columns.
                    if (o1 != null) {
                      return (o2 != null) ? o1.getCreatedDate().compareTo(o2.getCreatedDate()) : 1;
                    }
                    return -1;
                  }

            });
        this.addColumnSortHandler(createdColumnSortHandler);

        // Add a ColumnSortEvent.ListHandler to connect sorting to the List
        ListHandler<FlashCard> updatedColumnSortHandler = new ListHandler<FlashCard>(flashCardsList);
        updatedColumnSortHandler.setComparator(dateUpdatedColumn,
            new Comparator<FlashCard>() {
              public int compare(FlashCard o1, FlashCard o2) {
                  if (o1 == o2) {
                      return 0;
                    }

                    // Compare the name columns.
                    if (o1 != null) {
                      return (o2 != null) ? o1.getLastModifiedDate().compareTo(o2.getLastModifiedDate()) : 1;
                    }
                    return -1;
                  }

            });
        this.addColumnSortHandler(updatedColumnSortHandler);
        
        // We know that the data is sorted alphabetically by default.
        this.getColumnSortList().push(questionColumn);
	}

	private void createColumns() {

		questionCell = new LinkCell();
		questionColumn = new Column<FlashCard, String>(questionCell) {
          @Override
          public String getValue(FlashCard object) {
            return object.getQuestion();
          }
        };
        questionColumn.setSortable(true);
        this.addColumn(questionColumn, "Question");
        
        // Add a field updater to be notified when the user enters a new name.
        questionColumn.setFieldUpdater(new FieldUpdater<FlashCard, String>() {
			@Override
			public void update(int index, FlashCard object, String value) {
				AppUtils.EVENT_BUS.fireEvent(new LoadFlashCardEvent(object.getId()));
			}
		});
        
        tagsCell = new TagListCell();
        tagsColumn = new Column<FlashCard, List<Tag>>(tagsCell) {
            @Override
            public List<Tag> getValue(FlashCard object) {
            	return object.getTagsAsList();
            }
          };
          tagsColumn.setSortable(true);
          this.addColumn(tagsColumn, "Tags");
          
          // Add a field updater to be notified when the user enters a new name.
          tagsColumn.setFieldUpdater(new FieldUpdater<FlashCard, List<Tag>>() {
			@Override
			public void update(int index, FlashCard object, List<Tag> tagList) {
				GWT.log("update: " + tagList.get(0).getId());
				AppUtils.EVENT_BUS.fireEvent(new LoadTagEvent(tagList.get(0).getId()));
			}
		});
        
        dateCreatedCell = new DateCell(dateFormat);
        dateCreatedColumn = new Column<FlashCard, Date>(dateCreatedCell) {
          @Override
          public Date getValue(FlashCard tag) {
            return tag.getCreatedDate().toDate();
          }
        };
        dateCreatedColumn.setSortable(true);
        this.addColumn(dateCreatedColumn, "Date Created");

        dateUpdatedCell = new DateCell(dateFormat);
        dateUpdatedColumn = new Column<FlashCard, Date>(dateUpdatedCell) {
          @Override
          public Date getValue(FlashCard tag) {
            return tag.getLastModifiedDate().toDate();
          }
        };
        dateUpdatedColumn.setSortable(true);
        this.addColumn(dateUpdatedColumn, "Date Updated");
	}
}
