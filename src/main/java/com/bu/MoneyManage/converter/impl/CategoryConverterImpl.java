package com.bu.MoneyManage.converter.impl;

import com.bu.MoneyManage.converter.ICategoryConverter;
import com.bu.MoneyManage.entity.Category;
import com.bu.MoneyManage.vo.CategoryVO;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverterImpl implements ICategoryConverter{
    @Override
    public CategoryVO convertEntityToVO(Category category, CategoryVO categoryVO) {
        if(category != null) {
            if (categoryVO == null)
                categoryVO = new CategoryVO();
            categoryVO.setCategoryId(category.getCategoryId());
            categoryVO.setCategoryName(category.getCategoryName());
            categoryVO.setImageKey(category.getImageKey());
        }
        return categoryVO;
    }
}
