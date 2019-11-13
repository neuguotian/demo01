package com.miaosha.demo.service.impl;

import com.miaosha.demo.dao.PromoDoMapper;
import com.miaosha.demo.dataobject.PromoDo;
import com.miaosha.demo.error.BusinessException;
import com.miaosha.demo.error.EmBusinessError;
import com.miaosha.demo.service.ItemService;
import com.miaosha.demo.service.PromoService;
import com.miaosha.demo.service.model.ItemModel;
import com.miaosha.demo.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Classname PromoServiceImpl
 * @Description TODO
 * @Date 2019/11/2 15:36
 * @Author gt
 */
@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDoMapper promoDoMapper;

    @Autowired
    private ItemService itemService;


    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
     /*   // 查一下有没有这个item 我自己写的
        final ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "item不存在");
        }
*/
        final PromoDo promoDo = promoDoMapper.selectByItemId(itemId);
        final PromoModel promoModel = convertFromDataObject(promoDo);
        // 没有秒杀活动
        if (promoModel == null) {
            return null;
        }

        // 判断当前时间是否秒杀活动即将开始或正在进行
        if (promoModel.getStartDate().isAfterNow()) {
            promoModel.setStatus(1);
        } else if(promoModel.getEndDate().isBeforeNow()) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

            return promoModel;
    }


    private PromoModel convertFromDataObject(PromoDo promoDo) {
        if (promoDo == null) {
            return null;
        }
        final PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDo, promoModel);
        // 两个属性类型不一样， 需要手动转一下
        promoModel.setPromoItemPrice(new BigDecimal(promoDo.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDo.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDo.getEndDate()));

        return promoModel;
    }
}
