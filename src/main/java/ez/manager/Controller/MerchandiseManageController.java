package ez.manager.Controller;

import ez.manager.Mapper.ItemCategoryMapper;
import ez.manager.Mapper.ItemMapper;
import ez.manager.Mapper.ReItemMapper;
import ez.manager.Model.Item;
import ez.manager.Model.ItemCategory;
import ez.manager.Model.ReItem;
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
 * 商品管理
 */
@Controller
public class MerchandiseManageController {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    @Autowired
    private ReItemMapper reItemMapper;


    List<Item> itemList;


    @RequestMapping("/user/MerchandiseManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String itemManage(Item item, @PathVariable Integer pageCurrent,
                             @PathVariable Integer pageSize,
                             @PathVariable Integer pageCount,
                             Model model) {
        if (pageSize == 0) pageSize = 50;
        if (pageCurrent == 0) pageCurrent = 1;

        int rows = itemMapper.count(item);
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        item.setStart((pageCurrent - 1) * pageSize);
        item.setEnd(pageSize);
        itemList = itemMapper.list(item);
        for (Item i : itemList) {
            i.setUpdatedStr(DateUtil.getDateStr(i.getUpdated()));
        }
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setStart(0);
        itemCategory.setEnd(Integer.MAX_VALUE);
        List<ItemCategory> itemCategoryList = itemCategoryMapper.list(itemCategory);
        Integer minPrice = item.getMinPrice();
        Integer maxPrice = item.getMaxPrice();
        model.addAttribute("itemCategoryList", itemCategoryList);
        model.addAttribute("itemList", itemList);
        String pageHTML = PageUtil.getPageContent("MerchandiseManage_{pageCurrent}_{pageSize}_{pageCount}?title=" + item.getTitle() + "&cid=" + item.getCid() + "&minPrice" + minPrice + "&maxPrice" + maxPrice, pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("item", item);
        return "MerchandiseManage/MerchandiseManage";
    }

    @GetMapping("/user/ItemEdit")
    public String itemEditGet(Model model, Item item) {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setStart(0);
        itemCategory.setEnd(Integer.MAX_VALUE);
        List<ItemCategory> itemCategoryList = itemCategoryMapper.list(itemCategory);
        model.addAttribute("itemCategoryList", itemCategoryList);
        if (item.getId() != 0) {
            Item item1 = itemMapper.findById(item);
            model.addAttribute("item", item1);
        }
        return "MerchandiseManage/ItemEdit";
    }

    @PostMapping("/user/ItemEdit")
    public String itemEditPost(Model model, Item item) {
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        item.setBarcode("");
        item.setImage("");

        if (item.getId() != 0) {
            itemMapper.update(item);
        } else {

            itemMapper.insert(item);
        }
        return "redirect:MerchandiseManage_0_0_0";
    }

    @ResponseBody
    @PostMapping("/user/ItemEditState")
    public ResObject<Object> itemEditState(Item item1) {
        Item item = itemMapper.findById(item1);
        ReItem reItem = new ReItem();
        reItem.setId(item.getId());
        reItem.setBarcode(item.getBarcode());
        reItem.setCid(item.getCid());
        reItem.setImage(item.getImage());
        reItem.setPrice(item.getPrice());
        reItem.setNum(item.getNum());
        reItem.setSellPoint(item.getSellPoint());
        reItem.setStatus(item.getStatus());
        reItem.setTitle(item.getTitle());
        reItem.setRecovered(new Date());
        reItemMapper.insert(reItem);
        itemMapper.delete(item1);
        ResObject<Object> object = new ResObject<Object>(Constant.Code01, Constant.Msg01, null, null);
        return object;
    }

}
