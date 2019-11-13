package com.miaosha.demo.service.impl;

import com.miaosha.demo.dao.ItemDoMapper;
import com.miaosha.demo.dao.ItemStockDoMapper;
import com.miaosha.demo.dataobject.ItemDo;
import com.miaosha.demo.dataobject.ItemStockDo;
import com.miaosha.demo.dataobject.UserDo;
import com.miaosha.demo.dataobject.UserPasswordDo;
import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.service.ItemService;
import com.miaosha.demo.service.PromoService;
import com.miaosha.demo.service.model.ItemModel;
import com.miaosha.demo.service.model.PromoModel;
import com.miaosha.demo.service.model.UserModel;
import com.miaosha.demo.validator.ValidationResult;
import com.miaosha.demo.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname ItemServiceImpl
 * @Description TODO
 * @Date 2019/10/23 11:25
 * @Author gt
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        // 入库前的校验入参
        final ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        // 转化为itemModel --> dataObject
        final ItemDo itemDo = this.convertItemDoFromItemModel(itemModel);

        // ItemDo写入数据库
        itemDoMapper.insertSelective(itemDo);
        // 获取自增主键值,设置到itemModel中
        itemModel.setId(itemDo.getId());

        // itemModel --> itemStockDo, 并写入数据库
        final ItemStockDo itemStockDo = this.convertItemStockDoFromItemModel(itemModel);
        itemStockDoMapper.insertSelective(itemStockDo);

        // 返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    private ItemDo convertItemDoFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }

        final ItemDo itemDo = new ItemDo();
        BeanUtils.copyProperties(itemModel, itemDo);
        // price 在ItemModel中用的BigDecimal, 在ItemDo中用的Double
        itemDo.setPrice(itemModel.getPrice().doubleValue());
        return itemDo;
    }

    private ItemStockDo convertItemStockDoFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }

        final ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setItemId(itemModel.getId());
        itemStockDo.setStock(itemModel.getStock());

        return itemStockDo;
    }

    @Override
    public List<ItemModel> listItem() {
        final List<ItemDo> itemDoList = itemDoMapper.listItem();

        // java8 函数式编程
        final List<ItemModel> itemModels = itemDoList.stream().map(itemDo -> {
            final ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
            final ItemModel itemModel = this.convertModelFromDataObject(itemDo, itemStockDo);

            return itemModel;
        }).collect(Collectors.toList());


        return itemModels;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        final ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if (itemDo == null) {
            return null;
        }

        // 操作获得库存数量
        final Integer item_id = itemDo.getId();
        final ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(item_id);

        // dataObject
        final ItemModel itemModel = convertModelFromDataObject(itemDo, itemStockDo);

        // 获取商品的秒杀信息
        final PromoModel promoModel = promoService.getPromoByItemId(item_id);
        // 秒杀活动存在,没有结束 . 1没开始 2正在进行
        if (promoModel != null && promoModel.getStatus().intValue() != 3) {
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        final int affectedRow = itemStockDoMapper.decreaseStock(itemId, amount);
        if (affectedRow > 0)
            // 受影响的行数>0 , 更新库存成功
            return true;

        // 更新库存失败
        return false;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDoMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject(ItemDo itemDo, ItemStockDo itemStockDo) {
        if (itemDo == null) {
            return null;
        }

        final ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo, itemModel);
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));

        if (itemStockDo != null) {
            itemModel.setStock(itemStockDo.getStock());
        }

        return itemModel;
    }

}
