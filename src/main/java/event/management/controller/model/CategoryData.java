package event.management.controller.model;

import event.management.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryData {
  private Long categoryId;
  private String categoryName;
  private String categoryNote;
  
  public CategoryData(Category category) {
    this.categoryId = category.getCategoryId();
    this.categoryName = category.getCategoryName();
    this.categoryNote = category.getCategoryNote();
  }
}
