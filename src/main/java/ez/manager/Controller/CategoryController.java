package ez.manager.Controller;

import ez.manager.Mapper.ItemCategoryMapper;
import ez.manager.Model.ItemCategory;
import ez.manager.Model.ResObject;
import ez.manager.Util.Constant;
import ez.manager.Util.DateUtil;
import ez.manager.Util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * 商品分类管理
 */
@Controller
public class CategoryController {

    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    @RequestMapping("/user/CategoryManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String CategoryManage(ItemCategory ItemCategory,
                                     @PathVariable Integer pageCurrent,
                                     @PathVariable Integer pageSize,
                                     @PathVariable Integer pageCount,
                                     Model model){
        //判断
        if(pageSize == 0) pageSize = 20;
        if(pageCurrent == 0) pageCurrent = 1;
        int rows = itemCategoryMapper.count(ItemCategory);
        if(pageCount == 0) pageCount = rows%pageSize == 0 ? (rows/pageSize) : (rows/pageSize) + 1;
        ItemCategory.setStart((pageCurrent - 1)*pageSize);
        ItemCategory.setEnd(pageSize);
        List<ItemCategory> list = itemCategoryMapper.list(ItemCategory);
        for (ItemCategory i : list){
            i.setCreatedStr(DateUtil.getDateStr(i.getCreated()));
        }
        model.addAttribute("list", list);
        String pageHTML = PageUtil.getPageContent("CategoryManage_{pageCurrent}_{pageSize}_{pageCount}?name="+ItemCategory.getName(), pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("ItemCategory", ItemCategory);
        return "MerchandiseManage/CategoryManage";
    }

    @GetMapping("/user/ItemCategoryEdit")
    public String ItemCategoryEditGet(Model model, ItemCategory itemCategory) {
        if(itemCategory.getId() != 0){
            ItemCategory itemCategory0 = itemCategoryMapper.findById(itemCategory);
            model.addAttribute("ItemCategory",itemCategory0);
        }
        return "MerchandiseManage/ItemCategoryEdit";
    }
    @PostMapping("/user/ItemCategoryEdit")
    public String newsCategoryEditPost(Model model, ItemCategory ItemCategory) {
        Date date = new Date();
        ItemCategory.setCreated(date);
        ItemCategory.setUpdated(date);
        List<ItemCategory> list = itemCategoryMapper.list1();
        String name = ItemCategory.getName();
        for(ItemCategory i : list){
            if(i.getName().equals(name))
                return "redirect:CategoryManage_0_0_0";
        }
        if(ItemCategory.getId() != 0){
            itemCategoryMapper.update(ItemCategory);
        } else {
            itemCategoryMapper.insert(ItemCategory);
        }
        return "redirect:CategoryManage_0_0_0";
    }

    @ResponseBody
    @PostMapping("/user/ItemCategoryEditState")
    public ResObject<Object> itemCategoryEditState(ItemCategory itemCategory){
        itemCategoryMapper.delete(itemCategory);
        ResObject<Object> object = new ResObject<Object>(Constant.Code01, Constant.Msg01, null, null);
        return object;
    }
}
