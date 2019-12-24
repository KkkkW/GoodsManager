package ez.manager.Mapper;

import ez.manager.Model.ItemCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ItemCategoryMapper {

    ItemCategory findById(ItemCategory itemCategory);

    List<ItemCategory> list(ItemCategory itemCategory);

    List<ItemCategory> list1();

    int count(ItemCategory itemCategory);

    int insert(ItemCategory itemCategory);

    int update(ItemCategory itemCategory);

    void delete(ItemCategory itemCategory);

    int updateStatus(ItemCategory itemCategory);

}
