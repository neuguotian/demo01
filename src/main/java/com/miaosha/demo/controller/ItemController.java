package com.miaosha.demo.controller;

import com.miaosha.demo.controller.viewobject.ItemVO;
import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.response.CommonReturnType;
import com.miaosha.demo.service.ItemService;
import com.miaosha.demo.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname ItemController
 * @Description TODO
 * @Date 2019/10/23 12:12
 * @Author gt
 */
@RestController("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;


    // 创建商品
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl
                                       ) throws BusinessException {
        // 封装service请求用来创建商品
        final ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setImgUrl(imgUrl);
        itemModel.setStock(stock);

        final ItemModel itemModelFromReturn = itemService.createItem(itemModel);
        final ItemVO itemVO = convertFromItemModel(itemModelFromReturn);

        return CommonReturnType.create(itemVO);
    }


    // 根基商品id,获取商品信息
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id) {
        final ItemModel itemModel = itemService.getItemById(id);

        final ItemVO itemVO = convertFromItemModel(itemModel);

        return CommonReturnType.create(itemVO);
    }


    // 获取商品列表 item list
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public CommonReturnType listItem() {
        final List<ItemModel> itemModels = itemService.listItem();

        // itemModels --> itemVos. 使用stream api
        final List<ItemVO> itemVOList = itemModels.stream().map(itemModel -> {
            final ItemVO itemVO = this.convertFromItemModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOList);
    }


    /*
    ItemModel和ItemVO一样为什么还要转一下
    实际，企业中的两个很不一样, ItemVO可能会很大, 所有分层是必须的
     */
    private ItemVO convertFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }

        final ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);

        return itemVO;
    }

}
